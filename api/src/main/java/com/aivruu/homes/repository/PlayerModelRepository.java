package com.aivruu.homes.repository;

import com.aivruu.homes.player.EntityCachedPlayerModel;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository abstraction used for in-cache objects handling.
 *
 * @since 0.0.1
 */
public class PlayerModelRepository {
  private static PlayerModelRepository instance;
  private final Map<UUID, EntityCachedPlayerModel> players;

  private PlayerModelRepository() {
    this.players = new ConcurrentHashMap<>();
  }

  /**
   * Returns a reference of {@link PlayerModelRepository}.
   *
   * @return A reference of {@link PlayerModelRepository}.
   * @since 0.0.1
   */
  public static @NotNull PlayerModelRepository get() {
    if (instance == null) {
      instance = new PlayerModelRepository();
    }
    return instance;
  }

  /**
   * Add the {@link EntityCachedPlayerModel} reference given to cache.
   *
   * @param playerModel {@link EntityCachedPlayerModel} needed to store.
   * @since 0.0.1
   */
  public void add(final @NotNull EntityCachedPlayerModel playerModel) {
    this.players.put(playerModel.id(), playerModel);
  }

  /**
   * Removes the {@link EntityCachedPlayerModel} from cache.
   *
   * @param id player {@link UUID} needed to find the model.
   * @return A {@link EntityCachedPlayerModel} or nullable.
   * @since 0.0.1
   */
  public @Nullable EntityCachedPlayerModel remove(final @NotNull UUID id) {
    return this.players.remove(id);
  }

  /**
   * Returns a {@link EntityCachedPlayerModel} based on the {@link UUID} given.
   *
   * @param id needed to find the player data model.
   * @return A {@link EntityCachedPlayerModel} or {@code null} if the data not exists.
   * @since 0.0.1
   */
  public @Nullable EntityCachedPlayerModel findOne(final @NotNull UUID id) {
    return this.players.get(id);
  }
}
