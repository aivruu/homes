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
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.github.aivruu.homes.config.application.ConfigurationContainer;
import io.github.aivruu.homes.config.application.object.ConfigurationConfigurationModel;
import io.github.aivruu.homes.config.application.object.MessagesConfigurationModel;
import io.github.aivruu.homes.home.application.HomeCreatorService;
import io.github.aivruu.homes.home.application.HomePositionUpdater;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.minimessage.application.MiniMessageHelper;
import io.github.aivruu.homes.permission.application.Permissions;
import io.github.aivruu.homes.player.application.PlayerHomeController;
import io.github.aivruu.homes.player.application.PlayerManagerService;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.result.domain.ValueObjectMutationResult;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public final class HomeCommand extends AbstractRegistrableCommand {
  private final PlayerManagerService playerManagerService;
  private final PlayerHomeController playerHomeController;
  private final HomeCreatorService homeCreatorService;
  private final HomePositionUpdater homePositionUpdater;

  public HomeCommand(
    final @NotNull ConfigurationContainer<ConfigurationConfigurationModel> configuration,
    final @NotNull ConfigurationContainer<MessagesConfigurationModel> messages,
    final @NotNull PlayerManagerService playerManagerService,
    final @NotNull PlayerHomeController playerHomeController,
    final @NotNull HomeCreatorService homeCreatorService,
    final @NotNull HomePositionUpdater homePositionUpdater
  ) {
    super(configuration, messages);
    this.playerManagerService = playerManagerService;
    this.playerHomeController = playerHomeController;
    this.homeCreatorService = homeCreatorService;
    this.homePositionUpdater = homePositionUpdater;
  }

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public @NotNull LiteralCommandNode<CommandSourceStack> register() {
    return Commands.literal("home")
      .requires(src -> src.getSender() instanceof Player)
      .then(Commands.literal("create")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.CREATE))
        .executes(ctx -> Command.SINGLE_SUCCESS)
        .then(Commands.argument("id", StringArgumentType.word())
          .executes(ctx -> {
            final MessagesConfigurationModel messages = super.messages.model();
            final Player player = (Player) ctx.getSource().getSender();
            final HomeModelEntity homeModel = this.homeCreatorService.create(player, ctx.getArgument("id", String.class));
            if (homeModel == null) {
              player.sendMessage(MiniMessageHelper.parse(messages.creationError));
              return Command.SINGLE_SUCCESS;
            }
            if (!this.playerHomeController.addHome(player, homeModel)) {
              player.sendMessage(MiniMessageHelper.parse(messages.additionError));
            } else {
              player.sendMessage(MiniMessageHelper.parse(messages.created));
            }
            return Command.SINGLE_SUCCESS;
          })
        )
      )
      .then(Commands.literal("delete")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.DELETE))
        .executes(ctx -> Command.SINGLE_SUCCESS)
        .then(Commands.argument("id", StringArgumentType.word())
          .executes(ctx -> {
            final MessagesConfigurationModel messages = super.messages.model();
            final Player player = (Player) ctx.getSource().getSender();
            final boolean wasRemoved = this.playerHomeController.removeHome(player, ctx.getArgument("id", String.class));
            if (!wasRemoved) {
              player.sendMessage(MiniMessageHelper.parse(messages.unknownHome));
            } else {
              player.sendMessage(MiniMessageHelper.parse(messages.deleted));
            }
            return Command.SINGLE_SUCCESS;
          })
        )
      )
      .then(Commands.literal("list")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.LIST))
        .executes(ctx -> {
          final MessagesConfigurationModel messages = super.messages.model();
          final Player player = (Player) ctx.getSource().getSender();
          final PlayerAggregateRoot playerAggregateRoot = this.playerManagerService.playerAggregateRootOf(player.getUniqueId().toString());
          if (playerAggregateRoot == null) {
            player.sendMessage(MiniMessageHelper.parse(messages.playerUnknownInfo));
            return Command.SINGLE_SUCCESS;
          }
          if (playerAggregateRoot.homes().length == 0) {
            player.sendMessage(MiniMessageHelper.parse(messages.empty));
            return Command.SINGLE_SUCCESS;
          }
          final TextComponent.Builder componentBuilder = Component.text().append(MiniMessageHelper.parse(messages.listHeader));
          for (final HomeModelEntity homeModel : playerAggregateRoot.homes()) {
            final HomePositionValueObject position = homeModel.position();
            componentBuilder.append(MiniMessageHelper.parse(messages.homeListFormat,
              Placeholder.parsed("id", homeModel.id()),
              Placeholder.parsed("home-x", Integer.toString(position.x())),
              Placeholder.parsed("home-y", Integer.toString(position.y())),
              Placeholder.parsed("home-z", Integer.toString(position.z()))));
          }
          componentBuilder.append(MiniMessageHelper.parse(messages.listFooter));
          player.sendMessage(componentBuilder.build());
          return Command.SINGLE_SUCCESS;
        })
      )
      .then(Commands.literal("teleport")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.TELEPORT))
        .executes(ctx -> Command.SINGLE_SUCCESS)
        .then(Commands.argument("id", StringArgumentType.word())
          .executes(ctx -> {
            final Player player = (Player) ctx.getSource().getSender();
            final MessagesConfigurationModel messages = super.messages.model();
            final boolean wasTeleported = this.playerHomeController.teleportToHome(player, ctx.getArgument("id", String.class));
            if (!wasTeleported) {
              player.sendMessage(MiniMessageHelper.parse(messages.unknownHome));
            } else {
              player.sendMessage(MiniMessageHelper.parse(messages.teleported));
            }
            return Command.SINGLE_SUCCESS;
          })
        )
      )
      .then(Commands.literal("modify")
        .requires(src -> super.canUseIt(src.getSender(), Permissions.MODIFY))
        .executes(ctx -> Command.SINGLE_SUCCESS)
        .then(Commands.argument("id", StringArgumentType.word())
          .executes(ctx -> {
            final MessagesConfigurationModel messages = super.messages.model();
            final Player player = (Player) ctx.getSource().getSender();
            final ValueObjectMutationResult<HomePositionValueObject> result = this.homePositionUpdater.updatePosition(
              player, ctx.getArgument("id", String.class), player.getLocation());
            switch (result.status()) {
              case ValueObjectMutationResult.MUTATED_STATUS -> player.sendMessage(MiniMessageHelper.parse(messages.locationModified));
              case ValueObjectMutationResult.UNCHANGED_STATUS -> player.sendMessage(MiniMessageHelper.parse(messages.locationModifyError));
              case ValueObjectMutationResult.ERROR_STATUS -> player.sendMessage(MiniMessageHelper.parse(messages.unknownHome));
            }
            return Command.SINGLE_SUCCESS;
          })
        )
      )
      .build();
  }
}
