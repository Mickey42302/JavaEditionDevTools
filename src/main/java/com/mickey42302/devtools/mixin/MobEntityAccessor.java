package com.mickey42302.devtools.mixin;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ai.goal.GoalSelector;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({MobEntity.class})
public interface MobEntityAccessor {
    @Accessor("goalSelector")
    GoalSelector getGoalSelector();
}