package org.umuc.swen.capstone.brewer;

import java.awt.Component;
import javax.swing.Icon;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.application.swing.CytoPanelName;
import org.umuc.swen.capstone.brewer.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.capstone.brewer.view.BrewerScaleMainPanel;

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
    return ControlPanelAction.APP_TITLE;
  }

  public Icon getIcon() {
    return null;
  }
}
