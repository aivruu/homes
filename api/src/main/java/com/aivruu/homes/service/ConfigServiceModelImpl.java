package com.aivruu.homes.service;

import org.jetbrains.annotations.NotNull;
import com.aivruu.homes.config.model.BaseConfigModel;

import java.util.List;

/**
 * A simple implementation of {@link ServiceModel}
 * that provide a generics types list return method.
 *
 * @since 0.0.1
 */
public interface ConfigServiceModelImpl extends ServiceModel<BaseConfigModel> {
  /**
   * Returns a list with generic types references for this service implementation.
   *
   * @return A {@link List} with generic types (most common, configuration models) references used in this service model implementation.
   * @since 0.0.1
   */
  @NotNull List<@NotNull BaseConfigModel> getConfigurationModels();
}
