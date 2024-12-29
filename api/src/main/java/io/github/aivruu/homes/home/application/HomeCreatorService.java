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
package io.github.aivruu.homes.home.application;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.player.application.PlayerHomeController;
import io.github.aivruu.homes.player.application.PlayerManagerService;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * This service is used to proportionate {@link HomeModelEntity} creation for
 * information provided by the user.
 *
 * @since 2.0.0
 */
public final class HomeCreatorService {
  private final AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry;
  private final PlayerHomeController playerHomeController;

  /**
   * Creates a new {@link HomeCreatorService} with the provided parameters.
   *
   * @param playerAggregateRootRegistry the {@link io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry}.
   * @param playerHomeController the {@link PlayerHomeController}.
   * @since 2.0.0
   */
  public HomeCreatorService(final @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry, final @NotNull PlayerHomeController playerHomeController) {
    this.playerAggregateRootRegistry = playerAggregateRootRegistry;
    this.playerHomeController = playerHomeController;
  }

  /**
   * Creates a new home for the specified player and saves it into player's homes-array.
   *
   * @param player the player who is creating the home.
   * @param homeId the home's id.
   * @return Whether the home was created correctly and doesn't exist previously.
   * @see PlayerManagerService#playerAggregateRootOf(String)
   * @see PlayerAggregateRoot#home(String)
   * @since 2.0.0
   */
  public boolean create(final @NotNull Player player, final @NotNull String homeId) {
    final PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInCache(player.getUniqueId().toString());
    if (playerAggregateRoot == null) {
      return false;
    }
    if (playerAggregateRoot.home(homeId) != null) {
      return false;
    }
    final Location at = player.getLocation();
    final HomeModelEntity homeModel = new HomeModelEntity(homeId,
      new HomePositionValueObject(player.getWorld(), at.getBlockX(), at.getBlockY(), at.getBlockZ()));
    return this.playerHomeController.addHome(player, homeModel);
  }
}
