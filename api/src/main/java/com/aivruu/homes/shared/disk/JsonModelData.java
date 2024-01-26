package com.aivruu.homes.shared.disk;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.result.ValueObjectDataResult;
import com.aivruu.homes.shared.DataModel;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonWriter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.html.parser.Entity;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Implementation for JSON data storage.
 *
 * @since 0.0.1
 */
public class JsonModelData implements DataModel {
  private static final Executor EXECUTOR = Executors.newCachedThreadPool();
  private final File dataFolder;
  private final Gson gson;

  public JsonModelData(final @NotNull File pluginFolder) {
    this.dataFolder = new File(pluginFolder, "data");
    this.gson = new Gson();
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
        exception.printStackTrace();
        return ValueObjectDataResult.withError();
      }
    }, EXECUTOR);
  }

  @Override
  public @NotNull CompletableFuture<@NotNull ValueObjectDataResult<@Nullable EntityCachedPlayerModel>> performAsyncRead(final @NotNull String id) {
    return CompletableFuture.supplyAsync(() -> {
      final File file = new File(this.dataFolder, id + ".json");
      if (!file.exists()) {
        return ValueObjectDataResult.withError();
      }
      try (final Reader reader = new FileReader(file)) {
        final EntityCachedPlayerModel playerModel = this.gson.fromJson(reader, EntityCachedPlayerModel.class);
        return ValueObjectDataResult.withRead(playerModel);
      } catch (final IOException exception) {
        exception.printStackTrace();
        return ValueObjectDataResult.withError();
      }
    }, EXECUTOR);
  }
}
