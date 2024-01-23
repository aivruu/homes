package com.aivruu.homes.service;

import com.aivruu.homes.config.model.BaseConfigModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * A simple implementation of {@link ServiceModel}
 * that provide a {@link BaseConfigModel} type list return method.
 *
 * @since 0.0.1
 */
public interface ConfigServiceModelImpl extends ServiceModel<BaseConfigModel> {
  /**
   * Returns a list with {@link BaseConfigModel} references for this service implementation.
   *
   * @return A {@link List} with {@link BaseConfigModel} references used in this service model implementation.
   * @since 0.0.1
   */
  @NotNull List<? extends BaseConfigModel> getConfigurationModels();
}
