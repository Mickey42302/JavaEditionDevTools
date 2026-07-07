package com.mickey42302.devtools.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.nio.file.Path;
import java.util.Optional;
import java.util.function.Consumer;

@Mixin(TestInstanceBlockEntity.class)
public class TestInstanceBlockEntityMixin {

    @Shadow @Final private static Logger LOGGER;

    @Inject(method = "export", at = @At("HEAD"), cancellable = true)
    private static void restoreExportFunctionality(ServerLevel level, Identifier structureId, Consumer<Component> feedbackOutput, CallbackInfoReturnable<Boolean> cir) {
        StructureTemplateManager structureManager = level.getStructureManager();
        Optional<StructureTemplate> structureTemplate = structureManager.get(structureId);

        if (structureTemplate.isEmpty()) {
            feedbackOutput.accept(Component.literal("Could not find structure " + structureId).withStyle(ChatFormatting.RED));
            cir.setReturnValue(true);
            return;
        }

        Path runDir = level.getServer().getServerDirectory();
        Path baseExportDir = runDir.resolve("Minecraft.Server/src/test/convertables/data");

        Path outputFile = baseExportDir
                .resolve(structureId.getNamespace())
                .resolve("structures")
                .resolve(structureId.getPath() + ".snbt");

        try {
            StructureTemplateManager.save(outputFile, structureTemplate.get(), true);
        } catch (Exception e) {
            LOGGER.error("Failed to save structure file {} to {}", structureId, outputFile, e);
            feedbackOutput.accept(Component.literal("Failed to save structure file " + structureId + " to " + outputFile).withStyle(ChatFormatting.RED));
            cir.setReturnValue(true);
            return;
        }

        feedbackOutput.accept(Component.literal("Exported " + structureId + " to " + outputFile.toAbsolutePath()));
        cir.setReturnValue(false);
    }
}