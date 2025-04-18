package com.mickey42302.devtools.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import net.minecraft.client.gui.screen.Screen;

@Environment(EnvType.CLIENT)
public class DebugRenderersScreen extends Screen {
    public DebugRenderersScreen() {
        super(Text.translatable("debug.renderers.title"));
    }

    protected void init() {
        DebugRendererListWidget listWidget = new DebugRendererListWidget(this.client, this.width, this.height, 0, 22, this);
        this.addDrawableChild(listWidget);
    }

}