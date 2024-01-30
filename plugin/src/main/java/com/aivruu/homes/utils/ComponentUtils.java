package com.aivruu.homes.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

/**
 * Utility class to parse {@link String}
 * to {@link Component} objects by the Paper API.
 *
 * @since 0.0.1
 */
public class ComponentUtils {
  /**
   * Used to perform deserialization from string
   * to component using MiniMessage deserialization.
   *
   * @since 0.0.1
   */
  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  /**
   * Converts the object given to {@link Component} using {@link MiniMessage} format system.
   *
   * @param text needed to perform deserialization to {@link Component}.
   * @param resolvers {@link TagResolver} array used for
   * tags replacement in the current component.
   * @return A {@link Component}.
   * @since 0.0.1
   */
  public static @NotNull Component parse(final @NotNull String text, final TagResolver @NotNull... resolvers) {
    return MINI_MESSAGE.deserialize(text, resolvers);
  }
}
