package org.umuc.swen.colorcast.view.palettes;

import org.umuc.swen.colorcast.model.mapping.MapType;

/**
 * Created by cwancowicz on 11/26/16.
 */
public class MyDiscreteColorPalettePanel extends ColorCastPalettePanel {
  @Override
  public String getDisplayName() {
    return getMapType().getMapName();
  }

  @Override
  public MapType getMapType() {
    return MapType.DISCRETE;
  }
}