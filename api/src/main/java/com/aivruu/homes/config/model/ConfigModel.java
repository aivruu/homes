package com.aivruu.homes.config.model;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class ConfigModel extends SerializableConfigModel {
  public String prefix = "<green>[Homes] <gray>>";

  public byte normalMaxHomesAmount = 5;

  public byte rankMaxHomes = 10;

  public boolean showErrorStack = false;

  public String dataFormat = "JSON";

  public String host = "localhost";

  public short port = 3306;

  public String database = "database";

  public String username = "username";

  public String password = "password";
}
