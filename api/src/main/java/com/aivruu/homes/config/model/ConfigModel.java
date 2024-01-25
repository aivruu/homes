package com.aivruu.homes.config.model;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ConfigModel extends BaseConfigModel {
  public String prefix = "<green>[Homes] <gray>>";

  public String dataType = "JSON";
}
