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

import io.github.aivruu.homes.home.domain.HomeModelEntity;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.jetbrains.annotations.NotNull;

public enum MongoHomeModelEntityCodec implements Codec<HomeModelEntity> {
  INSTANCE;

  @Override
  public @NotNull HomeModelEntity decode(final BsonReader reader, final DecoderContext decoderContext) {
    return new HomeModelEntity(
      reader.readString("id"),
      decoderContext.decodeWithChildContext(MongoHomePositionValueObjectCodec.INSTANCE, reader)
    );
  }

  @Override
  public void encode(final BsonWriter writer, final HomeModelEntity homeModel, final EncoderContext encoderContext) {
    writer.writeString("id", homeModel.id());
    encoderContext.encodeWithChildContext(MongoHomePositionValueObjectCodec.INSTANCE, writer, homeModel.position());
  }

  @Override
  public Class<HomeModelEntity> getEncoderClass() {
    return HomeModelEntity.class;
  }
}
