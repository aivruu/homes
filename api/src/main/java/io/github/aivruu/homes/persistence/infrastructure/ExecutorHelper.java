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
package io.github.aivruu.homes.persistence.infrastructure;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * This class is used to proportionate a custom-size {@link Thread}-pool for the application.
 *
 * @since 2.0.0
 */
public final class ExecutorHelper {
  private static Executor executorPool;

  private ExecutorHelper() {
    throw new UnsupportedOperationException("This class is for utility and cannot be instantiated.");
  }

  /**
   * Returns this helper's {@link Executor}.
   *
   * @return The {@link Executor}.
   * @since 2.0.0
   */
  public static @NotNull Executor executorPool() {
    return executorPool;
  }

  /**
   * Creates a new thread-pool for the {@link Executor} using the given threads number.
   *
   * @param threads the number of threads to assign.
   * @since 2.0.0
   */
  public static void createPool(final int threads) {
    if (executorPool != null) {
      return;
    }
    executorPool = Executors.newFixedThreadPool(threads, r -> new Thread(r, "Homes-Thread-Executor"));
  }
}
