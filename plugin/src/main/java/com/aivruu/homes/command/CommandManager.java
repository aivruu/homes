package com.aivruu.homes.command;

import com.aivruu.homes.HomesPlugin;
import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.config.model.MessageConfigModel;
import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.teleport.HomeTeleportManager;
import com.aivruu.homes.utils.ComponentUtils;
import dev.triumphteam.cmd.bukkit.BukkitCommandManager;
import dev.triumphteam.cmd.bukkit.message.BukkitMessageKey;
import dev.triumphteam.cmd.core.message.MessageKey;
import dev.triumphteam.cmd.core.requirement.RequirementKey;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandManager {
  private final BukkitCommandManager<CommandSender> commandManager;
  private final ConfigModel config;
  private final MessageConfigModel message;

  public CommandManager(final @NotNull HomesPlugin plugin, final @NotNull ConfigModel config, final @NotNull MessageConfigModel message) {
    this.commandManager = BukkitCommandManager.create(plugin);
    this.config = config;
    this.message = message;
  }

  public void prepareRequirements() {
    this.commandManager.registerRequirement(RequirementKey.of("player"), sender -> sender instanceof Player);
    this.commandManager.registerRequirement(RequirementKey.of("about-perm"), sender -> sender.hasPermission("homes.about"));
    this.commandManager.registerRequirement(RequirementKey.of("help-perm"), sender -> sender.hasPermission("homes.help"));
    this.commandManager.registerRequirement(RequirementKey.of("reload-perm"), sender -> sender.hasPermission("homes.reload"));
    this.commandManager.registerRequirement(RequirementKey.of("tp-perm"), sender -> sender.hasPermission("homes.teleport"));
    this.commandManager.registerRequirement(RequirementKey.of("info-perm"), sender -> sender.hasPermission("homes.info"));
    this.commandManager.registerRequirement(RequirementKey.of("create-perm"), sender -> sender.hasPermission("homes.create"));
    this.commandManager.registerRequirement(RequirementKey.of("delete-perm"), sender -> sender.hasPermission("homes.delete"));
  }

  public void prepareMessages() {
    this.commandManager.registerMessage(BukkitMessageKey.PLAYER_ONLY, (sender, ctx) -> sender
      .sendMessage(ComponentUtils.parse("This command only can be executed by players!")));
    this.commandManager.registerMessage(BukkitMessageKey.NO_PERMISSION, (sender, ctx) -> sender
      .sendMessage(ComponentUtils.parse(this.message.requirePermission, Placeholder.parsed("prefix", this.config.prefix))));
    this.commandManager.registerMessage(MessageKey.UNKNOWN_COMMAND, (sender, ctx) -> sender
      .sendMessage(ComponentUtils.parse(this.message.unknownSubCommand, Placeholder.parsed("prefix", this.config.prefix))));
  }

  public void load(final @NotNull HomeAggregate aggregate, final @NotNull HomeTeleportManager teleportManager) {
    this.commandManager.registerCommand(new MainCommand(aggregate, teleportManager, this.config, this.message));
  }
}
