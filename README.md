# ScaffoldShifter

ScaffoldShifter is an Opai Client extension module.

You need to set 'Blocks' option to 2 and use 'None' Sprint mode to bypass Hypixel Staff. Set 'Blocks' option to 9 bypasses Vulcan scaffold speed check even if you are telly bridging.

## Module

- Category: `Misc`
- Name: `ScaffoldShifter`

## Features

When Opai's `Scaffold` module is enabled, ScaffoldShifter counts horizontal block movement and briefly presses sneak every configured number of blocks.

## Settings

- `Blocks`: `1 - 16`
- `Sneak Ticks`: `1 - 20`
- `AirOnly`: only sneak while the player is not on the ground

## Installation

Download the release JAR and put it into:

```text
%APPDATA%\Opai\extensions
```

Then restart the client or run:

```text
.extension reload
```

## Build

```bash
mvn package
```
