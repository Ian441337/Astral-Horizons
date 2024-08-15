
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package wuzu.ian.astralhorizons.init;

import wuzu.ian.astralhorizons.AstralHorizonsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;

public class AstralHorizonsModTabs {
	public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, AstralHorizonsMod.MODID);
	public static final RegistryObject<CreativeModeTab> ASTRAL_HORIZONS = REGISTRY.register("astral_horizons",
			() -> CreativeModeTab.builder().title(Component.translatable("item_group.astral_horizons.astral_horizons")).icon(() -> new ItemStack(AstralHorizonsModBlocks.ITEMCREATIVETAB.get())).displayItems((parameters, tabData) -> {
				tabData.accept(AstralHorizonsModBlocks.ARCFURNES.get().asItem());
				tabData.accept(AstralHorizonsModItems.IRONPLATTES.get());
				tabData.accept(AstralHorizonsModItems.STEELINGOT.get());
				tabData.accept(AstralHorizonsModItems.STEELPLATTES.get());
				tabData.accept(AstralHorizonsModItems.ALUMINIUMINGOT.get());
				tabData.accept(AstralHorizonsModItems.ALUMINIUMPLATE.get());
				tabData.accept(AstralHorizonsModBlocks.MOONSAND.get().asItem());
				tabData.accept(AstralHorizonsModBlocks.MOONSTONE.get().asItem());
				tabData.accept(AstralHorizonsModBlocks.MOONDEEPSLATE.get().asItem());
			})

					.build());
}
