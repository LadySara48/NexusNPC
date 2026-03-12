# NexusNPC

A feature-rich NPC plugin for [PowerNukkitX](https://github.com/PowerNukkitX/PowerNukkitX), built with Java 21+ and PNX API 2.0.0.  
Craft lifelike NPCs with custom skins, dynamic movement, and powerful command execution — all in one place.

---

## Requirements

| Requirement      | Version      |
|------------------|--------------|
| Java             | 21 or higher |
| PowerNukkitX API | 2.0.0        |

---

## Features

### 🎨 SkinLibrary

A complete pipeline for loading, validating and caching custom Minecraft skins at runtime.

- **File-based skin loading** — Drop `.png` (skin/cape) and `.json` (geometry) files into their respective folders and they will be automatically discovered, validated and cached on startup.
- **Async processing** — Skin and cape PNG files are processed asynchronously to keep the server thread free.
- **8-bit compliance check** — All textures are validated against Minecraft's 8-bit color requirements before being cached, preventing broken skin rendering.
- **Skin composition via `skins.yml`** — Combine a skin texture, cape and geometry definition into a single named skin entry. Once registered, it can be equipped in-game with `/skins` or applied directly onto a NexusNPC entity.

**Folder structure:**
```
plugins/NexusNPC/
├── skins/        ← skin PNG files
├── geometries/   ← geometry JSON files
├── capes/        ← cape PNG files
└── skins.yml     ← skin composition config
```

**`skins.yml` example:**
```yaml
my_skin:
  skin: skinFile #Not requires .png (auto)
  skinId: skinId
  geometry: geometryFile #Not requires .json (auto)
  geometryId: geometry.steve_custom
  cape: my_cape.png
```

---

### 🧍 NPC

Human-entity NPCs with full customisation support.

- **HumanEntity NPCs** — NPCs are rendered as human entities, supporting full skin and cape application via SkinLibrary.
- **Per-NPC command list** — Assign one or more commands to each NPC. Commands execute when a player interacts with or attacks the NPC. Use `{player}` as a placeholder for the interacting player's name.
  ```
  msg {player} Hello, traveller!
  give {player} diamond 1
  ```
- **Pathfinder / Idle mode** — Each NPC can either wander around its spawn point or stand completely still.
    - **Pathfinder** — The NPC randomly walks within a configurable radius around its `firstPosition`. If it strays too far it automatically returns home.
    - **Idle** — The NPC stays at its spawn position with no movement.
    - Movement speed, wander radius and timing are all adjustable in `pathfinder.yml`.

**`pathfinder.yml` reference:**

| Key                   | Default  | Description                                                               |
|-----------------------|----------|---------------------------------------------------------------------------|
| `Pathfinder-Per-Tick` | `2`      | Ticks between each pathfinder update. Lower = smoother, higher CPU cost.  |
| `MaxRadius`           | `5.0`    | Maximum blocks an NPC may wander from its spawn point.                    |
| `StepSize`            | `0.20`   | Distance moved per pathfinder tick.                                       |
| `IdleTicks`           | `60`     | Ticks the NPC idles at its destination before choosing a new goal (~3 s). |
| `NewGoalTicks`        | `100`    | Ticks between new random goal selections (~5 s).                          |

---

### ⚡ DPC — Dispatch Player Command

Execute commands **on behalf of a player** without giving them elevated permissions.

**Usage:**
```
/dpc <player> <command>
```

**Example:**
```
/dpc KanaFujiwara346 say Hi!
```

This was designed primarily to make NPC interactions that open forms easier to handle — instead of running a command as the server or console, it is dispatched as the targeted player, preserving context-sensitive form triggers and UI flows.

> **Note:** Use with caution. DPC bypasses the player's own permission level for the dispatched command.

---

## Commands

| Command                           | Description                                 | Permission           |
|-----------------------------------|---------------------------------------------|----------------------|
| `/npc Enum<create, delete, edit>` | Create, Delete, Edit NPCs                   | `nexus.npc.npc`      |
| `/nexusnpc`                       | NexusNPC About Command                      | `nexus.npc.about`    |
| `/skins`                          | Equip a skin from SkinLibrary onto yourself | `nexus.npc.skin`     |
| `/dpc <player> <command>`         | Dispatch a command as the target player     | `nexus.npc.dispatch` |

> NPC creation and management commands are handled through in-game interaction and configuration files. Additional commands may vary by build.

---

## Permissions

| Permission           | Description                       | Default |
|----------------------|-----------------------------------|---------|
| `nexus.npc.skin`     | Allows use of the `/skin` command | `op`    |
| `nexus.npc.dispatch` | Allows use of the `/dpc` command  | `op`    |
| `nexus.npc.create`   | Allows creating and editing NPCs  | `op`    |
| `nexus.npc.delete`   | Allows deleting NPCs              | `op`    |
| `nexus.npc.edit`     | Allows editing NPCs               | `op`    |
| `nexus.npc.npc`      | General NPC Command               | `op`    |
| `nexus.npc.about`    | NexusNPC About Command            | `true`  |

---

## Installation

1. Download the latest `.jar` from the [Releases](../../releases) page.
2. Place it in your server's `plugins/` folder.
3. Start the server once to generate the default config and folder structure.
4. Add your skin/geometry/cape files to the appropriate folders.
5. Configure `skins.yml` and `config.yml` as needed.
6. Reload or restart the server.

---

## Contributing

Pull requests are welcome! Please make sure your code targets **Java 21+** and is compatible with **PowerNukkitX API 2.0.0** before submitting.

---

## Credits

Developed by **[Hearlov](https://github.com/LadySara48)**.  
AI Contributor (Support): **Claude Sonnet 4.6** (Anthropic).