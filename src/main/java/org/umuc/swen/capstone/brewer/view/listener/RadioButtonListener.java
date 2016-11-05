package org.umuc.swen.capstone.brewer.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.jcolorbrewer.ui.DivergingColorPalettePanel;
import org.jcolorbrewer.ui.QualitativeColorPalettePanel;
import org.jcolorbrewer.ui.SequentialColorPalettePanel;
import org.umuc.swen.capstone.brewer.model.mapping.MapType;
import org.umuc.swen.capstone.brewer.view.ColorBrewerPaletteChooser;

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
      case CONTINUOUS:
        abstractColorChooserPanel = new SequentialColorPalettePanel();
        break;
      case DISCRETE:
        abstractColorChooserPanel = new QualitativeColorPalettePanel();
        break;
      case DIVERGING:
        abstractColorChooserPanel = new DivergingColorPalettePanel();
        break;
    }
    jDialog.setColorPanel(abstractColorChooserPanel, mapType);
    listener.radioButtonPressed();
  }
}
