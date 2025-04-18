package com.mickey42302.devtools.screen;

import com.mickey42302.devtools.DevToolsClient;
import com.mickey42302.devtools.util.RendererEntry;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Selectable;
import net.minecraft.text.Text;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ElementListWidget;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.widget.ButtonWidget;

@Environment(EnvType.CLIENT)
public class DebugRendererListWidget extends ElementListWidget<DebugRendererListWidget.Entry> {
    public DebugRendererListWidget(MinecraftClient minecraftClient, int width, int height, int y, int itemHeight, DebugRenderersScreen screen) {
        super(minecraftClient, width, height, y, itemHeight);
        DevToolsClient.RENDERERS.forEach((name, rendererEntry) -> addEntry(new Entry(name, rendererEntry, screen)));
    }

    @Environment(EnvType.CLIENT)
    public static class Entry extends ElementListWidget.Entry<Entry> {
        private final Text name;
        private final List<ClickableWidget> clickableWidgets;

        public Entry(String name, RendererEntry rendererEntry, DebugRenderersScreen screen) {
            this.name = Text.translatable(name);
            this.clickableWidgets = new ArrayList<>();
            this.clickableWidgets.add(ButtonWidget.builder(rendererEntry.getEnabledText(), button -> {
                        rendererEntry.toggle();

                        button.setMessage(rendererEntry.getEnabledText());
                    }).width(40)
                    .position(screen.width / 2 + 70, 0)
                    .build());
        }


        public List<? extends Element> children() {
            return this.clickableWidgets;
        }


        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
            this.clickableWidgets.forEach(widget -> {
                widget.setY(y);
                widget.render(context, mouseX, mouseY, tickDelta);
            });
            context.drawText((MinecraftClient.getInstance()).textRenderer, this.name, x + 50, y + 6, -1, true);
        }

        @Override
        public List<? extends Selectable> selectableChildren() {
            return List.of();
        }
    }

}