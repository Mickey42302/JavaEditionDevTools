package com.mickey42302.devtools.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mickey42302.devtools.screen.DebugRenderersScreen;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Environment(EnvType.CLIENT)
@Mixin({Keyboard.class})
public abstract class KeyboardMixin {

    @Shadow protected abstract void debugLog(Text text);

    @WrapOperation(method = {"onKey"}, at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/Keyboard;processF3(I)Z")})

    private boolean devtools$f3Key(Keyboard keyboard, int key, Operation<Boolean> original) {
        if (key == 296) {
            assert MinecraftClient.getInstance().player != null;
            this.debugLog(Text.translatable("debug.developer_help.message"));
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.sectionpath.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.fog.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.smartcull.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.frustum_culling_octree.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.cycle_renderdistance.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.frustum.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.sectionvisibility.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.wireframe.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.renderers.help"), false);
            MinecraftClient.getInstance().player.sendMessage(Text.translatable("debug.developer_help.help"), false);
        }
        if (key == 295) {
            MinecraftClient.getInstance().setScreen(new DebugRenderersScreen());
        }
        if (key == 82) {
            MinecraftClient client = MinecraftClient.getInstance();
            Integer currentViewDistance = client.options.getViewDistance().getValue();
            if (Screen.hasShiftDown()) {
                assert MinecraftClient.getInstance().player != null;
                if (currentViewDistance <= 31) {
                    client.options.getViewDistance().setValue(currentViewDistance - 1);
                } else {
                    client.options.getViewDistance().setValue(2);
                }
                this.debugLog(Text.translatable("debug.cycle_renderdistance.message", currentViewDistance.toString()));
            }
            else {
                    assert MinecraftClient.getInstance().player != null;
                    if (currentViewDistance <= 31) {
                        client.options.getViewDistance().setValue(currentViewDistance + 1);
                    } else {
                        client.options.getViewDistance().setValue(2);
                    }
                    this.debugLog(Text.translatable("debug.cycle_renderdistance.message", currentViewDistance.toString()));
            }
        }
        return (processDebugKeys(key) || original.call(keyboard, key));
    }

    @Shadow
    protected abstract boolean processDebugKeys(int paramInt);
}
