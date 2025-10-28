# Development Tools Patch (Java Edition)
In Java Edition, there are a bunch of development tools that are locked away by default. This mod re-enables them, allowing you to get a look at what Mojang uses to develop the game!

This mod is a rewrite of the original Dev Tools Unlocker mod (https://modrinth.com/mod/dev-tools-unlocker).

## Developer Commands

*Note: The "/chase" command is not available by default, even on development builds of the game. If you wish to enable this feature, please use the "CHASE_COMMAND" debug property (**Learn more:** https://www.youtube.com/watch?v=a2dVp0A3uwM).*

The following commands are available on both singleplayer and multiplayer:

• /debugmobspawning (**Learn more:** https://youtube.com/watch?v=ZWrxKFf1Xx4)

• /debugpath (**Learn more:** https://youtube.com/watch?v=9g2WI6V-W_A)

• /raid

• /spawn_armor_trims (**Warning**: This command can cause a lot of lag!)

• /serverpack (**Warning:** Incorrectly using this command can trigger an exception!)

• /warden_spawn_tracker

I've also made the "/warden_spawn_tracker" command user-friendly by adding the missing translation strings.

The following commands are only available on multiplayer:

• /debugconfig (**Warning:** This command will softlock the game if it is not used properly!)

*Tip: The developer commands can be enabled on vanilla installations using the "DEV_COMMANDS" debug property.*

## Sub-commands

The following sub-commands are available on both singleplayer and multiplayer:

• /test export <test>

• /test exportclosest

• /test exportthese

• /test exportthat

## Test Instance Blocks

This mod re-enables the "Export Structure" option in Test Instance Blocks. This option can be used to export data to an SNBT file within the "Minecraft.Server" directory.

## Exports

Structure data that has been exported to an SNBT file is able to be loaded across different worlds. This can be done using a Structure Block or the "/place" command.

For example, to place a structure that has been exported to an SNBT file named "cookies", the following command can be used:

• /place template minecraft:minecraft/structure/cookies

## Test Worlds

The "Create Test World" button on the title screen can be used to quickly generate a world with settings that are designed to make testing easier.

For these worlds, the superflat preset is set to "minecraft:bedrock,3\*minecraft:stone,116\*minecraft:sandstone;minecraft:desert". The "doMobSpawning", "doDaylightCycle", and "doWeatherCycle" game rules are also set to "false".

## Logs

The game will print debug messages to the log files.

## Installation

To install this mod, place the JAR file in your "mods" directory. You must also install the Fabric API.

*Note: Although the mod can be used on multiplayer, some features will only work if the server has the mod installed as well. For servers that don't use Fabric, the development features can be enabled by overriding the "isDevelopment" flag to true. Depending on what server engine you are using, you may need to override the "IS_RUNNING_IN_IDE" flag instead.*

## Disclaimer

This software has been released with fair use in mind; I am not affiliated with Mojang or Microsoft and do not own any of the games I have created content for. **While I work to ensure that my projects are of the best quality, they are provided with absolutely no warranty.**
