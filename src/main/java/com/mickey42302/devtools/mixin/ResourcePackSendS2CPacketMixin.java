package com.mickey42302.devtools.mixin;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.text.Text;
import net.minecraft.network.packet.s2c.common.ResourcePackSendS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ResourcePackSendS2CPacket.class})
public abstract class ResourcePackSendS2CPacketMixin {
    @Shadow
    @Mutable
    @Final
    private Optional<Text> prompt;

    @Inject(method = {"<init>"}, at = {@At("TAIL")})
    private void devtools$fixNullPrompt(UUID id, String url, String hash, boolean required, Optional<Text> prompt, CallbackInfo ci) {
        this.prompt = prompt;
    }
}