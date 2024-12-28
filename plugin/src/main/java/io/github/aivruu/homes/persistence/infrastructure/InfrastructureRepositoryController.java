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

import com.mongodb.client.MongoClient;
import io.github.aivruu.homes.config.application.object.ConfigurationConfigurationModel;
import io.github.aivruu.homes.persistence.domain.InfrastructureAggregateRootRepository;
import io.github.aivruu.homes.persistence.infrastructure.utils.MongoClientHelper;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.infrastructure.json.PlayerJsonInfrastructureAggregateRootRepository;
import io.github.aivruu.homes.player.infrastructure.mongodb.PlayerMongoInfrastructureAggregateRootRepository;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public final class InfrastructureRepositoryController {
  private final Path dataFolder;
  private final ConfigurationConfigurationModel configuration;
  private InfrastructureAggregateRootRepository<PlayerAggregateRoot> playerInfrastructureAggregateRootRepository;
  private InfrastructureRepositoryType playerInfrastructureRepositoryType;

  public InfrastructureRepositoryController(final @NotNull Path dataFolder, final @NotNull ConfigurationConfigurationModel configuration) {
    this.dataFolder = dataFolder;
    this.configuration = configuration;
  }

  private void validateInfrastructureType() {
    if (this.configuration.playerInfrastructureRepositoryType.equals("JSON")) {
      this.playerInfrastructureRepositoryType = InfrastructureRepositoryType.JSON;
    } else if (this.configuration.playerInfrastructureRepositoryType.equals("MONGODB")) {
      this.playerInfrastructureRepositoryType = InfrastructureRepositoryType.MONGODB;
    } else {
      this.playerInfrastructureRepositoryType = InfrastructureRepositoryType.JSON;
    }
  }

  public boolean selectAndInitialize() {
    this.validateInfrastructureType();
    MongoClient client = null;
    // MongoClient instance initialization if any repository require it.
    if (this.playerInfrastructureRepositoryType == InfrastructureRepositoryType.MONGODB) {
      MongoClientHelper.buildClient(this.configuration.mongoHost, this.configuration.mongoUsername, this.configuration.mongoDatabase, this.configuration.mongoPassword);
      client = MongoClientHelper.client();
      // Check if parameters are valid and client was initialized correctly.
      if (client == null) {
        return false;
      }
    }
    this.playerInfrastructureAggregateRootRepository = (this.playerInfrastructureRepositoryType == InfrastructureRepositoryType.JSON)
      ? new PlayerJsonInfrastructureAggregateRootRepository(this.dataFolder.resolve(this.configuration.playerCollectionAndDirectoryName))
      : new PlayerMongoInfrastructureAggregateRootRepository(client, this.configuration.mongoDatabase, this.configuration.playerCollectionAndDirectoryName);
    return this.playerInfrastructureAggregateRootRepository.start();
  }

  public void close() {
    if (this.playerInfrastructureRepositoryType == InfrastructureRepositoryType.MONGODB) {
      this.playerInfrastructureAggregateRootRepository.close();
    }
  }

  public @Nullable InfrastructureAggregateRootRepository<PlayerAggregateRoot> playerInfrastructureAggregateRootRepository() {
    return this.playerInfrastructureAggregateRootRepository;
  }
}
