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
package io.github.aivruu.homes.home.infrastructure.json;

import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.persistence.infrastructure.ExecutorHelper;
import io.github.aivruu.homes.persistence.domain.InfrastructureAggregateRootRepository;
import io.github.aivruu.homes.persistence.infrastructure.utils.JsonCodecHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public final class HomeJsonInfrastructureAggregateRootRepository extends InfrastructureAggregateRootRepository<HomeAggregateRoot> {
  private final Path directory;

  public HomeJsonInfrastructureAggregateRootRepository(final @NotNull Path directory) {
    super(ExecutorHelper.executorPool());
    this.directory = directory;
  }

  @Override
  public boolean start() {
    if (Files.exists(this.directory)) {
      return true;
    }
    try {
      Files.createDirectory(this.directory);
      return true;
    } catch (final IOException exception) {
      return false;
    }
  }

  @Override
  public void close() {}

  @Override
  public @NotNull CompletableFuture<@Nullable HomeAggregateRoot> findAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() -> {
      final Path file = this.directory.resolve(id + ".json");
      return Files.notExists(file) ? null : JsonCodecHelper.read(file, HomeAggregateRoot.class);
    }, super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> existsAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() -> Files.exists(this.directory.resolve(id + ".json")), super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> saveAsync(final @NotNull HomeAggregateRoot aggregateRoot) {
    return CompletableFuture.supplyAsync(() -> {
      final Path file = this.directory.resolve(aggregateRoot.id() + ".json");
      if (Files.notExists(file)) {
        try {
          Files.createFile(file);
        } catch (final IOException exception) {
          exception.printStackTrace();
          return false;
        }
      }
      return JsonCodecHelper.write(file, aggregateRoot);
    }, super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> deleteAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() -> {
      final Path file = this.directory.resolve(id + ".json");
      if (Files.notExists(file)) {
        return false;
      }
      try {
        Files.delete(file);
        return true;
      } catch (final IOException exception) {
        return false;
      }
    }, super.executor);
  }
}
