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
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.player.application.PlayerHomeController;
import io.github.aivruu.homes.player.application.PlayerManagerService;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import org.jetbrains.annotations.NotNull;

public interface Homes {
  @NotNull AggregateRootRepository<PlayerAggregateRoot> playerCacheRepository();

  @NotNull AggregateRootRepository<HomeAggregateRoot> homeCacheRepository();

  @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerRegistry();

  @NotNull AggregateRootRegistry<HomeAggregateRoot> homeRegistry();

  @NotNull HomeCreatorService homeCreatorService();

  @NotNull HomePositionUpdater homePositionUpdater();

  @NotNull PlayerHomeController playerHomeController();

  @NotNull PlayerManagerService playerManagerService();
}
