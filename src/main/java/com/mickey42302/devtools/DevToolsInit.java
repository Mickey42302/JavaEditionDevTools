package com.mickey42302.devtools;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ChaseCommand;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.fabricmc.loader.impl.FabricLoaderImpl.MOD_ID;

public class DevToolsInit implements ModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("Loading the development tools patch...");

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> ChaseCommand.register(dispatcher));

    }
}