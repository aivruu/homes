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
package io.github.aivruu.homes.aggregate.domain.registry;

import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface defines the set of methods that subclasses must implement to perform
 * any operation with {@link AggregateRoot}'s registries related.
 *
 * @param <A> an aggregate-root type.
 * @since 2.0.0
 */
public interface AggregateRootRegistry<A extends AggregateRoot> {
  /**
   * Returns the {@link AggregateRoot} specified from the cache if found.
   *
   * @param id the aggregate-root's identifier.
   * @return The {@link AggregateRoot} or {@code null} if not found in cache.
   * @since 2.0.0
   */
  @Nullable A findInCache(final @NotNull String id);

  /**
   * Returns the {@link AggregateRoot} specified from the cache-repository if found,
   * otherwise it will search at infrastructure-repository by aggregate-root's information
   * and will return it if found.
   *
   * @param id the aggregate-root's identifier.
   * @return The {@link AggregateRoot} or {@code null} if not exists.
   * @since 2.0.0
   */
  @Nullable A findInBoth(final @NotNull String id);

  /**
   * Returns the {@link AggregateRoot} specified from the infrastructure-repository
   * if found.
   *
   * @param id the aggregate-root's identifier.
   * @return The {@link AggregateRoot} or {@code null} if not found in infrastructure.
   * @since 2.0.0
   */
  @Nullable A findInInfrastructure(final @NotNull String id);

  /**
   * Checks if the aggregate-root specified is cached or saved at the infrastructure.
   *
   * @param id the aggregate-root's identifier.
   * @return Whether the aggregate-root is cached or at the infrastructure.
   * @since 2.0.0
   */
  boolean existsGlobally(final @NotNull String id);

  /**
   * Checks if the aggregate-root specified is cached.
   *
   * @param id the aggregate-root's identifier.
   * @return Whether the aggregate-root is cached.
   * @since 2.0.0
   */
  boolean existsInCache(final @NotNull String id);

  /**
   * Checks if the aggregate-root specified exists in the infrastructure.
   *
   * @param id the aggregate-root's identifier.
   * @return Whether the aggregate-root is in the infrastructure.
   * @since 2.0.0
   */
  boolean existsInInfrastructure(final @NotNull String id);

  /**
   * Loads the given {@link AggregateRoot} into cache and saves it at the infrastructure.
   *
   * @param aggregateRoot the {@link AggregateRoot} to register.
   * @return Whether the aggregate-root was registered and saved correctly.
   * @since 2.0.0
   */
  boolean registerAndSave(final @NotNull A aggregateRoot);

  /**
   * Unregisters the {@link AggregateRoot} specified from the cache and saves it in the
   * infrastructure.
   *
   * @param id the aggregate-root's identifier.
   * @return Whether the aggregate-root was unregistered and saved.
   * @since 2.0.0
   */
  boolean unregisterAndSave(final @NotNull String id);

  /**
   * Saves the given {@link AggregateRoot} into the infrastructure and returns a
   * {@code boolean} result.
   *
   * @param aggregateRoot the {@link AggregateRoot} to save.
   * @return Whether the aggregate-root was saved correctly.
   * @since 2.0.0
   */
  boolean save(final @NotNull A aggregateRoot);

  /**
   * Deletes the aggregate-root specified from the cache (if it is cached) and from
   * the infrastructure.
   *
   * @param id the aggregate-root's identifier.
   * @return Whether the aggregate-root was fully deleted successfully.
   * @since 2.0.0
   */
  boolean unregisterGlobally(final @NotNull String id);
}
