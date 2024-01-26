package com.aivruu.homes.home;

import com.aivruu.homes.event.HomeCreationEvent;
import com.aivruu.homes.event.HomeDeleteEvent;
import com.aivruu.homes.event.HomesCleanEvent;
import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.result.ValueObjectHomeResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.UUID;

/**
 * Aggregate for homes creation, deletion and searching.
 *
 * @since 0.0.1
 */
public class HomeAggregate {
  public static final byte MAX_ALLOWED_HOMES_AMOUNT = 16;
  private final PlayerModelRepository repository;

  public HomeAggregate(final @NotNull PlayerModelRepository repository) {
    this.repository = repository;
  }

  /**
   * Add a new home to the player data.
   *
   * @param player needed for get new home data.
   * @param homeId identifier needed for new home data.
   * @return A status code of {@link ValueObjectHomeResult}, possible status code returned.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the {@link EntityHomeModel}
   * and status code {@link ValueObjectHomeResult#ADDED_STATUS} for this home if was created correctly.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the status code -4 if {@link HomeCreationEvent} instance is cancelled.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the status code -10 if already exists a home with the same id that was given.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not founded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable EntityHomeModel> performHomeCreate(final @NotNull Player player, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(player.getUniqueId());
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    final EntityHomeModel[] playerHomes = playerModel.homes();
    for (final EntityHomeModel iteratedHomeModel : playerHomes) {
      if (!iteratedHomeModel.id().equals(homeId)) {
        continue;
      }
      return ValueObjectHomeResult.withStatus(null, (byte) -10);
    }
    final EntityHomeModel entityHomeModel = new EntityHomeModel(homeId, player.getWorld().getName(), player.getX(), player.getY(), player.getZ());
    final HomeCreationEvent homeCreationEvent = new HomeCreationEvent(player, homeId, entityHomeModel);
    Bukkit.getPluginManager().callEvent(homeCreationEvent);
    if (homeCreationEvent.isCancelled()) {
      return ValueObjectHomeResult.withStatus(null, (byte) -4);
    }
    final EntityHomeModel[] newHomesArray = Arrays.copyOf(playerHomes, playerHomes.length + 1);
    newHomesArray[playerHomes.length] = entityHomeModel;
    this.repository.add(new EntityCachedPlayerModel(player.getUniqueId(), player.getName(), newHomesArray));
    return ValueObjectHomeResult.withAdded(entityHomeModel);
  }

  /**
   * Removes the home specified from the player data.
   *
   * @param id needed for identify to the {@link Player}.
   * @param homeId needed for identify the home to delete.
   * @return A status code of {@link ValueObjectHomeResult}, expected status codes.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withRemoved()} if deletion was completed correctly.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with status code -3 if home identifier is not valid.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not founded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable EntityHomeModel> performHomeDeletion(final @NotNull UUID id, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(id);
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    final EntityHomeModel[] playerHomes = playerModel.homes();
    boolean founded = false;
    for (int i = 0 ; i < playerHomes.length ; i++) {
      if (!playerHomes[i].id().equals(homeId)) {
        continue;
      }
      playerHomes[i] = null;
      founded = true;
      break;
    }
    if (!founded) {
      return ValueObjectHomeResult.withStatus(null, (byte) -3);
    }
    final HomeDeleteEvent homeDeleteEvent = new HomeDeleteEvent(playerModel.findByUid(), homeId);
    Bukkit.getPluginManager().callEvent(homeDeleteEvent);
    this.repository.add(new EntityCachedPlayerModel(id, playerModel.name(), Arrays.copyOf(playerHomes, playerHomes.length - 1)));
    return ValueObjectHomeResult.withRemoved();
  }

  /**
   * Removes all the existing homes of the player.
   *
   * @param id needed to identify to player data model.
   * @return A status code of {@link ValueObjectHomeResult} for this operation, expected status codes.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withRemoved()} if the homes were deleted correctly.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the status code -4 if a {@link HomesCleanEvent}
   * instance has been cancelled during the operation.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not found.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable EntityHomeModel> performAllHomesClean(final @NotNull UUID id) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(id);
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    final EntityHomeModel[] newEmptyHomesArray = new EntityHomeModel[]{};
    final HomesCleanEvent homesCleanEvent = new HomesCleanEvent(playerModel.findByUid(), newEmptyHomesArray);
    Bukkit.getPluginManager().callEvent(homesCleanEvent);
    if (homesCleanEvent.isCancelled()) {
      return ValueObjectHomeResult.withStatus(null, (byte) -4);
    }
    this.repository.add(new EntityCachedPlayerModel(id, playerModel.name(), newEmptyHomesArray));
    return ValueObjectHomeResult.withRemoved();
  }

  /**
   * Tries to find the home specified for the player.
   *
   * @param id needed to identify the player data model.
   * @param homeId needed to identify the home to get.
   * @return A status code of {@link ValueObjectHomeResult} for this operation, expected status code.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withFounded(EntityHomeModel)} if the home was founded.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with status code -6 if home not exists.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not found.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable EntityHomeModel> performHomeSearch(final @NotNull UUID id, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModel = this.repository.findOne(id);
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    final EntityHomeModel[] playerHomes = playerModel.homes();
    EntityHomeModel entityHomeModel = null;
    for (final EntityHomeModel iteratedHomeModel : playerHomes) {
      if (!iteratedHomeModel.id().equals(homeId)) {
        continue;
      }
      entityHomeModel = iteratedHomeModel;
      break;
    }
    return (entityHomeModel == null)
      ? ValueObjectHomeResult.withStatus(null, (byte) -6)
      : ValueObjectHomeResult.withFounded(entityHomeModel);
  }
}
