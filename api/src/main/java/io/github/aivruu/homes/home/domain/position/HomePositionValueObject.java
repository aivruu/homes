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
package io.github.aivruu.homes.home.domain.position;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a {@link io.github.aivruu.homes.home.domain.HomeModelEntity}'s location.
 *
 * @param x the home's x-coordinate.
 * @param y the home's y-coordinate.
 * @param z the home's z-coordinate.
 * @since 2.0.0
 */
public record HomePositionValueObject(int x, int y, int z) {
  @Override
  public boolean equals(final @NotNull Object obj) {
    if (obj == this) return true;
    if (!(obj instanceof HomePositionValueObject homePosition)) {
      return false;
    }
    return this.x == homePosition.x && this.y == homePosition.y && this.z == homePosition.z;
  }
}
