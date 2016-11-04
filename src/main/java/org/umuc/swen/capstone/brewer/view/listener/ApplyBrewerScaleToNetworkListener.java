package org.umuc.swen.capstone.brewer.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.umuc.swen.capstone.brewer.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.capstone.brewer.view.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 9/24/16.
 */
public class ApplyBrewerScaleToNetworkListener implements ActionListener {

  private final ColorBrewerMapperUtil colorBrewerMapperUtil;

  public ApplyBrewerScaleToNetworkListener(ColorBrewerMapperUtil colorBrewerMapperUtil) {
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ColorBrewerPaletteChooser colorBrewerPaletteChooser = new ColorBrewerPaletteChooser(colorBrewerMapperUtil);
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
