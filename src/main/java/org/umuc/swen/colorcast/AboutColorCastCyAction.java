package org.umuc.swen.colorcast;

import java.awt.Component;
import java.awt.event.ActionEvent;
import org.cytoscape.application.swing.AbstractCyAction;
import org.umuc.swen.colorcast.view.dialog.ColorCastAboutDialog;
import org.umuc.swen.colorcast.view.dialog.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 12/4/16.
 */
public class AboutColorCastCyAction extends AbstractCyAction {

  private final Component component;

  public AboutColorCastCyAction(Component component) {
    super(ColorBrewerPaletteChooser.Resources.ABOUT);
    setPreferredMenu(ColorBrewerPaletteChooser.Resources.APP_MENU);
    this.component = component;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    new ColorCastAboutDialog(this.component).showAboutDialog();
  }
}