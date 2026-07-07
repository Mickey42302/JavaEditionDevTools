package com.mickey42302.devtools.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.commands.ChaseCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChaseCommand.class)
public class ChaseCommandMixin {

    @Redirect(
            method = "register",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/commands/Commands;literal(Ljava/lang/String;)Lcom/mojang/brigadier/builder/LiteralArgumentBuilder;"
            )
    )
    private static LiteralArgumentBuilder<CommandSourceStack> fixSecurityFlaw(String literal) {
        if ("chase".equals(literal)) {
            return Commands.literal(literal).requires(Commands.hasPermission(Commands.LEVEL_ADMINS));
        }
        return Commands.literal(literal);
    }
}