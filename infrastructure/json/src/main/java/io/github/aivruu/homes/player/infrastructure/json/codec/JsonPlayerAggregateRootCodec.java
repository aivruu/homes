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
package io.github.aivruu.homes.player.infrastructure.json.codec;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.domain.PlayerModelEntity;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public enum JsonPlayerAggregateRootCodec implements JsonSerializer<PlayerAggregateRoot>, JsonDeserializer<PlayerAggregateRoot> {
  INSTANCE;

  @Override
  public PlayerAggregateRoot deserialize(final JsonElement json, final Type type, final JsonDeserializationContext ctx) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    final List<HomeModelEntity> homesList = new ArrayList<>();
    for (final JsonElement homeElement : jsonObject.getAsJsonArray("homes")) {
      homesList.add(ctx.deserialize(homeElement, HomeModelEntity.class));
    }
    final PlayerModelEntity playerModel = new PlayerModelEntity(jsonObject.get("id").getAsString(), homesList.toArray(HomeModelEntity[]::new));
    return new PlayerAggregateRoot(playerModel);
  }

  @Override
  public JsonElement serialize(final PlayerAggregateRoot playerAggregateRoot, final Type type, final JsonSerializationContext ctx) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", playerAggregateRoot.id());
    final HomeModelEntity[] homes = playerAggregateRoot.homes();
    final JsonArray jsonArray = new JsonArray(homes.length);
    for (final HomeModelEntity homeModel : homes) {
      jsonArray.add(ctx.serialize(homeModel, HomeModelEntity.class));
    }
    jsonObject.add("homes", jsonArray);
    return jsonObject;
  }
}
