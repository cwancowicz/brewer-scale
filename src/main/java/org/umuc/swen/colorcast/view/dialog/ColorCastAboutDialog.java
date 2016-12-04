package org.umuc.swen.colorcast.view.dialog;

import java.awt.Component;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.swing.JOptionPane;

/**
 * Created by cwancowicz on 12/4/16.
 */
public class ColorCastAboutDialog extends JOptionPane {

  private final static String COLORCAST_VERSION = "project.version";
  private final static String COLORCAST_PROPERTIES = "/properties/colorcast.properties";
  private final static String DEFAULT_VERSION = "";
  private final Component component;
  private final Properties properties;

  public ColorCastAboutDialog(Component component) {
    this.component = component;
    String resourceName = COLORCAST_PROPERTIES;
    properties = new Properties();
    try (InputStream resourceStream = getClass().getResourceAsStream(resourceName)) {
      properties.load(resourceStream);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void showAboutDialog() {
    showMessageDialog(component, getMessageBody(), getTitle(), JOptionPane.NO_OPTION);
  }

  private String getTitle() {
    return ColorBrewerPaletteChooser.Resources.APP_TITLE;
  }

  private String getVersion() {
    return properties.getProperty(COLORCAST_VERSION, DEFAULT_VERSION);
  }

  private String getMessageBody() {
    return "<html><h1 style=\"text-align: center;\"><span style=\"color: #ff0000;\">C</span><span style=\"color: #ff6600;\">ol</span><span style=\"color: #ffcc00;\">or</span> <span style=\"color: #339966;\">C</span><span style=\"color: #0000ff;\">a</span><span style=\"color: #cc99ff;\">s</span><span style=\"color: #800080;\">t</span></h1><h3 style=\"text-align: center;\">A Cytoscape App</h3><h3 style=\"text-align: center;\">to Apply Brewer Color Palettes</h3><h3 style=\"text-align: center;\">Version " + getVersion() + "</h3><p>&nbsp;</p><p style=\"text-align: center;\"><strong>University Maryland University College</strong></p></div><div><p style=\"text-align: center;\">By Jeff Dayhoff, Janet Garcia, Neville Grant</p></div><div><p style=\"text-align: center;\">Sr., Fabian Naranjo, Brandon Nesmith, and</p></div><div><p style=\"text-align: center;\">Christopher Wancowicz</p></div>";
  }
}