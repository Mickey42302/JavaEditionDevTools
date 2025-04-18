package com.mickey42302.devtools;

import com.mickey42302.devtools.util.RendererEntry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class DevToolsClient implements ClientModInitializer {

    public static final Map<String, RendererEntry> RENDERERS = new HashMap<>();

    public static void addRenderer(String name, RendererEntry.Renderer renderer) {
        RENDERERS.put(name, new RendererEntry(renderer));
    }

    public void onInitializeClient() {
    }
}
