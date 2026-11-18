package com.nightfall;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;

public class NightfallClient implements ClientModInitializer {

    private static final KeyMapping.Category CATEGORY =
        KeyMapping.Category.register(
            Identifier.fromNamespaceAndPath("nightfall", "main")
        );

    private KeyMapping sprintKey;
    private KeyMapping clickGuiKey;

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

        clickGuiKey = KeyMappingHelper.registerKeyMapping(
            new KeyMapping(
                "key.nightfall.clickgui",
                InputConstants.Type.KEYBOARD,
                InputConstants.KEY_RSHIFT,
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

            while (clickGuiKey.consumeClick()) {
                if (client.gui.screen() instanceof ClickGuiScreen) {
                    client.gui.setScreen(null);
                } else {
                    client.gui.setScreen(new ClickGuiScreen());
                }
            }
        });
    }
}
