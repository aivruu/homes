package com.aivruu.homes.listener;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.shared.DataModel;
import com.aivruu.homes.utils.ComponentUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDataListener implements Listener {
  private final DataModel data;
  private final PlayerModelRepository repository;
  private final ComponentLogger logger;

  public PlayerDataListener(final @NotNull DataModel data, final @NotNull PlayerModelRepository repository, final @NotNull ComponentLogger logger) {
    this.data = data;
    this.repository = repository;
    this.logger = logger;
  }

  @EventHandler
  public void onAsyncServerLogIn(final @NotNull AsyncPlayerPreLoginEvent event) {
    if (event.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) {
      return;
    }
    this.data.performAsyncRead(event.getName()).thenAccept(playerDataStatus -> {
      if (playerDataStatus.error()) {
        this.logger.warn(ComponentUtils.parse("<red>Could not read model data for player <player-name>", Placeholder.parsed("player-name", event.getName())));
        return;
      }
      final EntityCachedPlayerModel playerModel = playerDataStatus.result();
      assert playerModel != null;
      this.repository.add(playerModel);
    });
  }

  @EventHandler
  public void onServerQuit(final @NotNull PlayerQuitEvent event) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(event.getPlayer().getUniqueId());
    if (playerModel == null) {
      return;
    }
    this.data.performAsyncWrite(playerModel).thenAccept(playerDataStatus -> {
      if (playerDataStatus.error()) {
        this.logger.warn(ComponentUtils.parse("<red>Could not write data into model for player <player-name>", Placeholder.parsed("player-name", playerModel.name())));
      }
    });
  }
}
