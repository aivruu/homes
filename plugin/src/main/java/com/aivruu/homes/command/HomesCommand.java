package com.aivruu.homes.command;

import com.aivruu.homes.Constants;
import com.aivruu.homes.HomesPlugin;
import com.aivruu.homes.config.ValueObjectConfigManager;
import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.config.model.MessageConfigModel;
import com.aivruu.homes.home.EntityHomeModel;
import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.result.ValueObjectHomeResult;
import com.aivruu.homes.teleport.HomeTeleportManager;
import com.aivruu.homes.utils.ComponentUtils;
import dev.triumphteam.cmd.core.BaseCommand;
import dev.triumphteam.cmd.core.annotation.ArgName;
import dev.triumphteam.cmd.core.annotation.Default;
import dev.triumphteam.cmd.core.annotation.Requirement;
import dev.triumphteam.cmd.core.annotation.Requirements;
import dev.triumphteam.cmd.core.annotation.SubCommand;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class HomesCommand extends BaseCommand {
  private final HomeAggregate aggregate;
  private final HomeTeleportManager teleportManager;
  private MessageConfigModel message;
  private ConfigModel config;

  public HomesCommand(final @NotNull HomeAggregate aggregate, final @NotNull HomeTeleportManager teleportManager, final @NotNull ConfigModel config, final @NotNull MessageConfigModel message) {
    super("homes");
    this.aggregate = aggregate;
    this.teleportManager = teleportManager;
    this.config = config;
    this.message = message;
  }

  @Default
  @Requirement("player")
  public void mainCommand(final @NotNull Player player) {
    player.sendMessage(ComponentUtils.parse("<prefix> Thanks for using the plugin! You can see the commands available typing <yellow>/homes help", Placeholder.parsed("prefix", this.config.prefix)));
  }

  @SubCommand("about")
  @Requirements({
    @Requirement("player"),
    @Requirement("about-perm")
  })
  public void aboutCommand(final @NotNull Player player) {
    player.sendMessage(ComponentUtils.parse(
      "<prefix> Running on Paper <aqua><paper-release></aqua> with the release <green><version>",
      Placeholder.parsed("prefix", this.config.prefix),
      Placeholder.parsed("paper-release", Bukkit.getBukkitVersion()),
      Placeholder.parsed("version", Constants.VERSION)));
  }

  @SubCommand("help")
  @Requirements({
    @Requirement("player"),
    @Requirement("help-perm")
  })
  public void helpCommand(final @NotNull Player player) {
    for (final String iteratedMessage : this.message.help) {
      player.sendMessage(ComponentUtils.parse(iteratedMessage));
    }
  }

  @SubCommand("reload")
  @Requirements({
    @Requirement("player"),
    @Requirement("reload-perm")
  })
  public void reloadCommand(final @NotNull Player player) {
    final Path pluginFolder = HomesPlugin.getPlugin(HomesPlugin.class).getDataFolder().toPath();
    final ConfigModel newConfigModel = ValueObjectConfigManager.loadConfig(pluginFolder, "config.yml", ConfigModel.class);
    final MessageConfigModel newMessageModel = ValueObjectConfigManager.loadConfig(pluginFolder, "messages.yml", MessageConfigModel.class);
    if ((newConfigModel == null) || (newMessageModel == null)) {
      player.sendMessage(ComponentUtils.parse(this.message.reloadFailed, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    this.config = newConfigModel;
    this.message = newMessageModel;
    player.sendMessage(ComponentUtils.parse(this.message.reloadSuccess, Placeholder.parsed("prefix", this.config.prefix)));
  }

  @SubCommand("tp")
  @Requirements({
    @Requirement("player"),
    @Requirement("tp-perm")
  })
  public void teleportCommand(final @NotNull Player player, final @NotNull @ArgName("home-identifier") String homeId) {
    final byte teleportStatusCode = this.teleportManager.performTeleport(player, homeId);
    if (teleportStatusCode == -50) {
      player.sendMessage(ComponentUtils.parse(this.message.unknownHomeIdentifier, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    } else if (teleportStatusCode == -6) {
      player.sendMessage(ComponentUtils.parse(this.message.failedHomeTeleport, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    player.sendMessage(ComponentUtils.parse(this.message.successHomeTeleport, Placeholder.parsed("prefix", this.config.prefix), Placeholder.parsed("home-id", homeId)));
  }

  @SubCommand("info")
  @Requirements({
    @Requirement("player"),
    @Requirement("info-perm")
  })
  public void infoCommand(final @NotNull Player player, final @NotNull @ArgName("home-identifier") String homeId) {
    final ValueObjectHomeResult<EntityHomeModel> homeSearchStatus = this.aggregate.performHomeSearch(player.getUniqueId(), homeId);
    if (homeSearchStatus.statusIs((byte) -50)) {
      player.sendMessage(ComponentUtils.parse(this.message.unknownHomeIdentifier, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    final EntityHomeModel entityHomeModel = homeSearchStatus.result();
    assert entityHomeModel != null;
    for (final String iteratedMessage : this.message.homeInfo) {
      player.sendMessage(ComponentUtils.parse(iteratedMessage,
        Placeholder.parsed("home-id", entityHomeModel.id()),
        Placeholder.parsed("home-world", entityHomeModel.worldName()),
        Placeholder.parsed("home-formatted-position", entityHomeModel.asFormattedPosition())));
    }
  }

  @SubCommand("list")
  @Requirement("player")
  public void listCommand(final @NotNull Player player) {
    final ValueObjectHomeResult<EntityHomeModel[]> homesSearchStatus = this.aggregate.performPlayerHomesList(player.getUniqueId());
    if (homesSearchStatus.error()) {
      return;
    }
    final EntityHomeModel[] playerHomes = homesSearchStatus.result();
    assert playerHomes != null;
    if (playerHomes.length - 1 == 0) {
      player.sendMessage(ComponentUtils.parse(this.message.notHomesDetected, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    final List<String> homesIdsList = new ArrayList<>(playerHomes.length);
    for (final EntityHomeModel entityHomeModel : playerHomes) {
      homesIdsList.add(entityHomeModel.id());
    }
    player.sendMessage(ComponentUtils.parse(this.message.homesList, Placeholder.parsed("homes-list-formatted", homesIdsList.toString()), Placeholder.parsed("homes-amount", Integer.toString(playerHomes.length))));
  }

  @SubCommand("create")
  @Requirements({
    @Requirement("player"),
    @Requirement("create-perm")
  })
  public void createExecutor(final @NotNull Player player, final @NotNull @ArgName("home-identifier") String homeId) {
    final byte maxAllowedHomes;
    if (player.hasPermission("homes.limit.rank")) {
      maxAllowedHomes = this.config.rankMaxHomes;
    } else {
      maxAllowedHomes = this.config.normalMaxHomesAmount;
    }
    final EntityHomeModel[] playerHomesArray = this.aggregate.performPlayerHomesList(player.getUniqueId()).result();
    if (playerHomesArray.length - 1 == maxAllowedHomes) {
      player.sendMessage(ComponentUtils.parse(this.message.homesLimit, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    final ValueObjectHomeResult<EntityHomeModel> homeCreationStatus = this.aggregate.performHomeCreate(player, homeId);
    if (homeCreationStatus.statusIs((byte) -4)) {
      return;
    } else if (homeCreationStatus.statusIs((byte) -10)) {
      player.sendMessage(ComponentUtils.parse(this.message.alreadyHomeExisting, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    player.sendMessage(ComponentUtils.parse(this.message.homeCreation, Placeholder.parsed("prefix", this.config.prefix), Placeholder.parsed("home-id", homeId)));
  }

  @SubCommand("delete")
  @Requirements({
    @Requirement("player"),
    @Requirement("delete-perm")
  })
  public void deleteCommand(final @NotNull Player player, final @NotNull @ArgName("home-identifier") String homeId) {
    final ValueObjectHomeResult<?> homeDeleteStatus = this.aggregate.performHomeDeletion(player.getUniqueId(), homeId);
    if (homeDeleteStatus.statusIs((byte) -3)) {
      player.sendMessage(ComponentUtils.parse(this.message.unknownHomeIdentifier, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    } else if (homeDeleteStatus.statusIs((byte) -15)) {
      player.sendMessage(ComponentUtils.parse(this.message.notHomesDetected, Placeholder.parsed("prefix", this.config.prefix)));
      return;
    }
    player.sendMessage(ComponentUtils.parse(this.message.homeDeletion, Placeholder.parsed("prefix", this.config.prefix), Placeholder.parsed("home-id", homeId)));
  }
  
  @SubCommand("clean")
  @Requirement("player")
  public void cleanCommand(final @NotNull Player player) {
    final ValueObjectHomeResult<Byte> homesCleanStatus = this.aggregate.performAllHomesClean(player.getUniqueId());
    if (homesCleanStatus.statusIs((byte) -4)) {
      return;
    }
    final byte cleanedHomesArrayLength = homesCleanStatus.result();
    player.sendMessage(ComponentUtils.parse(this.message.homesCleaned, Placeholder.parsed("prefix", this.config.prefix), Placeholder.parsed("homes-amount", Byte.toString(cleanedHomesArrayLength))));
  }
}
