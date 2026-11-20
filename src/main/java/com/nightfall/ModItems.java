package com.nightfall;

import java.util.function.Function;

import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;

public class ModItems {

	// Tags (their JSON lives in src/main/resources/data/nightfall/tags/...).
	public static final TagKey<Block> INCORRECT_FOR_NIGHTFALL_TOOL =
			TagKey.create(Registries.BLOCK, NightfallMod.id("incorrect_for_nightfall_tool"));
	public static final TagKey<Item> REPAIRS_NIGHTFALL =
			TagKey.create(Registries.ITEM, NightfallMod.id("repairs_nightfall"));

	// Netherite is 2031 durability, 4.0 attack bonus, 15 enchantability.
	// Nightfall: a bit more durable, hits harder (tooltip shows 9 vs netherite's 8), better enchantability.
	public static final ToolMaterial NIGHTFALL_MATERIAL = new ToolMaterial(
			INCORRECT_FOR_NIGHTFALL_TOOL, // blocks this tool can't harvest
			2500, // durability
			9.0F, // mining speed (irrelevant for a sword, required by the constructor)
			5.0F, // attack damage bonus
			20, // enchantment value
			REPAIRS_NIGHTFALL // repair items
	);

	public static final ResourceKey<Item> NIGHTFALL_KEY =
			ResourceKey.create(Registries.ITEM, NightfallMod.id("nightfall"));

	public static final Item NIGHTFALL = register(
			NIGHTFALL_KEY,
			Item::new,
			// sword(material, attack damage, attack speed): same base numbers as a vanilla sword.
			new Item.Properties().sword(NIGHTFALL_MATERIAL, 3.0F, -2.4F).rarity(Rarity.EPIC)
	);

	public static Item register(ResourceKey<Item> key, Function<Item.Properties, Item> factory, Item.Properties properties) {
		// The id has to be on the Properties before the Item is constructed.
		Item item = factory.apply(properties.setId(key));
		Registry.register(BuiltInRegistries.ITEM, key, item);
		return item;
	}

	public static void initialize() {
		CreativeModeTabEvents.modifyOutputEvent(CreativeModeTabs.COMBAT)
				.register(tab -> tab.accept(NIGHTFALL));
	}
}
