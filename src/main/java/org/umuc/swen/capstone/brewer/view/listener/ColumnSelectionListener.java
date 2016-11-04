package org.umuc.swen.capstone.brewer.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.umuc.swen.capstone.brewer.view.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 11/4/16.
 */
public class ColumnSelectionListener implements ActionListener {

  private final ColorBrewerPaletteChooser colorBrewerPaletteChooser;
  private final DisableApplyColorSchemeListener listener;

  public ColumnSelectionListener(ColorBrewerPaletteChooser colorBrewerPaletteChooser, DisableApplyColorSchemeListener listener) {
    this.colorBrewerPaletteChooser = colorBrewerPaletteChooser;
    this.listener = listener;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    colorBrewerPaletteChooser.setColumnSelection();
    listener.columnWasSelected();
  }
}
