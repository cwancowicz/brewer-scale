package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.mappings.BoundaryRangeValues;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;

/**
 * Created by cwancowicz on 9/29/16.
 */
public class SequentialBrewerScaleMapper<T extends Number> extends VisualStyleFilterMapper {

  public SequentialBrewerScaleMapper(String columnName, ColorBrewer colorBrewer, Class<T> type, List<T> values,
                                     CyActivator cyActivator) {
    super(cyActivator, columnName, type);
    setBoundaryRangeValues(colorBrewer, values);
  }

  @Override
  public ContinuousMapping createVisualMappingFunction() {
    return (ContinuousMapping) this.cyActivator.getVmfFactoryContinuous().createVisualMappingFunction(columnName, type,
            BasicVisualLexicon.NODE_FILL_COLOR);
  }

  @Override
  public MapType getMapType() {
    return MapType.SEQUENTIAL;
  }

  private void setBoundaryRangeValues(ColorBrewer colorBrewer, List<T> values) {
    int maxColorSize = getMaxColorSize(colorBrewer);
    // need an extra color to have points for the most min and most max values.
    Color[] colors = colorBrewer.getColorPalette(maxColorSize + 1);

    double minValue = getMinValue(values);
    double maxValue = getMaxValue(values);
    double intervalSize = (maxValue - minValue) / maxColorSize;

    IntStream.rangeClosed(0, maxColorSize).forEach(itr ->
            ((ContinuousMapping) getVisualMappingFunction()).addPoint(minValue + (intervalSize * itr),
                    new BoundaryRangeValues(colors[itr], colors[itr], colors[itr]))
    );
  }

  private double getMaxValue(List<T> values) {
    return values.stream()
            .filter(Objects::nonNull)
            .map(value -> Double.valueOf(value.doubleValue()))
            .max((d1, d2) -> d1.compareTo(d2))
            .orElseThrow(() -> new InvalidDataException(columnName));
  }

  private double getMinValue(List<T> values) {
    return values.stream()
            .filter(Objects::nonNull)
            .map(value -> Double.valueOf(value.doubleValue()))
            .min((d1, d2) -> d1.compareTo(d2))
            .orElseThrow(() -> new InvalidDataException(columnName));
  }

  private int getMaxColorSize(ColorBrewer colorBrewer) {
    return colorBrewer.getMaximumColorCount() - 1;
  }
}
