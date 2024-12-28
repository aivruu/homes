// This file is part of homes, licensed under the GNU License.
//
// Copyright (c) 2024 aivruu
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
package io.github.aivruu.homes.config.application.object;

import io.github.aivruu.homes.config.application.ConfigurationInterface;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public final class MessagesConfigurationModel implements ConfigurationInterface {
  public String[] help = {
    "<gradient:blue:green>AldrHomes | Available Commands Guide:",
    "",
    "<gray> - <yellow>/homes help</yellow> Shows this messages.",
    "<gray> - <yellow>/homes reload</yellow> Reloads the plugin's configurations.",
    "<gray> - <yellow>/home create <id></yellow> Creates a new home with that ID in your current location.",
    "<gray> - <yellow>/home delete <id></yellow> Deletes the home with that ID.",
    "<gray> - <yellow>/home list</yellow> Lists all your homes.",
    "<gray> - <yellow>/home teleport <id></yellow> Teleports you to the home with that ID.",
    "<gray> - <yellow>/home modify <id></yellow> Modifies the home with that ID's location.",
  };

  public String reloadSuccess = "<blue>[AldrHomes] <dark_gray><b>></b> <gradient:green:yellow>The configurations has been updated correctly.";

  public String reloadError = "<blue>[AldrHomes] <dark_gray><b>></b> <red>An error occurred during configurations reloading.";

  public String playerUnknownInfo = "<blue>[AldrHomes] <dark_gray><b>></b> <red>Seems your information isn't available.";

  public String unknownHome = "<blue>[AldrHomes] <dark_gray><b>></b> <red>That home-point doesn't exist.";

  public String teleported = "<blue>[AldrHomes] <dark_gray><b>></b> <gradient:green:yellow>You have been teleported to your home-point.";

  public String locationModified = "<blue>[AldrHomes] <dark_gray><b>></b> <gradient:green:yellow>The location of your home-point was modified.";

  public String locationModifyError = "<blue>[AldrHomes] <dark_gray><b>></b> <red>The new location for your home must be different from the current one.";

  public String empty = "<blue>[AldrHomes] <dark_gray><b>></b> <red>You don't have any homes yet.";

  public String homeListFormat = "<yellow><id> <gray>[<hover:show_text:'<gray>Go to this home.'><click:run_command:'/home teleport <id>'>TP</click></hover> | <hover:show_text:'<red>X: <gray><home-x>,</gray> Y: <gray><home-y>,</gray> Z: <gray><home-z>'>Location</hover> | <hover:show_text:'<gray>Use current location as home its new position.'><click:run_command:'/home modify <id>'>Modify</click></hover> | <hover:show_text:'<gray>Click to delete this home.'><click:run_command:'/home delete <id>'>Delete</click></hover>]";

  public String listHeader = """
    <gradient:gold:yellow>AldrHomes | Your Current Homes:

    """;

  public String deleted = "<blue>[AldrHomes] <dark_gray><b>></b> <red>The home-point has been deleted.";

  public String creationError = "<blue>[AldrHomes] <dark_gray><b>></b> <red>That home already exists or you've reached the max-homes limit.";

  public String created = "<blue>[AldrHomes] <dark_gray><b>></b> <gradient:green:yellow>The home-point has been created.";
}
