package org.umuc.swen.colorcast.model.mapping;

/**
 * Created by cwancowicz on 10/16/16.
 */
public enum MapType {
  DISCRETE("Discrete Mapper"),
  SEQUENTIAL("Continuous Mapper"),
  DIVERGING("Diverging Mapper"),
  PREVIOUS("Previous Map");

  private String mapName;

  MapType(String mapName) {
    this.mapName = mapName;
  }

  public String getMapName() {
    return this.mapName;
  }
}
