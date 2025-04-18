package com.mickey42302.devtools.mixin;

import net.minecraft.SharedConstants;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SharedConstants.class})
public abstract class SharedConstantsMixin {
    @Shadow
    @Mutable
    public static boolean isDevelopment;

    @Inject(method = {"<clinit>"}, at = {@At("TAIL")})
    private static void devtools$clinit(CallbackInfo ci) {
        isDevelopment = true;
    }
}