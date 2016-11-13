package org.umuc.swen.capstone.brewer.view.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.umuc.swen.capstone.brewer.view.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 11/13/16.
 */
public class AboutColorCastListener implements ActionListener {

  private final Component component;

  public AboutColorCastListener(Component component) {
    this.component = component;
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(component, getMessage(), getTitle(), JOptionPane.NO_OPTION);
  }

  private String getTitle() {
    return ColorBrewerPaletteChooser.Resources.APP_TITLE;
  }

  private String getMessage() {
    return "<html><h1 style=\"text-align: center;\"><span style=\"color: #ff0000;\">C</span><span style=\"color: #ff6600;\">ol</span><span style=\"color: #ffcc00;\">or</span> <span style=\"color: #339966;\">C</span><span style=\"color: #0000ff;\">a</span><span style=\"color: #cc99ff;\">s</span><span style=\"color: #800080;\">t</span></h1><h3 style=\"text-align: center;\">A Cytoscape App</h3><h3 style=\"text-align: center;\">to Apply Brewer Color Palettes</h3><h3 style=\"text-align: center;\">Version 1.0</h3><p style=\"text-align: center;\">By Jeff Dayhoff, Janet Garcia, Neville Grant</p><p style=\"text-align: center;\">Sr., Fabian Naranjo, Brandon Nesmith, and</p><p style=\"text-align: center;\">Christopher Wancowicz</p></html>";
  }
}
