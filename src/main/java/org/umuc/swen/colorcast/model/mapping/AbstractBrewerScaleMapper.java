package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Collection;
import java.util.Optional;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.jcolorbrewer.ColorBrewer;

/**
 * Created by cwancowicz on 10/15/16.
 */
public abstract class AbstractBrewerScaleMapper implements FilterMapper {

  protected final String columnName;
  protected final ColorBrewer colorBrewer;

  public AbstractBrewerScaleMapper(ColorBrewer colorBrewer, String columnName) {
    this.columnName = columnName;
    this.colorBrewer = colorBrewer;
    validateColorBrewer(colorBrewer);
  }

  @Override
  public void applyFilterMapping(Collection<CyNetworkView> networkViews, CyNode node, CyRow row) {
    getColor(row).ifPresent(brewerColor -> {
      networkViews.stream()
              .forEach(networkView -> networkView.getNodeView(node).setLockedValue(
                      BasicVisualLexicon.NODE_FILL_COLOR, brewerColor));
    });
  }

  @Override
  public void updateNetworkViews(Collection<CyNetworkView> networkViews) {
    networkViews.stream().forEach(CyNetworkView::updateView);
  }

  protected abstract Optional<Color> getColor(CyRow row);

  protected abstract void validateColorBrewer(ColorBrewer colorBrewer);
}
