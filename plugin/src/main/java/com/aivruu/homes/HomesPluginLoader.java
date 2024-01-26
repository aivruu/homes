package com.aivruu.homes;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;
import org.jetbrains.annotations.NotNull;

/**
 * Paper Plugin Loader implementation to perform dependencies download during server startup.
 *
 * @since 0.0.1
 */
@SuppressWarnings({"unused", "UnstableApiUsage"})
public class HomesPluginLoader implements PluginLoader {
  @Override
  public void classloader(final @NotNull PluginClasspathBuilder pluginClasspathBuilder) {
    final MavenLibraryResolver libraryResolver = new MavenLibraryResolver();
    libraryResolver.addRepository(new RemoteRepository.Builder("central", "default", "https://repo1.maven.org/maven2/").build());
    libraryResolver.addRepository(new RemoteRepository.Builder("triumphteam", "default", "https://repo.triumphteam.dev/snapshots/").build());
    libraryResolver.addDependency(new Dependency(new DefaultArtifact("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT"), null));
    libraryResolver.addDependency(new Dependency(new DefaultArtifact("org.spongepowered:configurate-gson:4.1.2"), null));
    libraryResolver.addDependency(new Dependency(new DefaultArtifact("org.mongodb:mongodb-driver-sync:4.11.0"), null));
    pluginClasspathBuilder.addLibrary(libraryResolver);
  }
}
