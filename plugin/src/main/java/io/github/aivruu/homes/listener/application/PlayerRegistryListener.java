// This file is part of homes, licensed under the GNU License.
//
// Copyright (c) 2024 aivruu
//
// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program. If not, see <https://www.gnu.org/licenses/>.
package io.github.aivruu.homes.listener.application;

import io.github.aivruu.homes.player.application.PlayerManagerService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public final class PlayerRegistryListener implements Listener {
  private final PlayerManagerService playerManagerService;

  public PlayerRegistryListener(final @NotNull PlayerManagerService playerManagerService) {
    this.playerManagerService = playerManagerService;
  }

  @EventHandler
  public void onAsyncPreLogin(final @NotNull AsyncPlayerPreLoginEvent event) {
    this.playerManagerService.loadOne(event.getUniqueId().toString());
  }

  @EventHandler
  public void onQuit(final @NotNull PlayerQuitEvent event) {
    this.playerManagerService.unloadOne(event.getPlayer().getUniqueId().toString());
  }
}
