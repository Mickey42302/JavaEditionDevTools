package com.mickey42302.devtools.mixin;

import java.util.*;
import java.util.stream.Collectors;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.custom.*;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.server.network.DebugInfoSender;
import net.minecraft.util.math.*;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.block.WireOrientation;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.village.raid.Raid;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.event.listener.GameEventListener;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.entity.mob.BreezeEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({DebugInfoSender.class})
public abstract class DebugInfoSenderMixin {

    @Shadow
    private static void sendToAll(ServerWorld world, CustomPayload payload) {
        throw new AssertionError();
    }

    @Shadow
    private static List<String> listMemories(LivingEntity entity, long currentTime) {
        throw new AssertionError();
    }

    @Inject(method = {"sendChunkWatchingChange"}, at = {@At("HEAD")})
    private static void devtools$sendChunkWatchingChange(ServerWorld world, ChunkPos pos, CallbackInfo ci) {
        sendToAll(world, new DebugWorldgenAttemptCustomPayload(pos.getStartPos().up(100), 1.0F, 1.0F, 1.0F, 1.0F, 1.0F));
    }


    @Inject(method = {"sendPoiAddition"}, at = {@At("HEAD")})
    private static void devtools$sendPoiAddition(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        world.getPointOfInterestStorage().getType(pos).ifPresent(registryEntry -> {
            int tickets = world.getPointOfInterestStorage().getFreeTickets(pos);
            String name = registryEntry.getIdAsString();
            sendToAll(world, new DebugPoiAddedCustomPayload(pos, name, tickets));
        });
    }

    @Inject(method = {"sendPoiRemoval"}, at = {@At("HEAD")})
    private static void devtools$sendPoiRemoval(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        sendToAll(world, new DebugPoiRemovedCustomPayload(pos));
    }


    @Inject(method = {"sendPointOfInterest"}, at = {@At("HEAD")})
    private static void devtools$sendPointOfInterest(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        int tickets = world.getPointOfInterestStorage().getFreeTickets(pos);
        sendToAll(world, new DebugPoiTicketCountCustomPayload(pos, tickets));
    }

    @Inject(method = {"sendPoi"}, at = {@At("HEAD")})
    private static void devtools$sendPoi(ServerWorld world, BlockPos pos, CallbackInfo ci) {
        Registry<Structure> registry = world.getRegistryManager().getOrThrow(RegistryKeys.STRUCTURE);
        ChunkSectionPos chunkSectionPos = ChunkSectionPos.from(pos);
        for (RegistryEntry<Structure> entry : registry.iterateEntries(StructureTags.VILLAGE)) {
            if (!world.getStructureAccessor().getStructureStarts(chunkSectionPos, entry.value()).isEmpty()) {
                sendToAll(world, new DebugVillageSectionsCustomPayload(Set.of(chunkSectionPos), Set.of()));
                return;
            }
        }
        sendToAll(world, new DebugVillageSectionsCustomPayload(Set.of(), Set.of(chunkSectionPos)));
    }

    @Inject(method = {"sendPathfindingData"}, at = {@At("HEAD")})
    private static void devtools$sendPathfindingData(World world, MobEntity mob, @Nullable Path path, float nodeReachProximity, CallbackInfo ci) {
        if (path != null) {
            sendToAll((ServerWorld) world, new DebugPathCustomPayload(mob.getId(), path, nodeReachProximity));
        }
    }

    @Inject(method = {"sendNeighborUpdate"}, at = {@At("HEAD")})
    private static void devtools$sendNeighborUpdate(World world, BlockPos pos, CallbackInfo ci) {
        if (!world.isClient) {
            sendToAll((ServerWorld) world, new DebugNeighborsUpdateCustomPayload(world.getTime(), pos));
        }
    }

    @Inject(method = {"sendStructureStart"}, at = {@At("HEAD")})
    private static void devtools$sendStructureStart(StructureWorldAccess world, StructureStart structureStart, CallbackInfo ci) {
        List<DebugStructuresCustomPayload.Piece> pieces = new ArrayList<>();
        for (int i = 0; i < structureStart.getChildren().size(); i++) {
            pieces.add(new DebugStructuresCustomPayload.Piece(structureStart.getChildren().get(i).getBoundingBox(), (i == 0)));
        }
        ServerWorld serverWorld = world.toServerWorld();
        sendToAll(serverWorld, new DebugStructuresCustomPayload(serverWorld
                .getRegistryKey(), structureStart
                .getBoundingBox(), pieces));
    }

    @Inject(method = {"sendGoalSelector"}, at = {@At("HEAD")})
    private static void devtools$sendGoalSelector(World world, MobEntity mob, GoalSelector goalSelector, CallbackInfo ci) {
        List<DebugGoalSelectorCustomPayload.Goal> goals = ((MobEntityAccessor) mob).getGoalSelector().getGoals().stream().map(goal -> new DebugGoalSelectorCustomPayload.Goal(goal.getPriority(), goal.isRunning(), goal.getGoal().toString())).toList();
        sendToAll((ServerWorld) world, new DebugGoalSelectorCustomPayload(mob.getId(), mob.getBlockPos(), goals));
    }

    @Inject(method = {"sendRaids"}, at = {@At("HEAD")})
    private static void devtools$sendRaids(ServerWorld world, Collection<Raid> raids, CallbackInfo ci) {
        sendToAll(world, new DebugRaidsCustomPayload(raids.stream().map(Raid::getCenter).toList()));
    }

