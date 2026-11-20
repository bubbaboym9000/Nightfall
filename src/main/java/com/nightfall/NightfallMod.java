package com.nightfall;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NightfallMod implements ModInitializer {

	public static final String MOD_ID = "nightfall";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// Loads the item class so the sword gets registered, and adds it to the creative tab.
		ModItems.initialize();
		// Hooks up Nightfall's on-hit blindness.
		NightfallAbility.register();

		LOGGER.info("Nightfall has fallen.");
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
