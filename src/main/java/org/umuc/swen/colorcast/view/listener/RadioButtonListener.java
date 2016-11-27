package org.umuc.swen.colorcast.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.umuc.swen.colorcast.model.mapping.MapType;
import org.umuc.swen.colorcast.view.ColorBrewerPaletteChooser;
import org.umuc.swen.colorcast.view.palettes.MyDiscreteColorPalettePanel;
import org.umuc.swen.colorcast.view.palettes.MyDivergingColorPalettePanel;
import org.umuc.swen.colorcast.view.palettes.MySequentialColorPalettePanel;

/**
 * Created by cwancowicz on 11/1/16.
 */
public class RadioButtonListener implements ActionListener {

  private final MapType mapType;
  private final ColorBrewerPaletteChooser jDialog;
  private final DisableApplyColorSchemeListener listener;

  public RadioButtonListener(ColorBrewerPaletteChooser jDialog, MapType mapType,
                             DisableApplyColorSchemeListener listener) {
    this.mapType = mapType;
    this.jDialog = jDialog;
    this.listener = listener;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    AbstractColorChooserPanel abstractColorChooserPanel = null;
    switch (this.mapType) {
      case SEQUENTIAL:
        abstractColorChooserPanel = new MySequentialColorPalettePanel();
        break;
      case DISCRETE:
        abstractColorChooserPanel = new MyDiscreteColorPalettePanel();
        break;
      case DIVERGING:
        abstractColorChooserPanel = new MyDivergingColorPalettePanel();
        break;
    }
    jDialog.setColorPanel(abstractColorChooserPanel, mapType);
    listener.radioButtonPressed();
  }
}
