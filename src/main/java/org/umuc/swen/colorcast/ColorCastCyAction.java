package org.umuc.swen.colorcast;

import java.awt.Component;
import java.awt.event.ActionEvent;
import org.cytoscape.application.swing.AbstractCyAction;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.colorcast.view.dialog.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 11/19/16.
 */
public class ColorCastCyAction extends AbstractCyAction {

  private final ColorBrewerMapperUtil colorBrewerMapperUtil;
  private final Component rootComponent;

  public ColorCastCyAction(Component rootComponent, ColorBrewerMapperUtil colorBrewerMapperUtil) {
    super(ColorBrewerPaletteChooser.Resources.APP_TITLE);
    setPreferredMenu(ColorBrewerPaletteChooser.Resources.APP_MENU);
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
    this.rootComponent = rootComponent;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ColorBrewerPaletteChooser colorBrewerPaletteChooser = new ColorBrewerPaletteChooser(
            this.rootComponent, this.colorBrewerMapperUtil);
    colorBrewerPaletteChooser.setVisible(true);
    if (colorBrewerPaletteChooser.getSelectedPalette().isPresent() &&
            colorBrewerPaletteChooser.getSelectedColumn().isPresent() &&
            colorBrewerPaletteChooser.getSelectedMapType().isPresent()) {
      colorBrewerMapperUtil.applyFilterToNetworks(colorBrewerPaletteChooser.getSelectedColumn().get(),
              colorBrewerPaletteChooser.getSelectedPalette().get(),
              colorBrewerPaletteChooser.getSelectedMapType().get());
    }
  }
}
