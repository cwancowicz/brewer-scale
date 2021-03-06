package org.umuc.swen.colorcast.view.dialog;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Objects;
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
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;
import org.umuc.swen.colorcast.view.MyColorPanelSelectionModel;
import org.umuc.swen.colorcast.view.listener.RadioButtonListener;
import org.umuc.swen.colorcast.model.mapping.MapType;
import org.umuc.swen.colorcast.view.listener.ColumnSelectionListener;
import org.umuc.swen.colorcast.view.listener.DisableApplyColorSchemeListener;
import org.umuc.swen.colorcast.view.listener.ColorChangeListener;
import org.umuc.swen.colorcast.view.palettes.ColorCastPalettePanel;
import org.umuc.swen.colorcast.view.palettes.MySequentialColorPalettePanel;

/**
 * Created by cwancowicz on 11/1/16.
 */
public class ColorBrewerPaletteChooser extends JDialog implements ColorChangeListener, ActionListener {

  private final static String APPLY_COLOR_PALLETE = "Apply Color Palette";
  private final static String CANCEL = "Cancel";
  private final static String RESET = "Reset";
  private final static String COLUMN_LABEL = "Please select a data column below";
  private final static String PREVIEW = "Preview";

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
  private JButton previewButton;

  private JPanel mainPanel = new JPanel();
  private JPanel buttonPanel;

  public ColorBrewerPaletteChooser(Component rootComponent, ColorBrewerMapperUtil colorBrewerMapperUtil) {
    super(null, Resources.APP_TITLE, ModalityType.APPLICATION_MODAL);
    this.colorBrewerMapperUtil = colorBrewerMapperUtil;
    listener = new DisableApplyColorSchemeListener(this);
    mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
    colorBrewerMapperUtil.createCurrentVisualStyle();
    initializeDialog(rootComponent);
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
    this.previewButton.setEnabled(false);
  }

  public void enableApplyColorBrewerButton() {
    this.applyColorBrewerButton.setEnabled(true);
    this.previewButton.setEnabled(true);
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
      case PREVIEW:
        setNewSelections();
        applySelectionToNetworkForPreview();
        break;
      case RESET:
        clearSelections();
        break;
      case CANCEL:
        cancelSelected();
        break;
    }
  }

  public void setColumnSelection() {
    selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
  }

  private void initializeDialog(Component rootComponent) {
    createAndAddJComboBoxForColumnSelection(mainPanel);
    createAndAddRadioButtonLayout(mainPanel);
    createAndAddColorPanel(mainPanel);
    createAndAddButtonLayout(mainPanel);

    add(mainPanel);

    setDefaultSelectionToSequentialMapper();

    disableApplyColorBrewerButton();
    pack();
    setLocationRelativeTo(rootComponent);
  }

  private void setDefaultSelectionToSequentialMapper() {
    ((JRadioButton) ((JPanel) mainPanel.getComponent(RADIO_BUTTON_INDEX)).getComponent(0)).setSelected(true);
    // remove all panels and preview panels
    colorPanel.setPreviewPanel(new JPanel());
    colorPanel.setChooserPanels(new AbstractColorChooserPanel[]{});
    setColorPanel(new MySequentialColorPalettePanel(), MapType.SEQUENTIAL);
    this.selectedMapType = Optional.of(MapType.SEQUENTIAL);
    this.selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
  }

  private void createAndAddColorPanel(JPanel panel) {
    colorPanel = new JColorChooser(new MyColorPanelSelectionModel(listener));
    colorPanel.removeAll();
    ((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).setSelectedColorChangedListener(this);
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

    sequential.addActionListener(new RadioButtonListener(this, MapType.SEQUENTIAL, listener));
    diverging.addActionListener(new RadioButtonListener(this, MapType.DIVERGING, listener));
    qualitative.addActionListener(new RadioButtonListener(this, MapType.DISCRETE, listener));

    sequential.setActionCommand(MapType.SEQUENTIAL.name());
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
    previewButton = new JButton(PREVIEW);

    applyColorBrewerButton.setActionCommand(APPLY_COLOR_PALLETE);
    cancelButton.setActionCommand(CANCEL);
    resetButton.setActionCommand(RESET);
    previewButton.setActionCommand(PREVIEW);

    applyColorBrewerButton.addActionListener(this);
    cancelButton.addActionListener(this);
    resetButton.addActionListener(this);
    previewButton.addActionListener(this);

    buttonPanel.add(cancelButton);
    buttonPanel.add(resetButton);
    buttonPanel.add(previewButton);
    buttonPanel.add(applyColorBrewerButton);

    panel.add(buttonPanel, BUTTONS_INDEX);
  }

  private void clearSelections() {
    selectedColorBrewer = Optional.empty();
    ((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).setColorBrewer(null);
    if (Objects.nonNull(colorPanel.getChooserPanels())) {
      Arrays.asList(colorPanel.getChooserPanels()).stream().filter(Objects::nonNull)
              .forEach(panel -> ((ColorCastPalettePanel) panel).deselectAllPalettes());
    }
    selectedColumnName = Optional.empty();
    columnsComboBox.setSelectedItem(null);
    disableApplyColorBrewerButton();
  }

  private void setNewSelections() {
    selectedColorBrewer = Optional.ofNullable(((MyColorPanelSelectionModel) colorPanel.getSelectionModel()).getColorBrewer());
    selectedColumnName = Optional.ofNullable((String) columnsComboBox.getSelectedItem());
    selectedMapType = Optional.ofNullable(MapType.valueOf(mappersButtonGroup.getSelection().getActionCommand()));
  }

  private void applySelectionToNetworkForPreview() {
    colorBrewerMapperUtil.applyFilterToNetworks(getSelectedColumn().get(),
            getSelectedPalette().get(),
            getSelectedMapType().get());
  }

  private void cancelSelected() {
    clearSelections();
    colorBrewerMapperUtil.applyFilterToNetworks();
    dispose();
  }

  public class Resources {
    public static final String APP_TITLE = "Color Cast";
    public static final String ABOUT = "About";
    public static final String APP_MENU = "Tools." + APP_TITLE;
  }
}
