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
package io.github.aivruu.homes.persistence.domain;

import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import io.github.aivruu.homes.aggregate.domain.repository.AsyncAggregateRootRepository;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

/**
 * An infrastructure aggregate-root repository model.
 *
 * @param <A> an aggregate-root type.
 * @since 2.0.0
 */
public abstract class InfrastructureAggregateRootRepository<A extends AggregateRoot> implements AsyncAggregateRootRepository<A> {
  protected final Executor executor;

  protected InfrastructureAggregateRootRepository(final @NotNull Executor executor) {
    this.executor = executor;
  }

  /**
   * Executes this repository its start-up logic.
   *
   * @return Whether the infrastructure-repository was successfully started.
   * @since 2.0.0
   */
  public abstract boolean start();

  /**
   * Executes this repository its shutdown logic.
   *
   * @since 2.0.0
   */
  public abstract void close();

  /**
   * Returns the {@link Executor} used for this {@link InfrastructureAggregateRootRepository}'s
   * asynchronous operations.
   *
   * @return This repository its {@link Executor}.
   * @since 2.0.0
   */
  public @NotNull Executor executor() {
    return this.executor;
  }
}
