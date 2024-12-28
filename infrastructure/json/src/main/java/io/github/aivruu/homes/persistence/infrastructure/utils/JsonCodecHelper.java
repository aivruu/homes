// This file is part of homes, licensed under the GNU License.
//
// Copyright (c) 2024 aivruu
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
package io.github.aivruu.homes.persistence.infrastructure.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import io.github.aivruu.homes.aggregate.domain.AggregateRoot;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import io.github.aivruu.homes.home.infrastructure.json.JsonHomeModelEntityCodec;
import io.github.aivruu.homes.home.infrastructure.json.JsonHomePositionValueObjectCodec;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.infrastructure.json.codec.JsonPlayerAggregateRootCodec;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonCodecHelper {
  private static final Gson GSON = new GsonBuilder()
    .registerTypeAdapter(PlayerAggregateRoot.class, JsonPlayerAggregateRootCodec.INSTANCE)
    .registerTypeAdapter(HomeModelEntity.class, JsonHomeModelEntityCodec.INSTANCE)
    .registerTypeAdapter(HomePositionValueObject.class, JsonHomePositionValueObjectCodec.INSTANCE)
    .setPrettyPrinting()
    .create();

  private JsonCodecHelper() {
    throw new UnsupportedOperationException("This class shouldn't be instantiated.");
  }

  public static <A extends AggregateRoot> @Nullable A read(final @NotNull Path file, final @NotNull Class<A> aggregateRootClass) {
    try (final Reader reader = Files.newBufferedReader(file)) {
      return GSON.fromJson(reader, aggregateRootClass);
    } catch (final IOException exception) {
      return null;
    }
  }

  public static <A extends AggregateRoot> boolean write(final @NotNull Path file, final @NotNull A aggregateRoot) {
    try (final Writer writer = Files.newBufferedWriter(file)) {
      GSON.toJson(aggregateRoot, writer);
      return true;
    } catch (final IOException | JsonIOException exception) {
      return false;
    }
  }
}
