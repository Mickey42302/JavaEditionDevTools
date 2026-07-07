package com.mickey42302.devtools.mixin;

import com.google.common.collect.ImmutableList;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.DataFixer;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplatePathFactory;
import net.minecraft.world.level.levelgen.structure.templatesystem.loader.TemplateSource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.datafix.DataFixTypes;
import org.apache.commons.io.IOUtils;
import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Mixin(StructureTemplateManager.class)
public abstract class StructureTemplateManagerMixin {

    @Shadow @Final private static Logger LOGGER;

    @Mutable
    @Final
    @Shadow private TemplatePathFactory testTemplates;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void restoreTestExportFactory(
            ResourceManager resourceManager,
            LevelStorageSource.LevelStorageAccess storage,
            DataFixer fixerUpper,
            HolderGetter<Block> blockLookup,
            CallbackInfo ci
    ) {
        if (this.testTemplates == null) {
            Path exportPath = Paths.get("Minecraft.Server/src/test/convertables/data");
            this.testTemplates = new TemplatePathFactory(exportPath, PackType.SERVER_DATA);
        }
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/collect/ImmutableList$Builder;build()Lcom/google/common/collect/ImmutableList;"
            )
    )
    private void addTestExportDirectorySource(
            ResourceManager resourceManager,
            LevelStorageSource.LevelStorageAccess storage,
            DataFixer fixerUpper,
            HolderGetter<Block> blockLookup,
            CallbackInfo ci,
            @Local(name = "sources") ImmutableList.Builder<TemplateSource> sources
    ) {
        sources.add(new TemplateSource(fixerUpper, blockLookup) {
            private final Path exportPath = Paths.get("Minecraft.Server/src/test/convertables/data");

            @Override
            public @NonNull Optional<StructureTemplate> load(@NonNull Identifier id) {
                String pathString = id.getPath();

                if (pathString.startsWith("minecraft/structure/")) {
                    pathString = pathString.substring("minecraft/structure/".length());
                }

                Path file = exportPath.resolve("minecraft").resolve("structures").resolve(pathString + ".snbt");
                if (!Files.exists(file)) {
                    return Optional.empty();
                }

                try (BufferedReader reader = Files.newBufferedReader(file)) {
                    String input = IOUtils.toString(reader);
                    CompoundTag tag = NbtUtils.snbtToStructure(input);

                    StructureTemplate structureTemplate = new StructureTemplate();
                    int version = NbtUtils.getDataVersion(tag, 500);
                    structureTemplate.load(blockLookup, DataFixTypes.STRUCTURE.updateToCurrentVersion(fixerUpper, tag, version));

                    return Optional.of(structureTemplate);
                } catch (Exception e) {
                    LOGGER.error("Failed to load test template from {}", file, e);
                    return Optional.empty();
                }
            }

            @Override
            public @NonNull Stream<Identifier> list() {
                Path searchFolder = exportPath.resolve("minecraft").resolve("structures");
                if (!Files.isDirectory(searchFolder)) {
                    return Stream.empty();
                }

                List<Identifier> foundTemplates = new ArrayList<>();
                try (Stream<Path> walk = Files.walk(searchFolder)) {
                    walk.filter(Files::isRegularFile)
                            .filter(p -> p.toString().endsWith(".snbt"))
                            .forEach(p -> {
                                String relative = searchFolder.relativize(p).toString().replace("\\", "/");
                                String cleanName = relative.substring(0, relative.length() - ".snbt".length());

                                foundTemplates.add(Identifier.fromNamespaceAndPath("minecraft", "minecraft/structure/" + cleanName));
                            });
                } catch (IOException e) {
                    LOGGER.error("Failed to list files in custom export folder", e);
                }
                return foundTemplates.stream();
            }
        });
    }
}