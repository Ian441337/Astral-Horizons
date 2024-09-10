package wuzu.ian.astralhorizons.procedures;

import org.joml.Matrix4f;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.api.distmarker.Dist;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Minecraft;

import javax.annotation.Nullable;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexBuffer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PlanetsProcedure {
	private static boolean texture = false;
	private static BufferBuilder bufferBuilder = null;
	private static VertexBuffer vertexBuffer = null;
	private static PoseStack poseStack = null;
	private static Matrix4f projectionMatrix = null;
	private static boolean worldCoordinate = true;
	private static Vec3 offset = Vec3.ZERO;
	private static float partialTick = 0.0F;
	private static int ticks = 0;
	private static int currentStage = 0;
	private static int targetStage = 0; // NONE: 0, SKY: 1, WORLD: 2

	private static void add(double x, double y, double z, float u, float v, int color) {
		if (bufferBuilder == null || !bufferBuilder.building())
			return;
		if (texture) {
			bufferBuilder.vertex(x, y, z).uv(u, v).color(color).endVertex();
		} else {
			bufferBuilder.vertex(x, y, z).color(color).endVertex();
		}
	}

	private static boolean begin(VertexFormat.Mode mode, boolean texture, boolean update) {
		if (bufferBuilder == null || !bufferBuilder.building()) {
			if (update)
				clear();
			if (vertexBuffer == null) {
				PlanetsProcedure.texture = texture;
				bufferBuilder = Tesselator.getInstance().getBuilder();
				if (texture) {
					bufferBuilder.begin(mode, DefaultVertexFormat.POSITION_TEX_COLOR);
				} else {
					bufferBuilder.begin(mode, DefaultVertexFormat.POSITION_COLOR);
				}
				return true;
			}
		}
		return false;
	}

	private static void clear() {
		if (vertexBuffer != null) {
			vertexBuffer.close();
			vertexBuffer = null;
		}
	}

	private static void end() {
		if (bufferBuilder == null || !bufferBuilder.building())
			return;
		if (vertexBuffer != null)
			vertexBuffer.close();
		vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
		vertexBuffer.bind();
		vertexBuffer.upload(bufferBuilder.end());
		VertexBuffer.unbind();
	}

	private static void offset(double x, double y, double z) {
		offset = new Vec3(x, y, z);
	}

	private static void release() {
		targetStage = 0;
	}

	private static VertexBuffer shape() {
		return vertexBuffer;
	}

	private static void system(boolean worldCoordinate) {
		PlanetsProcedure.worldCoordinate = worldCoordinate;
	}

	private static boolean target(int targetStage) {
		if (targetStage == currentStage) {
			PlanetsProcedure.targetStage = targetStage;
			return true;
		}
		return false;
	}

	private static void renderShape(VertexBuffer vertexBuffer, double x, double y, double z, float yaw, float pitch, float roll, float xScale, float yScale, float zScale, int color) {
		if (currentStage == 0 || currentStage != targetStage)
			return;
		if (poseStack == null || projectionMatrix == null)
			return;
		if (vertexBuffer == null)
			return;
		float i, j, k;
		if (!worldCoordinate) {
			i = (float) x;
			j = (float) y;
			k = (float) z;
		} else {
			Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
			i = (float) (x - pos.x());
			j = (float) (y - pos.y());
			k = (float) (z - pos.z());
		}
		poseStack.pushPose();
		poseStack.translate(i, j, k);
		poseStack.mulPose(com.mojang.math.Axis.YN.rotationDegrees(yaw));
		poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(pitch));
		poseStack.mulPose(com.mojang.math.Axis.ZN.rotationDegrees(roll));
		poseStack.scale(xScale, yScale, zScale);
		poseStack.translate(offset.x(), offset.y(), offset.z());
		RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F, (color & 255) / 255.0F, (color >>> 24) / 255.0F);
		vertexBuffer.bind();
		Matrix4f matrix4f = poseStack.last().pose();
		vertexBuffer.drawWithShader(matrix4f, projectionMatrix, vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader() : GameRenderer.getPositionColorShader());
		VertexBuffer.unbind();
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		poseStack.popPose();
	}

	@SubscribeEvent
	public static void renderLevel(RenderLevelStageEvent event) {
		poseStack = event.getPoseStack();
		projectionMatrix = event.getProjectionMatrix();
		partialTick = event.getPartialTick();
		ticks = event.getRenderTick();
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
			currentStage = 1;
			RenderSystem.depthMask(false);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
		} else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
			currentStage = 2;
			RenderSystem.depthMask(true);
			renderShapes(event);
			RenderSystem.enableCull();
			RenderSystem.depthMask(true);
		}
		currentStage = 0;
	}

	private static void renderShapes(Event event) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
		if (level != null && entity != null) {
			Vec3 pos = entity.getPosition(partialTick);
			RenderSystem.enableBlend();
			RenderSystem.defaultBlendFunc();
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			execute(event, level);
			RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
			RenderSystem.defaultBlendFunc();
			RenderSystem.disableBlend();
			RenderSystem.enableDepthTest();
		}
	}

	public static void execute(LevelAccessor world) {
		execute(null, world);
	}

	private static void execute(@Nullable Event event, LevelAccessor world) {
		double i = 0;
		double j = 0;
		double k = 0;
		double l = 0;
		if ((world instanceof Level _lvl ? _lvl.dimension() : Level.OVERWORLD) == (ResourceKey.create(Registries.DIMENSION, new ResourceLocation("astral_horizons:space")))) {
			if (begin(VertexFormat.Mode.QUADS, false, false)) {
				for (int index0 = 0; index0 < 90; index0++) {
					for (int index1 = 0; index1 < 45; index1++) {
						k = 255 - (j / 180) * 95;
						l = 255 - ((j + 4) / 180) * 95;
						add((Math.sin(Math.toRadians(i)) * Math.sin(Math.toRadians(j)) * 0.5), (Math.cos(Math.toRadians(j)) * 0.5), (Math.cos(Math.toRadians(i)) * Math.sin(Math.toRadians(j)) * 0.5), 0.0F, 0.0F,
								255 << 24 | (int) k << 16 | (int) k << 8 | (int) k);
						add((Math.sin(Math.toRadians(i)) * Math.sin(Math.toRadians(j + 4)) * 0.5), (Math.cos(Math.toRadians(j + 4)) * 0.5), (Math.cos(Math.toRadians(i)) * Math.sin(Math.toRadians(j + 4)) * 0.5), 0.0F, 0.0F,
								255 << 24 | (int) l << 16 | (int) l << 8 | (int) l);
						add((Math.sin(Math.toRadians(i + 4)) * Math.sin(Math.toRadians(j + 4)) * 0.5), (Math.cos(Math.toRadians(j + 4)) * 0.5), (Math.cos(Math.toRadians(i + 4)) * Math.sin(Math.toRadians(j + 4)) * 0.5), 0.0F, 0.0F,
								255 << 24 | (int) l << 16 | (int) l << 8 | (int) l);
						add((Math.sin(Math.toRadians(i + 4)) * Math.sin(Math.toRadians(j)) * 0.5), (Math.cos(Math.toRadians(j)) * 0.5), (Math.cos(Math.toRadians(i + 4)) * Math.sin(Math.toRadians(j)) * 0.5), 0.0F, 0.0F,
								255 << 24 | (int) k << 16 | (int) k << 8 | (int) k);
						j = j + 4;
					}
					j = 0;
					i = i + 4;
				}
				i = 0;
				end();
			}
			if (target(2)) {
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 0, 0, 0, 0, 0, 0, 1500, 1500, 1500, 255 << 24 | 0 << 16 | 255 << 8 | 255);
				release();
			}
			if (target(2)) {
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 1800, 0, 0, 0, 0, 0, 100, 100, 100, 255 << 24 | 255 << 16 | 0 << 8 | 255);
				release();
			}
			if (target(2)) {
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 2000, 0, 0, 0, 0, 0, 150, 150, 150, 255 << 24 | 255 << 16 | 255 << 8 | 0);
				release();
			}
			if (target(2)) {
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 2500, 0, 0, 0, 0, 0, 300, 300, 300, 100 << 24 | 255 << 16 | 0 << 8 | 0);
				release();
			}
			if (target(2)) {
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 3000, 0, 0, 0, 0, 0, 400, 400, 400, 255 << 24 | 0 << 16 | 255 << 8 | 0);
				release();
			}
			if (target(2)) {
				RenderSystem.defaultBlendFunc();
				RenderSystem.setShaderTexture(0, new ResourceLocation(("astral_horizons" + ":textures/" + "logo" + ".png")));
				renderShape((shape()), 3500, 0, 0, 0, 0, 0, 350, 350, 350, 255 << 24 | 0 << 16 | 0 << 8 | 255);
				release();
			}
		}
	}
}
