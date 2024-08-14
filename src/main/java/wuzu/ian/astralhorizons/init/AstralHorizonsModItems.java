
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package wuzu.ian.astralhorizons.init;

import wuzu.ian.astralhorizons.AstralHorizonsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.BlockItem;

public class AstralHorizonsModItems {
	public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, AstralHorizonsMod.MODID);
	public static final RegistryObject<Item> MOONSTONE = block(AstralHorizonsModBlocks.MOONSTONE);
	public static final RegistryObject<Item> MOONSAND = block(AstralHorizonsModBlocks.MOONSAND);
	public static final RegistryObject<Item> ITEMCREATIVETAB = block(AstralHorizonsModBlocks.ITEMCREATIVETAB);

	private static RegistryObject<Item> block(RegistryObject<Block> block) {
		return REGISTRY.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
	}
}
