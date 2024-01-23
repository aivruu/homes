package com.aivruu.homes.result;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ValueObjectDataResult<T>(@Nullable T result, byte status) {
  public static final byte WRITE_RESULT = 1;
  public static final byte DELETE_RESULT = 2;
  public static final byte READ_RESULT = 3;
  public static final byte ERROR_RESULT = 4;

  public static <T> @NotNull ValueObjectDataResult<@Nullable T> withWrite() {
    return new ValueObjectDataResult<>(null, WRITE_RESULT);
  }

  public static <T> @NotNull ValueObjectDataResult<@Nullable T> withDelete() {
    return new ValueObjectDataResult<>(null, DELETE_RESULT);
  }

  public static <T> @NotNull ValueObjectDataResult<@Nullable T> withRead() {
    return new ValueObjectDataResult<>(null, READ_RESULT);
  }

  public static <T> @NotNull ValueObjectDataResult<@Nullable T> withError() {
    return new ValueObjectDataResult<>(null, ERROR_RESULT);
  }

  public boolean write() {
    return this.status == WRITE_RESULT;
  }

  public boolean delete() {
    return this.status == DELETE_RESULT;
  }

  public boolean read() {
    return this.status == READ_RESULT;
  }

  public boolean error() {
    return this.status == ERROR_RESULT;
  }

  public boolean statusIs(final byte status) {
    return this.status == status;
  }
}
