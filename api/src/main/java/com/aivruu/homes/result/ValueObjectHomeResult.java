package com.aivruu.homes.result;

import com.aivruu.homes.home.EntityHomeModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents the result of an operation performed on a home.
 *
 * @param result the result of the operation.
 * @param status the status of the operation.
 * @param <T> the type of the value object.
 * @since 0.0.1
 */
public record ValueObjectHomeResult<T>(@Nullable T result, byte status) {
  /**
   * The addition was successful.
   *
   * @since 0.0.1
   */
  public static final byte ADDED_STATUS = 0;
  /**
   * The deletion was performed.
   *
   * @since 0.0.1
   */
  public static final byte REMOVED_STATUS = 1;
  /**
   * The model was founded correctly.
   *
   * @since 0.0.1
   */
  public static final byte FOUNDED_STATUS = 2;
  /**
   * The array was cleared correctly.
   *
   * @since 0.0.1
   */
  public static final byte CLEAN_STATUS = 3;
  /**
   * Something went wrong during the operation.
   *
   * @since 0.0.1
   */
  public static final byte ERROR_STATUS = 4;

  /**
   * Creates a new {@link ValueObjectHomeResult} with a {@link EntityHomeModel}.
   *
   * @return A {@link ValueObjectHomeResult} with the status {@link ValueObjectHomeResult#ADDED_STATUS}
   * and a {@link EntityHomeModel} that represents the home created and added.
   * @since 0.0.1
   */
  public static @NotNull ValueObjectHomeResult<@NotNull EntityHomeModel> withAdded(final @NotNull EntityHomeModel entityHomeModel) {
    return new ValueObjectHomeResult<>(entityHomeModel, ADDED_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectHomeResult} that represents a removed.
   *
   * @return A {@link ValueObjectHomeResult} with the status {@link ValueObjectHomeResult#REMOVED_STATUS}.
   * @param <T> object for this result.
   * @since 0.0.1
   */
  public static <T> @NotNull ValueObjectHomeResult<@Nullable T> withRemoved() {
    return new ValueObjectHomeResult<>(null, REMOVED_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectHomeResult} that represents the 'founded' status.
   *
   * @param entityHomeModel needed to return as a result for this operation.
   * @return A {@link EntityHomeModel} as result.
   * @since 0.0.1
   */
  public static @NotNull ValueObjectHomeResult<@NotNull EntityHomeModel> withFounded(final @NotNull EntityHomeModel entityHomeModel) {
    return new ValueObjectHomeResult<>(entityHomeModel, FOUNDED_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectHomeResult} that represents the 'clean' status.
   *
   * @param cleanedArrayLengthAsByte the cleaned array length as a {@code byte} type reference.
   * @return A {@code byte} value as result.
   * @since 0.0.1
   */
  public static @NotNull ValueObjectHomeResult<@Nullable Byte> withClean(final byte cleanedArrayLengthAsByte) {
    return new ValueObjectHomeResult<>(cleanedArrayLengthAsByte, CLEAN_STATUS);
  }

  /**
   * Creates a new {@link ValueObjectHomeResult} that represents an error.
   *
   * @return A {@link ValueObjectHomeResult} with the status {@link ValueObjectHomeResult#ERROR_STATUS}.
   * @param <T> object for this result.
   * @since 0.0.1
   */
  public static <T> @NotNull ValueObjectHomeResult<@Nullable T> withError() {
    return new ValueObjectHomeResult<>(null, ERROR_STATUS);
  }


  /**
   * Creates a new {@link ValueObjectHomeResult} that represents the status specified.
   *
   * @param result result object for this operation.
   * @param status code of status.
   * @return A {@link ValueObjectHomeResult} with the status expected.
   * @param <T> object for this result.
   * @since 0.0.1
   */
  public static <T> @NotNull ValueObjectHomeResult<@Nullable T> withStatus(final @Nullable T result, final byte status) {
    return new ValueObjectHomeResult<>(result, status);
  }

  /**
   * Returns whether the home has been added.
   *
   * @return whether the home has been added.
   * @since 0.0.1
   */
  public boolean added() {
    return this.status == ADDED_STATUS;
  }

  /**
   * Returns whether the home has been removed.
   *
   * @return whether the home has been removed.
   * @since 0.0.1
   */
  public boolean removed() {
    return this.status == REMOVED_STATUS;
  }

  /**
   * Returns whether the home model was founded.
   *
   * @return whether the home model was founded.
   * @since 0.0.1
   */
  public boolean founded() {
    return this.status == FOUNDED_STATUS;
  }

  /**
   * Returns whether something went wrong.
   *
   * @return whether something went wrong.
   * @since 0.0.1
   */
  public boolean error() {
    return this.status == ERROR_STATUS;
  }

  /**
   * Returns whether the status indicates.
   *
   * @param status code of status expected.
   * @return the status code expected.
   * @since 0.0.1
   */
  public boolean statusIs(final byte status) {
    return this.status == status;
  }
}
