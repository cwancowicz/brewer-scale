package org.umuc.swen.colorcast.model.mapping;

import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualStyle;
import org.umuc.swen.colorcast.CyActivator;

/**
 * Created by cwancowicz on 11/27/16.
 */
public class PreviousVisualStyleMapper extends VisualStyleFilterMapper {

  private final VisualStyle previousVisualStyle;

  public PreviousVisualStyleMapper(CyActivator cyActivator, VisualStyle previousVisualStyle) {
    super(cyActivator);
    this.previousVisualStyle = previousVisualStyle;
  }

  @Override
  public MapType getMapType() {
    return MapType.PREVIOUS;
  }

  @Override
  protected VisualMappingFunction createVisualMappingFunction() {
    return previousVisualStyle.getVisualMappingFunction(BasicVisualLexicon.NODE_FILL_COLOR);
  }
}