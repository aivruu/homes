package com.aivruu.homes.service.type;

import com.aivruu.homes.HomesPlugin;
import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.service.ServiceModel;
import com.aivruu.homes.shared.DataModel;
import com.aivruu.homes.shared.cloud.MongoDBModelData;
import com.aivruu.homes.shared.disk.JsonModelData;
import com.aivruu.homes.utils.ComponentUtils;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DataServiceType implements ServiceModel<DataModel> {
  private final ComponentLogger logger;
  private final ConfigModel config;
  private DataModel dataModel;
  private boolean isOk;

  public DataServiceType(final @NotNull ComponentLogger logger, final @NotNull ConfigModel config) {
    this.logger = logger;
    this.config = config;
  }

  @Override
  public boolean isOk() {
    return this.isOk;
  }

  @Override
  public void setOk(final boolean ok) {
    this.isOk = ok;
  }

  @Override
  public @NotNull String id() {
    return "data-service";
  }

  @Override
  public boolean start() {
    if (this.config.dataType.equals("JSON")) {
      this.dataModel = new JsonModelData(HomesPlugin.getPlugin(HomesPlugin.class).getDataFolder());
    } else if (this.config.dataType.equals("MONGODB")) {
      this.dataModel = new MongoDBModelData();
    } else {
      this.logger.info(ComponentUtils.parse("<red>Unknown storage type detected in configuration. Illegal type <data-type>", Placeholder.parsed("data-type", this.config.dataType)));
      return false;
    }
    final boolean couldStartCorrectly = this.dataModel.performLoad().join();
    if (!couldStartCorrectly) {
      this.logger.error(ComponentUtils.parse("<red>Storage could not be loaded correctly."));
      return false;
    }
    this.logger.info(ComponentUtils.parse("<green>Storage loaded correctly. Using type <data-type>", Placeholder.parsed("data-type", this.config.dataType)));
    return true;
  }

  @Override
  public void stop() {
    if (this.dataModel == null) {
      return;
    }
    this.logger.info(ComponentUtils.parse("<yellow>Performing repository data writing backup."));
    final PlayerModelRepository repository = PlayerModelRepository.get();
    repository.playerModelsCollection().forEach(this.dataModel::performAsyncWrite);
    repository.clean();
    this.dataModel.performUnload().thenAccept(couldStoppedCorrectly -> {
      if (!couldStoppedCorrectly) {
        this.logger.warn(ComponentUtils.parse("<red>The connection with the storage could not be closed correctly."));
        return;
      }
      this.logger.info(ComponentUtils.parse("<green>The storage has been closed correctly and the data has been saved."));
    });
  }

  @Override
  public @Nullable DataModel getGenericType() {
    return this.dataModel;
  }
}
