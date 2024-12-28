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
package io.github.aivruu.homes.home.infrastructure.mongodb;

import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

public enum MongoHomePositionValueObjectCodec implements Codec<HomePositionValueObject> {
  INSTANCE;

  @Override
  public @NotNull HomePositionValueObject decode(final BsonReader reader, final DecoderContext decoderContext) {
    final String worldName = reader.readString("world");
    return new HomePositionValueObject(
      worldName.equals("unknown") ? null : Bukkit.getWorld(worldName),
      reader.readInt32("x"),
      reader.readInt32("y"),
      reader.readInt32("z")
    );
  }

  @Override
  public void encode(final BsonWriter writer, final HomePositionValueObject position, final EncoderContext encoderContext) {
    final World world = position.world();
    if (world == null) {
      writer.writeString("world", "unknown");
    } else {
      writer.writeString("world", world.getName());
    }
    writer.writeInt32("x", position.x());
    writer.writeInt32("y", position.y());
    writer.writeInt32("z", position.z());
  }

  @Override
  public Class<HomePositionValueObject> getEncoderClass() {
    return HomePositionValueObject.class;
  }
}
