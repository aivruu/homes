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

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player deletes a home.
 *
 * @since 2.0.0
 */
public final class HomeDeleteEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final String homeId;

  /**
   * Creates a new {@link HomeDeleteEvent} with the provided parameters.
   *
   * @param player the player involved.
   * @param homeId the deleted home's id.
   * @since 2.0.0
   */
  public HomeDeleteEvent(final @NotNull Player player, final @NotNull String homeId) {
    this.player = player;
    this.homeId = homeId;
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
   * Returns the deleted home's id.
   *
   * @return The home's id.
   * @since 2.0.0
   */
  public @NotNull String homeId() {
    return this.homeId;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
