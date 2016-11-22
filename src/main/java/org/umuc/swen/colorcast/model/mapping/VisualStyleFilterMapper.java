package org.umuc.swen.colorcast.model.mapping;

import java.util.Collection;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualMappingFunction;
import org.cytoscape.view.vizmap.VisualStyle;
import org.umuc.swen.colorcast.CyActivator;

/**
 * Created by cwancowicz on 11/21/16.
 */
public abstract class VisualStyleFilterMapper implements FilterMapper {

  protected final CyActivator cyActivator;
  protected final String columnName;
  protected final Class type;
  protected final VisualMappingFunction visualMappingFunction;
  private final VisualStyle visualStyle;

  public VisualStyleFilterMapper(CyActivator cyActivator, String columnName, Class type) {
    this.cyActivator = cyActivator;
    this.columnName = columnName;
    this.type = type;
    this.visualStyle = this.cyActivator.getVisualMappingManager().getCurrentVisualStyle();
    this.visualMappingFunction = createVisualMappingFunction();
    this.visualStyle.addVisualMappingFunction(this.visualMappingFunction);
  }

  protected abstract VisualMappingFunction createVisualMappingFunction();

  @Override
  public void applyFilterMapping(Collection<CyNetworkView> networkViews, CyNode node, CyRow row) {
    networkViews.stream().forEach(networkView -> this.visualMappingFunction.apply(row, networkView.getNodeView(node)));
  }

  @Override
  public void updateNetworkViews(Collection<CyNetworkView> networkViews) {
    networkViews.stream().forEach(newtworkView -> {
      visualStyle.apply(newtworkView);
      newtworkView.updateView();
    });
  }
}