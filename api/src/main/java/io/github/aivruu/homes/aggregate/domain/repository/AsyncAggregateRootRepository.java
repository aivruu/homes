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
package io.github.aivruu.homes.aggregate.domain.repository;

import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * A {@link AggregateRootRepository} implementation interface-contract that defines the methods
 * required for {@link AggregateRoot} information-handling at infrastructure.
 *
 * @param <A> an aggregate-root type.
 * @since 2.0.0
 */
public interface AsyncAggregateRootRepository<A extends AggregateRoot> extends AggregateRootRepository<A> {
  @Override
  default @Nullable A findSync(final @NotNull String id) {
    return null;
  }

  @Override
  default @NotNull Collection<A> findAllSync() {
    return List.of();
  }

  @Override
  default void saveSync(final @NotNull A aggregateRoot) {}

  @Override
  default boolean existsSync(final @NotNull String id) {
    return false;
  }

  @Override
  default @Nullable A deleteSync(final @NotNull String id) {
    return null;
  }

  @Override
  default void clearSync() {}

  /**
   * Returns the {@link AggregateRoot} for the id specified.
   *
   * @param id the aggregate-root's id.
   * @return A {@link CompletableFuture} with the {@link AggregateRoot} if found, otherwise {@code null}.
   * @since 2.0.0
   */
  @NotNull CompletableFuture<@Nullable A> findAsync(final @NotNull String id);

  /**
   * Checks if the {@link AggregateRoot} specified exists in repository.
   *
   * @param id the aggregate-root's id.
   * @return A {@link CompletableFuture} with a {@code boolean} value for aggregate-root existing.
   * @since 2.0.0
   */
  @NotNull CompletableFuture<Boolean> existsAsync(final @NotNull String id);

  /**
   * Saves the given {@link AggregateRoot} into repository.
   *
   * @param aggregateRoot the aggregate-root to save.
   * @return A {@link CompletableFuture} with a {@code boolean} value for successful saving.
   * @since 2.0.0
   */
  @NotNull CompletableFuture<Boolean> saveAsync(final @NotNull A aggregateRoot);

  /**
   * Deletes the {@link AggregateRoot} specified from repository.
   *
   * @param id the aggregate-root's id.
   * @return A {@link CompletableFuture} with a {@code boolean} value for successful deletion.
   * @since 2.0.0
   */
  @NotNull CompletableFuture<Boolean> deleteAsync(final @NotNull String id);
}
