package com.aivruu.homes.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Used to detect when a home is deleted.
 *
 * @since 0.0.1
 */
public class HomeDeleteEvent extends Event {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final String homeId;

  public HomeDeleteEvent(final @NotNull Player player, final @NotNull String homeId) {
    this.player = player;
    this.homeId = homeId;
  }


  /**
   * Returns the {@link Player} involved in this event.
   *
   * @return The involved {@link Player}.
   * @since 0.0.1
   */
  public @NotNull Player player() {
    return this.player;
  }

  /**
   * Returns the ID for the home created.
   *
   * @return The home ID.
   * @since 0.0.1
   */
  public @NotNull String homeId() {
    return this.homeId;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
