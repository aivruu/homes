package com.aivruu.homes.config.model;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class MessageConfigModel extends BaseConfigModel {
  public String requirePermission = "<prefix> <red>You don't have permission for this!";
}
