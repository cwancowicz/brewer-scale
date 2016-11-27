package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.List;
import java.util.stream.IntStream;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;

/**
 * Created by cwancowicz on 10/17/16.
 */
public class DivergingBrewerScaleMapper<T extends Number> extends VisualStyleFilterMapper {

  public DivergingBrewerScaleMapper(String columnName, ColorBrewer colorBrewer, Class<T> type, List<T> values,
                                    CyActivator cyActivator) {
    super(cyActivator, columnName, type);
    initializeBoundaryRanges(colorBrewer, values);
  }

  @Override
  public MapType getMapType() {
    return MapType.DIVERGING;
  }

  @Override
  protected VisualMappingFunction createVisualMappingFunction() {
    return this.cyActivator.getVmfFactoryContinuous().createVisualMappingFunction(columnName,
            type, BasicVisualLexicon.NODE_FILL_COLOR);
  }

  private void initializeBoundaryRanges(ColorBrewer colorBrewer, List values) {
    double maxValue = getMaxValue(values);
    int maxColorSize = getMaxColorSize(colorBrewer);
    int halfMaxColorSize = maxColorSize / 2;
    Color[] colors = colorBrewer.getColorPalette(maxColorSize);

    double intervalSize = (maxValue / halfMaxColorSize);

    // Create a range that starts from negative Half Color Size to positive Half Color Size
    IntStream.rangeClosed(-1 * halfMaxColorSize, halfMaxColorSize).forEach(
            itr -> {
              Color color = colors[itr + halfMaxColorSize];
              ((ContinuousMapping) visualMappingFunction)
                      .addPoint(itr * intervalSize, new BoundaryRangeValues(color, color, color));
            }
    );
  }

  private Double getMaxValue(List<T> values) {
    return values.stream()
            .map(value -> Double.valueOf(Math.abs(value.doubleValue())))
            .max((v1, v2) -> v1.compareTo(v2))
            .orElseThrow(() -> new InvalidDataException(columnName));
  }

  private int getMaxColorSize(ColorBrewer colorBrewer) {
    // make max color size odd number of colors if it is even
    if (colorBrewer.getMaximumColorCount() % 2 == 0) {
      return colorBrewer.getMaximumColorCount() + 1;
    }
    return colorBrewer.getMaximumColorCount();
  }
}
