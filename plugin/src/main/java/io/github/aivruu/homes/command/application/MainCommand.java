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

import com.mojang.brigadier.Command;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.aivruu.homes.config.application.ConfigurationContainer;
import io.github.aivruu.homes.config.application.object.ConfigurationConfigurationModel;
import io.github.aivruu.homes.config.application.object.MessagesConfigurationModel;
import io.github.aivruu.homes.minimessage.application.MiniMessageHelper;
import io.github.aivruu.homes.permission.application.Permissions;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class MainCommand extends AbstractRegistrableCommand {
  public MainCommand(
    final @NotNull ConfigurationContainer<ConfigurationConfigurationModel> configuration,
    final @NotNull ConfigurationContainer<MessagesConfigurationModel> messages
  ) {
    super(configuration, messages);
  }

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public @NotNull LiteralCommandNode<CommandSourceStack> register() {
    return Commands.literal("homes")
      .executes(ctx -> Command.SINGLE_SUCCESS)
      .then(Commands.literal("help")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.HELP))
        .executes(ctx -> {
          ctx.getSource().getSender().sendMessage(MiniMessageHelper.parse(super.messages.model().help));
          return Command.SINGLE_SUCCESS;
        })
      )
      .then(Commands.literal("reload")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.RELOAD))
        .executes(ctx -> {
          final CommandSender sender = ctx.getSource().getSender();
          final MessagesConfigurationModel messages = super.messages.model();
          super.configuration = super.configuration.reload();
          super.messages = super.messages.reload();
          if (super.configuration == null || super.messages == null) {
            sender.sendMessage(MiniMessageHelper.parse(messages.reloadSuccess));
          } else {
            sender.sendMessage(MiniMessageHelper.parse(messages.reloadError));
          }
          return Command.SINGLE_SUCCESS;
        })
      )
      .build();
  }
}
