package com.aivruu.homes.teleport;

import com.aivruu.homes.home.ValueObjectHomePosition;
import com.aivruu.homes.result.ValueObjectHomeResult;
import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.repository.PlayerModelRepository;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class manage the homes teleport operations for the player.
 *
 * @since 0.0.1
 */
public class HomeTeleportManager {
  private static HomeTeleportManager instance;
  private final PlayerModelRepository repository;

  private HomeTeleportManager(final @NotNull PlayerModelRepository repository) {
    this.repository = repository;
  }

  /**
   * Returns a reference of {@link HomeTeleportManager}.
   *
   * @return A reference of {@link HomeTeleportManager}.
   * @since 0.0.1
   */
   public static @NotNull HomeTeleportManager get() {
    if (instance == null) {
      instance = new HomeTeleportManager(PlayerModelRepository.get());
    }
    return instance;
  }

  /**
   * Teleports the player to the home expected.
   *
   * @param player needed to perform teleport.
   * @param homeId home id expected to be teleported.
   * @return A status boolean expected devolved by {@link ValueObjectHomeResult#withStatus(Object, byte)},
   * @see ValueObjectHomePosition#performTeleportAsync(Player)
   * @since 0.0.1
   */
  public boolean performTeleport(final @NotNull Player player, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModelEntity = this.repository.findOne(player.getUniqueId());
    if (playerModelEntity == null) {
      return false;
    }
    final ValueObjectHomePosition valueObjectHomePosition = playerModelEntity.homes().get(homeId);
    if (valueObjectHomePosition == null) {
      return false;
    }
    return valueObjectHomePosition.performTeleportAsync(player).statusIs((byte) -5);
  }
}
