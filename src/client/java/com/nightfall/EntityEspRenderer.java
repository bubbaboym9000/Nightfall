
package com.nightfall;

import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.gizmos.DrawableGizmoPrimitives;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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

        DrawableGizmoPrimitives gizmos =
            new DrawableGizmoPrimitives();

        for (Entity entity : client.level.entitiesForRendering()) {
            if (!(entity instanceof Mob)) {
                continue;
            }

            AABB box = entity.getBoundingBox();

            Vec3 a = new Vec3(box.minX, box.minY, box.minZ);
            Vec3 b = new Vec3(box.maxX, box.minY, box.minZ);
            Vec3 c = new Vec3(box.maxX, box.minY, box.maxZ);
            Vec3 d = new Vec3(box.minX, box.minY, box.maxZ);

            Vec3 e = new Vec3(box.minX, box.maxY, box.minZ);
            Vec3 f = new Vec3(box.maxX, box.maxY, box.minZ);
            Vec3 g = new Vec3(box.maxX, box.maxY, box.maxZ);
            Vec3 h = new Vec3(box.minX, box.maxY, box.maxZ);

            int green = 0xFF00FF00;

            gizmos.addLine(a, b, green, 2.0F);
            gizmos.addLine(b, c, green, 2.0F);
            gizmos.addLine(c, d, green, 2.0F);
            gizmos.addLine(d, a, green, 2.0F);

            gizmos.addLine(e, f, green, 2.0F);
            gizmos.addLine(f, g, green, 2.0F);
            gizmos.addLine(g, h, green, 2.0F);
            gizmos.addLine(h, e, green, 2.0F);

            gizmos.addLine(a, e, green, 2.0F);
            gizmos.addLine(b, f, green, 2.0F);
            gizmos.addLine(c, g, green, 2.0F);
            gizmos.addLine(d, h, green, 2.0F);
        }

        gizmos.submit(
            context.submitNodeCollector(),
            context.levelState().cameraRenderState,
            true
        );
    }
}
