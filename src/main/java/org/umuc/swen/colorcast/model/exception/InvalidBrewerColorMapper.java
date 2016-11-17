package org.umuc.swen.colorcast.model.exception;

import java.util.Arrays;
import java.util.stream.Collectors;
import org.umuc.swen.colorcast.model.mapping.MapType;

/**
 * Created by cwancowicz on 10/21/16.
 */
public class InvalidBrewerColorMapper extends BrewerColorAppException {

  public InvalidBrewerColorMapper(MapType mayType, InvalidElement... elements) {
    super(String.format("Cannot create %s due to %s", mayType.getMapName(), mapElements(elements)));
  }

  private static String mapElements(InvalidElement[] elements) {
    return Arrays.asList(elements).stream().map(element -> element.getElement())
            .collect(Collectors.joining(" and "));
  }
}
