package com.aivruu.homes.result;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the result of an operation performed on the data.
 * <p>
 * NOTE: If you want to return a custom status use values between -128 and -1.
 *
 * @param result the result of the operation.
 * @param status the status of the operation.
 * @param <T> the type of the value object.
 * @since 0.0.1
 */
public record ValueObjectDataResult<T>(@Nullable T result, byte status) {
  /**
   * The data was written and saved successful.
   *
   * @since 0.0.1
   */
  public static final byte WRITE_STATUS = 0;
  /**
   * The data reading was successful.
   *
   * @since 0.0.1
   */
  public static final byte READ_STATUS = 1;
  /**
   * Something went wrong during data manipulation.
   *
   * @since 0.0.1
   */
  public static final byte ERROR_STATUS = 2;

  /**
   * Creates a new {@link ValueObjectDataResult} that represents the 'write' status.
   *
   * @param playerModel needed to return as operation result.
   * @return A status code of {@link ValueObjectDataResult} with a result type {@link EntityCachedPlayerModel}
   * that represents the object that was used to perform data writing.
   * @since 0.0.1
   */
  public static @NotNull ValueObjectDataResult<@NotNull EntityCachedPlayerModel> withWrite(final @NotNull EntityCachedPlayerModel playerModel) {
    return new ValueObjectDataResult<>(playerModel, WRITE_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectDataResult} that represents the 'read' status.
   *
   * @param playerModel needed to return as operation result.
   * @return A status code of {@link ValueObjectDataResult} with a result type {@link EntityCachedPlayerModel}
   * that represents the object that was read since the data.
   * @since 0.0.1
   */
  public static @NotNull ValueObjectDataResult<@NotNull EntityCachedPlayerModel> withRead(final @NotNull EntityCachedPlayerModel playerModel) {
    return new ValueObjectDataResult<>(playerModel, READ_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectDataResult} that represents the 'error' status.
   * 
   * @return A {@link ValueObjectDataResult} with the {@link ValueObjectDataResult#ERROR_STATUS} status.
   * @param <T> A generic type to return for this operation.
   * @since 0.0.1
   */
  public static <T> @NotNull ValueObjectDataResult<@Nullable T> withError() {
    return new ValueObjectDataResult<>(null, ERROR_STATUS);
  }

  /**
   * Returns whether the status is {@link ValueObjectDataResult#WRITE_STATUS}.
   * 
   * @return Whether the status is {@link ValueObjectDataResult#WRITE_STATUS}.
   * @since 0.0.1
   */
  public boolean write() {
    return this.status == WRITE_STATUS;
  }

  /**
   * Returns whether the status is {@link ValueObjectDataResult#READ_STATUS}.
   *
   * @return Whether the status is {@link ValueObjectDataResult#READ_STATUS}.
   * @since 0.0.1
   */
  public boolean read() {
    return this.status == READ_STATUS;
  }

  /**
   * Returns whether the status is {@link ValueObjectDataResult#ERROR_STATUS}.
   *
   * @return Whether the status is {@link ValueObjectDataResult#ERROR_STATUS}.
   * @since 0.0.1
   */
  public boolean error() {
    return this.status == ERROR_STATUS;
  }

  /**
   * Returns whether the status is the same that the expected.
   *
   * @param status status code to compare.
   * @return Whether the status is the same that the expected.
   * @since 0.0.1
   */
  public boolean statusIs(final byte status) {
    return this.status == status;
  }
}
