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
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.domain.PlayerModelEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This service provides players-information loading and saving when required.
 *
 * @since 2.0.0
 */
public final class PlayerManagerService {
  private final AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry;

  /**
   * Creates a new {@link PlayerManagerService} with the provided parameters.
   *
   * @param playerAggregateRootRegistry the {@link io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry}.
   * @since 2.0.0
   */
  public PlayerManagerService(final @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry) {
    this.playerAggregateRootRegistry = playerAggregateRootRegistry;
  }

  /**
   * Returns a {@link PlayerAggregateRoot} for the provided id.
   *
   * @param id the player's id.
   * @return The {@link PlayerAggregateRoot} or {@code null} if player is offline.
   * @since 2.0.0
   */
  public @Nullable PlayerAggregateRoot playerAggregateRootOf(final @NotNull String id) {
    return this.playerAggregateRootRegistry.findInCache(id);
  }

  /**
   * Loads the specified player's information.
   *
   * @param id the player's id.
   * @return Whether the information was loaded correctly.
   * @since 2.0.0
   */
  public boolean loadOne(final @NotNull String id) {
    PlayerAggregateRoot playerAggregateRoot = this.playerAggregateRootRegistry.findInInfrastructure(id);
    if (playerAggregateRoot == null) {
      playerAggregateRoot = new PlayerAggregateRoot(new PlayerModelEntity(id, new HomeModelEntity[0]));
    }
    return this.playerAggregateRootRegistry.registerAndSave(playerAggregateRoot);
  }

  /**
   * Unloads and saves the specified player's information.
   *
   * @param id the player's id.
   * @return Whether the information was unloaded and saved.
   * @since 2.0.0
   */
  public boolean unloadOne(final @NotNull String id) {
    return this.playerAggregateRootRegistry.unregisterAndSave(id);
  }
}
