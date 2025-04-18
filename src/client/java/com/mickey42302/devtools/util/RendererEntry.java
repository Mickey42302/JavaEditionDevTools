package com.mickey42302.devtools.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class RendererEntry {
    private static final Text ON = Text.translatable("debug.renderers.enabled");
    private static final Text OFF = Text.translatable("debug.renderers.disabled");

    public final Renderer renderer;
    public boolean enabled = false;

    public RendererEntry(RendererEntry.Renderer renderer) {
        this.renderer = renderer;
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ) {
        if (this.enabled) {
            this.renderer.render(matrices, vertexConsumers, cameraX, cameraY, cameraZ);
        }
    }

    public void toggle() {
        this.enabled = !this.enabled;
    }

    public Text getEnabledText() {
        return this.enabled ? ON : OFF;
    }

    @Environment(EnvType.CLIENT)
    public interface Renderer {
        void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, double cameraX, double cameraY, double cameraZ);
    }
}