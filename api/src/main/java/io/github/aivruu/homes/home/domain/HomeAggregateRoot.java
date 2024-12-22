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

import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.result.domain.ValueObjectMutationResult;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link AggregateRoot} implementation for {@link HomeModelEntity}.
 *
 * @since 2.0.0
 */
public final class HomeAggregateRoot extends AggregateRoot {
  /** Indicates that the new position provided is the same that current one. */
  public static final byte POSITION_GIVEN_MUST_BE_DIFFERENT = -1;
  private final HomeModelEntity homeModel;

  /**
   * Creates a new {@link HomeAggregateRoot} with the provided parameters.
   *
   * @param homeModel the {@link HomeModelEntity} to be used.
   * @since 2.0.0
   */
  public HomeAggregateRoot(final @NotNull HomeModelEntity homeModel) {
    super(homeModel.id());
    this.homeModel = homeModel;
  }

  /**
   * Returns the identifier of the home's owner.
   *
   * @return The {@link HomeModelEntity}'s owner's id.
   * @see HomeModelEntity#ownerId()
   * @since 2.0.0
   */
  public @NotNull String ownerId() {
    return this.homeModel.ownerId();
  }

  /**
   * Returns the position of the home.
   *
   * @return The {@link HomePositionValueObject}'s coordinates container.
   * @see HomeModelEntity#position()
   * @since 2.0.0
   */
  public @NotNull HomePositionValueObject position() {
    return this.homeModel.position();
  }

  /**
   * Sets a new {@link HomePositionValueObject} for this aggregate-root's {@link HomeModelEntity}.
   *
   * @param newPosition the new home's location.
   * @return A status code which can be:
   * <ul>
   * <li>{@link #POSITION_GIVEN_MUST_BE_DIFFERENT} if the given position is the same than the current one.</li>
   * <li>{@link ValueObjectMutationResult#MUTATED_STATUS} if the position was updated.</li>
   * </ul>
   * @see HomeModelEntity#position(HomePositionValueObject)
   * @since 2.0.0
   */
  public byte position(final @NotNull HomePositionValueObject newPosition) {
    if (newPosition.equals(this.homeModel.position())) {
      return POSITION_GIVEN_MUST_BE_DIFFERENT;
    }
    this.homeModel.position(newPosition);
    return ValueObjectMutationResult.MUTATED_STATUS;
  }
}
