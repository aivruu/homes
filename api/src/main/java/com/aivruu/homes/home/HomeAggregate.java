package com.aivruu.homes.home;

import com.aivruu.homes.event.HomeCreationEvent;
import com.aivruu.homes.event.HomeDeleteEvent;
import com.aivruu.homes.player.EntityCachedPlayerModel;
import com.aivruu.homes.repository.PlayerModelRepository;
import com.aivruu.homes.result.ValueObjectHomeResult;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Aggregate for homes creation and deletion.
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
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the {@link ValueObjectHomePosition} and status code {@link ValueObjectHomeResult#ADDED_RESULT}
   * for this home if was created correctly.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the status code -3 if {@link HomeCreationEvent} instance is cancelled.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with the status code -2 if player has reached the allowed homes amount limit.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not founded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable ValueObjectHomePosition> performHomeCreate(final @NotNull Player player, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(player.getUniqueId());
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    Map<String, ValueObjectHomePosition> playerHomes = playerModel.homes();
    if (playerHomes.size() > MAX_ALLOWED_HOMES_AMOUNT) {
      return ValueObjectHomeResult.withStatus(null, (byte) -2);
    }
    final ValueObjectHomePosition valueObjectHomePosition = new ValueObjectHomePosition(player.getWorld().getName(), player.getX(), player.getY(), player.getZ());
    final HomeCreationEvent homeCreationEvent = new HomeCreationEvent(player, homeId, valueObjectHomePosition);
    Bukkit.getPluginManager().callEvent(homeCreationEvent);
    if (homeCreationEvent.isCancelled()) {
      return ValueObjectHomeResult.withStatus(null, (byte) -3);
    }
    playerHomes.put(homeId, valueObjectHomePosition);
    this.repository.add(new EntityCachedPlayerModel(player.getUniqueId(), player.getName(), new HashMap<>(playerHomes)));
    playerHomes.clear();
    playerHomes = null;
    return ValueObjectHomeResult.withStatus(valueObjectHomePosition, ValueObjectHomeResult.ADDED_RESULT);
  }

  /**
   * Removes the home specified from the player data.
   *
   * @param id needed for identify to the {@link Player}.
   * @param homeId needed for identify the home to delete.
   * @return A status code of {@link ValueObjectHomeResult}, expected status codes.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withRemoved()} if deletion was completed correctly.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with status code -4 if home identifier is not valid.<p>
   * • {@link ValueObjectHomeResult#withError()} if player information was not founded.
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable ValueObjectHomePosition> performHomeDeletion(final @NotNull UUID id, final @NotNull String homeId) {
    final EntityCachedPlayerModel playerModel = this.repository.remove(id);
    if (playerModel == null) {
      return ValueObjectHomeResult.withError();
    }
    Map<String, ValueObjectHomePosition> playerHomes = playerModel.homes();
    if (playerHomes.remove(homeId) == null) {
      return ValueObjectHomeResult.withStatus(null, (byte) -4);
    }
    final HomeDeleteEvent homeDeleteEvent = new HomeDeleteEvent(playerModel.findByUid(), homeId);
    Bukkit.getPluginManager().callEvent(homeDeleteEvent);
    this.repository.add(new EntityCachedPlayerModel(id, playerModel.name(), new HashMap<>(playerHomes)));
    playerHomes.clear();
    playerHomes = null;
    return ValueObjectHomeResult.withRemoved();
  }
}
