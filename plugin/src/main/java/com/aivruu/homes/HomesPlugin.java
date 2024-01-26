package com.aivruu.homes;

import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.service.ConfigServiceModelImpl;
import com.aivruu.homes.service.ServiceManager;
import com.aivruu.homes.service.ServiceModel;
import com.aivruu.homes.service.type.ConfigServiceType;
import com.aivruu.homes.service.type.DataServiceType;
import com.aivruu.homes.teleport.HomeTeleportManager;
import com.aivruu.homes.utils.ComponentUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class HomesPlugin extends JavaPlugin implements Homes {
  private ComponentLogger logger;
  private ServiceManager serviceManager;
  private PlayerModelRepository repository;
  private HomeAggregate homeAggregate;
  private HomeTeleportManager homeTeleportManager;

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public void onLoad() {
    Provider.load(this);
    this.logger = getComponentLogger();
    this.logger.info(ComponentUtils.parse("<green>Prepare plugin internal components, loading service models."));
    this.logger.info(ComponentUtils.parse("<yellow>Loading service manager."));
    final ConfigServiceModelImpl configService = new ConfigServiceType(getDataFolder().toPath());
    if (!configService.start()) {
      this.logger.error(ComponentUtils.parse("<red>The configuration service could not be started correctly."));
      this.setEnabled(false);
      return;
    }
    final ServiceModel<?>[] servicesArray = new ServiceModel[] {
      configService,
      new DataServiceType(this.logger, (ConfigModel) configService.getConfigurationModels().get(0))
    };
    this.serviceManager = new ServiceManager(getSLF4JLogger(), servicesArray);
  }

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public void onEnable() {
    this.serviceManager.start().thenAccept(couldStartAll -> {
      if (!couldStartAll) {
        this.logger.error(ComponentUtils.parse("<red>One or several services could not start correctly."));
        this.setEnabled(false);
        return;
      }
      this.logger.info(ComponentUtils.parse("<green>Plugin services enabled successful."));
      this.logger.info(ComponentUtils.parse("<yellow>Has been enabled correctly."));
      this.logger.info(ComponentUtils.parse("<green>Running on version: <version>", Placeholder.parsed("version", Constants.VERSION)));
    });
  }

  @Override
  public void onDisable() {
    Provider.unload();
    this.logger.info(ComponentUtils.parse("<green>Preparing plugin services to perform stop execution."));
    this.serviceManager.stop();
  }

  @Override
  public @NotNull PlayerModelRepository playerModelRepository() {
    return this.repository;
  }

  @Override
  public @NotNull HomeAggregate homeAggregate() {
    return this.homeAggregate;
  }

  @Override
  public @NotNull HomeTeleportManager homeTeleportManager() {
    return this.homeTeleportManager;
  }
}
