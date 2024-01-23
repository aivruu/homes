package com.aivruu.homes.shared.disk;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.result.ValueObjectDataResult;
import com.aivruu.homes.shared.DataModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation for JSON data storage.
 *
 * @since 0.0.1
 */
public class JsonModelData implements DataModel {
  private final Path dataFolder;

  public JsonModelData(final @NotNull Path dataFolder) {
    this.dataFolder = dataFolder;
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performLoad() {
    return CompletableFuture.supplyAsync(() -> {
      try {
        Files.createDirectory(this.dataFolder);
      } catch (final IOException exception) {
        exception.printStackTrace();
        return false;
      }
      return Files.exists(this.dataFolder);
    });
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performUnload() {
    return CompletableFuture.completedFuture(true);
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel) {
    return CompletableFuture.completedFuture(ValueObjectDataResult.withWrite());
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncRead(final @NotNull String id) {
    return CompletableFuture.completedFuture(ValueObjectDataResult.withRead());
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable List<@NotNull EntityCachedPlayerModel>>> performAsyncCollectionRead() {
    return CompletableFuture.completedFuture(ValueObjectDataResult.withRead());
  }
}
