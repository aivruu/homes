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
package io.github.aivruu.homes.home.infrastructure.json.codec;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import io.github.aivruu.homes.home.domain.HomeAggregateRoot;
import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;

import java.lang.reflect.Type;

public enum JsonHomeAggregateRootCodec implements JsonSerializer<HomeAggregateRoot>, JsonDeserializer<HomeAggregateRoot> {
  INSTANCE;

  @Override
  public HomeAggregateRoot deserialize(final JsonElement json, final Type type, final JsonDeserializationContext ctx) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    final HomeModelEntity homeModel = new HomeModelEntity(
      jsonObject.get("id").getAsString(),
      jsonObject.get("owner-id").getAsString(),
      ctx.deserialize(jsonObject.get("position"), HomePositionValueObject.class)
    );
    return new HomeAggregateRoot(homeModel);
  }

  @Override
  public JsonElement serialize(final HomeAggregateRoot homeAggregateRoot, final Type type, final JsonSerializationContext ctx) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("id", homeAggregateRoot.id());
    jsonObject.addProperty("owner-id", homeAggregateRoot.ownerId());
    jsonObject.add("position", ctx.serialize(homeAggregateRoot.position(), HomePositionValueObject.class));
    return jsonObject;
  }
}
