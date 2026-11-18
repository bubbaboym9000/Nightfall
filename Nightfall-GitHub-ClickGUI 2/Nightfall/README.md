# Nightfall

A Fabric client-side mod project for Minecraft 26.3.

## Controls
- **Right Shift** — open/close the Nightfall ClickGUI.
- **G** — toggle Sprint.

## ClickGUI
The menu contains 32 entries across Combat, Movement, Render, Player, and Misc.
Left-click an entry to toggle its displayed state. Right-click to cycle its displayed
profile setting. Scroll to see additional entries.

**Important:** Sprint is the only implemented gameplay module in this starter project.
The other 31 entries are UI placeholders: their switches and profile labels are not
connected to gameplay behavior. They are not working cheats yet.

## Build
Use the Java version required by the Minecraft/Fabric toolchain configured in
`gradle.properties`, then run:

```bash
./gradlew build
```

The GitHub Actions workflow in `.github/workflows/build.yml` is intended to build the
project and upload the resulting JAR as an Actions artifact. The project has not been
verified against the Minecraft 26.3 toolchain in this archive.
