package com.aivruu.homes.shared.cloud;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.result.ValueObjectDataResult;
import com.aivruu.homes.shared.DataModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
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
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel) {
    return null;
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncRead(final @NotNull String name) {
    return null;
  }
}
