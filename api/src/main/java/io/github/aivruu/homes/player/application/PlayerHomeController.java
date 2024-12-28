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
package io.github.aivruu.homes.player.application;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.home.domain.event.HomeCreateEvent;
import io.github.aivruu.homes.home.domain.event.HomeDeleteEvent;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This class provides handling methods for player-homes adding, deletion or teleporting.
 *
 * @since 2.0.0
 */
public final class PlayerHomeController {
  private final AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry;

  /**
   * Creates a new {@link PlayerHomeController} with the provided parameters.
   *
   * @param playerAggregateRootRegistry the {@link io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry}.
   * @since 2.0.0
   */
  public PlayerHomeController(final @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry) {
    this.playerAggregateRootRegistry = playerAggregateRootRegistry;
  }

  /**
   * Adds the given {@link HomeModelEntity} to the player's homes.
   *
   * @param player the player.
   * @param homeModel the {@link HomeModelEntity} to add.
   * @return Whether the home was added, is player's owner, player hasn't reached {@link PlayerAggregateRoot#MAX_PLAYER_HOMES_ALLOWED}
   * and the home doesn't exist yet.
   * @see io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry#findInCache(String)
   * @since 2.0.0
   */
  public boolean addHome(final @NotNull Player player, final @NotNull HomeModelEntity homeModel) {
    final PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInCache(player.getUniqueId().toString());
    if (playerAggregateRoot == null) {
      return false;
    }
    final HomeModelEntity[] homes = playerAggregateRoot.homes();
    if (homes.length >= PlayerAggregateRoot.MAX_PLAYER_HOMES_ALLOWED) {
      return false;
    }
    Bukkit.getPluginManager().callEvent(new HomeCreateEvent(player, homeModel));
    // Array size-increasing logic.
    final HomeModelEntity[] newHomes = new HomeModelEntity[homes.length + 1];
    byte i = 0;
    for (; i < homes.length; i++) {
      if (homes[i].id().equals(homeModel.id())) return false;
      newHomes[i] = homes[i];
    }
    newHomes[i] = homeModel;
    playerAggregateRoot.homes(newHomes);
    return true;
  }

  /**
   * Removes the specified home from the player's homes.
   *
   * @param player the player.
   * @param homeId the home to delete.
   * @return Whether the home was existing and removed.
   * @see io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry#findInCache(String)
   * @see PlayerAggregateRoot#home(String)
   * @since 2.0.0
   */
  public boolean removeHome(final @NotNull Player player, final @NotNull String homeId) {
    final PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInCache(player.getUniqueId().toString());
    if (playerAggregateRoot == null) {
      return false;
    }
    // Will return false even if the player doesn't have homes.
    if (playerAggregateRoot.home(homeId) == null) {
      return false;
    }
    Bukkit.getPluginManager().callEvent(new HomeDeleteEvent(player, homeId));
    final HomeModelEntity[] homes = playerAggregateRoot.homes();
    final HomeModelEntity[] newHomes = new HomeModelEntity[homes.length - 1];
    for (byte i = 0; i < newHomes.length; i++) {
      if (homes[i].id().equals(homeId)) continue;
      newHomes[i] = homes[i];
    }
    playerAggregateRoot.homes(newHomes);
    return true;
  }

  /**
   * Teleports the player to the specified home, only if he's the owner.
   *
   * @param player the player.
   * @param homeId the home to teleport to.
   * @return Whether the player was teleport (if is home's owner).
   * @see io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry#findInCache(String)
   * @see PlayerAggregateRoot#home(String)
   * @since 2.0.0
   */
  public boolean teleportToHome(final @NotNull Player player, final @NotNull String homeId) {
    final PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInCache(player.getUniqueId().toString());
    if (playerAggregateRoot == null) {
      return false;
    }
    final HomeModelEntity homeModel = playerAggregateRoot.home(homeId);
    if (homeModel == null) {
      return false;
    }
    final HomePositionValueObject position = homeModel.position();
    player.teleportAsync(new Location(player.getWorld(), position.x(), position.y(), position.z()));
    return true;
  }
}
