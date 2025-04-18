package com.mickey42302.devtools.mixin;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.util.math.BlockPos;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.entity.ai.pathing.TargetPathNode;
import net.minecraft.entity.ai.pathing.PathNode;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Path.class})
public abstract class PathMixin {

    @Shadow
    @Mutable
    @Nullable
    private Path.DebugNodeInfo debugNodeInfos;

    @Inject(method = {"toBuf"}, at = {@At("HEAD")})
    private void devtools$toBuf(PacketByteBuf buf, CallbackInfo ci) {
        this.debugNodeInfos = new Path.DebugNodeInfo(this.nodes.stream().filter(pathNode -> !pathNode.visited).toArray(PathNode[]::new), this.nodes.stream().filter(pathNode -> pathNode.visited).toArray(PathNode[]::new), Set.of(new TargetPathNode(this.target.getX(), this.target.getY(), this.target.getZ())));
    }

    @Shadow
    @Final
    private List<PathNode> nodes;
    @Shadow
    @Final
    private BlockPos target;
}