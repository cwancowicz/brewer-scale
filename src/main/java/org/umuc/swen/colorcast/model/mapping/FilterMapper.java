package org.umuc.swen.colorcast.model.mapping;

import java.util.Collection;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;

/**
 * Created by cwancowicz on 9/24/16.
 */
public interface FilterMapper {
  void applyFilterMapping(Collection<CyNetworkView> networkViews, CyNode node, CyRow row);
  void updateNetworkViews(Collection<CyNetworkView> networkViews);

  MapType getMapType();
}
