package com.mickey42302.devtools.mixin;

import com.mickey42302.devtools.DevToolsClient;

import java.util.Objects;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.debug.*;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin({DebugRenderer.class})
public abstract class DebugRendererMixin {
    @Shadow
    @Final
    public PathfindingDebugRenderer pathfindingDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer waterDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer heightmapDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer collisionDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer supportingBlockDebugRenderer;
    @Shadow
    @Final
    public NeighborUpdateDebugRenderer neighborUpdateDebugRenderer;
    @Shadow
    @Final
    public StructureDebugRenderer structureDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer skyLightDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer worldGenAttemptDebugRenderer;

    @Inject(method = {"render"}, at = {@At("TAIL")})
    private void devtools$render(MatrixStack matrices, Frustum frustum, VertexConsumerProvider.Immediate vertexConsumers, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        DevToolsClient.RENDERERS.values().forEach(rendererEntry -> rendererEntry.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ));
    }

    @Shadow
    @Final
    public DebugRenderer.Renderer blockOutlineDebugRenderer;
    @Shadow
    @Final
    public DebugRenderer.Renderer chunkLoadingDebugRenderer;
    @Shadow
    @Final
    public VillageDebugRenderer villageDebugRenderer;
    @Shadow
    @Final
    public VillageSectionsDebugRenderer villageSectionsDebugRenderer;
    @Shadow
    @Final
    public BeeDebugRenderer beeDebugRenderer;
    @Shadow
    @Final
    public RaidCenterDebugRenderer raidCenterDebugRenderer;
    @Shadow
    @Final
    public GameEventDebugRenderer gameEventDebugRenderer;
    @Shadow
    @Final
    public LightDebugRenderer lightDebugRenderer;
    @Shadow
    @Final
    public BreezeDebugRenderer breezeDebugRenderer;
    @Shadow
    @Final
    public GoalSelectorDebugRenderer goalSelectorDebugRenderer;

    @Shadow
    @Final
    public RedstoneUpdateOrderDebugRenderer redstoneUpdateOrderDebugRenderer;

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void devtools$init(MinecraftClient client, CallbackInfo ci) {
        DevToolsClient.addRenderer("debug.renderers.bee", this.beeDebugRenderer::render);
        Objects.requireNonNull(this.beeDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.breeze", this.breezeDebugRenderer::render);
        Objects.requireNonNull(this.breezeDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.block_outline", this.blockOutlineDebugRenderer::render);
        Objects.requireNonNull(this.blockOutlineDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.chunk_loading", this.chunkLoadingDebugRenderer::render);
        Objects.requireNonNull(this.chunkLoadingDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.collision", this.collisionDebugRenderer::render);
        Objects.requireNonNull(this.collisionDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.game_event", this.gameEventDebugRenderer::render);
        Objects.requireNonNull(this.gameEventDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.goal", this.goalSelectorDebugRenderer::render);
        Objects.requireNonNull(this.goalSelectorDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.heightmap", this.heightmapDebugRenderer::render);
        Objects.requireNonNull(this.heightmapDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.light", this.lightDebugRenderer::render);
        Objects.requireNonNull(this.lightDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.neighbor_update", this.neighborUpdateDebugRenderer::render);
        Objects.requireNonNull(this.neighborUpdateDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.pathfinding", this.pathfindingDebugRenderer::render);
        Objects.requireNonNull(this.pathfindingDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.raid_center", this.raidCenterDebugRenderer::render);
        Objects.requireNonNull(this.raidCenterDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.sky_light", this.skyLightDebugRenderer::render);
        Objects.requireNonNull(this.skyLightDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.structure", this.structureDebugRenderer::render);
        Objects.requireNonNull(this.structureDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.supporting_block", this.supportingBlockDebugRenderer::render);
        Objects.requireNonNull(this.supportingBlockDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.village", this.villageDebugRenderer::render);
        Objects.requireNonNull(this.villageDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.village_sections", this.villageSectionsDebugRenderer::render);
        Objects.requireNonNull(this.villageSectionsDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.water", this.waterDebugRenderer::render);
        Objects.requireNonNull(this.waterDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.world_gen_attempt", this.worldGenAttemptDebugRenderer::render);
        Objects.requireNonNull(this.worldGenAttemptDebugRenderer);

        DevToolsClient.addRenderer("debug.renderers.redstone_update_order", this.redstoneUpdateOrderDebugRenderer::render);
        Objects.requireNonNull(this.redstoneUpdateOrderDebugRenderer);
    }

}