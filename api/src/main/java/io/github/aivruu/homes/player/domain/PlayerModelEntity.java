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

import io.github.aivruu.homes.home.domain.HomeModelEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a connected player for the plugin.
 *
 * @since 2.0.0
 */
public final class PlayerModelEntity {
  private final String id;
  private HomeModelEntity[] homes;

  /**
   * Creates a new {@link PlayerModelEntity} with the provided parameters.
   *
   * @param id the player's id.
   * @param homes the player's current homes.
   * @since 2.0.0
   */
  public PlayerModelEntity(final @NotNull String id, final @NotNull HomeModelEntity[] homes) {
    if (homes.length > PlayerAggregateRoot.MAX_PLAYER_HOMES_ALLOWED) {
      throw new IllegalArgumentException("Player can't have more than expected homes limit: " + PlayerAggregateRoot.MAX_PLAYER_HOMES_ALLOWED);
    }
    this.id = id;
    this.homes = homes;
  }

  /**
   * Returns the player's id.
   *
   * @return This {@link PlayerModelEntity}'s id.
   * @since 2.0.0
   */
  @NotNull String id() {
    return this.id;
  }

  /**
   * Returns the {@link HomeModelEntity} for the specified id.
   *
   * @param home the home to check.
   * @return A {@link HomeModelEntity} or {@code null} if not found.
   * @since 2.0.0
   */
  @Nullable HomeModelEntity home(final @NotNull String home) {
    for (byte i = 0; i < this.homes.length; i++) {
      if (this.homes[i] == null || !this.homes[i].id().equals(home)) continue;
      return this.homes[i];
    }
    return null;
  }

  /**
   * Returns the player's homes.
   *
   * @return This {@link PlayerModelEntity}'s {@link HomeModelEntity} array.
   * @since 2.0.0
   */
  @NotNull HomeModelEntity[] homes() {
    return this.homes;
  }

  /**
   * Returns a {@link HomeModelEntity} from the homes-array based on the provided home-id.
   *
   * @param home the home to search.
   * @return The {@link HomeModelEntity} or {@code null} if not exists.
   * @since 2.0.0
   */
  @Nullable HomeModelEntity indexOfHome(final @NotNull String home) {
    byte start = 0;
    byte end = (byte) (this.homes.length - 1);

    while (start <= end) {
      final int mid = start + (end - start) >>> 1;
      final int comparison = homes[mid].id().compareTo(home);
      if (comparison == 0) {
        return homes[mid];
      } else if (comparison < 0) {
        start = (byte) (mid + 1);
      } else {
        end = (byte) (mid - 1);
      }
    }
    return null;
  }

  /**
   * Sets a new {@link HomeModelEntity} array for this player.
   *
   * @param homes the new homes array.
   * @since 2.0.0
   */
  void homes(final @NotNull HomeModelEntity[] homes) {
    this.homes = homes;
  }
}
