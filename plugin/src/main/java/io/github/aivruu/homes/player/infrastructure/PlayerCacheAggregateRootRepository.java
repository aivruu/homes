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
package io.github.aivruu.homes.player.infrastructure;

import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class PlayerCacheAggregateRootRepository implements AggregateRootRepository<PlayerAggregateRoot> {
  private final Map<String, PlayerAggregateRoot> cache = new HashMap<>();

  @Override
  public @Nullable PlayerAggregateRoot findSync(final @NotNull String id) {
    return this.cache.get(id);
  }

  @Override
  public @NotNull Collection<PlayerAggregateRoot> findAllSync() {
    return this.cache.values();
  }

  @Override
  public void saveSync(final @NotNull PlayerAggregateRoot aggregateRoot) {
    this.cache.put(aggregateRoot.id(), aggregateRoot);
  }

  @Override
  public boolean existsSync(final @NotNull String id) {
    return this.cache.get(id) != null;
  }

  @Override
  public @Nullable PlayerAggregateRoot deleteSync(final @NotNull String id) {
    return this.cache.remove(id);
  }

  @Override
  public void clearSync() {
    this.cache.clear();
  }
}
