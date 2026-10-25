![MirraNET Logo](https://raw.githubusercontent.com/LinzN/Stem/master/stem-core/src/main/resources/header-logo.png)

# calendar

> Calendar and waste collection dates for [STEM](https://github.com/LinzN/Stem), served as JSON.

calendar reads iCalendar (`.ics`) files, downloads shared calendars and the EVS waste collection calendar
and offers the upcoming entries through [web-api](https://github.com/LinzN/web-api), for example for a
dashboard.

---

## Features

- Reads all `.ics` files in `plugins/calendar/calendarFiles/`; private events are ignored
- Files are checked every 30 seconds and only parsed again when they changed
- Shared calendars (e.g. Nextcloud or Google share links) are downloaded regularly
- EVS waste calendar is downloaded every night at 03:30 with the STEM script `get_evs-data`
- A new file only replaces the old one if it can be parsed
- Waste types are recognized by the event title:

| Title                                                 | Type     |
|-------------------------------------------------------|----------|
| `Restabfalltonne`                                     | `BLACK`  |
| `Gelber Sack`, `Gelbe Tonne`, `Gelbe Tonne/Gelber Sack` | `YELLOW` |
| `Biotonne`                                            | `GREEN`  |
| `Blaue Tonne`                                         | `BLUE`   |
| anything else                                         | `OTHER`  |

---

## Requirements

| Requirement | Version                                               |
|-------------|-------------------------------------------------------|
| STEM        | 1.2.0 or newer (`stem-api`)                           |
| Plugins     | [web-api](https://github.com/LinzN/web-api)           |
| Script      | `get_evs-data` in the STEM scripts (only for the EVS calendar) |

---

## Installation

1. Build the plugin with `mvn clean package` (or take the build from Jenkins).
2. Copy `target/calendar.jar` into the `plugins` folder of STEM and the libraries from
   `target/libraries` as `libraries.zip` (folder `libraries` inside) into `plugins/calendar/`.
3. Start STEM once to create `plugins/calendar/config.yml`, add your calendars and restart STEM.

---

## Configuration (`config.yml`)

| Key                               | Default | Description                                        |
|-----------------------------------|---------|----------------------------------------------------|
| `general.shared_refresh_minutes`  | `15`    | Download interval of the shared calendars (minutes) |
| `sharedCalendars.<key>.link`      | –       | Download link of a shared calendar                 |

Example:

```yaml
general:
  shared_refresh_minutes: 15
sharedCalendars:
  family:
    link: "https://cloud.example.com/remote.php/dav/public-calendars/abc123?export"
  work:
    link: "https://calendar.example.com/share/xyz.ics"
```

Every shared calendar is stored as `calendarFiles/shared_<key>.ics`. Calendars removed from the
configuration are deleted from the folder. Other `.ics` files can be put into `calendarFiles/` by hand.

---

## Web API

| Path                 | Description                                                                 |
|----------------------|-----------------------------------------------------------------------------|
| `/calendar/reminder` | All entries from today 00:00 until 30 hours later, sorted by date           |
| `/calendar/trash`    | Next collection date for `BLACK`, `GREEN`, `YELLOW` and `BLUE`              |

`/calendar/reminder` returns entries like:

```json
{ "reminderType": "trash", "name": "...", "date": "Montag 3 November 2026", "type": "BLACK" }
```

`reminderType` is `other` for all non-waste entries. Dates are formatted in German.

---

## License

Copyright (c) MirraNET, Niklas Linz. Licensed under the
[GNU Lesser General Public License v3.0](https://www.gnu.org/licenses/lgpl-3.0.html).
