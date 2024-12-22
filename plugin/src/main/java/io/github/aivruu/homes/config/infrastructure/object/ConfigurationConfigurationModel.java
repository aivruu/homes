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
package io.github.aivruu.homes.config.infrastructure.object;

import io.github.aivruu.homes.config.application.ConfigurationInterface;
import io.github.aivruu.homes.persistence.infrastructure.InfrastructureRepositoryType;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public final class ConfigurationConfigurationModel implements ConfigurationInterface<ConfigurationConfigurationModel> {
  @Override
  public @NotNull ConfigurationConfigurationModel configuration() {
    return this;
  }

  @Comment("""
    Represents the amount of threads that the 'executor-pool' of the plugin will be able to use, this threads
    are used for asynchronous-operations for the plugin's infrastructure, such as load or save information into
    persistent-storage.""")
  public int threadsPoolSize = 2;

  @Comment("""
    It means whether the plugin should run on debug-mode, this is used for a more-detailed information about plugin's
    internal processes (such as information loading or saving), this only must be used in case plugin present a non-correct
    working.""")
  public boolean debugIt = false;

  public InfrastructureRepositoryType playerInfrastructureRepositoryType = InfrastructureRepositoryType.JSON;

  public InfrastructureRepositoryType homeInfrastructureRepositoryType = InfrastructureRepositoryType.JSON;

  public String homeCollectionAndDirectoryName = "homes";

  public String playerCollectionAndDirectoryName = "players";

  public String mongoHost = "localhost";

  public String mongoDatabase = "database";

  public String mongoUsername = "username";

  public String mongoPassword = "password";
}
