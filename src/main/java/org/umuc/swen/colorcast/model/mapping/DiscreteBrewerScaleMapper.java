package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.Stack;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.cytoscape.model.CyRow;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidElement;

/**
 * Created by cwancowicz on 10/14/16.
 */
public class DiscreteBrewerScaleMapper<T> extends AbstractBrewerScaleMapper {

  private Map<T, Color> valueColorMap = null;

  public DiscreteBrewerScaleMapper(final Set<T> values, ColorBrewer colorBrewer, String columnName) {
    super(colorBrewer, columnName);
    createValueColorMap(values, colorBrewer);
  }

  @Override
  protected Optional<Color> getColor(CyRow row) {
    return Optional.ofNullable(valueColorMap.get(row.get(this.columnName, Object.class)));
  }

  @Override
  protected void validateColorBrewer(ColorBrewer colorBrewer) {
    if (!Arrays.asList(ColorBrewer.getQualitativeColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(getMapType(), InvalidElement.EXPECTED_QUALITATIVE_PALETTE);
    }
  }

  @Override
  public MapType getMapType() {
    return MapType.DISCRETE;
  }

  private void createValueColorMap(Set<T> values, ColorBrewer colorBrewer) {
    Stack<Color> colors = new Stack();
    colors.addAll(Arrays.asList(colorBrewer.getColorPalette(values.size())));
    valueColorMap = values.stream().collect(Collectors.toMap(Function.identity(), value -> colors.pop()));
  }
}
