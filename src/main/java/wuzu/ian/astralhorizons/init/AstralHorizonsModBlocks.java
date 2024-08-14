
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package wuzu.ian.astralhorizons.init;

import wuzu.ian.astralhorizons.block.MoonstoneBlock;
import wuzu.ian.astralhorizons.block.MoonsandBlock;
import wuzu.ian.astralhorizons.block.MoondeepslateBlock;
import wuzu.ian.astralhorizons.block.ItemcreativetabBlock;
import wuzu.ian.astralhorizons.block.ArcfurnesBlock;
import wuzu.ian.astralhorizons.AstralHorizonsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.level.block.Block;

public class AstralHorizonsModBlocks {
	public static final DeferredRegister<Block> REGISTRY = DeferredRegister.create(ForgeRegistries.BLOCKS, AstralHorizonsMod.MODID);
	public static final RegistryObject<Block> MOONSTONE = REGISTRY.register("moonstone", () -> new MoonstoneBlock());
	public static final RegistryObject<Block> MOONSAND = REGISTRY.register("moonsand", () -> new MoonsandBlock());
	public static final RegistryObject<Block> ITEMCREATIVETAB = REGISTRY.register("itemcreativetab", () -> new ItemcreativetabBlock());
	public static final RegistryObject<Block> MOONDEEPSLATE = REGISTRY.register("moondeepslate", () -> new MoondeepslateBlock());
	public static final RegistryObject<Block> ARCFURNES = REGISTRY.register("arcfurnes", () -> new ArcfurnesBlock());
}
