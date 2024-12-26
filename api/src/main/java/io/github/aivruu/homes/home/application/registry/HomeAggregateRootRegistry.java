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
package io.github.aivruu.homes.home.application.registry;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.aggregate.domain.repository.AsyncAggregateRootRepository;
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * An {@link AggregateRootRegistry} implementation for {@link HomeAggregateRoot} management.
 *
 * @since 2.0.0
 */
public final class HomeAggregateRootRegistry implements AggregateRootRegistry<HomeAggregateRoot> {
  private final AggregateRootRepository<HomeAggregateRoot> homeAggregateRootRepository;
  private final AsyncAggregateRootRepository<HomeAggregateRoot> homeAsyncAggregateRootRepository;

  public HomeAggregateRootRegistry(
    final @NotNull AggregateRootRepository<HomeAggregateRoot> homeAggregateRootRepository,
    final @NotNull AsyncAggregateRootRepository<HomeAggregateRoot> homeAsyncAggregateRootRepository) {
    this.homeAggregateRootRepository = homeAggregateRootRepository;
    this.homeAsyncAggregateRootRepository = homeAsyncAggregateRootRepository;
  }

  @Override
  public @Nullable HomeAggregateRoot findInCache(final @NotNull String id) {
    return this.homeAggregateRootRepository.findSync(id);
  }

  @Override
  public @Nullable HomeAggregateRoot findInBoth(final @NotNull String id) {
    final HomeAggregateRoot homeAggregateRoot = this.homeAggregateRootRepository.findSync(id);
    return (homeAggregateRoot != null) ? homeAggregateRoot : this.findInInfrastructure(id);
  }

  @Override
  public @Nullable HomeAggregateRoot findInInfrastructure(final @NotNull String id) {
    final AtomicReference<HomeAggregateRoot> homeAggregateRootAtomicReference = new AtomicReference<>();
    // Process to infrastructure information-request for error-printing and value return.
    this.homeAsyncAggregateRootRepository.findAsync(id)
      .handle((providedHomeAggregateRoot, exception) -> {
        if (exception != null) {
          exception.printStackTrace();
        }
        return providedHomeAggregateRoot;
      })
      .thenAccept((providedHomeAggregateRoot) -> {
        if (providedHomeAggregateRoot != null) {
          this.homeAggregateRootRepository.saveSync(providedHomeAggregateRoot);
        }
        homeAggregateRootAtomicReference.set(providedHomeAggregateRoot);
      });
    return homeAggregateRootAtomicReference.get();
  }

  @Override
  public @NotNull Collection<HomeAggregateRoot> findAllInCache() {
    return this.homeAggregateRootRepository.findAllSync();
  }

  @Override
  public boolean existsGlobally(final @NotNull String id) {
    return this.homeAggregateRootRepository.existsSync(id) || this.existsInInfrastructure(id);
  }

  @Override
  public boolean existsInCache(final @NotNull String id) {
    return this.homeAggregateRootRepository.existsSync(id);
  }

  @Override
  public boolean existsInInfrastructure(final @NotNull String id) {
    final AtomicBoolean provider = new AtomicBoolean();
    this.homeAsyncAggregateRootRepository.existsAsync(id).thenAccept(provider::set);
    return provider.get();
  }

  @Override
  public boolean registerAndSave(final @NotNull HomeAggregateRoot aggregateRoot) {
    if (this.homeAggregateRootRepository.existsSync(aggregateRoot.id())) {
      return false;
    }
    this.homeAggregateRootRepository.saveSync(aggregateRoot);
    final AtomicBoolean provider = new AtomicBoolean();
    this.homeAsyncAggregateRootRepository.saveAsync(aggregateRoot).thenAccept(provider::set);
    return provider.get();
  }

  @Override
  public boolean unregisterAndSave(final @NotNull String id) {
    // At infrastructure aggregate-root data saving is already made by the removal-listener.
    return this.homeAggregateRootRepository.deleteSync(id) != null;
  }

  @Override
  public boolean save(final @NotNull HomeAggregateRoot aggregateRoot) {
    final AtomicBoolean provider = new AtomicBoolean();
    this.homeAsyncAggregateRootRepository.saveAsync(aggregateRoot).thenAccept(provider::set);
    return provider.get();
  }

  @Override
  public boolean unregisterGlobally(final @NotNull String id) {
    if (!this.existsGlobally(id)) {
      return false;
    }
    this.homeAggregateRootRepository.deleteSync(id);
    final AtomicBoolean provider = new AtomicBoolean();
    this.homeAsyncAggregateRootRepository.deleteAsync(id).thenAccept(provider::set);
    return provider.get();
  }
}
