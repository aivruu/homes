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
package io.github.aivruu.homes.player.infrastructure.mongodb.codec;

import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.infrastructure.mongodb.MongoHomeModelEntityCodec;
import io.github.aivruu.homes.player.domain.PlayerAggregateRoot;
import io.github.aivruu.homes.player.domain.PlayerModelEntity;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

public enum MongoPlayerAggregateRootCodec implements Codec<PlayerAggregateRoot> {
  INSTANCE;

  @Override
  public PlayerAggregateRoot decode(final BsonReader reader, final DecoderContext decoderContext) {
    reader.readStartArray();
    final List<HomeModelEntity> homes = new ArrayList<>();
    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
      homes.add(decoderContext.decodeWithChildContext(MongoHomeModelEntityCodec.INSTANCE, reader));
    }
    reader.readEndArray();
    return new PlayerAggregateRoot(new PlayerModelEntity(reader.readString("id"), homes.toArray(HomeModelEntity[]::new)));
  }

  @Override
  public void encode(final BsonWriter writer, final PlayerAggregateRoot playerAggregateRoot, final EncoderContext encoderContext) {
    writer.writeString("id", playerAggregateRoot.id());
    writer.writeStartArray("homes");
    for (final HomeModelEntity homeModel : playerAggregateRoot.homes()) {
      if (homeModel == null) continue;
      encoderContext.encodeWithChildContext(MongoHomeModelEntityCodec.INSTANCE, writer, homeModel);
    }
    writer.writeEndArray();
  }

  @Override
  public Class<PlayerAggregateRoot> getEncoderClass() {
    return PlayerAggregateRoot.class;
  }
}
