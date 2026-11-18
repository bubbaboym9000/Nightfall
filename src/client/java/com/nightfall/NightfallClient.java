
package com.nightfall;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NightfallClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("nightfall", "main")
        );

    private static final Map<Entity, Boolean> GLOWING_ENTITIES =
        new IdentityHashMap<>();

    private KeyMapping sprintKey;
    private KeyMapping clickGuiKey;

    @Override
    public void onInitializeClient() {
EntityEspRenderer.register();
        sprintKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.nightfall.sprint",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_G,
                CATEGORY
            )
        );

        clickGuiKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.nightfall.clickgui",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_RSHIFT,
                CATEGORY
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player != null && ClickGuiScreen.sprintEnabled) {
                client.player.setSprinting(true);
            }


            while (sprintKey.consumeClick()) {
                if (client.player != null) {
                    client.player.setSprinting(
                        !client.player.isSprinting()
                    );
                }
            }

            while (clickGuiKey.consumeClick()) {
                if (client.gui.screen() instanceof ClickGuiScreen) {
                    client.gui.setScreen(null);
                } else {
                    client.gui.setScreen(new ClickGuiScreen());
                }
            }
        });
    }

    private static void updateEntityEsp(Minecraft client) {

        if (!ClickGuiScreen.espEnabled ||
            client.level == null ||
            client.player == null) {

            restoreGlowingEntities();
            return;
        }

        Set<Entity> seen =
            Collections.newSetFromMap(new IdentityHashMap<>());

        for (Entity entity : client.level.entitiesForRendering()) {

            if (entity == client.player) {
    continue;
}

            seen.add(entity);

            GLOWING_ENTITIES.computeIfAbsent(
                entity,
                Entity::hasGlowingTag
            );

            entity.setGlowingTag(true);
        }

        Iterator<Map.Entry<Entity, Boolean>> iterator =
            GLOWING_ENTITIES.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Entity, Boolean> entry = iterator.next();

            if (!seen.contains(entry.getKey())) {
                entry.getKey().setGlowingTag(entry.getValue());
                iterator.remove();
            }
        }
    }

    private static void restoreGlowingEntities() {
        for (Map.Entry<Entity, Boolean> entry :
            GLOWING_ENTITIES.entrySet()) {

            entry.getKey().setGlowingTag(entry.getValue());
        }

        GLOWING_ENTITIES.clear();
    }
}
