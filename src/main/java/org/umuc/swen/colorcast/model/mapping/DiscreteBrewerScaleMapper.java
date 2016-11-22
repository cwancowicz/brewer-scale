package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Set;
import java.util.Stack;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;

/**
 * Created by cwancowicz on 10/14/16.
 */
public class DiscreteBrewerScaleMapper extends VisualStyleFilterMapper {

  public DiscreteBrewerScaleMapper(final Set values, Class type, ColorBrewer colorBrewer, String columnName, CyActivator cyActivator) {
    super(cyActivator, columnName, type);
    createValueColorMap(values, colorBrewer);
  }

  @Override
  public MapType getMapType() {
    return MapType.DISCRETE;
  }

  private void createValueColorMap(Set values, ColorBrewer colorBrewer) {
    Stack<Color> colors = new Stack();
    colors.addAll(Arrays.asList(colorBrewer.getColorPalette(values.size())));
    values.stream().forEach(value -> ((DiscreteMapping)visualMappingFunction).putMapValue(value, colors.pop()));
  }

  @Override
  public DiscreteMapping createVisualMappingFunction() {
    return (DiscreteMapping) cyActivator.getVmfFactoryDiscrete().createVisualMappingFunction(columnName, type,
            BasicVisualLexicon.NODE_FILL_COLOR);
  }
}
