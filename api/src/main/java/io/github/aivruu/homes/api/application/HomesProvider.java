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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link Homes} instances provider.
 *
 * @since 2.0.0
 */
public final class HomesProvider {
  private static @Nullable Homes instance;

  private HomesProvider() {
    throw new UnsupportedOperationException("This class shouldn't be instantiated.");
  }

  /**
   * Returns the instance for the {@link Homes} type-field.
   *
   * @return The {@link Homes}.
   * @since 2.0.0
   */
  public static @NotNull Homes get() {
    if (instance == null) {
      throw new IllegalStateException("Plugin's API-instance has not been initialized yet.");
    }
    return instance;
  }

  /**
   * Sets the given instance for the {@link Homes} type-field.
   *
   * @param impl a {@link Homes} implementation.
   * @since 2.0.0
   */
  public static void set(final @NotNull Homes impl) {
    if (instance != null) {
      throw new IllegalStateException("Plugin's API-instance has already been initialized.");
    }
    instance = impl;
  }
}
