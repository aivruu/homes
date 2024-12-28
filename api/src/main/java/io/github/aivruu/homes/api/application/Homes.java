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
package io.github.aivruu.homes.api.application;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.home.application.HomeCreatorService;
import io.github.aivruu.homes.home.application.HomePositionUpdater;
import io.github.aivruu.homes.player.application.PlayerHomeController;
import io.github.aivruu.homes.player.application.PlayerManagerService;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.jetbrains.annotations.NotNull;

/**
 * An interface used for plugin's API classes instances accessing.
 *
 * @since 2.0.0
 */
public interface Homes {
  /**
   * Returns the {@link AggregateRootRepository} implementation for {@link PlayerAggregateRoot}
   * in-cache management.
   *
   * @return The {@link AggregateRootRepository} implementation for {@link PlayerAggregateRoot}.
   * @since 2.0.0
   */
  @NotNull AggregateRootRepository<PlayerAggregateRoot> playerCacheRepository();

  /**
   * Returns the {@link AggregateRootRegistry} implementation for {@link PlayerAggregateRoot}
   * global-registry management.
   *
   * @return The {@link AggregateRootRegistry} implementation for {@link PlayerAggregateRoot}.
   * @since 2.0.0
   */
  @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerRegistry();

  /**
   * Returns the {@link HomeCreatorService} instance for {@link io.github.aivruu.homes.home.domain.HomeModelEntity} creation
   * and deletion handling.
   *
   * @return The {@link HomeCreatorService}.
   * @since 2.0.0
   */
  @NotNull HomeCreatorService homeCreatorService();

  /**
   * Returns the {@link HomePositionUpdater} instance for {@link io.github.aivruu.homes.home.domain.HomeModelEntity}'s position
   * modifying.
   *
   * @return The {@link HomePositionUpdater}.
   * @since 2.0.0
   */
  @NotNull HomePositionUpdater homePositionUpdater();

  /**
   * Returns the {@link PlayerHomeController} instance for {@link PlayerAggregateRoot}'s homes
   * management, such as addition, deletion or teleporting.
   *
   * @return The {@link PlayerHomeController}.
   * @since 2.0.0
   */
  @NotNull PlayerHomeController playerHomeController();

  /**
   * Returns the {@link PlayerManagerService} instance for {@link PlayerAggregateRoot} loading
   * and unloading operations.
   *
   * @return The {@link PlayerManagerService}.
   * @since 2.0.0
   */
  @NotNull PlayerManagerService playerManagerService();
}
