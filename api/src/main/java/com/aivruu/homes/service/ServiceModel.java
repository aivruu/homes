package com.aivruu.homes.service;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents an executable service model.
 *
 * @since 0.0.1
 */
public interface ServiceModel<T> {
  /**
   * Returns whether the service is ok to be used.
   *
   * @return Whether the service is ok to be used.
   * @since 0.0.1
   */
  boolean isOk();

  /**
   * Marks the service as OK to be used or not.
   *
   * @param ok a boolean status.
   * @since 0.0.1
   */
  void setOk(final boolean ok);

  /**
   * Returns the ID for this service.
   *
   * @return The service ID.
   * @since 0.0.1
   */
  @NotNull String id();

  /**
   * Starts the service execution.
   *
   * @return A boolean status expected for this function.<p>
   * • {@code true} if service was started successful.<p>
   * • {@code false} if service was not enabled or something went wrong.
   * @since 0.0.1
   */
  boolean start();

  /**
   * Stops the service execution.
   *
   * @since 0.0.1
   */
  default void stop() {}

  /**
   * Returns generic object for this service implementation.
   *
   * @return A generic object.
   * @since 0.0.1
   */
  default @Nullable T getGenericType() {
    return null;
  }
}
