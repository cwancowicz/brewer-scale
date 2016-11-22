package org.umuc.swen.colorcast.model.validation;

import java.util.Arrays;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidElement;
import org.umuc.swen.colorcast.model.mapping.MapType;

/**
 * Created by cwancowicz on 11/22/16.
 */
public class ColorCastMapValidator {

  public static void validateDiscreteMapper(ColorBrewer colorBrewer) {
        if (!Arrays.asList(ColorBrewer.getQualitativeColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.DISCRETE, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  public static void validateDivergingMapper(ColorBrewer colorBrewer) {
    if (!Arrays.asList(ColorBrewer.getDivergingColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.DIVERGING, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  public static void validateSequentialMapper(ColorBrewer colorBrewer) {
    if (!Arrays.asList(ColorBrewer.getSequentialColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.SEQUENTIAL, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }
}
