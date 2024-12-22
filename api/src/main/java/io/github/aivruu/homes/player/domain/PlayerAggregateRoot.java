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
package io.github.aivruu.homes.player.domain;

import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link AggregateRoot} implementation for {@link PlayerModelEntity}.
 *
 * @since 2.0.0
 */
public final class PlayerAggregateRoot extends AggregateRoot {
  public static final byte MAX_PLAYER_HOMES_ALLOWED = 5;
  private final PlayerModelEntity playerModel;

  /**
   * Creates a new {@link PlayerAggregateRoot} with the provided parameters.
   *
   * @param playerModel the {@link PlayerModelEntity} to be used.
   * @since 2.0.0
   */
  public PlayerAggregateRoot(final @NotNull PlayerModelEntity playerModel) {
    super(playerModel.id());
    this.playerModel = playerModel;
  }

  /**
   * Returns the identifier of the player.
   *
   * @return The {@link PlayerModelEntity}'s id.
   * @see PlayerModelEntity#id()
   * @since 2.0.0
   */
  public @NotNull String id() {
    return this.playerModel.id();
  }

  /**
   * Returns the player's homes-array.
   *
   * @return The {@link PlayerModelEntity}'s homes.
   * @see PlayerModelEntity#homes()
   * @since 2.0.0
   */
  public @NotNull HomeModelEntity[] homes() {
    return this.playerModel.homes();
  }

  /**
   * Checks if the player has a home with the provided id.
   *
   * @param home the home to check.
   * @return Whether the player has the specified home.
   * @see PlayerModelEntity#has(String)
   * @since 2.0.0
   */
  public boolean exists(final @NotNull String home) {
    return this.playerModel.has(home);
  }

  /**
   * Returns a {@link HomeModelEntity} from the player's homes-array with the provided id.
   *
   * @param home the home to find.
   * @return The {@link HomeModelEntity} or {@code null} if not exist.
   * @since 2.0.0
   */
  public @Nullable HomeModelEntity home(final @NotNull String home) {
    return this.playerModel.indexOfHome(home);
  }

  /**
   * Sets a new homes-array for this aggregate-root's {@link PlayerModelEntity}.
   *
   * @param homes the new homes-array.
   * @since 2.0.0
   */
  public void homes(final @NotNull HomeModelEntity[] homes) {
    this.playerModel.homes(homes);
  }
}
