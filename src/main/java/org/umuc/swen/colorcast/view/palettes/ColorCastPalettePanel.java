package org.umuc.swen.colorcast.view.palettes;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.colorchooser.ColorSelectionModel;
import org.jcolorbrewer.ColorBrewer;
import org.jcolorbrewer.ui.ColorPanelSelectionModel;
import org.jcolorbrewer.ui.PaletteIcon;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.NoBrewerColorFoundException;
import org.umuc.swen.colorcast.model.mapping.MapType;

/**
 * Created by cwancowicz on 11/26/16.
 */
public abstract class ColorCastPalettePanel extends AbstractColorChooserPanel implements ActionListener {

  private static final Color DARK_BLUE = new Color(11, 55, 127);

  public abstract MapType getMapType();

  @Override
  public void updateChooser() {
  }

  @Override
  public Icon getSmallDisplayIcon() {
    return null;
  }

  @Override
  public Icon getLargeDisplayIcon() {
    return null;
  }

  @Override
  protected void buildChooser() {
    setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 0));

    ButtonGroup boxOfPalettes = new ButtonGroup();
    Border border = BorderFactory.createMatteBorder(2, 2, 2, 2, DARK_BLUE);
    getColorBrewerPalettes(getMapType(), false).stream()
            .forEach(palette -> {
              JToggleButton button = createPalette(palette, border);
              boxOfPalettes.add(button);
              add(button);
            });
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    ColorSelectionModel model = getColorSelectionModel();

    String command = ((JToggleButton) e.getSource()).getActionCommand();
    ((ColorPanelSelectionModel) model).setColorBrewer(
            getSelectedColorBrewer(getColorBrewerPalettes(getMapType(), false), command));
  }

  protected JToggleButton createPalette(ColorBrewer brewer, Border normalBorder) {
    JToggleButton palette = new JToggleButton();
    palette.setActionCommand(brewer.name());
    palette.addActionListener(this);
    Icon icon = new PaletteIcon(brewer, 5, 15, 15);
    palette.setIcon(icon);
    palette.setToolTipText(brewer.getPaletteDescription());
    palette.setBorder(normalBorder);
    // set the border painted when selected and not painted when not selected
    palette.setBorderPainted(false);
    palette.addChangeListener(changeEvent ->
            palette.setBorderPainted(palette.isSelected()));
    return palette;
  }

  private ColorBrewer getSelectedColorBrewer(List<ColorBrewer> colorBrewers, String name) {
    return colorBrewers.stream()
            .filter(palette -> palette.name().equals(name))
            .findFirst()
            .orElseThrow(() -> new NoBrewerColorFoundException());
  }

  private List<ColorBrewer> getColorBrewerPalettes(MapType mapType, boolean colorBlind) {
    switch (mapType) {
      case CONTINUOUS:
        return Arrays.asList(ColorBrewer.getSequentialColorPalettes(colorBlind));
      case DIVERGING:
        return Arrays.asList(ColorBrewer.getDivergingColorPalettes(colorBlind));
      case DISCRETE:
        return Arrays.asList(ColorBrewer.getQualitativeColorPalettes(colorBlind));
      default:
        throw new InvalidBrewerColorMapper(getMapType());
    }
  }
}