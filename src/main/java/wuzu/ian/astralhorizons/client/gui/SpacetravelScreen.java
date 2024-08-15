package wuzu.ian.astralhorizons.client.gui;

import wuzu.ian.astralhorizons.world.inventory.SpacetravelMenu;
import wuzu.ian.astralhorizons.network.SpacetravelButtonMessage;
import wuzu.ian.astralhorizons.AstralHorizonsMod;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.GuiGraphics;

import java.util.HashMap;

import com.mojang.blaze3d.systems.RenderSystem;

public class SpacetravelScreen extends AbstractContainerScreen<SpacetravelMenu> {
	private final static HashMap<String, Object> guistate = SpacetravelMenu.guistate;
	private final Level world;
	private final int x, y, z;
	private final Player entity;
	Button button_earth;
	Button button_moon;

	public SpacetravelScreen(SpacetravelMenu container, Inventory inventory, Component text) {
		super(container, inventory, text);
		this.world = container.world;
		this.x = container.x;
		this.y = container.y;
		this.z = container.z;
		this.entity = container.entity;
		this.imageWidth = 176;
		this.imageHeight = 166;
	}

	private static final ResourceLocation texture = new ResourceLocation("astral_horizons:textures/screens/spacetravel.png");

	@Override
	public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
		RenderSystem.setShaderColor(1, 1, 1, 1);
		RenderSystem.enableBlend();
		RenderSystem.defaultBlendFunc();
		guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
		RenderSystem.disableBlend();
	}

	@Override
	public boolean keyPressed(int key, int b, int c) {
		if (key == 256) {
			this.minecraft.player.closeContainer();
			return true;
		}
		return super.keyPressed(key, b, c);
	}

	@Override
	public void containerTick() {
		super.containerTick();
	}

	@Override
	protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
	}

	@Override
	public void onClose() {
		super.onClose();
	}

	@Override
	public void init() {
		super.init();
		button_earth = Button.builder(Component.translatable("gui.astral_horizons.spacetravel.button_earth"), e -> {
			if (true) {
				AstralHorizonsMod.PACKET_HANDLER.sendToServer(new SpacetravelButtonMessage(0, x, y, z));
				SpacetravelButtonMessage.handleButtonAction(entity, 0, x, y, z);
			}
		}).bounds(this.leftPos + 5, this.topPos + 5, 51, 20).build();
		guistate.put("button:button_earth", button_earth);
		this.addRenderableWidget(button_earth);
		button_moon = Button.builder(Component.translatable("gui.astral_horizons.spacetravel.button_moon"), e -> {
			if (true) {
				AstralHorizonsMod.PACKET_HANDLER.sendToServer(new SpacetravelButtonMessage(1, x, y, z));
				SpacetravelButtonMessage.handleButtonAction(entity, 1, x, y, z);
			}
		}).bounds(this.leftPos + 5, this.topPos + 29, 46, 20).build();
		guistate.put("button:button_moon", button_moon);
		this.addRenderableWidget(button_moon);
	}
}
