package com.aivruu.homes.shared;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Model used for storage implementations.
 *
 * @since 0.0.1
 */
public interface DataModel {
  /**
   * Used to perform async operations
   * for plugin data handling.
   *
   * @since 0.0.1
   */
  Executor EXECUTOR = Executors.newCachedThreadPool();

  /**
   * Loads the storage for this implementation.
   *
   * @return A {@link CompletableFuture} with a boolean status.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull Boolean> performLoad();

  /**
   * Unloads the storage for this implementation.
   *
   * @return A {@link CompletableFuture} that provide a
   * boolean status for this operation.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull Boolean> performUnload();

  /**
   * Saves the player model data into the plugin storage.
   *
   * @param playerModel {@link EntityCachedPlayerModel} needed to perform writing.
   * @return A {@link CompletableFuture} that provide a
   * boolean status for this operation.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull Boolean> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel);

  /**
   * Reads the player model data and
   * create a new entity cached player model.
   *
   * @param name player name needed to find and read model file.
   * @return A {@link CompletableFuture} that provide a
   * {@link EntityCachedPlayerModel} or {@code null}
   * if there aren't data about the player.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@Nullable EntityCachedPlayerModel> performAsyncRead(final @NotNull String name);
}
