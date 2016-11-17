package org.umuc.swen.colorcast.view;

import javax.swing.JButton;
import javax.swing.JPanel;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.colorcast.view.listener.AboutColorCastListener;
import org.umuc.swen.colorcast.view.listener.ApplyBrewerScaleToNetworkListener;

/**
 * Created by cwancowicz on 9/22/16.
 */
public class BrewerScaleMainPanel extends JPanel {

  private static final String APPLY_BREWER_SCALE_BUTTON_TEXT = "Apply Color Cast to Network";
  private static final String ABOUT_COLOR_CAST = "About Color Cast";

  private JButton applyBrewerScaleButton;
  private JButton aboutColorCast;
  final private ColorBrewerMapperUtil colorBrewerMapperUtil;

  public BrewerScaleMainPanel(ColorBrewerMapperUtil colorBrewerMapperUtil) {
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
    createViewElements();
    addViewElements();
  }

  private void createViewElements() {
    applyBrewerScaleButton = new JButton(APPLY_BREWER_SCALE_BUTTON_TEXT);
    applyBrewerScaleButton.addActionListener(new ApplyBrewerScaleToNetworkListener(colorBrewerMapperUtil));

    aboutColorCast = new JButton(ABOUT_COLOR_CAST);
    aboutColorCast.addActionListener(new AboutColorCastListener(this));
  }

  private void addViewElements() {
    this.add(applyBrewerScaleButton);
    this.add(aboutColorCast);
    this.setVisible(true);
  }
}
