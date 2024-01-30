package com.aivruu.homes;

import com.aivruu.homes.command.CommandManager;
import com.aivruu.homes.config.ValueObjectConfigManager;
import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.config.model.HomesMenuConfigModel;
import com.aivruu.homes.config.model.MessageConfigModel;
import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.listener.PlayerDataListener;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.shared.DataModel;
import com.aivruu.homes.shared.cloud.MongoDBModelData;
import com.aivruu.homes.shared.disk.JsonModelData;
import com.aivruu.homes.teleport.HomeTeleportManager;
import com.aivruu.homes.utils.ComponentUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

public final class HomesPlugin extends JavaPlugin implements Homes {
  private ComponentLogger logger;
  private ConfigModel config;
  private MessageConfigModel message;
  private HomesMenuConfigModel homesMenu;
  private DataModel data;
  private PlayerModelRepository repository;
  private HomeAggregate homeAggregate;
  private HomeTeleportManager homeTeleportManager;

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public void onLoad() {
    Provider.load(this);
    this.logger = getComponentLogger();
    this.logger.info(ComponentUtils.parse("<green>Prepare plugin internal components."));
    this.logger.info(ComponentUtils.parse("<yellow>Loading configuration models."));
    final Path pluginFolder = this.getDataFolder().toPath();
    final ConfigModel configModel = ValueObjectConfigManager.loadConfig(pluginFolder, "config.json", ConfigModel.class);
    final MessageConfigModel messageModel= ValueObjectConfigManager.loadConfig(pluginFolder, "messages.json", MessageConfigModel.class);
    final HomesMenuConfigModel homesMenuModel = ValueObjectConfigManager.loadConfig(pluginFolder, "homes_menu.json", HomesMenuConfigModel.class);
    if ((configModel == null) || (messageModel == null) || (homesMenuModel == null)) {
      this.logger.error(ComponentUtils.parse("<red>One or several of the configuration models could not be loaded correctly."));
      this.setEnabled(false);
      return;
    }
    this.config = configModel;
    this.message = messageModel;
    this.homesMenu = homesMenuModel;
    this.repository = new PlayerModelRepository();
    this.homeAggregate = new HomeAggregate(this.repository);
    this.homeTeleportManager = new HomeTeleportManager(this.homeAggregate);
    if (this.config.dataFormat.equals("JSON")) {
      this.data = new JsonModelData(this.getDataFolder(), this.config);
      this.logger.info(ComponentUtils.parse("<green>JSON storage initialized."));
    } else if (this.config.dataFormat.equals("MONGODB")) {
      this.data = new MongoDBModelData();
      this.logger.info(ComponentUtils.parse("<green>MongoDB storage initialized."));
    } else {
      this.logger.error(ComponentUtils.parse("<red>Unknown storage type detected in configuration. Illegal type '<data-type>'", Placeholder.parsed("data-type", this.config.dataFormat)));
      this.setEnabled(false);
    }
  }

  @SuppressWarnings("UnstableApiUsage")
  @Override
  public void onEnable() {
    this.data.performLoad().thenAccept(wasLoaded -> {
      if (!wasLoaded) {
        this.logger.error(ComponentUtils.parse("<red>Storage could not be loaded correctly."));
        this.setEnabled(false);
        return;
      }
      this.logger.info(ComponentUtils.parse("<green>Storage loaded correctly. Using type <data-type>", Placeholder.parsed("data-type", this.config.dataFormat)));
    });
    final CommandManager commandManager = new CommandManager(this, this.config, this.message);
    commandManager.prepareRequirements();
    commandManager.prepareMessages();
    commandManager.load(this.homeAggregate, this.homeTeleportManager);
    this.getServer().getPluginManager().registerEvents(new PlayerDataListener(this.data, this.repository, this.logger), this);
  }

  @Override
  public void onDisable() {
    Provider.unload();
    if ((this.data == null) || (this.repository == null)) {
      return;
    }
    this.logger.info(ComponentUtils.parse("<green>Preparing plugin services to perform stop execution."));
    this.repository.clean();
    this.data.performUnload().thenAccept(wasUnloaded -> {
      if (!wasUnloaded) {
        this.logger.warn(ComponentUtils.parse("<red>The connection with the storage could not be closed correctly."));
        return;
      }
      this.logger.info(ComponentUtils.parse("<green>The storage has been closed correctly and the data has been saved."));
    });
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
