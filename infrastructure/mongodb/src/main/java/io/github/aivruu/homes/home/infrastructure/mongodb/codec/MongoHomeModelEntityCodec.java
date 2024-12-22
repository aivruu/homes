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
package io.github.aivruu.homes.home.infrastructure.mongodb.codec;

import io.github.aivruu.homes.home.domain.HomeModelEntity;
import io.github.aivruu.homes.home.domain.position.HomePositionValueObject;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

public enum MongoHomeModelEntityCodec implements Codec<HomeModelEntity> {
  INSTANCE;

  @Override
  public HomeModelEntity decode(final BsonReader reader, final DecoderContext decoderContext) {
    final String id = reader.readString("id");
    final String ownerId = reader.readString("owner-id");
    final HomePositionValueObject position = decoderContext.decodeWithChildContext(MongoHomePositionValueObjectCodec.INSTANCE, reader);
    return new HomeModelEntity(id, ownerId, position);
  }

  @Override
  public void encode(final BsonWriter writer, final HomeModelEntity homeModel, final EncoderContext encoderContext) {
    writer.writeString("id", homeModel.id());
    writer.writeString("owner-id", homeModel.ownerId());
    encoderContext.encodeWithChildContext(MongoHomePositionValueObjectCodec.INSTANCE, writer, homeModel.position());
  }

  @Override
  public Class<HomeModelEntity> getEncoderClass() {
    return HomeModelEntity.class;
  }
}
