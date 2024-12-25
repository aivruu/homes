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
package io.github.aivruu.homes.command.application;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.aivruu.homes.config.application.ConfigurationContainer;
import io.github.aivruu.homes.config.application.ConfigurationInterface;
import io.github.aivruu.homes.config.application.object.ConfigurationConfigurationModel;
import io.github.aivruu.homes.config.application.object.MessagesConfigurationModel;
import io.github.aivruu.homes.permission.application.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractRegistrableCommand {
  protected ConfigurationContainer<ConfigurationConfigurationModel> configuration;
  protected ConfigurationContainer<MessagesConfigurationModel> messages;

  protected AbstractRegistrableCommand(
    final @NotNull ConfigurationContainer<ConfigurationConfigurationModel> configuration,
    final @NotNull ConfigurationContainer<MessagesConfigurationModel> messages
  ) {
    this.configuration = configuration;
    this.messages = messages;
  }

  @SuppressWarnings("UnstableApiUsage")
  public abstract @NotNull LiteralCommandNode<CommandSourceStack> register();

  protected final boolean canUseIt(final @NotNull CommandSender sender, final @NotNull Permissions permissionEnum) {
    return sender.hasPermission(permissionEnum.permission());
  }
}
