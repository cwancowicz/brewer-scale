package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.List;
import java.util.Objects;
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

  private static final int colorSize = 100;

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
    // get the min and max colors from the continuous palette
    Color[] colors = colorBrewer.getColorPalette(colorSize);
    // Define and Set the points.
    ((ContinuousMapping) this.visualMappingFunction).addPoint(getMinValue(values),
            new BoundaryRangeValues(colors[0], colors[0], colors[1]));
    ((ContinuousMapping) this.visualMappingFunction).addPoint(getMaxValue(values),
            new BoundaryRangeValues(colors[colorSize - 2], colors[colorSize - 1], colors[colorSize - 1]));
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
}
