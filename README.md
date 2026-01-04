# ⚔️ Chronicles

[![SpigotMC](https://img.shields.io/badge/SpigotMC-Download-orange?style=for-the-badge&logo=spigotmc)](https://www.spigotmc.org/resources/chronicles.131460/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge)](LICENSE)

> [!NOTE]
> Languages: [English](README.md) | [Português](README_PT.md)

**Every Item Has a Story.**

Chronicles brings life to your server's items by automatically recording their history. From the moment a sword is forged to the legendary battles it fights, every significant event is tracked and stored. Players can view the history of their weapons, tools, and armor, turning ordinary items into legendary artifacts with their own unique lore.

### 🎥 Video Review

[![Video Review](https://img.youtube.com/vi/5z8jFEyG_eo/0.jpg)](https://youtu.be/5z8jFEyG_eo)

## ✨ Features

- **Automatic Tracking:** No manual input required. The plugin listens to events and updates item history automatically.
- **Detailed History:** Tracks a wide range of events:
    - **Creation:** Crafted (`Forged by <player>`) or Traded.
    - **Upgrades:** Enchanted, Renamed, or Repaired in an anvil.
    - **Combat:** Records kills on Players and Bosses (Ender Dragon, Wither, Warden, Elder Guardian).
    - **Defense:** Records when a Shield blocks a powerful attack or a Boss.
    - **Ownership:** Tracks when items are given to other players, stolen from death drops, or found in the world.
- **Supported Items:** Works with all Swords, Axes, Bows, Crossbows, Tridents, Shields, Elytra, Armor, and Tools (Pickaxes, Shovels, Hoes).
- **Multi-Language Support:** Comes with English and Portuguese (PT-BR) localization.
- **Local Storage:** Uses SQLite for lightweight and efficient data storage, no external database setup required.

## 🚀 Installation

1. Download the latest `.jar` file from [SpigotMC](https://www.spigotmc.org/resources/chronicles.131460/) or the Releases tab.
2. Place the file in your server's `plugins` folder.
3. Restart your server.
4. The `config.yml` file will be automatically generated in `plugins/Chronicles/`.

## 🛠️ Configuration

The `config.yml` file allows you to configure the plugin language.

```yaml
# Language of the plugin messages
# Available: en (English), pt (Portuguese)
language: en
```

## 💻 Commands

| Command | Description | Permission |
|---|---|---|
| `/history` | Displays the full chronological history of the item in your main hand. | `masterpl.history` |
| `/lore` | Alias for `/history`. | `masterpl.history` |
| `/chronicle` | Alias for `/history`. | `masterpl.history` |

## 🛡️ Permissions

- `masterpl.history`: Allows players to use the `/history` command to view item lore. (Default: true)

## 🏗️ How to Build

This project uses Maven for dependency management and building.

### Prerequisites

- JDK 17 or higher
- Maven installed

### Steps

1. Clone this repository:
   ```bash
   git clone https://github.com/juniodevs/Chonicle-Plugin.git
   ```
2. Navigate to the project folder:
   ```bash
   cd Chonicle-Plugin
   ```
3. Build the project:
   ```bash
   mvn clean package
   ```
4. The compiled `.jar` file will be in the `target/` folder (e.g., `Chronicles-1.0-SNAPSHOT.jar`).

## 🤝 Contribution

This project is free and open to everyone! Any help is very welcome.

If you want to contribute code, fix bugs, suggest new features, or **translate the plugin to other languages**, feel free to open an Issue or submit a Pull Request.

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
