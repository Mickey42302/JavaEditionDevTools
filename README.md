# Development Tools Patch (Java Edition)
In Java Edition, there are a bunch of development tools that are locked away by default. This mod re-enables them, allowing you to get a look at what Mojang uses to develop the game!

This mod is a rewrite of the original Dev Tools Unlocker mod (https://modrinth.com/mod/dev-tools-unlocker).

## Commands

The following commands are available on both singleplayer and multiplayer:

• /chase (**Learn more:** https://www.youtube.com/watch?v=a2dVp0A3uwM)

• /debugmobspawning (**Learn more:** https://youtube.com/watch?v=ZWrxKFf1Xx4)

• /debugpath (**Learn more:** https://youtube.com/watch?v=9g2WI6V-W_A)

• /raid

• /spawn_armor_trims (**Warning**: This command can cause a lot of lag!)

• /serverpack (**Warning:** Incorrectly using this command can trigger an exception!)

• /warden_spawn_tracker

I've also made the "/warden_spawn_tracker" command user-friendly by adding the missing translation strings.

The following commands are only available on multiplayer:

• /debugconfig (**Warning:** This command will softlock the game if it is not used properly!)

## Sub-commands

The following sub-commands are available on both singleplayer and multiplayer:

• /test export <test>

• /test exportclosest

• /test exportthese

• /test exportthat

Some of the export functions for the "/test" command are also available in the Test Instance Block. You can click "Export Structure" to trigger them.

## F3 Key Binds

This mod re-enables the developer F3 key binds. To see a list of them in game, you can use the F3 + F7 key bind I added.

F3 + F = Fog

F3 + U = Frustum, hold shift to kill

F3 + O = Frustum culling Octree

F3 + E = SectionPath

F3 + V = SectionVisibility

F3 + L = SmartCull

F3 + W = WireFrame

This mod also restores key binds that were removed from the game with small improvements. These can also be listed using the F3 + F7 key bind.

F3 + R = Cycle render distance (shift to invert, alt to reset)

## Debug Renderers

To make the debug renderers user-friendly, I added a menu that can be opened using F3 + F6. To toggle a debug renderer, click on the respective button.

## Test Worlds

The "Create Test World" button on the title screen can be used to quickly generate a world with settings that are designed to make testing easier.

## Logs

The game will print debug messages to the log files.

## Installation

To install this mod, place the JAR file in your "mods" directory. You must also install the Fabric API.

*Note: Although the mod can be used on multiplayer, some features will only work if the server has the mod installed as well. For servers that don't use Fabric, the development features can be enabled by overriding the "isDevelopment" flag to true.*

## Disclaimer

This software has been released with fair use in mind; I am not affiliated with Mojang or Microsoft and do not own any of the games I have created content for. **While I work to ensure that my projects are of the best quality, they are provided with absolutely no warranty.**
