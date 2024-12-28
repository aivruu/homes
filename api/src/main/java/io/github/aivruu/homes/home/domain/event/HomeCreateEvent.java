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
package io.github.aivruu.homes.home.domain.event;

import io.github.aivruu.homes.home.domain.HomeModelEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player creates a new home.
 *
 * @since 2.0.0
 */
public final class HomeCreateEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final HomeModelEntity homeModel;

  /**
   * Creates a new {@link HomeCreateEvent} with the provided parameters.
   *
   * @param player the player involved.
   * @param homeModel the created home's {@link HomeModelEntity}.
   * @since 2.0.0
   */
  public HomeCreateEvent(final @NotNull Player player, final @NotNull HomeModelEntity homeModel) {
    this.player = player;
    this.homeModel = homeModel;
  }

  /**
   * Returns the player involved in this event.
   *
   * @return The {@link Player}.
   * @since 2.0.0
   */
  public @NotNull Player player() {
    return this.player;
  }

  /**
   * Returns the created home's {@link HomeModelEntity}.
   *
   * @return The {@link HomeModelEntity}.
   * @since 2.0.0
   */
  public @NotNull HomeModelEntity homeModel() {
    return this.homeModel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
