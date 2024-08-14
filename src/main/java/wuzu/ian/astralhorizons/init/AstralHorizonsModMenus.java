
/*
 *	MCreator note: This file will be REGENERATED on each build.
 */
package wuzu.ian.astralhorizons.init;

import wuzu.ian.astralhorizons.world.inventory.ArcfurnesguiMenu;
import wuzu.ian.astralhorizons.AstralHorizonsMod;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.common.extensions.IForgeMenuType;

import net.minecraft.world.inventory.MenuType;

public class AstralHorizonsModMenus {
	public static final DeferredRegister<MenuType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.MENU_TYPES, AstralHorizonsMod.MODID);
	public static final RegistryObject<MenuType<ArcfurnesguiMenu>> ARCFURNESGUI = REGISTRY.register("arcfurnesgui", () -> IForgeMenuType.create(ArcfurnesguiMenu::new));
}
