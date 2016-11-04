package org.umuc.swen.capstone.brewer.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Optional;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.jcolorbrewer.ColorBrewer;
import org.jcolorbrewer.ui.SequentialColorPalettePanel;
import org.umuc.swen.capstone.brewer.model.mapping.MapType;
import org.umuc.swen.capstone.brewer.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.capstone.brewer.view.listener.ColumnSelectionListener;
import org.umuc.swen.capstone.brewer.view.listener.DisableApplyColorSchemeListener;
import org.umuc.swen.capstone.brewer.view.listener.ColorChangeListener;
import org.umuc.swen.capstone.brewer.view.listener.RadioButtonListener;

/**
 * Created by cwancowicz on 11/1/16.
 */
public class ColorBrewerPaletteChooser extends JDialog implements ColorChangeListener, ActionListener {

  private final static String APPLY_COLOR_PALLETE = "Apply Color Palette";
  private final static String CANCEL = "Cancel";
  private final static String RESET = "Reset";

  private final JColorChooser colorPanel;
  private final ColorBrewerMapperUtil colorBrewerMapperUtil;
  private Optional<ColorBrewer> colorBrewer = Optional.empty();
  private Optional<MapType> mapType = Optional.empty();
  private Optional<String> columnName = Optional.empty();
  private JComboBox<String> columns;
  private ButtonGroup mappersButtonGroup;
  private DisableApplyColorSchemeListener listener;
  private JButton applyColorBrewer;

  private JPanel mainPanel = new JPanel();
  private JPanel buttonPanel = new JPanel();

  public ColorBrewerPaletteChooser(ColorBrewerMapperUtil colorBrewerMapperUtil) {
    super(null, "ColorBrewer", ModalityType.DOCUMENT_MODAL);
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
    listener = new DisableApplyColorSchemeListener(this);

    colorPanel = new JColorChooser(new MyColorPanelSelectionModel(listener));
    applyColorBrewer = new JButton(APPLY_COLOR_PALLETE);
    applyColorBrewer.setActionCommand(APPLY_COLOR_PALLETE);

    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    initializeDialog();
  }

  public void setColorPanel(AbstractColorChooserPanel abstractColorChooserPanel, MapType mapType) {
    clearSelections();
    colorPanel.setChooserPanels(new AbstractColorChooserPanel[]{abstractColorChooserPanel});
    columns.setModel(new DefaultComboBoxModel(colorBrewerMapperUtil.getColumns(mapType).toArray()));
    setNewSelections();
    revalidate();
    repaint();
  }

  public Optional<ColorBrewer> getSelectedPalette() {
    return this.colorBrewer;
  }

  public Optional<MapType> getSelectedMapType() {
    return this.mapType;
  }

  public Optional<String> getSelectedColumn() {
    return this.columnName;
  }

  public void disableApplyColorBrewerButton() {
    this.applyColorBrewer.setEnabled(false);
  }

  public void enableApplyColorBrewerButton() {
    this.applyColorBrewer.setEnabled(true);
  }

  @Override
  public void colorChanged() {
    colorBrewer = Optional.ofNullable(((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).getColorBrewer());
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    switch (e.getActionCommand()) {
      case APPLY_COLOR_PALLETE:
        setNewSelections();
        dispose();
        break;
      case RESET:
        clearSelections();
        break;
      case CANCEL:
        clearSelections();
        dispose();
        break;
    }
  }

  public void setColumnSelection() {
    columnName = Optional.ofNullable((String)columns.getSelectedItem());
  }

  private void initializeDialog() {
    JPanel radioPanel = new JPanel();

    JRadioButton sequential = new JRadioButton("Sequential");
    JRadioButton diverging = new JRadioButton("Diverging");
    JRadioButton qualitative = new JRadioButton("Qualitative");

    sequential.addActionListener(new RadioButtonListener(this, MapType.CONTINUOUS, listener));
    diverging.addActionListener(new RadioButtonListener(this, MapType.DIVERGING, listener));
    qualitative.addActionListener(new RadioButtonListener(this, MapType.DISCRETE, listener));

    sequential.setActionCommand(MapType.CONTINUOUS.name());
    diverging.setActionCommand(MapType.DIVERGING.name());
    qualitative.setActionCommand(MapType.DISCRETE.name());

    mappersButtonGroup = new ButtonGroup();
    mappersButtonGroup.add(sequential);
    mappersButtonGroup.add(diverging);
    mappersButtonGroup.add(qualitative);

    radioPanel.add(sequential);
    radioPanel.add(qualitative);
    radioPanel.add(diverging);

    columns = new JComboBox();
    columns.addActionListener(new ColumnSelectionListener(this, listener));

    JButton cancel = new JButton(CANCEL);
    cancel.setActionCommand(CANCEL);
    cancel.addActionListener(this);
    JButton reset = new JButton(RESET);
    reset.setActionCommand(RESET);
    reset.addActionListener(this);
    buttonPanel.add(reset);
    buttonPanel.add(cancel);
    buttonPanel.add(applyColorBrewer);
    applyColorBrewer.addActionListener(this);
    colorPanel.removeAll();

    // add everything
    mainPanel.add(columns);
    mainPanel.add(radioPanel);
    mainPanel.add(colorPanel);
    mainPanel.add(buttonPanel);
    add(mainPanel);
    ((MyColorPanelSelectionModel)colorPanel.getSelectionModel()).setSelectedColorChangedListener(this);

    // set initial selection to sequential
    sequential.setSelected(true);
    setColorPanel(new SequentialColorPalettePanel(), MapType.CONTINUOUS);
    this.mapType = Optional.of(MapType.CONTINUOUS);
    this.columnName = Optional.ofNullable((String)columns.getSelectedItem());

    disableApplyColorBrewerButton();
    pack();
  }

  private void clearSelections() {
    colorBrewer = Optional.empty();
    ((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).setColorBrewer(null);
    columnName = Optional.empty();
    columns.setSelectedItem(null);
    disableApplyColorBrewerButton();
  }

  private void setNewSelections() {
    colorBrewer = Optional.ofNullable(((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).getColorBrewer());
    columnName = Optional.ofNullable((String)columns.getSelectedItem());
    mapType = Optional.ofNullable(MapType.valueOf(mappersButtonGroup.getSelection().getActionCommand()));
  }
}
