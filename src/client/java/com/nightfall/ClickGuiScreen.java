
package com.nightfall;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClickGuiScreen extends Screen {

    public static boolean sprintEnabled = false;
    private boolean fullbrightEnabled = false;
    public static boolean espEnabled = false;
    public static boolean xrayEnabled = false;
    private double oldGamma = 0.5;

    public ClickGuiScreen() {
        super(Component.literal("Nightfall"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 50;

        addRenderableWidget(Button.builder(
            Component.literal("Sprint: " + (sprintEnabled ? "ON" : "OFF")),
            button -> {
                sprintEnabled = !sprintEnabled;
                button.setMessage(Component.literal(
                    "Sprint: " + (sprintEnabled ? "ON" : "OFF")
                ));
            }
        ).bounds(centerX - 100, startY, 200, 20).build());

        addRenderableWidget(Button.builder(
            Component.literal("Fullbright: " + (fullbrightEnabled ? "ON" : "OFF")),
            button -> {
                fullbrightEnabled = !fullbrightEnabled;

                if (fullbrightEnabled) {
                    oldGamma = Minecraft.getInstance().options.gamma().get();
                    Minecraft.getInstance().options.gamma().set(1.0);
                } else {
                    Minecraft.getInstance().options.gamma().set(oldGamma);
                }

                button.setMessage(Component.literal(
                    "Fullbright: " + (fullbrightEnabled ? "ON" : "OFF")
                ));
            }
        ).bounds(centerX - 100, startY + 25, 200, 20).build());

        addRenderableWidget(Button.builder(
            Component.literal("ESP: " + (espEnabled ? "ON" : "OFF")),
            button -> {
                espEnabled = !espEnabled;
                button.setMessage(Component.literal(
                    "ESP: " + (espEnabled ? "ON" : "OFF")
                ));
            }
        ).bounds(centerX - 100, startY + 50, 200, 20).build());

        addRenderableWidget(Button.builder(
            Component.literal("X-Ray: " + (xrayEnabled ? "ON" : "OFF")),
            button -> {
                xrayEnabled = !xrayEnabled;
                button.setMessage(Component.literal(
                    "X-Ray: " + (xrayEnabled ? "ON" : "OFF")
                ));
            }
        ).bounds(centerX - 100, startY + 75, 200, 20).build());
    }

    @Override
    public void extractRenderState(
        GuiGraphicsExtractor graphics,
        int mouseX,
        int mouseY,
        float delta
    ) {
        super.extractRenderState(graphics, mouseX, mouseY, delta);

        graphics.text(
            this.font,
            "NIGHTFALL",
            this.width / 2 - 35,
            this.height / 2 - 85,
            0xFFB56CFF,
            true
        );
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
