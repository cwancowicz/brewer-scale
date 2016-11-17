package org.umuc.swen.colorcast.view.listener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;
import org.umuc.swen.colorcast.view.ColorBrewerPaletteChooser;

/**
 * Created by cwancowicz on 11/13/16.
 */
public class AboutColorCastListener implements ActionListener {

  private final static String COLORCAST_VERSION = "project.version";
  private final static String COLORCAST_PROPERTIES = "/properties/colorcast.properties";
  private final static String DEFAULT_VERSION = "";
  private final Component component;
  private final Properties properties;

  public AboutColorCastListener(Component component) {
    this.component = component;
    String resourceName = COLORCAST_PROPERTIES;
    properties = new Properties();
    try(InputStream resourceStream = getClass().getResourceAsStream(resourceName)) {
      properties.load(resourceStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    JOptionPane.showMessageDialog(component, getMessage(), getTitle(), JOptionPane.NO_OPTION);
  }

  private String getTitle() {
    return ColorBrewerPaletteChooser.Resources.APP_TITLE;
  }

  private String getVersion() {
    return properties.getProperty(COLORCAST_VERSION, DEFAULT_VERSION);
  }

  private String getMessage() {
    return "<html><h1 style=\"text-align: center;\"><span style=\"color: #ff0000;\">C</span><span style=\"color: #ff6600;\">ol</span><span style=\"color: #ffcc00;\">or</span> <span style=\"color: #339966;\">C</span><span style=\"color: #0000ff;\">a</span><span style=\"color: #cc99ff;\">s</span><span style=\"color: #800080;\">t</span></h1><h3 style=\"text-align: center;\">A Cytoscape App</h3><h3 style=\"text-align: center;\">to Apply Brewer Color Palettes</h3><h3 style=\"text-align: center;\">Version "+getVersion()+"</h3><p style=\"text-align: center;\">By Jeff Dayhoff, Janet Garcia, Neville Grant</p><p style=\"text-align: center;\">Sr., Fabian Naranjo, Brandon Nesmith, and</p><p style=\"text-align: center;\">Christopher Wancowicz</p></html>";
  }
}
