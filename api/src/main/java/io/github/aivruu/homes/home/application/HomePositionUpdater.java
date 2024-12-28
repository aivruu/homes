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
import io.github.aivruu.homes.home.domain.event.HomePositionUpdateEvent;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.result.domain.ValueObjectMutationResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is used to proportionate the logic for any {@link io.github.aivruu.homes.home.domain.HomeModelEntity}'s
 * {@link HomePositionValueObject} modification.
 *
 * @since 2.0.0
 */
public final class HomePositionUpdater {
  private final AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry;

  public HomePositionUpdater(final @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry) {
    this.playerAggregateRootRegistry = playerAggregateRootRegistry;
  }

  /**
   * Updates the {@link HomePositionValueObject} of the {@link io.github.aivruu.homes.home.domain.HomeModelEntity} specified
   * by the given identifier, and return a mutation-result.
   *
   * @param player the player who is updating their home's position.
   * @param homeId the home's id.
   * @param newLocation the player's {@link Location} used as new home-position.
   * @return A {@link ValueObjectMutationResult} for this operation. The result for this operation can be:
   * <ul>
   * <li>{@link ValueObjectMutationResult#mutated(Object)} if the position was modified. {@link ValueObjectMutationResult#result()} isn't {@code null}.</li>
   * <li>{@link ValueObjectMutationResult#unchanged()} if the position given is the same, or event is cancelled. {@link ValueObjectMutationResult#result()} is {@code null}.</li>
   * <li>{@link ValueObjectMutationResult#error()} if the home doesn't exist. {@link ValueObjectMutationResult#result()} is {@code null}.</li>
   * </ul>
   * @see io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry#findInCache(String)
   * @since 1.0.0
   */
  public @NotNull ValueObjectMutationResult<@Nullable HomePositionValueObject> updatePosition(
    final @NotNull Player player,
    final @NotNull String homeId,
    final @NotNull Location newLocation
  ) {
    final PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInCache(player.getUniqueId().toString());
    if (playerAggregateRoot == null) {
      return ValueObjectMutationResult.error();
    }
    final HomeModelEntity homeModel = playerAggregateRoot.home(homeId);
    if (homeModel == null) {
      return ValueObjectMutationResult.error();
    }
    // Get current home's position (location), and make some checks to avoid unnecessary updates if the
    // location has no changes.
    final HomePositionValueObject homePosition = homeModel.position();
    final int x = newLocation.getBlockX();
    final int y = newLocation.getBlockY();
    final int z = newLocation.getBlockZ();
    if (homePosition.x() == x && homePosition.y() == y && homePosition.z() == z) {
      return ValueObjectMutationResult.unchanged();
    }
    // Position-updating event firing process.
    final HomePositionValueObject newPosition = new HomePositionValueObject(x, y, z);
    final HomePositionUpdateEvent homePositionUpdateEvent = new HomePositionUpdateEvent(player, homeModel, homePosition, newPosition);
    Bukkit.getPluginManager().callEvent(homePositionUpdateEvent);
    if (homePositionUpdateEvent.isCancelled()) {
      return ValueObjectMutationResult.unchanged();
    }
    homeModel.position(newPosition);
    return ValueObjectMutationResult.mutated(newPosition);
  }
}
