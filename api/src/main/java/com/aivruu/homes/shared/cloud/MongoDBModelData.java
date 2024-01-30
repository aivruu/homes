package com.aivruu.homes.shared.cloud;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.shared.DataModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class MongoDBModelData implements DataModel {
  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performLoad() {
    return null;
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performUnload() {
    return null;
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel) {
    return null;
  }

  @Override
  public @NotNull CompletableFuture<@Nullable EntityCachedPlayerModel> performAsyncRead(final @NotNull String name) {
    return null;
  }
}
