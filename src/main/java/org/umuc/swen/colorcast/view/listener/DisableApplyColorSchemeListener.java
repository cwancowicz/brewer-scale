package org.umuc.swen.colorcast.view.listener;

import org.umuc.swen.colorcast.view.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 11/3/16.
 */
public class DisableApplyColorSchemeListener {

  private final ColorBrewerPaletteChooser colorBrewerPaletteChooser;

  public DisableApplyColorSchemeListener(ColorBrewerPaletteChooser colorBrewerPaletteChooser) {
    this.colorBrewerPaletteChooser = colorBrewerPaletteChooser;
  }

  public void radioButtonPressed() {
    validate();
  }

  public void colorWasSelected() {
    validate();
  }

  public void columnWasSelected() {
    validate();
  }

  private void validate() {
    if (colorBrewerPaletteChooser.getSelectedPalette().isPresent()
            && colorBrewerPaletteChooser.getSelectedColumn().isPresent()
            && colorBrewerPaletteChooser.getSelectedMapType().isPresent()) {
      colorBrewerPaletteChooser.enableApplyColorBrewerButton();
    }
    else {
      colorBrewerPaletteChooser.disableApplyColorBrewerButton();
    }
  }
}
