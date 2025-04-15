# Development Tools Patch (Java Edition)
In Java Edition, there are a bunch of development tools that are locked away by default. This mod re-enables them, allowing you to get a look at what Mojang uses to develop the game!

## Commands

The following commands are available on both singleplayer and multiplayer:

• /chase

• /debugmobspawning (**Tip:** To see this command in action, use a Repeating Command Block and make sure that all players are a certain distance away).

• /debugpath (**Tip:** To see the results of this command, you will need to enable the pathfinding debug renderer.)

• /raid

• /spawn_armor_trims (**Warning**: This command can cause a lot of lag!)

• /serverpack (**Warning:** Incorrectly using this command can trigger an exception!)

• /warden_spawn_tracker

The following commands are only available on multiplayer:

• /debugconfig (**Warning:** This command will softlock the game if it is not used properly!)

## F3 Key Binds

This mod also re-enables the developer F3 key binds. To see a list of them in game, you can use the F3 + F7 key bind I added.

F3 + F = Fog

F3 + U = Frustum, hold Shift to kill

F3 + O = Frustum culling Octree

F3 + E = SectionPath

F3 + V = SectionVisibility

F3 + L = SmartCull

F3 + W = WireFrame

## Debug Renderers

To make the debug renderers user-friendly, I added a menu that can be opened using F3 + F6. To toggle a debug renderer, click on the respective button. I've also made the "/warden_spawn_tracker" command user-friendly by adding the missing translation strings.

## Installation

To install this mod, place the JAR file in your "mods" directory. You must also install the Fabric API.

*Note: Although the mod can be used on multiplayer, some features will only work if the server has the mod installed as well.*

## Disclaimer

This software has been released with fair use in mind; I am not affiliated with Mojang or Microsoft and do not own any of the games I have created content for. **While I work to ensure that my projects are of the best quality, they are provided with absolutely no warranty.**
