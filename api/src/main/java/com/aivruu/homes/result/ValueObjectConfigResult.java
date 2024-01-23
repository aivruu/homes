package com.aivruu.homes.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ValueObjectConfigResult<T>(@Nullable T result, byte status) {
  public static final byte LOAD_STATUS = 0;
  public static final byte UNLOAD_STATUS = 1;

  public static <T> @NotNull ValueObjectConfigResult<@Nullable T> withLoad() {
    return new ValueObjectConfigResult<>(null, LOAD_STATUS);
  }

  public static <T> @NotNull ValueObjectConfigResult<@Nullable T> withUnload() {
    return new ValueObjectConfigResult<>(null, UNLOAD_STATUS);
  }

  public static <T> @NotNull ValueObjectConfigResult<@Nullable T> withStatus(final @NotNull T result, final byte status) {
    return new ValueObjectConfigResult<>(result, status);
  }

  public boolean load() {
    return this.status == LOAD_STATUS;
  }

  public boolean unload() {
    return this.status == UNLOAD_STATUS;
  }

  public boolean statusIs(final byte status) {
    return this.status == status;
  }
}
