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
package io.github.aivruu.homes.home.infrastructure;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Collection;

public final class HomeCacheAggregateRootRepository implements AggregateRootRepository<HomeAggregateRoot> {
  private final AggregateRootRegistry<HomeAggregateRoot> homeAggregateRootRegistry;
  private final Cache<String, HomeAggregateRoot> cache;
  private boolean should = true;

  public HomeCacheAggregateRootRepository(final @NotNull AggregateRootRegistry<HomeAggregateRoot> homeAggregateRootRegistry) {
    this.homeAggregateRootRegistry = homeAggregateRootRegistry;
    this.cache = Caffeine.newBuilder()
      .expireAfterWrite(Duration.ofMinutes(5))
      .removalListener((key, value, cause) -> {
        if (value == null || !should) {
          return;
        }
        this.homeAggregateRootRegistry.save((HomeAggregateRoot) value);
      })
      .build();
  }

  @Override
  public @Nullable HomeAggregateRoot findSync(final @NotNull String id) {
    return this.cache.getIfPresent(id);
  }

  @Override
  public @NotNull Collection<HomeAggregateRoot> findAllSync() {
    return this.cache.asMap().values();
  }

  @Override
  public void saveSync(final @NotNull HomeAggregateRoot aggregateRoot) {
    this.cache.put(aggregateRoot.id(), aggregateRoot);
  }

  @Override
  public boolean existsSync(final @NotNull String id) {
    return this.cache.getIfPresent(id) != null;
  }

  @Override
  public @Nullable HomeAggregateRoot deleteSync(final @NotNull String id) {
    final HomeAggregateRoot homeAggregateRoot = this.cache.getIfPresent(id);
    if (homeAggregateRoot != null) {
      this.cache.invalidate(id);
    }
    return homeAggregateRoot;
  }

  @Override
  public void clearSync() {
    should = false;
    this.cache.invalidateAll();
  }
}
