package com.aivruu.homes.player;

import com.aivruu.homes.home.EntityHomeModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Represents a cached-player information object.
 *
 * @param id {@link UUID} needed for this player.
 * @param name needed for this player.
 * @since 0.0.1
 */
public record EntityCachedPlayerModel(@NotNull UUID id, @NotNull String name, @NotNull EntityHomeModel[] homes) {
  /**
   * Returns the {@link UUID} for this entity/player.
   *
   * @return The {@link Player} uuid.
   * @since 0.0.1
   */
  public @NotNull UUID id() {
    return this.id;
  }

  /**
   * Returns the name for this entity/player.
   *
   * @return The {@link Player} name.
   * @since 0.0.1
   */
  public @NotNull String name() {
    return this.name;
  }

  /**
   * Returns all the homes for this entity/player.
   *
   * @return A {@link EntityHomeModel} array.
   * @since 0.0.1
   */
  public @NotNull EntityHomeModel[] homes() {
    return this.homes;
  }

  /**
   * Returns the {@link Player} using their {@link UUID}.
   *
   * @return A {@link Player}.
   * @since 0.0.1
   */
  public @NotNull Player findByUid() {
    final Player player = Bukkit.getPlayer(this.id);
    if (player == null) {
      throw new IllegalArgumentException("Player with uid: %s could not be founded.".formatted(this.id));
    }
    return player;
  }
}
