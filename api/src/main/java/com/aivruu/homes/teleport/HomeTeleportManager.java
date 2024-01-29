package com.aivruu.homes.teleport;

import com.aivruu.homes.home.EntityHomeModel;
import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.result.ValueObjectHomeResult;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * This class manage the homes teleport operations for the player.
 *
 * @since 0.0.1
 */
public class HomeTeleportManager {
  private final HomeAggregate aggregate;

  public HomeTeleportManager(final @NotNull HomeAggregate aggregate) {
    this.aggregate = aggregate;
  }

  /**
   * Performs the teleport of the player to the home specified.
   * 
   * @param player needed to find home and perform teleportation.
   * @param homeId identifier needed to find the home.
   * @return A boolean status expected by {@link ValueObjectHomeResult}, possible status devolved.<p>
   * <p>
   * • {@code true} if status code returned by {@link EntityHomeModel#performTeleportAsync(Player)}
   * is -5 (teleport successful).<p>
   * • {@code false} if the status code returned by {@link HomeAggregate#performHomeSearch(UUID, String)}
   * is -6 (home not found).
   * @see HomeAggregate#performHomeSearch(UUID, String)
   * @see EntityHomeModel#performTeleportAsync(Player)
   * @since 0.0.1
   */
  public byte performTeleport(final @NotNull Player player, final @NotNull String homeId) {
    final ValueObjectHomeResult<EntityHomeModel> homeResult = this.aggregate.performHomeSearch(player.getUniqueId(), homeId);
    if (homeResult.statusIs((byte) -50)) {
      return -50;
    }
    final EntityHomeModel entityHomeModel = homeResult.result();
    assert entityHomeModel != null;
    return entityHomeModel.performTeleportAsync(player).status();
  }
}
