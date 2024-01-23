package com.aivruu.homes.home;

import com.aivruu.homes.result.ValueObjectHomeResult;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public record ValueObjectHomePosition(@NotNull String worldName, double x, double y, double z) {
  public ValueObjectHomePosition {}

  /**
   * Returns the name of the {@link World} for this home.
   *
   * @return The {@link World} for this home.
   * @since 0.0.1
   */
  public @NotNull String worldName() {
    return this.worldName;
  }

  /**
   * Returns the X value in the location of the established home.
   *
   * @return The X coordinate.
   * @since 0.0.1
   */
  public double x() {
    return this.x;
  }

  /**
   * Returns the Y value in the location.
   *
   * @return The Y coordinate.
   * @since 0.0.1
   */
  public double y() {
    return this.y;
  }

  /**
   * Returns the Z value in the location.
   *
   * @return The Z coordinate.
   * @since 0.0.1
   */
  public double z() {
    return this.z;
  }

  /**
   * Returns a new {@link Location} based on the position values for this object.
   *
   * @return The home {@link Location}.
   * @since 0.0.1
   */
  public @NotNull Location toLocation() {
    final World world = Bukkit.getWorld(this.worldName);
    if (world == null) {
      throw new IllegalArgumentException("World %s cannot be resolved.".formatted(this.worldName));
    }
    return new Location(world, this.x, this.y, this.z);
  }

  /**
   * Performs an async operation for the player targeted teleport to this home location.
   *
   * @param player needed to perform the teleport.
   * @return A status code of {@link ValueObjectHomeResult}, expected status codes.<p>
   * <p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with status code -5 and this {@link ValueObjectHomePosition}
   * reference if teleport is successful.<p>
   * • {@link ValueObjectHomeResult#withStatus(Object, byte)} with status code -6 if teleport is failed.
   * @see ValueObjectHomePosition#toLocation()
   * @since 0.0.1
   */
  public @NotNull ValueObjectHomeResult<@Nullable ValueObjectHomePosition> performTeleportAsync(final @NotNull Player player) {
    final boolean successTeleport = player.teleportAsync(toLocation(), PlayerTeleportEvent.TeleportCause.PLUGIN).join();
    return successTeleport ? ValueObjectHomeResult.withStatus(this, (byte) -5) : ValueObjectHomeResult.withStatus(null, (byte) -6);
  }
}
