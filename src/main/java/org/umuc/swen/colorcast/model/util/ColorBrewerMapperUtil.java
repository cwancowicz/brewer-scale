package org.umuc.swen.colorcast.model.util;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.vizmap.VisualStyle;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidElement;
import org.umuc.swen.colorcast.model.mapping.BrewerScaleMapperFactory;
import org.umuc.swen.colorcast.model.mapping.FilterMapper;
import org.umuc.swen.colorcast.model.mapping.MapType;
import org.umuc.swen.colorcast.model.mapping.PreviousVisualStyleMapper;

/**
 * Created by cwancowicz on 9/24/16.
 */
public class ColorBrewerMapperUtil {

  private final CyActivator cyActivator;
  private VisualStyle currentVisualStyle;

  public ColorBrewerMapperUtil(CyActivator cyActivator) {
    this.cyActivator = cyActivator;
    createCurrentVisualStyle();
  }

  /**
   * Applies a {@link FilterMapper} to each network available
   * in the {@link CyNetworkManager}
   *
   * @param columnName  {@link String}
   * @param colorBrewer {@link ColorBrewer}
   * @param mapType     {@link MapType}
   */
  public void applyFilterToNetworks(String columnName, ColorBrewer colorBrewer, MapType mapType) {
    this.cyActivator.getNetworkManager().getNetworkSet()
            .stream()
            .forEach(cyNetwork -> applyFilterToNetwork(cyNetwork, columnName, colorBrewer, mapType));
  }

  /**
   * Applies a {@link PreviousVisualStyleMapper} to each network available in the {@link CyNetworkManager}
   */
  public void applyFilterToNetworks() {
    applyFilterToNetworks(null, null, MapType.PREVIOUS);
  }

  /**
   * Applies a {@link FilterMapper} to specified {@link CyNetwork} and its
   * {@link Collection} of {@link CyNetworkView}
   *
   * @param network     {@link CyNetwork}
   * @param columnName  {@link String}
   * @param colorBrewer {@link ColorBrewer}
   * @param mapType     {@link MapType}
   */
  public void applyFilterToNetwork(CyNetwork network, String columnName, ColorBrewer colorBrewer, MapType mapType) {
    Collection<CyNetworkView> networkViews = cyActivator.getNetworkViewManager().getNetworkViews(network);
    final FilterMapper mapper = createFilterMapper(network, columnName, colorBrewer, mapType);
    network.getDefaultNodeTable().getAllRows()
            .stream()
            .forEach(row -> mapper.applyFilterMapping(
                    networkViews, network.getNode(row.get(CyNetwork.SUID, Long.class)), row));
    mapper.updateNetworkViews(networkViews);
  }

  /**
   * Returns the {@link Set} of Column Names that are compatible for the specified {@link MapType}
   *
   * @param mapType {@link MapType}
   * @return {@link Set} of {@link String}
   */
  public Collection<String> getColumns(MapType mapType) {
    return cyActivator.getNetworkManager().getNetworkSet().stream()
            .map(cyNetwork -> cyNetwork.getDefaultNodeTable())
            .map(cyTable -> cyTable.getColumns())
            .filter(Objects::nonNull)
            .flatMap(Collection::stream)
            .filter(cyColumn -> shouldFilterColumn(mapType, cyColumn))
            .map(cyColumn -> cyColumn.getName())
            .filter(Objects::nonNull)
            .filter(name -> !name.isEmpty())
            .sorted((name1, name2) -> name1.compareTo(name2))
            .collect(Collectors.toList());
  }

  public void createCurrentVisualStyle() {
    this.currentVisualStyle = this.cyActivator.getVisualStyleFactory().createVisualStyle(
            this.cyActivator.getVisualMappingManager().getCurrentVisualStyle()
    );
  }

  private FilterMapper createFilterMapper(CyNetwork network, String columnName, ColorBrewer colorBrewer, MapType mapType) {
    switch (mapType) {
      case PREVIOUS:
        return new PreviousVisualStyleMapper(cyActivator, currentVisualStyle);
      default:
        return BrewerScaleMapperFactory.createFilterMapper(network, columnName, colorBrewer, mapType, cyActivator);
    }
  }

  private boolean shouldFilterColumn(MapType mapType, CyColumn cyColumn) {
    switch (mapType) {
      case DISCRETE:
        return isNumeric(cyColumn.getType()) || cyColumn.getType() == String.class;
      case SEQUENTIAL:
      case DIVERGING:
        return isNumeric(cyColumn.getType());
    }
    throw new InvalidBrewerColorMapper(mapType, InvalidElement.UNSUPPORTED_MAP_TYPE);
  }

  private boolean isNumeric(Class type) {
    return type == Integer.class ||
            type == Double.class ||
            type == Long.class ||
            type == Float.class;
  }
}
