package com.aivruu.homes.service.type;

import com.aivruu.homes.config.ValueObjectConfigManager;
import com.aivruu.homes.config.model.BaseConfigModel;
import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.config.model.MessageConfigModel;
import com.aivruu.homes.result.ValueObjectConfigResult;
import com.aivruu.homes.service.ConfigServiceModelImpl;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.List;

public class ConfigServiceType implements ConfigServiceModelImpl {
  private final Path pluginFolder;
  private ValueObjectConfigManager configManager;
  private ConfigModel configModel;
  private MessageConfigModel messageConfigModel;
  private boolean isOk;

  public ConfigServiceType(final @NotNull Path pluginFolder) {
    this.pluginFolder = pluginFolder;
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
    return "config-service";
  }

  @Override
  public boolean start() {
    this.configManager = ValueObjectConfigManager.INSTANCE;
    final ValueObjectConfigResult<ConfigModel> configResult = this.configManager.loadConfig(this.pluginFolder);
    if (configResult.load()) {
      this.configModel = configResult.result();
    }
    final ValueObjectConfigResult<MessageConfigModel> messageResult = this.configManager.loadMessages(this.pluginFolder);
    if (messageResult.load()) {
      this.messageConfigModel = messageResult.result();
    }
    return (this.configModel != null) && (this.messageConfigModel != null);
  }

  @Override
  public @NotNull List<BaseConfigModel> getConfigurationModels() {
    return List.of(this.configModel, this.messageConfigModel);
  }
}
