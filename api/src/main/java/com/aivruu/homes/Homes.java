package com.aivruu.homes;

import com.aivruu.homes.home.HomeAggregate;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.teleport.HomeTeleportManager;
import org.jetbrains.annotations.NotNull;

/**
 * Interface used to handle API components references.
 *
 * @since 0.0.1
 */
public interface Homes {
  /**
   * Returns the {@link PlayerModelRepository} reference.
   *
   * @return A {@link PlayerModelRepository} reference.
   * @since 0.0.1
   */
  @NotNull PlayerModelRepository playerModelRepository();

  /**
   * Returns the {@link HomeAggregate} reference.
   *
   * @return A {@link HomeAggregate} reference.
   * @since 0.0.1
   */
  @NotNull HomeAggregate homeAggregate();

  /**
   * Returns the {@link HomeTeleportManager} reference.
   *
   * @return A {@link HomeTeleportManager} reference.
   * @since 0.0.1
   */
  @NotNull HomeTeleportManager homeTeleportManager();

  /**
   * Used to provide API reference handling.
   *
   * @since 0.0.1
   */
  class Provider {
    private static Homes instance;

    /**
     * Returns the {@link Homes} reference.
     *
     * @return A {@link Homes} reference.
     * @since 0.0.1
     */
    public static @NotNull Homes get() {
      if (instance == null) {
        throw new IllegalStateException("Homes reference is not initialized and can't be returned.");
      }
      return instance;
    }

    /**
     * Establish the reference given for the API usage.
     *
     * @param instance needed to perform establishment.
     * @since 0.0.1
     */
    public static void load(final @NotNull Homes instance) {
      if (Provider.instance != null) {
        throw new IllegalStateException("Homes reference is already initialized.");
      }
      Provider.instance = instance;
    }

    /**
     * Removes the reference for the API.
     *
     * @since 0.0.1
     */
    public static void unload() {
      instance = null;
    }
  }
}
