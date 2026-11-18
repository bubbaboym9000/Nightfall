package com.nightfall;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class NightfallClient implements ClientModInitializer {

    private KeyBinding sprintKey;

    @Override
    public void onInitializeClient() {
        sprintKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.nightfall.sprint",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                KeyBinding.Category.MISC
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (sprintKey.wasPressed()) {
                if (client.player != null) {
                    client.player.setSprinting(!client.player.isSprinting());
                }
            }
        });
    }
}
