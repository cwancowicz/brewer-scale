package org.umuc.swen.capstone.brewer.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Random;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.jcolorbrewer.ColorBrewer;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.umuc.swen.capstone.brewer.model.exception.InvalidBrewerColorMapper;

/**
 * Created by cwancowicz on 10/25/16.
 */
public class DivergingBrewerScaleMapperTest {

  private static final Random RANDOM = new Random();
  private Double maxValue = RANDOM.nextDouble() + 100;
  private String columnName = "testColumn";

  private CyNetworkView cyNetworkView;
  private CyNode node;
  private CyRow row;
  private View<CyNode> view;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    cyNetworkView = mock(CyNetworkView.class);
    node = mock(CyNode.class);
    row = mock(CyRow.class);
    view = mock(View.class);
  }

  @Test
  public void shouldGetZeroColor() {

    ColorBrewer colorBrewer = ColorBrewer.BrBG;
    Color expectedColorAtZero = getColorsFromPalette(colorBrewer)[100];
    Class type = Double.class;

    DivergingBrewerScaleMapper divergingMapper = new DivergingBrewerScaleMapper(columnName, colorBrewer, maxValue, type);

    when(row.get(columnName, type)).thenReturn(0);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    divergingMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    // verify we are setting the view with colorAtZero
    verify(view).setLockedValue(any(), eq(expectedColorAtZero));
  }

  @Test
  public void shouldGetColorFromPositiveScale() {
    ColorBrewer colorBrewer = ColorBrewer.BrBG;
    Color[] colors = getColorsFromPalette(colorBrewer);
    Class type = Integer.class;
    Double maxValue = 100D;
    int value = 72;

    when(row.get(columnName, type)).thenReturn(value);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    DivergingBrewerScaleMapper divergingMapper = new DivergingBrewerScaleMapper(columnName, colorBrewer, maxValue, type);

    divergingMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    // color should be from the positive scale (+100 and 1 for zero) and at the 72nd spot (-1 for 0 index)
    verify(view).setLockedValue(any(), eq(colors[101 + value-1]));
  }

  @Test
  public void shouldGetColorFromNegativeScale() {
    ColorBrewer colorBrewer = ColorBrewer.BrBG;
    Color[] colors = getColorsFromPalette(colorBrewer);
    Class type = Double.class;
    Double maxValue = 100D;
    Double value = -13D;

    when(row.get(columnName, type)).thenReturn(value);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    DivergingBrewerScaleMapper divergingMapper = new DivergingBrewerScaleMapper(columnName, colorBrewer, maxValue, type);

    divergingMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    // color should be from the negative scale. Add 100 colors because we reverse the negative scale and add -13 since this is our value
    verify(view).setLockedValue(any(), eq(colors[100 + value.intValue()]));
  }

  @Test
  public void shouldThrowExceptionWhenColorBrewerIsTypeQualitative() {
    exception.expect(InvalidBrewerColorMapper.class);
    new DivergingBrewerScaleMapper(columnName, ColorBrewer.Accent, maxValue, Integer.class);
  }

  @Test
  public void shouldThrowExceptionWhenColorBrewerIsTypeSequential() {
    exception.expect(InvalidBrewerColorMapper.class);
    new DivergingBrewerScaleMapper(columnName, ColorBrewer.Blues, maxValue, Integer.class);
  }

  @Test
  public void shouldReturnDivergingMapType() {
    assertEquals(MapType.DIVERGING, new DivergingBrewerScaleMapper("", ColorBrewer.BrBG, 0D, Integer.class).getMapType());
  }

  private Color[] getColorsFromPalette(ColorBrewer colorBrewer) {
    return colorBrewer.getColorPalette(201);
  }
}
