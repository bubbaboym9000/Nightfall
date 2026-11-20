# Nightfall (Fabric, Minecraft 26.3)

Adds **Nightfall**, a netherite-tier sword.

- 9 attack damage on the tooltip (a netherite sword shows 8), 1.6 attack speed, 2500 durability, Epic rarity, repaired with netherite ingots
- Melee hits blind the target for 3 seconds
- Shows up in the Combat creative tab
- Craft it with an echo shard on top, a netherite ingot in the middle, a stick at the bottom (one column)
- Gets the normal sword enchantments (it is in the `minecraft:swords` tag)

## Build

Needs JDK 25.

The Gradle wrapper scripts and jar are not included. Either copy `gradlew`, `gradlew.bat` and
`gradle/wrapper/gradle-wrapper.jar` from the `26.3` branch of
https://github.com/FabricMC/fabric-example-mod, or run `gradle wrapper --gradle-version 9.8.2` once
with a Gradle you already have. Then:

    ./gradlew build

The mod jar is `build/libs/nightfall-1.0.0.jar` (not the `-sources` one). Put it in your `mods`
folder next to Fabric API for 26.3.

## Tweaking

- Stats: `ModItems.java` (damage, durability, enchantability, recipe repair item in `data/.../tags/item/repairs_nightfall.json`)
- Ability: `NightfallAbility.java` (effect, duration)
- Recipe: `src/main/resources/data/nightfall/recipe/nightfall.json`
- Look: `src/main/resources/assets/nightfall/textures/item/nightfall.png` (16x16)
- Name: `assets/nightfall/lang/en_us.json`

## Versions this was written against

Minecraft 26.3, Fabric Loader 0.20.1, Fabric API 0.165.0+26.3, Fabric Loom 1.19-SNAPSHOT, Gradle 9.8.2.
Taken from Fabric's example mod for 26.3.
