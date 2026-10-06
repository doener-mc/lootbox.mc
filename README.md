# Battle Loot (Forge 1.20.1)

Konfigurierbare Lootboxen mit eigenem Config-GUI, z.B. fuer Battle Royale / Minigames.

## Bauen (Java 17 noetig)
Variante A (empfohlen): Forge MDK 1.20.1 (47.2.0) von https://files.minecraftforge.net herunterladen,
den Ordner `src` aus diesem Projekt ueber den `src`-Ordner der MDK kopieren (ersetzen),
in der MDK-`gradle.properties` mod_id=battleloot setzen, dann `./gradlew build`.

Variante B: Mit installiertem Gradle 8.1.1 in diesem Ordner `gradle wrapper` ausfuehren, danach `./gradlew build`.

Die fertige Mod liegt danach in `build/libs/battleloot-1.0.0.jar` -> in den `mods`-Ordner legen.

## Benutzung
- Creative-Tab "Battle Loot": Lootbox + Konfigurationsstab
- Lootbox platzieren, mit dem Stab (OP, Level 2) Rechtsklick -> Config-GUI
- Oben: Item-Pool (27 Slots). Stackgroesse = max. Menge, die pro Zug droppen kann
- Min./Max. Items: wie viele Items pro Oeffnung gezogen werden
- Modus: nach Oeffnen zerstoeren ODER nach X Sekunden wieder auffuellen
- Spieler oeffnen die Box mit normalem Rechtsklick. Im Survival ist die Box unzerstoerbar.
- Konfig kopieren: Im Creative Strg + Mittelklick auf eine konfigurierte Box -> Item enthaelt die Config
