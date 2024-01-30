package com.aivruu.homes.config.model;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class MessageConfigModel extends SerializableConfigModel {
  public String requirePermission = "<prefix> <red>You don't have permission for this!";

  public String unknownSubCommand = "<prefix> <red>That sub-command has not been added yet!";

  public String reloadSuccess = "<prefix> <gradient:green:blue>The plugin has been reloaded correctly!";

  public String reloadFailed = "<prefix> <red>The plugin could not be reloaded correctly.";

  public String unknownHomeIdentifier = "<prefix> <red>The home-identifier given is not valid.";

  public String successHomeTeleport = "<prefix> <gradient:green:light_purple>You have been teleported to your home <home-id>";

  public String failedHomeTeleport = "<prefix> <red>You could not be teleported to the home.";

  public List<String> help = List.of(
    "<green>[Homes] <gray>> Available Plugin Commands",
    "<dark_gray> • <green><hover:show_text:'<yellow>Shows exactly this message.'>/homes help</hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Reloads the plugin configuration.<br><br><gray>[Click to execute the command]'><click:run_command:'/homes reload'>/homes reload</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Shows information about the plugin.<br><br><gray>[Click to execute the command]'><click:run_command:'/homes about'>/homes about</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Shows a list with the current homes.<br><br><gray>[Click to execute the command]'><click:run_command:'/homes list'>/homes list</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Creates a new home on the current location.<br><br><gray>[Click to execute the command]'><click:suggest_command:'/homes create <home-id>'>/homes create</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Deletes the home specified.<br><br><gray>[Click to execute the command]'><click:suggest_command:'/homes delete <home-id>'>/homes delete</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Shows information about the home given.<br><br><gray>[Click to execute the command]'><click:suggest_command:'/homes info <home-id>'>/homes info</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Performs a teleport to the home specified.<br><br><gray>[Click to execute the command]'><click:suggest_command:'/homes tp <home-id>'>/homes tp</click></hover>",
    "<dark_gray> • <green><hover:show_text:'<yellow>Deletes all the homes of the player.<br><br><gray>[Click to execute the command]'><click:run_command:'/homes clean'>/homes clean</click></hover>");

  public List<String> homeInfo = List.of(
    "<gray>Information about <yellow><home-id></yellow> home",
    "<dark_gray> • <white>Identifier: <green><home-id>",
    "<dark_gray> • <white>World: <green><home-world>",
    "<dark_gray> • <white>Location: <green><home-formatted-position>");

  public String notHomesDetected = "<prefix> <red>You don't have any home created now!";

  public String homesList = "<gray>Showing all your homes, you currently have <green><homes-amount></green> homes created.<br><yellow><homes-list-formatted>";

  public String alreadyHomeExisting = "<prefix> <red>You've already a home with this identifier.";

  public String homeCreation = "<prefix> <gray>The home <green><home-id></green> has been established successful!";

  public String homeDeletion = "<prefix> <gray>The home <red><home-id></red> has been deleted correctly!";

  public String homesCleaned = "<prefix> <green>Deletion Performed! <gray>Your <cyan><homes-amount></cyan> have been deleted correctly!";

  public String homesLimit = "<prefix> <red>You has reached the limit for homes creation!";
}
