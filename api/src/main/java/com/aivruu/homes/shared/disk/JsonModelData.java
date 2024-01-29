package com.aivruu.homes.shared.disk;

import com.aivruu.homes.config.model.ConfigModel;
import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.result.ValueObjectDataResult;
import com.aivruu.homes.shared.DataModel;
import com.google.gson.Gson;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.concurrent.CompletableFuture;

/**
 * Implementation for JSON data storage.
 *
 * @since 0.0.1
 */
public class JsonModelData implements DataModel {
  private final File dataFolder;
  private final Gson gson;
  private final ConfigModel config;

  public JsonModelData(final @NotNull File pluginFolder, final @NotNull ConfigModel config) {
    this.dataFolder = new File(pluginFolder, "data");
    this.gson = new Gson();
    this.config = config;
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performLoad() {
    return CompletableFuture.completedFuture(!this.dataFolder.exists() && this.dataFolder.mkdir());
  }

  @Override
  public @NotNull CompletableFuture<@NotNull Boolean> performUnload() {
    return CompletableFuture.completedFuture(true);
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncWrite(final @NotNull EntityCachedPlayerModel playerModel) {
    return CompletableFuture.supplyAsync(() -> {
      final File file = new File(this.dataFolder, playerModel.name() + ".json");
      try (final Writer writer = new FileWriter(file)) {
        if (!file.exists() && !file.createNewFile()) {
          return ValueObjectDataResult.withError();
        }
        this.gson.toJson(playerModel, writer);
        return ValueObjectDataResult.withWrite(playerModel);
      } catch (final IOException exception) {
        if (this.config.showErrorStack) {
          exception.printStackTrace();
        }
        return ValueObjectDataResult.withError();
      }
    }, EXECUTOR);
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncRead(final @NotNull String playerName) {
    return CompletableFuture.supplyAsync(() -> {
      final File file = new File(this.dataFolder, playerName + ".json");
      if (!file.exists()) {
        return ValueObjectDataResult.withError();
      }
      try (final Reader reader = new FileReader(file)) {
        final EntityCachedPlayerModel playerModel = this.gson.fromJson(reader, EntityCachedPlayerModel.class);
        return ValueObjectDataResult.withRead(playerModel);
      } catch (final IOException exception) {
        if (this.config.showErrorStack) {
          exception.printStackTrace();
        }
        return ValueObjectDataResult.withError();
      }
    }, EXECUTOR);
  }
}
