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
package io.github.aivruu.homes.home.infrastructure.mongodb;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.persistence.domain.InfrastructureAggregateRootRepository;
import io.github.aivruu.homes.persistence.infrastructure.ExecutorHelper;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public final class HomeMongoInfrastructureAggregateRootRepository extends InfrastructureAggregateRootRepository<HomeAggregateRoot> {
  private final MongoClient client;
  private final String databaseName;
  private final String collectionName;
  private MongoCollection<HomeAggregateRoot> homeAggregateRootMongoCollection;

  public HomeMongoInfrastructureAggregateRootRepository(
    final @NotNull MongoClient client,
    final @NotNull String databaseName,
    final @NotNull String collectionName) {
    super(ExecutorHelper.pool());
    this.client = client;
    this.databaseName = databaseName;
    this.collectionName = collectionName;
  }

  @Override
  public boolean start() {
    try {
      final MongoDatabase database = this.client.getDatabase(this.databaseName);
      this.homeAggregateRootMongoCollection = database.getCollection(this.collectionName, HomeAggregateRoot.class);
      return true;
    } catch (final IllegalArgumentException exception) {
      return false;
    }
  }

  @Override
  public void close() {
    this.client.close();
  }

  @Override
  public @NotNull CompletableFuture<@Nullable HomeAggregateRoot> findAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() ->
      this.homeAggregateRootMongoCollection.find(new Document("id", id)).first(), super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> existsAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() ->
      this.homeAggregateRootMongoCollection.find(new Document("id", id)).first() != null, super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> saveAsync(final @NotNull HomeAggregateRoot aggregateRoot) {
    return CompletableFuture.supplyAsync(() -> {
      final boolean alreadyExists = this.homeAggregateRootMongoCollection
        .find(new Document("id", aggregateRoot.id()))
        .first() != null;
      return !alreadyExists && this.homeAggregateRootMongoCollection.insertOne(aggregateRoot).wasAcknowledged();
    }, super.executor);
  }

  @Override
  public @NotNull CompletableFuture<Boolean> deleteAsync(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() ->
      this.homeAggregateRootMongoCollection.deleteOne(new Document("id", id)).wasAcknowledged(), super.executor);
  }
}
