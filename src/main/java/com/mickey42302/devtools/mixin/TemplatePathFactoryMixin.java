package com.mickey42302.devtools.mixin;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplatePathFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TemplatePathFactory.class)
public class TemplatePathFactoryMixin {

    @ModifyVariable(
            method = "createAndValidatePathToStructure(Lnet/minecraft/resources/Identifier;Lnet/minecraft/resources/FileToIdConverter;)Ljava/nio/file/Path;",
            at = @At("HEAD"),
            argsOnly = true,
            name = "id")
    private Identifier stripDoubleMinecraftPathPrefix(Identifier id) {
        if (id.getPath().startsWith("minecraft/")) {
            String cleanPath = id.getPath().substring("minecraft/".length());
            return Identifier.fromNamespaceAndPath(id.getNamespace(), cleanPath);
        }
        return id;
    }
}