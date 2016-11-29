package org.umuc.swen.colorcast.view;

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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import org.jcolorbrewer.ColorBrewer;
import org.jcolorbrewer.ui.SequentialColorPalettePanel;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.colorcast.view.listener.RadioButtonListener;
import org.umuc.swen.colorcast.model.mapping.MapType;
import org.umuc.swen.colorcast.view.listener.ColumnSelectionListener;
import org.umuc.swen.colorcast.view.listener.DisableApplyColorSchemeListener;
import org.umuc.swen.colorcast.view.listener.ColorChangeListener;

/**
 * Created by cwancowicz on 11/1/16.
 */
public class ColorBrewerPaletteChooser extends JDialog implements ColorChangeListener, ActionListener {

  private final static String APPLY_COLOR_PALLETE = "Apply";
  private final static String CANCEL = "Cancel";
  private final static String RESET = "Reset";
  private final static String COLUMN_LABEL = "Please select a data column below";

  private final static int COLUMNS_LABEL_PANEL = 0;
  private final static int COLUMNS_INDEX = 1;
  private final static int RADIO_BUTTON_INDEX = 2;
  private final static int COLOR_PANEL_INDEX = 3;
  private final static int BUTTONS_INDEX = 4;

  private final ColorBrewerMapperUtil colorBrewerMapperUtil;
  private JColorChooser colorPanel;
  private Optional<ColorBrewer> selectedColorBrewer = Optional.empty();
  private Optional<MapType> selectedMapType = Optional.empty();
  private Optional<String> selectedColumnName = Optional.empty();
  private JComboBox<String> columnsComboBox;
  private ButtonGroup mappersButtonGroup;
  private DisableApplyColorSchemeListener listener;
  private JButton applyColorBrewerButton;

  private JPanel mainPanel = new JPanel();
  private JPanel buttonPanel;

  public ColorBrewerPaletteChooser(ColorBrewerMapperUtil colorBrewerMapperUtil) {
    super(null, Resources.APP_TITLE, ModalityType.APPLICATION_MODAL);
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
    listener = new DisableApplyColorSchemeListener(this);

    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

    initializeDialog();
  }

  public void setColorPanel(AbstractColorChooserPanel abstractColorChooserPanel, MapType mapType) {
    clearSelections();
    colorPanel.setChooserPanels(new AbstractColorChooserPanel[]{abstractColorChooserPanel});
    columnsComboBox.setModel(new DefaultComboBoxModel(colorBrewerMapperUtil.getColumns(mapType).toArray()));
    setNewSelections();
    revalidate();
    repaint();
  }

  public Optional<ColorBrewer> getSelectedPalette() {
    return this.selectedColorBrewer;
  }

  public Optional<MapType> getSelectedMapType() {
    return this.selectedMapType;
  }

  public Optional<String> getSelectedColumn() {
    return this.selectedColumnName;
  }

  public void disableApplyColorBrewerButton() {
    this.applyColorBrewerButton.setEnabled(false);
  }

  public void enableApplyColorBrewerButton() {
    this.applyColorBrewerButton.setEnabled(true);
  }

  @Override
  public void colorChanged() {
    selectedColorBrewer = Optional.ofNullable(((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).getColorBrewer());
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
    selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
  }

  private void initializeDialog() {
    createAndAddJComboBoxForColumnSelection(mainPanel);
    createAndAddRadioButtonLayout(mainPanel);
    createAndAddColorPanel(mainPanel);
    createAndAddButtonLayout(mainPanel);

    add(mainPanel);

    setDefaultSelectionToSequentialMapper();

    disableApplyColorBrewerButton();
    pack();
  }

  private void setDefaultSelectionToSequentialMapper() {
    ((JRadioButton)((JPanel)mainPanel.getComponent(RADIO_BUTTON_INDEX)).getComponent(0)).setSelected(true);
    setColorPanel(new SequentialColorPalettePanel(), MapType.CONTINUOUS);
    this.selectedMapType = Optional.of(MapType.CONTINUOUS);
    this.selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
  }

  private void createAndAddColorPanel(JPanel panel) {
    colorPanel = new JColorChooser(new MyColorPanelSelectionModel(listener));
    colorPanel.removeAll();
    ((MyColorPanelSelectionModel)colorPanel.getSelectionModel()).setSelectedColorChangedListener(this);
    panel.add(colorPanel, COLOR_PANEL_INDEX);
  }

  private void createAndAddJComboBoxForColumnSelection(JPanel panel) {
    JPanel columnPanel = new JPanel();
    columnsComboBox = new JComboBox();
    columnsComboBox.addActionListener(new ColumnSelectionListener(this, listener));
    columnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
    columnPanel.add(new JLabel(COLUMN_LABEL));
    panel.add(columnPanel, COLUMNS_LABEL_PANEL);
    panel.add(columnsComboBox, COLUMNS_INDEX);
  }

  private void createAndAddRadioButtonLayout(JPanel panel) {
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

    panel.add(radioPanel, RADIO_BUTTON_INDEX);
  }

  private void createAndAddButtonLayout(JPanel panel) {
    buttonPanel = new JPanel();
    buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

    applyColorBrewerButton = new JButton(APPLY_COLOR_PALLETE);
    JButton cancelButton = new JButton(CANCEL);
    JButton resetButton = new JButton(RESET);

    applyColorBrewerButton.setActionCommand(APPLY_COLOR_PALLETE);
    cancelButton.setActionCommand(CANCEL);
    resetButton.setActionCommand(RESET);

    applyColorBrewerButton.addActionListener(this);
    cancelButton.addActionListener(this);
    resetButton.addActionListener(this);

    buttonPanel.add(resetButton);
    buttonPanel.add(cancelButton);
    buttonPanel.add(applyColorBrewerButton);

    panel.add(buttonPanel, BUTTONS_INDEX);
  }

  private void clearSelections() {
    selectedColorBrewer = Optional.empty();
    ((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).setColorBrewer(null);
    selectedColumnName = Optional.empty();
    columnsComboBox.setSelectedItem(null);
    disableApplyColorBrewerButton();
  }

  private void setNewSelections() {
    selectedColorBrewer = Optional.ofNullable(((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).getColorBrewer());
    selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
    selectedMapType = Optional.ofNullable(MapType.valueOf(mappersButtonGroup.getSelection().getActionCommand()));
  }

  public class Resources {
    public static final String APP_TITLE = "Color Cast";
    public static final String APP_MENU = "Apps";
  }
}
