package com.aivruu.homes.event;

import com.aivruu.homes.home.EntityHomeModel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * Used to detect when a home is created.
 *
 * @since 0.0.1
 */
public class HomeCreationEvent extends Event implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final String homeId;
  private final EntityHomeModel entityHomeModel;
  private boolean cancelled;

  public HomeCreationEvent(final @NotNull Player player, final @NotNull String homeId, final @NotNull EntityHomeModel entityHomeModel) {
    this.player = player;
    this.homeId = homeId;
    this.entityHomeModel = entityHomeModel;
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

  /**
   * Returns the {@link EntityHomeModel} for the home location.
   *
   * @return The {@link EntityHomeModel}.
   * @since 0.0.1
   */
  public @NotNull EntityHomeModel homePosition() {
    return this.entityHomeModel;
  }

  @Override
  public boolean isCancelled() {
    return this.cancelled;
  }

  @Override
  public void setCancelled(final boolean cancel) {
    this.cancelled = cancel;
  }

  @Override
  public @NotNull HandlerList getHandlers() {
    return HANDLER_LIST;
  }

  public static @NotNull HandlerList getHandlerList() {
    return HANDLER_LIST;
  }
}
