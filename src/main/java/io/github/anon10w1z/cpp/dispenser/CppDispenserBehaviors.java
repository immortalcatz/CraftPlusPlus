package io.github.anon10w1z.cpp.dispenser;

import io.github.anon10w1z.cpp.handlers.CppConfigHandler;
import io.github.anon10w1z.cpp.items.CppItems;
import io.github.anon10w1z.cpp.main.CraftPlusPlus;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDispenser;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.client.resources.I18n;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemFlintAndSteel;

import java.util.Arrays;
import java.util.List;

public class CppDispenserBehaviors {
	/**
	 * Registers the dispenser behaviors for Craft++
	 */
	@SuppressWarnings("unchecked")
	public static void registerDispenserBehaviors() {
		Iterable<Item> items = Item.itemRegistry;
		for (Item item : items) {
			//Falling Blocks
			if (item instanceof ItemBlock) {
				Block block = ((ItemBlock) item).block;
				List<Material> fallingMaterials = Arrays.asList(Material.sand, Material.ground, Material.clay, Material.snow, Material.craftedSnow);
				if ((block instanceof BlockFalling || CppConfigHandler.additionalFallingBlocks.contains(block)) && fallingMaterials.contains(block.getDefaultState().getMaterial()))
					registerDispenserBehavior(item, new BehaviorDispenseBlockFalling());
			}
			//Flint And Steel (default behavior)
			if (item instanceof ItemFlintAndSteel && !CppConfigHandler.enableFlintAndSteelDispenserBehavior)
				registerDispenserBehavior(item, new BehaviorDefaultDispenseItem());
		}
		registerDispenserBehavior(CppItems.dynamite, new BehaviorDispenseDynamite());
		registerDispenserBehavior(CppItems.obsidian_boat, new BehaviorDispenseObsidianBoat());
	}

	/**
	 * Registers a dispenser behavior for an item with the game
	 *
	 * @param item              The item dispensed by the dispenser
	 * @param dispenserBehavior The dispenser behavior carried out for the item
	 */
	private static void registerDispenserBehavior(Item item, BehaviorDefaultDispenseItem dispenserBehavior) {
		String localizedName = I18n.format(item.getUnlocalizedName() + ".name");
		if (dispenserBehavior.getClass() != BehaviorDefaultDispenseItem.class)
			CraftPlusPlus.logInfo("Registering dispenser behavior for " + localizedName);
		else
			CraftPlusPlus.logInfo("Registering default dispenser behavior for " + localizedName);
		BlockDispenser.dispenseBehaviorRegistry.putObject(item, dispenserBehavior);
	}
}
