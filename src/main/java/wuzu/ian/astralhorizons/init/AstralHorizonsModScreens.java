
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package wuzu.ian.astralhorizons.init;

import wuzu.ian.astralhorizons.client.gui.SpacetravelScreen;
import wuzu.ian.astralhorizons.client.gui.ArcFurnaceScreen;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.client.gui.screens.MenuScreens;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class AstralHorizonsModScreens {
	@SubscribeEvent
	public static void clientLoad(FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			MenuScreens.register(AstralHorizonsModMenus.ARC_FURNACE.get(), ArcFurnaceScreen::new);
			MenuScreens.register(AstralHorizonsModMenus.SPACETRAVEL.get(), SpacetravelScreen::new);
		});
	}
}
