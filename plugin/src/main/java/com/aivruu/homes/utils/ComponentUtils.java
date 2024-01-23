package com.aivruu.homes.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to parse {@link String} to {@link Component} usable by Paper API.
 *
 * @since 0.0.1
 */
public class ComponentUtils {
  /**
   * Used to perform deserialization from {@link String} to {@link Component} objects using their format system.
   *
   * @since 0.0.1
   */
  private static final MiniMessage MINI_MESSAGE = MiniMessage.miniMessage();

  /**
   * Converts the object given to {@link Component} using {@link MiniMessage} format system.
   *
   * @param text needed to perform deserialization to {@link Component}.
   * @param resolvers {@link TagResolver} array used for replacement for the current {@link Component}.
   * @return A {@link Component}.
   * @since 0.0.1
   */
  public static @NotNull Component parse(final @NotNull String text, final TagResolver @NotNull... resolvers) {
    return MINI_MESSAGE.deserialize(text, resolvers);
  }

  /**
   * Converts all the elements in the {@link List} to {@link Component}.
   *
   * @param text list needed to perform deserialization to {@link Component}.
   * @param resolvers {@link TagResolver} array used for replacement in a {@link Component}.
   * @return A {@link List} of {@link Component}.
   * @since 0.0.1
   */
  public static @NotNull List<@NotNull Component> parse(final @NotNull List<@NotNull String> text, final TagResolver @NotNull... resolvers) {
    final List<Component> parsedComponentsList = new ArrayList<>();
    for (final var iterated : text) {
      parsedComponentsList.add(MINI_MESSAGE.deserialize(iterated, resolvers));
    }
    return parsedComponentsList;
  }
}
