package com.aivruu.homes.service;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * This class represents a manager used to perform handling
 * about the internal required plugin services.
 *
 * @since 0.0.1
 */
public class ServiceManager {
  private final Logger logger;
  private List<ServiceModel<?>> services;

  public ServiceManager(final @NotNull Logger logger, final ServiceModel<?> @NotNull... services) {
    this.logger = logger;
    this.services = new ArrayList<>(Arrays.asList(services));
  }

  /**
   * Starts the running for the inactive services.
   *
   * @return A {@link CompletableFuture} with a boolean status, possible status devolved.<p>
   * <p>
   * • {@code true} if all services are active, index verification {@code index == services.size()}.<p>
   * • {@code false} if some services are not enabled.
   * @since 0.0.1
   */
  public @NotNull CompletableFuture<@NotNull Boolean> start() {
    return CompletableFuture.supplyAsync(() -> {
      byte index = 0;
      for (final ServiceModel<?> serviceModel : this.services) {
        final String serviceId = serviceModel.id();
        if (!serviceModel.start()) {
          this.logger.error("The service %s has could not start correctly.".formatted(serviceId));
          serviceModel.setOk(false);
          continue;
        }
        index++;
        serviceModel.setOk(true);
        this.logger.info("The service %s has been started.".formatted(serviceId));
      }
      return index == this.services.size() - 1;
    });
  }

  /**
   * Stops the execution for the running services.
   *
   * @since 0.0.1
   */
  public void stop() {
    for (final ServiceModel<?> serviceModel : this.services) {
      if (!serviceModel.isOk()) {
        continue;
      }
      serviceModel.setOk(false);
      this.logger.info("Unloaded service %s correctly.".formatted(serviceModel.id()));
    }
    this.services.clear();
  }

  /**
   * Reloads all the loaded services.
   * 
   * @param services {@link ServiceModel} array needed to perform {@link ServiceModel#start()} execution.
   * @return A status boolean returned by {@link ServiceManager#start()}.
   * @see ServiceManager#stop()
   * @see ServiceManager#start()
   * @since 0.0.1
   */
  public boolean reload(final ServiceModel<?> @NotNull... services) {
    Arrays.sort(services);
    this.services = new ArrayList<>(Arrays.asList(services));
    this.stop();
    return this.start().join();
  }
}
