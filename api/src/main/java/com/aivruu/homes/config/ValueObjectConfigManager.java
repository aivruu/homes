package com.aivruu.homes.config;

import com.aivruu.homes.config.model.SerializableConfigModel;
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
public class ValueObjectConfigManager {
  /**
   * Loads and return a serializable config model reference.
   *
   * @param pluginFolder route for file creation.
   * @param file name for the configuration file.
   * @param serializableConfigClassType a class that extends of {@link  SerializableConfigModel}.
   * @return A class that extends of {@link SerializableConfigModel} or {@code null}.
   * @since 0.0.1
   */
  public static <T> @Nullable T loadConfig(final @NotNull Path pluginFolder, final @NotNull String file, final @NotNull Class<T> serializableConfigClassType) {
    final GsonConfigurationLoader loader = GsonConfigurationLoader.builder()
      .lenient(true)
      .defaultOptions(opts -> opts.shouldCopyDefaults(true))
      .path(pluginFolder.resolve(file))
      .build();
    try {
      final BasicConfigurationNode node = loader.load();
      final T serializedModel = node.get(serializableConfigClassType);

      node.set(serializableConfigClassType, serializedModel);
      loader.save(node);
      assert serializedModel != null;

      return serializedModel;
    } catch (final ConfigurateException exception) {
      exception.printStackTrace();
      return null;
    }
  }
}