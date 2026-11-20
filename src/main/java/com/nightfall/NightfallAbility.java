package com.nightfall;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

/**
 * Nightfall's one trick: a melee hit with it blinds the target for a few seconds.
 *
 * <p>Done with a Fabric API event rather than an Item override so there is no
 * vanilla method signature to get wrong. To change the ability, edit the lambda below.
 */
public final class NightfallAbility {

	/** Blindness length in ticks (20 ticks = 1 second). */
	private static final int BLIND_TICKS = 3 * 20;

	private NightfallAbility() {
	}

	public static void register() {
		// Runs on the server after an entity takes damage (and survives).
		ServerLivingEntityEvents.AFTER_DAMAGE.register((entity, source, baseDamageTaken, damageTaken, blocked) -> {
			if (blocked || damageTaken <= 0.0F) {
				return;
			}

			// Only melee hits: the thing that dealt the damage is the attacker itself (not an arrow, etc.).
			if (source.getEntity() instanceof LivingEntity attacker
					&& source.getDirectEntity() == attacker
					&& attacker != entity
					&& attacker.getMainHandItem().is(ModItems.NIGHTFALL)) {
				entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, BLIND_TICKS, 0, false, true, true));
			}
		});
	}
}
