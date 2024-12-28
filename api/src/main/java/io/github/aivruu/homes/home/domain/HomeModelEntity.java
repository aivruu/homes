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
package io.github.aivruu.homes.home.domain;

import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an existing home for a player.
 *
 * @since 2.0.0
 */
public final class HomeModelEntity {
  private final String id;
  private HomePositionValueObject position;

  /**
   * Creates a new {@link HomeModelEntity} with the provided parameters.
   *
   * @param id the home's id.
   * @param position the home's position.
   * @since 2.0.0
   */
  public HomeModelEntity(final @NotNull String id, final @NotNull HomePositionValueObject position) {
    this.id = id;
    this.position = position;
  }

  /**
   * Returns the home's id.
   *
   * @return The home's identifier.
   * @since 2.0.0
   */
  public @NotNull String id() {
    return this.id;
  }

  /**
   * Returns this home's {@link HomePositionValueObject}.
   *
   * @return The home's coordinates container value-object.
   * @since 2.0.0
   */
  public @NotNull HomePositionValueObject position() {
    return this.position;
  }

  /**
   * Sets a new {@link HomePositionValueObject} for this home.
   *
   * @param position a new position for this home.
   * @since 2.0.0
   */
  public void position(final @NotNull HomePositionValueObject position) {
    this.position = position;
  }
}