    @Inject(method = {"sendBrainDebugData"}, at = {@At("HEAD")})
    private static void devtools$sendBrainDebugData(LivingEntity living, CallbackInfo ci) {
        MobEntity entity = (MobEntity) living;
        ServerWorld serverWorld = (ServerWorld) entity.getWorld();
        int angerLevel = 0;
        String profession;
        int xp;
        String inventory;
        if(entity instanceof WardenEntity wardenEntity) {
            angerLevel = wardenEntity.getAnger();
        }
        List<String> gossips = new ArrayList<>();
        Set<BlockPos> pois = new HashSet<>();
        Set<BlockPos> potentialPois = new HashSet<>();
        profession = "";
        xp = 0;
        inventory = "";

        sendToAll(serverWorld, new DebugBrainCustomPayload(new DebugBrainCustomPayload.Brain(entity
                .getUuid(), entity
                .getId(), entity
                .getName().getString(), profession, xp, entity


                .getHealth(), entity
                .getMaxHealth(), entity
                .getPos(), inventory, entity

                .getNavigation().getCurrentPath(), false, angerLevel, entity


                .getBrain().getPossibleActivities().stream().map(Activity::toString).toList(), entity
                .getBrain().getRunningTasks().stream().map(Task::getName).toList(),
                listMemories(entity, serverWorld.getTime()), gossips, pois, potentialPois)));
    }


    @Inject(method = {"sendBeeDebugData"}, at = {@At("HEAD")})
    private static void devtools$sendBeeDebugData(BeeEntity bee, CallbackInfo ci) {

        sendToAll((ServerWorld) bee.getWorld(), new DebugBeeCustomPayload(new DebugBeeCustomPayload.Bee(bee
                .getUuid(), bee
                .getId(), bee
                .getPos(), bee
                .getNavigation().getCurrentPath(), bee
                .getHivePos(), bee
                .getFlowerPos(), bee
                .getMoveGoalTicks(), bee
                .getGoalSelector().getGoals().stream().map(prioritizedGoal -> prioritizedGoal.getGoal().toString()).collect(Collectors.toSet()), bee
                .getPossibleHives())));
    }

    @Inject(at = @At("HEAD"), method = "sendBeehiveDebugData(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BeehiveBlockEntity;)V")
    private static void devtools$sendBeehiveDebugData(World world, BlockPos pos, BlockState state, BeehiveBlockEntity blockEntity, CallbackInfo ci) {
        if (world.isClient()) return;
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeBlockPos(pos);
        buf.writeString(blockEntity.getType().toString());
        buf.writeInt(blockEntity.getBeeCount());
        buf.writeInt(BeehiveBlockEntity.getHoneyLevel(state));
        buf.writeBoolean(blockEntity.isSmoked());
        sendToAll((ServerWorld) world, new DebugHiveCustomPayload(new DebugHiveCustomPayload.HiveInfo(buf)));
    }

    @Inject(method = {"sendBreezeDebugData"}, at = {@At("HEAD")})
    private static void devtools$sendBreezeDebugData(BreezeEntity breeze, CallbackInfo ci) {

        sendToAll((ServerWorld) breeze.getWorld(), new DebugBreezeCustomPayload(new DebugBreezeCustomPayload.BreezeInfo(breeze
                .getUuid(), breeze
                .getId(),
                (breeze.getTarget() == null) ? null : breeze.getTarget().getId(), Objects.requireNonNull(breeze
                .getBrain().getOptionalMemory(MemoryModuleType.BREEZE_JUMP_TARGET)).orElse(null))));
    }

    @Inject(method = {"sendRedstoneUpdateOrder"}, at = {@At("HEAD")})
    private static void devtools$sendRedstoneUpdateOrder(World world, DebugRedstoneUpdateOrderCustomPayload payload, CallbackInfo ci) {

        List<DebugRedstoneUpdateOrderCustomPayload.Wire> updateOrders = new ArrayList<>();
        updateOrders.add(new DebugRedstoneUpdateOrderCustomPayload.Wire((BlockPos) payload.wires().stream().map(DebugRedstoneUpdateOrderCustomPayload.Wire::pos).toList(), (WireOrientation) payload.wires().stream().map(DebugRedstoneUpdateOrderCustomPayload.Wire::orientation).toList()));

        sendToAll((ServerWorld) world, new DebugRedstoneUpdateOrderCustomPayload(payload.time(), updateOrders));
    }

    @Inject(method = {"sendGameEvent"}, at = {@At("HEAD")})
    private static void devtools$sendGameEvent(World world, RegistryEntry<GameEvent> event, Vec3d pos, CallbackInfo ci) {

        if (world instanceof ServerWorld serverWorld) {
            event.getKey().ifPresent(key -> sendToAll(serverWorld, new DebugGameEventCustomPayload(key, pos)));
        }

    }

    @Inject(method = {"sendGameEventListener"}, at = {@At("HEAD")})
    private static void devtools$sendGameEventListener(World world, GameEventListener eventListener, CallbackInfo ci) {

        if (world instanceof ServerWorld serverWorld) {
            sendToAll(serverWorld, new DebugGameEventListenersCustomPayload(eventListener.getPositionSource(), eventListener.getRange()));
        }

    }
}
