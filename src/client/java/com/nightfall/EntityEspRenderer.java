
package com.nightfall;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class EntityEspRenderer {

    public static void register() {
        LevelRenderEvents.COLLECT_SUBMITS.register(
            EntityEspRenderer::render
        );
    }

    private static void render(LevelRenderContext context) {

        if (!ClickGuiScreen.espEnabled) {
            return;
        }

        Minecraft client = Minecraft.getInstance();

        if (client.level == null || client.player == null) {
            return;
        }

        Vec3 camera = context.levelState().cameraRenderState.pos;
        PoseStack matrices = context.poseStack();

        for (Entity entity : client.level.entitiesForRendering()) {

            if (!(entity instanceof Mob)) {
                continue;
            }

            var box = entity.getBoundingBox().move(
                -entity.getX(),
                -entity.getY(),
                -entity.getZ()
            );

            matrices.pushPose();

            matrices.translate(
                entity.getX() - camera.x,
                entity.getY() - camera.y,
                entity.getZ() - camera.z
            );

            context.submitNodeCollector().submitShapeOutline(
                matrices,
                Shapes.create(box),
                RenderTypes.linesTranslucent(),
                0xFF00FF00,
                2.0F,
                false
            );

            matrices.popPose();
        }
    }
}
