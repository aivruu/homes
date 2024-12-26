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
package io.github.aivruu.homes;

import io.github.aivruu.homes.aggregate.domain.registry.AggregateRootRegistry;
import io.github.aivruu.homes.aggregate.domain.repository.AggregateRootRepository;
import io.github.aivruu.homes.api.application.Homes;
import io.github.aivruu.homes.api.application.HomesProvider;
import io.github.aivruu.homes.command.application.AbstractRegistrableCommand;
import io.github.aivruu.homes.command.application.HomeCommand;
import io.github.aivruu.homes.command.application.MainCommand;
import io.github.aivruu.homes.config.application.ConfigurationContainer;
import io.github.aivruu.homes.config.application.object.ConfigurationConfigurationModel;
import io.github.aivruu.homes.config.application.object.MessagesConfigurationModel;
import io.github.aivruu.homes.home.application.HomeCreatorService;
import io.github.aivruu.homes.home.application.HomePositionUpdater;
import io.github.aivruu.homes.home.application.registry.HomeAggregateRootRegistry;
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.home.infrastructure.HomeCacheAggregateRootRepository;
import io.github.aivruu.homes.player.application.listener.PlayerRegistryListener;
import io.github.aivruu.homes.persistence.domain.InfrastructureAggregateRootRepository;
import io.github.aivruu.homes.persistence.infrastructure.ExecutorHelper;
import io.github.aivruu.homes.persistence.infrastructure.InfrastructureRepositoryController;
import io.github.aivruu.homes.player.application.PlayerHomeController;
import io.github.aivruu.homes.player.application.PlayerManagerService;
import io.github.aivruu.homes.player.application.registry.PlayerAggregateRootRegistry;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.infrastructure.PlayerCacheAggregateRootRepository;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public final class HomesPlugin extends JavaPlugin implements Homes {
  private final ComponentLogger logger = super.getComponentLogger();
  private @Nullable ConfigurationContainer<ConfigurationConfigurationModel> configurationModelContainer;
  private @Nullable ConfigurationContainer<MessagesConfigurationModel> messagesModelContainer;
  private AggregateRootRepository<HomeAggregateRoot> homeAggregateRootRepository;
  private AggregateRootRepository<PlayerAggregateRoot> playerAggregateRootRepository;
  private AggregateRootRegistry<HomeAggregateRoot> homeAggregateRootRegistry;
  private HomeCreatorService homeCreatorService;
  private HomePositionUpdater homePositionUpdater;
  private AggregateRootRegistry<PlayerAggregateRoot> playerAggregateRootRegistry;
  private PlayerHomeController playerHomeController;
  private PlayerManagerService playerManagerService;
  private InfrastructureRepositoryController infrastructureRepositoryController;

  @Override
  public @NotNull AggregateRootRepository<PlayerAggregateRoot> playerCacheRepository() {
    if (this.playerAggregateRootRepository == null) {
      throw new IllegalStateException("Player aggregate-root repository is not available yet.");
    }
    return this.playerAggregateRootRepository;
  }

  @Override
  public @NotNull AggregateRootRepository<HomeAggregateRoot> homeCacheRepository() {
    if (this.homeAggregateRootRepository == null) {
      throw new IllegalStateException("Home aggregate-root repository is not available yet.");
    }
    return this.homeAggregateRootRepository;
  }

  @Override
  public @NotNull AggregateRootRegistry<PlayerAggregateRoot> playerRegistry() {
    if (this.playerAggregateRootRegistry == null) {
      throw new IllegalStateException("Player aggregate-root registry is not available yet.");
    }
    return this.playerAggregateRootRegistry;
  }

  @Override
  public @NotNull AggregateRootRegistry<HomeAggregateRoot> homeRegistry() {
    if (this.homeAggregateRootRegistry == null) {
      throw new IllegalStateException("Home aggregate-root registry is not available yet.");
    }
    return this.homeAggregateRootRegistry;
  }

  @Override
  public @NotNull HomeCreatorService homeCreatorService() {
    if (this.homeCreatorService == null) {
      throw new IllegalStateException("Home creator-service is not available yet.");
    }
    return this.homeCreatorService;
  }

  @Override
  public @NotNull HomePositionUpdater homePositionUpdater() {
    if (this.homePositionUpdater == null) {
      throw new IllegalStateException("Home position-updater is not available yet.");
    }
    return this.homePositionUpdater;
  }

  @Override
  public @NotNull PlayerHomeController playerHomeController() {
    if (this.playerHomeController == null) {
      throw new IllegalStateException("Player home-controller is not available yet.");
    }
    return this.playerHomeController;
  }

  @Override
  public @NotNull PlayerManagerService playerManagerService() {
    if (this.playerManagerService == null) {
      throw new IllegalStateException("Player manager-service is not available yet.");
    }
    return this.playerManagerService;
  }

  @Override
  public void onLoad() {
    final Path dataFolder = super.getDataPath();
    this.logger.info("Preparing plugin's configuration files.");
    this.configurationModelContainer = ConfigurationContainer.of(dataFolder, "config", ConfigurationConfigurationModel.class);
    this.messagesModelContainer = ConfigurationContainer.of(dataFolder, "messages", MessagesConfigurationModel.class);
    if (this.configurationModelContainer == null || this.messagesModelContainer == null) {
      this.logger.error("Plugin's configuration couldn't be created/loaded correctly, the plugin won't start correctly.");
      return;
    }
    this.logger.info("Initializing player-cache repository.");
    this.playerAggregateRootRepository = new PlayerCacheAggregateRootRepository();
  }

  @Override
  public void onEnable() {
    if (this.playerAggregateRootRepository == null) {
      return;
    }
    // Thread-pool creation before infrastructure-repositories initialization
    ExecutorHelper.createPool(this.configurationModelContainer.model().threadsPoolSize);
    this.logger.info("Initializing infrastructure-repository controller and selecting repositories implementations.");
    this.infrastructureRepositoryController = new InfrastructureRepositoryController(super.getDataPath(), this.configurationModelContainer.model());
    if (!this.infrastructureRepositoryController.selectAndInitialize()) {
      this.logger.error("""
        Repository controller couldn't initialize the required infrastructure-repositories implementations.
        The plugin won't start correctly.""");
      return;
    }

    this.logger.info("Initializing infrastructure and application classes for homes management.");
    // Infrastructure-repositories shouldn't be null at this point.
    final InfrastructureAggregateRootRepository<HomeAggregateRoot> homeInfrastructureAggregateRootRepository = this.infrastructureRepositoryController.homeInfrastructureAggregateRootRepository();
    this.homeAggregateRootRepository = new HomeCacheAggregateRootRepository(this.homeAggregateRootRegistry);
    this.homeAggregateRootRegistry = new HomeAggregateRootRegistry(this.homeAggregateRootRepository, homeInfrastructureAggregateRootRepository);
    this.homeCreatorService = new HomeCreatorService(this.homeAggregateRootRegistry);
    this.homePositionUpdater = new HomePositionUpdater(this.homeAggregateRootRegistry);
    this.logger.info("Initializing infrastructure and application classes for players management.");
    final InfrastructureAggregateRootRepository<PlayerAggregateRoot> playerInfrastructureAggregateRootRepository = this.infrastructureRepositoryController.playerInfrastructureAggregateRootRepository();
    this.playerAggregateRootRegistry = new PlayerAggregateRootRegistry(this.playerAggregateRootRepository, playerInfrastructureAggregateRootRepository);
    this.playerManagerService = new PlayerManagerService(this.playerAggregateRootRegistry);
    this.playerHomeController = new PlayerHomeController(this.playerAggregateRootRegistry, this.playerManagerService);

    this.registerCommands(
      new MainCommand(this.configurationModelContainer, this.messagesModelContainer),
      new HomeCommand(
        this.configurationModelContainer,
        this.messagesModelContainer,
        this.playerManagerService,
        this.playerHomeController,
        this.homeCreatorService,
        this.homePositionUpdater
      )
    );
    super.getServer().getPluginManager().registerEvents(new PlayerRegistryListener(this.logger, this.playerManagerService), this);

    this.logger.info("Plugin's API has been initialized.");
    HomesProvider.set(this);
    this.logger.info("The plugin has been fully enabled!");
  }

  @SuppressWarnings("UnstableApiUsage")
  private void registerCommands(final @NotNull AbstractRegistrableCommand... registrableCommands) {
    super.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, eventHandler -> {
      final Commands commands = eventHandler.registrar();
      for (final AbstractRegistrableCommand command : registrableCommands) {
        commands.register(command.register());
      }
    });
  }

  @Override
  public void onDisable() {
    this.logger.info("Verifying cache aggregate-root repositories availability for data clean-up.");
    if (this.homeAggregateRootRepository != null) {
      for (final HomeAggregateRoot homeAggregateRoot : this.homeAggregateRootRepository.findAllSync()) {
        if (!this.homeAggregateRootRegistry.save(homeAggregateRoot)) {
          this.logger.error("Home aggregate-root with id {} couldn't be saved in the infrastructure.", homeAggregateRoot.id());
        }
      }
      this.homeAggregateRootRepository.clearSync();
    }
    if (this.playerAggregateRootRepository != null) {
      for (final PlayerAggregateRoot playerAggregateRoot : this.playerAggregateRootRepository.findAllSync()) {
        if (!this.playerAggregateRootRegistry.save(playerAggregateRoot)) {
          this.logger.error("Player aggregate-root with id {} couldn't be saved in the infrastructure.", playerAggregateRoot.id());
        }
      }
      this.playerAggregateRootRepository.clearSync();
    }
    if (this.infrastructureRepositoryController != null) {
      this.infrastructureRepositoryController.close();
    }
  }
}
