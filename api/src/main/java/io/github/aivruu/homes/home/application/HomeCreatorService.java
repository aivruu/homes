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
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This service is used to proportionate homes creation and deletion operations.
 *
 * @since 2.0.0
 */
public final class HomeCreatorService {
  private final AggregateRootRegistry<HomeAggregateRoot> homeAggregateRootRegistry;

  public HomeCreatorService(final @NotNull AggregateRootRegistry<HomeAggregateRoot> homeAggregateRootRegistry) {
    this.homeAggregateRootRegistry = homeAggregateRootRegistry;
  }

  /**
   * Returns a {@link HomeAggregateRoot} for the specified id if exists.
   *
   * @param homeId the home's id.
   * @return The {@link HomeAggregateRoot} or {@code null}.
   * @since 2.0.0
   */
  public @Nullable HomeAggregateRoot homeAggregateRootOf(final @NotNull String homeId) {
    return this.homeAggregateRootRegistry.findInBoth(homeId);
  }

  /**
   * Creates a new home for the specified player and saves it into the {@link AggregateRootRegistry}
   * for homes.
   *
   * @param player the player who is creating the home.
   * @param homeId the home's id.
   * @return The created {@link HomeModelEntity} if the home was registered and saved, otherwise {@code null}.
   * @see io.github.aivruu.homes.home.application.registry.HomeAggregateRootRegistry#registerAndSave(HomeAggregateRoot)
   * @since 2.0.0
   */
  public @Nullable HomeModelEntity createHome(final @NotNull Player player, final @NotNull String homeId) {
    // Where this home is going to be located.
    final Location at = player.getLocation();
    final HomeModelEntity homeModel = new HomeModelEntity(
      homeId, player.getUniqueId().toString(), new HomePositionValueObject(at.getBlockX(), at.getBlockY(), at.getBlockZ()));
    final HomeAggregateRoot homeAggregateRoot = new HomeAggregateRoot(homeModel);
    return this.homeAggregateRootRegistry.registerAndSave(homeAggregateRoot) ? homeModel : null;
  }

  /**
   * Deletes the specified home's information.
   *
   * @param homeId the home's id.
   * @return Whether the home was fully deleted.
   * @see io.github.aivruu.homes.home.application.registry.HomeAggregateRootRegistry#unregisterGlobally(String)
   * @since 2.0.0
   */
  public boolean deleteHome(final @NotNull String homeId) {
    return this.homeAggregateRootRegistry.unregisterGlobally(homeId);
  }
}
