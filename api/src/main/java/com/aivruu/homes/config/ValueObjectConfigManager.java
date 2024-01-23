package com.aivruu.homes.config;

import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.config.model.MessageConfigModel;
import com.aivruu.homes.result.ValueObjectConfigResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.BasicConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.gson.GsonConfigurationLoader;

import java.nio.file.Path;

/**
 * This class is used to perform configuration models load.
 *
 * @since 0.0.1
 */
public enum ValueObjectConfigManager {
  // We use lazy singleton for access to the reference.
  INSTANCE;

  /**
   * Loads and return a {@link ConfigModel}.
   *
   * @param pluginFolder plugin folder needed to know where create the file.
   * @return A status code of {@link ValueObjectConfigResult}, possible status codes.<p>
   * <p>
   * • {@link ValueObjectConfigResult#withUnload()} if model was not loaded correctly.<p>
   * • {@link ValueObjectConfigResult#withStatus(Object, byte)} if the model has been loaded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectConfigResult<@Nullable ConfigModel> loadConfig(final @NotNull Path pluginFolder) {
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
      .lenient(true)
      .defaultOptions(opts -> opts.shouldCopyDefaults(true))
      .path(pluginFolder.resolve("config.json"))
      .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final ConfigModel configModel = node.get(ConfigModel.class);
      node.set(ConfigModel.class, configModel);
      loader.save(node);
      assert configModel != null;
      return ValueObjectConfigResult.withStatus(configModel, ValueObjectConfigResult.LOAD_STATUS);
    } catch (final ConfigurateException exception) {
      exception.printStackTrace();
      return ValueObjectConfigResult.withUnload();
    }
  }

  /**
   * Loads and return a {@link MessageConfigModel}.
   *
   * @param pluginFolder plugin folder needed to know where create the file.
   * @return A status code of {@link ValueObjectConfigResult}, possible status codes.<p>
   * <p>
   * • {@link ValueObjectConfigResult#withUnload()} if model was not loaded correctly.<p>
   * • {@link ValueObjectConfigResult#withStatus(Object, byte)} if the model has been loaded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectConfigResult<@Nullable MessageConfigModel> loadMessages(final @NotNull Path pluginFolder) {
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
      .lenient(true)
      .defaultOptions(opts -> opts.shouldCopyDefaults(true))
      .path(pluginFolder.resolve("messages.json"))
      .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final MessageConfigModel messageConfigModel = node.get(MessageConfigModel.class);
      node.set(MessageConfigModel.class, messageConfigModel);
      loader.save(node);
      assert messageConfigModel != null;
      return ValueObjectConfigResult.withStatus(messageConfigModel, ValueObjectConfigResult.LOAD_STATUS);
    } catch (final ConfigurateException exception) {
      exception.printStackTrace();
      return ValueObjectConfigResult.withUnload();
    }
  }
}