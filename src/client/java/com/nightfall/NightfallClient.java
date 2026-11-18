package com.nightfall;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;

public class NightfallClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("nightfall", "main")
        );

    private KeyMapping sprintKey;

    @Override
    public void onInitializeClient() {

        sprintKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.nightfall.sprint",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_G,
                CATEGORY
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sprintKey.consumeClick()) {
                if (client.player != null) {
                    client.player.setSprinting(
                        !client.player.isSprinting()
                    );
                }
            }
        });
    }
}
