package com.aivruu.homes.shared;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.result.ValueObjectDataResult;
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
   * Used to perform async operations for plugin data handling.
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
   * @return A {@link CompletableFuture} with a boolean status.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull Boolean> performUnload();

  /**
   * Writes the {@link EntityCachedPlayerModel} data into JSON file expected for this model.
   *
   * @param playerModel {@link EntityCachedPlayerModel} needed to perform writing.
   * @return A {@link CompletableFuture} that provide a {@link ValueObjectDataResult} with an
   * expected status code for this operation, possible status codes.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel);

  /**
   * Reads the {@link EntityCachedPlayerModel} data from JSON file expected for this model data.
   *
   * @param name player name needed to find and read model file.
   * @return A {@link CompletableFuture} that provide a {@link ValueObjectDataResult} with an
   * expected status code for this operation, possible status codes.
   * @since 0.0.1
   */
  @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncRead(final @NotNull String name);
}
