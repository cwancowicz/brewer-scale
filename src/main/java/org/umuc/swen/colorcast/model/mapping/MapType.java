package org.umuc.swen.colorcast.model.mapping;

/**
 * Created by cwancowicz on 10/16/16.
 */
public enum MapType {
  DISCRETE(2, "Discrete Mapper"), CONTINUOUS(3, "Continuous Mapper"), DIVERGING(1, "Diverging Mapper");

  private String mapName;
  private int paletteType;

  MapType(int type, String mapName) {
    this.mapName = mapName;
    this.paletteType = type;
  }

  public String getMapName() {
    return this.mapName;
  }

  public int getPaletteType() {
    return paletteType;
  }
}
