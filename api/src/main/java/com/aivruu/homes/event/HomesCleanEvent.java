package com.aivruu.homes.event;

import com.aivruu.homes.home.EntityHomeModel;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HomesCleanEvent extends Event implements Cancellable {
  private static final HandlerList HANDLER_LIST = new HandlerList();
  private final Player player;
  private final EntityHomeModel[] homes;
  private boolean cancelled;

  public HomesCleanEvent(final @NotNull Player player, final @NotNull EntityHomeModel[] homes) {
    this.player = player;
    this.homes = homes;
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
   * Returns the homes of the player that were deleted.
   *
   * @return A {@link EntityHomeModel} array.
   * @since 0.0.1
   */
  public @NotNull EntityHomeModel[] homes() {
    return this.homes;
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
