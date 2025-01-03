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
package io.github.aivruu.homes.player.application.registry;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.aggregate.domain.repository.AsyncAggregateRootRepository;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * An {@link AggregateRootRegistry} implementation for {@link PlayerAggregateRoot} management.
 *
 * @since 2.0.0
 */
public final class PlayerAggregateRootRegistry implements AggregateRootRegistry<PlayerAggregateRoot> {
  private final AggregateRootRepository<PlayerAggregateRoot> playerAggregateRootRepository;
  private final AsyncAggregateRootRepository<PlayerAggregateRoot> playerAsyncAggregateRootRepository;

  public PlayerAggregateRootRegistry(
    final @NotNull AggregateRootRepository<PlayerAggregateRoot> playerAggregateRootRepository,
    final @NotNull AsyncAggregateRootRepository<PlayerAggregateRoot> playerAsyncAggregateRootRepository) {
    this.playerAggregateRootRepository = playerAggregateRootRepository;
    this.playerAsyncAggregateRootRepository = playerAsyncAggregateRootRepository;
  }

  @Override
  public @Nullable PlayerAggregateRoot findInCache(final @NotNull String id) {
    return this.playerAggregateRootRepository.findSync(id);
  }

  @Override
  public @Nullable PlayerAggregateRoot findInBoth(final @NotNull String id) {
    return null;
  }

  @Override
  public @Nullable PlayerAggregateRoot findInInfrastructure(final @NotNull String id) {
    return this.playerAsyncAggregateRootRepository.findAsync(id).join();
  }

  @Override
  public @NotNull Collection<PlayerAggregateRoot> findAllInCache() {
    return this.playerAggregateRootRepository.findAllSync();
  }

  @Override
  public boolean existsGlobally(final @NotNull String id) {
    return this.playerAggregateRootRepository.existsSync(id) || this.existsInInfrastructure(id);
  }

  @Override
  public boolean existsInCache(final @NotNull String id) {
    return this.playerAggregateRootRepository.existsSync(id);
  }

  @Override
  public boolean existsInInfrastructure(final @NotNull String id) {
    final AtomicBoolean provider = new AtomicBoolean();
    this.playerAsyncAggregateRootRepository.existsAsync(id).thenAccept(provider::set);
    return provider.get();
  }

  @Override
  public void register(final @NotNull PlayerAggregateRoot aggregateRoot) {
    this.playerAggregateRootRepository.saveSync(aggregateRoot);
  }

  @Override
  public @Nullable PlayerAggregateRoot unregister(final @NotNull String id) {
    return this.playerAggregateRootRepository.deleteSync(id);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> save(final @NotNull PlayerAggregateRoot aggregateRoot) {
    return this.playerAsyncAggregateRootRepository.saveAsync(aggregateRoot);
  }
}
