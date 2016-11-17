package org.umuc.swen.colorcast;

import java.awt.Component;
import javax.swing.Icon;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.colorcast.view.BrewerScaleMainPanel;
import org.umuc.swen.colorcast.view.ColorBrewerPaletteChooser;

public class BrewerPanelComponent implements CytoPanelComponent {

  private final BrewerScaleMainPanel brewerScaleMainPanel;

  public BrewerPanelComponent(ColorBrewerMapperUtil colorBrewerMapperUtil) {
    this.brewerScaleMainPanel = new BrewerScaleMainPanel(colorBrewerMapperUtil);
  }

  public Component getComponent() {
    return this.brewerScaleMainPanel;
  }

  public CytoPanelName getCytoPanelName() {
    return CytoPanelName.WEST;
  }

  public String getTitle() {
    return ColorBrewerPaletteChooser.Resources.APP_TITLE;
  }

  public Icon getIcon() {
    return null;
  }
}
