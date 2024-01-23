package com.aivruu.homes;

import com.aivruu.homes.service.ServiceManager;
import com.aivruu.homes.service.type.ConfigModelService;
import com.aivruu.homes.utils.ComponentUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;

public final class HomesPlugin extends JavaPlugin {
  private ComponentLogger logger;
  private ServiceManager serviceManager;

  @Override
  public void onLoad() {
    this.logger = getComponentLogger();
    this.logger.info(ComponentUtils.parse("<green>Prepare plugin internal components, loading service models."));
    this.logger.info(ComponentUtils.parse("<yellow>Loading service manager."));
    this.serviceManager = new ServiceManager(getSLF4JLogger(), new ConfigModelService(getDataFolder().toPath()));
  }

  @Override
  public void onEnable() {
    this.serviceManager.start().thenAccept(couldStartAll -> {
      if (!couldStartAll) {
        this.logger.error(ComponentUtils.parse("<red>One or several services could not start correctly."));
        this.setEnabled(false);
        return;
      }
      this.logger.info(ComponentUtils.parse("<green>Plugin services enabled successful."));
      this.logger.info(ComponentUtils.parse("<green>[Homes] <yellow>has been enabled correctly."));
      this.logger.info(ComponentUtils.parse("<green>Running on version: <version>", Placeholder.parsed("version", Constants.VERSION)));
    });
  }

  @Override
  public void onDisable() {
    this.logger.info(ComponentUtils.parse("<green>Preparing plugin services to perform stop execution."));
    this.logger.info(ComponentUtils.parse("<yellow>Preparing data backup."));
    this.serviceManager.stop();
  }
}
