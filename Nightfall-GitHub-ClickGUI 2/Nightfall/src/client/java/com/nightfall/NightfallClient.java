package com.nightfall;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class NightfallClient implements ClientModInitializer {
    private static KeyMapping toggleSprintKey;
    private static KeyMapping clickGuiKey;

    private static final List<Module> MODULES = new ArrayList<>();
    private static final String[] CATEGORIES = {
            "Combat", "Movement", "Render", "Player", "Misc"
    };

    private static final class Module {
        final String name;
        final String category;
        final String settingName;
        final String[] settingOptions;
        int settingIndex;
        boolean enabled;

        Module(String name, String category, String settingName, String... settingOptions) {
            this.name = name;
            this.category = category;
            this.settingName = settingName;
            this.settingOptions = settingOptions;
        }

        String settingValue() {
            return settingOptions.length == 0 ? "N/A" : settingOptions[settingIndex];
        }

        void cycleSetting() {
            if (settingOptions.length > 0) {
                settingIndex = (settingIndex + 1) % settingOptions.length;
            }
        }
    }

    private static void add(String category, String... names) {
        for (String name : names) {
            String setting = name.equals("Sprint") ? "Mode" : "Profile";
            MODULES.add(new Module(name, category, setting,
                    "Default", "Option 2", "Option 3"));
        }
    }

    private static void registerModules() {
        if (!MODULES.isEmpty()) return;
        add("Combat", "Kill Aura", "Aim Assist", "Auto Clicker", "Reach",
                "Trigger Bot", "Velocity", "Criticals");
        add("Movement", "Fly", "Speed", "Sprint", "Step", "No Fall",
                "Jesus", "Spider", "High Jump");
        add("Render", "ESP", "Tracers", "Name Tags", "Fullbright",
                "X-Ray", "Storage ESP", "Entity ESP");
        add("Player", "Auto Totem", "Auto Eat", "Fast Place", "Fast Mine",
                "Anti-Hunger", "Inventory Move");
        add("Misc", "Freecam", "Timer", "Auto Fish", "Auto Tool");
    }

    private static Module findModule(String name) {
        for (Module module : MODULES) {
            if (module.name.equals(name)) return module;
        }
        return null;
    }

    @Override
    public void onInitializeClient() {
        registerModules();

        KeyMapping.Category category = KeyMapping.Category.register(
                ResourceLocation.fromNamespaceAndPath("nightfall", "main")
        );

        toggleSprintKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.nightfall.toggle_sprint",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_G,
                category
        ));

        clickGuiKey = KeyBindingHelper.registerKeyBinding(new KeyMapping(
                "key.nightfall.click_gui",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                category
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleSprintKey.consumeClick()) {
                Module sprint = findModule("Sprint");
                sprint.enabled = !sprint.enabled;
                if (client.player != null) {
                    client.player.displayClientMessage(
                            Component.literal("Nightfall Sprint: " +
                                    (sprint.enabled ? "ON" : "OFF")), true);
                }
            }

            while (clickGuiKey.consumeClick()) {
                client.setScreen(new ClickGuiScreen());
            }

            Module sprint = findModule("Sprint");
            if (client.player != null && sprint.enabled) {
                client.player.setSprinting(client.player.input.hasForwardImpulse());
            }
        });
    }

    private static final class ClickGuiScreen extends Screen {
        private String selectedCategory = "Combat";
        private int scrollOffset;

        ClickGuiScreen() {
            super(Component.literal("Nightfall"));
        }

        @Override
        public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
            renderBackground(graphics, mouseX, mouseY, delta);

            int panelWidth = Math.min(520, this.width - 24);
            int panelHeight = Math.min(390, this.height - 24);
            int left = (this.width - panelWidth) / 2;
            int top = (this.height - panelHeight) / 2;

            graphics.fill(left, top, left + panelWidth, top + panelHeight, 0xF0181B25);
            graphics.fill(left, top, left + panelWidth, top + 28, 0xFF292E40);
            graphics.drawString(this.font, "NIGHTFALL", left + 12, top + 10, 0xFFFFFFFF);
            graphics.drawString(this.font, "Right-click a module to cycle its displayed profile",
                    left + 118, top + 10, 0xFFB9C0D4);

            int tabY = top + 38;
            int tabX = left + 10;
            for (String category : CATEGORIES) {
                int tabWidth = 88;
                int color = category.equals(selectedCategory) ? 0xFF596B9B : 0xFF303747;
                graphics.fill(tabX, tabY, tabX + tabWidth - 4, tabY + 20, color);
                graphics.drawCenteredString(this.font, category, tabX + (tabWidth - 4) / 2,
                        tabY + 6, 0xFFFFFFFF);
                tabX += tabWidth;
            }

            int rowTop = top + 68;
            int rowHeight = 25;
            int availableRows = Math.max(1, (panelHeight - 104) / rowHeight);
            List<Module> visible = new ArrayList<>();
            for (Module module : MODULES) {
                if (module.category.equals(selectedCategory)) visible.add(module);
            }
            int maxScroll = Math.max(0, visible.size() - availableRows);
            scrollOffset = Math.max(0, Math.min(scrollOffset, maxScroll));

            for (int i = scrollOffset; i < visible.size() && i < scrollOffset + availableRows; i++) {
                Module module = visible.get(i);
                int y = rowTop + (i - scrollOffset) * rowHeight;
                boolean implemented = module.name.equals("Sprint");
                graphics.fill(left + 10, y, left + panelWidth - 10, y + 22,
                        module.enabled ? 0xFF263D39 : 0xFF252B38);
                graphics.drawString(this.font, module.name, left + 18, y + 7, 0xFFFFFFFF);

                String profile = module.settingName + ": " + module.settingValue();
                graphics.drawString(this.font, profile, left + 170, y + 7, 0xFFB8C0D0);

                String status = module.enabled ? "ON" : "OFF";
                int statusColor = module.enabled ? 0xFF55D6A2 : 0xFFB2B8C5;
                graphics.drawString(this.font, status, left + panelWidth - 54, y + 7, statusColor);

                if (!implemented) {
                    graphics.drawString(this.font, "UI ONLY", left + panelWidth - 112, y + 7, 0xFFE0B36A);
                }
            }

            graphics.drawString(this.font,
                    "Sprint works. Other entries are interface placeholders, not active cheats.",
                    left + 12, top + panelHeight - 24, 0xFFE0B36A);
            super.render(graphics, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            int panelWidth = Math.min(520, this.width - 24);
            int panelHeight = Math.min(390, this.height - 24);
            int left = (this.width - panelWidth) / 2;
            int top = (this.height - panelHeight) / 2;

            int tabY = top + 38;
            int tabX = left + 10;
            for (String category : CATEGORIES) {
                int tabWidth = 88;
                if (mouseY >= tabY && mouseY <= tabY + 20 &&
                        mouseX >= tabX && mouseX <= tabX + tabWidth - 4) {
                    selectedCategory = category;
                    scrollOffset = 0;
                    return true;
                }
                tabX += tabWidth;
            }

            int rowTop = top + 68;
            int rowHeight = 25;
            int availableRows = Math.max(1, (panelHeight - 104) / rowHeight);
            List<Module> visible = new ArrayList<>();
            for (Module module : MODULES) {
                if (module.category.equals(selectedCategory)) visible.add(module);
            }

            for (int row = 0; row < availableRows; row++) {
                int index = scrollOffset + row;
                if (index >= visible.size()) break;
                int y = rowTop + row * rowHeight;
                if (mouseY >= y && mouseY <= y + 22 &&
                        mouseX >= left + 10 && mouseX <= left + panelWidth - 10) {
                    Module module = visible.get(index);
                    if (button == 1) {
                        module.cycleSetting();
                    } else if (button == 0) {
                        module.enabled = !module.enabled;
                    }
                    return true;
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount,
                                     double verticalAmount) {
            scrollOffset -= (int) Math.signum(verticalAmount);
            return true;
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
                this.minecraft.setScreen(null);
                return true;
            }
            return super.keyPressed(keyCode, scanCode, modifiers);
        }

        @Override
        public boolean isPauseScreen() {
            return false;
        }
    }
}
