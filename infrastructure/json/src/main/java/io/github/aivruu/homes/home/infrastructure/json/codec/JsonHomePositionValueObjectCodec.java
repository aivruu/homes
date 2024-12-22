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
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;

import java.lang.reflect.Type;

public enum JsonHomePositionValueObjectCodec implements JsonSerializer<HomePositionValueObject>, JsonDeserializer<HomePositionValueObject> {
  INSTANCE;

  @Override
  public HomePositionValueObject deserialize(final JsonElement json, final Type type, final JsonDeserializationContext ctx) throws JsonParseException {
    final JsonObject jsonObject = json.getAsJsonObject();
    return new HomePositionValueObject(
      jsonObject.get("x").getAsInt(),
      jsonObject.get("y").getAsInt(),
      jsonObject.get("z").getAsInt()
    );
  }

  @Override
  public JsonElement serialize(final HomePositionValueObject position, final Type type, final JsonSerializationContext ctx) {
    final JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("x", position.x());
    jsonObject.addProperty("y", position.y());
    jsonObject.addProperty("z", position.z());
    return jsonObject;
  }
}
