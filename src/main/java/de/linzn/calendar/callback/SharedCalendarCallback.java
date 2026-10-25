/*
 * Copyright (c) 2025 MirraNET, Niklas Linz. All rights reserved.
 *
 * This file is part of the MirraNET project and is licensed under the
 * GNU Lesser General Public License v3.0 (LGPLv3).
 *
 * You may use, distribute and modify this code under the terms
 * of the LGPLv3 license. You should have received a copy of the
 * license along with this file. If not, see <https://www.gnu.org/licenses/lgpl-3.0.html>
 * or contact: niklas.linz@mirranet.de
 */

package de.linzn.calendar.callback;

import de.linzn.calendar.CalendarManager;
import de.linzn.calendar.CalendarPlugin;
import de.linzn.stem.taskManagment.AbstractCallback;
import de.linzn.stem.taskManagment.CallbackTime;
import de.linzn.stem.taskManagment.operations.AbstractOperation;
import de.linzn.stem.taskManagment.operations.OperationOutput;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Downloads the configured shared calendars ("sharedCalendars.<key>.link").
 * Downloads use Java's HTTP client (links containing "&" work), a file is only replaced after a complete
 * download and calendars removed from the config are deleted.
 */
public class SharedCalendarCallback extends AbstractCallback {
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .followRedirects(HttpClient.Redirect.NORMAL)
            .build();

    private static File calendarDirectory() {
        File calendarDirectory = new File(CalendarPlugin.calendarPlugin.getDataFolder(), "calendarFiles");
        if (!calendarDirectory.exists()) {
            calendarDirectory.mkdirs();
        }
        return calendarDirectory;
    }

    @Override
    public void operation() {
        addOperationData(new AbstractOperation() {
            @Override
            public OperationOutput runOperation() {
                OperationOutput output = new OperationOutput(this);
                output.setExit(downloadAll());
                return output;
            }
        });
    }

    private int downloadAll() {
        File calendarDirectory = calendarDirectory();
        Set<String> expectedFiles = new HashSet<>();
        int failures = 0;
        if (CalendarPlugin.calendarPlugin.getDefaultConfig().get("sharedCalendars") instanceof Map<?, ?> sharedCalendars) {
            for (Object keyObject : sharedCalendars.keySet()) {
                String key = String.valueOf(keyObject);
                String link = CalendarPlugin.calendarPlugin.getDefaultConfig().getString("sharedCalendars." + key + ".link");
                String fileName = "shared_" + key.replaceAll("[^a-zA-Z0-9_-]", "_") + ".ics";
                expectedFiles.add(fileName);
                if (link == null || link.isBlank()) {
                    continue;
                }
                if (!this.download(link, new File(calendarDirectory, fileName).toPath())) {
                    failures++;
                }
            }
        }
        File[] existing = calendarDirectory.listFiles((dir, name) -> name.startsWith("shared_"));
        if (existing != null) {
            for (File file : existing) {
                if (!expectedFiles.contains(file.getName())) {
                    file.delete();
                }
            }
        }
        return failures == 0 ? 0 : 1;
    }

    private boolean download(String link, Path destination) {
        Path tempFile = null;
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(link.trim())).timeout(Duration.ofSeconds(30)).GET().build();
            HttpResponse<InputStream> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofInputStream());
            try (InputStream in = response.body()) {
                if (response.statusCode() != 200) {
                    CalendarPlugin.calendarPlugin.getPluginLogger().warning("Shared calendar download failed with HTTP " + response.statusCode() + ": " + destination.getFileName());
                    return false;
                }
                tempFile = Files.createTempFile(destination.getParent(), "download_", ".tmp");
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
            CalendarManager.parse(tempFile.toFile());
            Files.move(tempFile, destination, StandardCopyOption.REPLACE_EXISTING);
            tempFile = null;
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        } catch (Exception e) {
            CalendarPlugin.calendarPlugin.getPluginLogger().warning("Shared calendar download failed for " + destination.getFileName() + ": " + e.getMessage());
            return false;
        } finally {
            if (tempFile != null) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (Exception ignored) {
                }
            }
        }
    }

    @Override
    public void callback(OperationOutput operationOutput) {
        CalendarPlugin.calendarPlugin.getPluginLogger().debug("Shared calender pull finish " + operationOutput.getExit());
    }

    @Override
    public CallbackTime getTime() {
        int minutes = Math.max(1, CalendarPlugin.calendarPlugin.getDefaultConfig().getInt("general.shared_refresh_minutes", 15));
        return new CallbackTime(1, minutes, TimeUnit.MINUTES);
    }
}
