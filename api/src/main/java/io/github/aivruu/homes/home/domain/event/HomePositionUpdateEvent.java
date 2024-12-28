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
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * This event is fired when a player updates' home's position, only if the provided one is
 * different.
 *
 * @since 2.0.0
 */
public final class HomePositionUpdateEvent extends Event implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final HomeModelEntity homeModel;
  private final HomePositionValueObject oldPosition;
  private final HomePositionValueObject newPosition;
  private boolean cancelled;

  /**
   * Creates a new {@link HomePositionUpdateEvent} with the provided parameters.
   *
   * @param player the player involved.
   * @param homeModel the {@link HomeModelEntity} for this event.
   * @param oldPosition the home's old location.
   * @param newPosition the home's current location.
   * @since 2.0.0
   */
  public HomePositionUpdateEvent(
    final @NotNull Player player,
    final @NotNull HomeModelEntity homeModel,
    final @NotNull HomePositionValueObject oldPosition,
    final @NotNull HomePositionValueObject newPosition) {
    this.player = player;
    this.homeModel = homeModel;
    this.oldPosition = oldPosition;
    this.newPosition = newPosition;
  }

  /**
   * Returns the {@link Player} involved in this event.
   *
   * @return The {@link Player}.
   * @since 2.0.0
   */
  public @NotNull Player player() {
    return this.player;
  }

  /**
   * Returns the {@link HomeModelEntity} involved in this event.
   *
   * @return The {@link HomeModelEntity}.
   * @since 2.0.0
   */
  public @NotNull HomeModelEntity homeModel() {
    return this.homeModel;
  }

  /**
   * Returns the home's old position.
   *
   * @return The home's old position.
   * @since 2.0.0
   */
  public @NotNull HomePositionValueObject oldPosition() {
    return this.oldPosition;
  }

  /**
   * Returns the home's new position.
   *
   * @return The home's new position.
   * @since 2.0.0
   */
  public @NotNull HomePositionValueObject newPosition() {
    return this.newPosition;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
