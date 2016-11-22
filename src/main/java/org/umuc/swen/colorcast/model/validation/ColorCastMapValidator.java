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

  public static void validateDiscreteMapper(ColorBrewer colorBrewer, Class type) {
    validateTypeForDiscreteMapping(type);
    if (!Arrays.asList(ColorBrewer.getQualitativeColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.DISCRETE, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  public static void validateDivergingMapper(ColorBrewer colorBrewer, Class type) {
    validateTypeForContinuousMapping(type);
    if (!Arrays.asList(ColorBrewer.getDivergingColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.DIVERGING, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  public static void validateSequentialMapper(ColorBrewer colorBrewer, Class type) {
    validateTypeForContinuousMapping(type);
    if (!Arrays.asList(ColorBrewer.getSequentialColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(MapType.SEQUENTIAL, InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  private static void validateTypeForContinuousMapping(Class type) {
    if (type.getSuperclass() != Number.class) {
      throw new IllegalArgumentException();
    }
  }

  private static void validateTypeForDiscreteMapping(Class type) {
    // noop
    return;
  }
}
