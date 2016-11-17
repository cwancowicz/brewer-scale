package org.umuc.swen.colorcast.view;

import org.jcolorbrewer.ColorBrewer;
import org.jcolorbrewer.ui.ColorPanelSelectionModel;
import org.umuc.swen.colorcast.view.listener.DisableApplyColorSchemeListener;
import org.umuc.swen.colorcast.view.listener.ColorChangeListener;

/**
 * Created by cwancowicz on 11/3/16.
 */
public class MyColorPanelSelectionModel extends ColorPanelSelectionModel {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private ColorBrewer brewer;
  private DisableApplyColorSchemeListener listener;
  private ColorChangeListener colorChangedListener;

  public MyColorPanelSelectionModel(DisableApplyColorSchemeListener listener) {
    this.listener = listener;
  }

  public void setSelectedColorChangedListener(ColorChangeListener colorChangedListener) {
    this.colorChangedListener = colorChangedListener;
  }

  public void setColorBrewer(ColorBrewer brewer) {
    this.brewer = brewer;
    super.setSelectedColor(null);
    this.colorChangedListener.colorChanged();
    listener.colorWasSelected();
  }

  public ColorBrewer getColorBrewer() {
    return brewer;
  }
}
