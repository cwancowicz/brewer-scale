package org.umuc.swen.capstone.brewer.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.jcolorbrewer.ColorBrewer;
import static org.junit.Assert.assertEquals;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.umuc.swen.capstone.brewer.model.exception.InvalidBrewerColorMapper;

/**
 * Created by cwancowicz on 10/28/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class ContinuousBrewerScaleMapperTest {

  private static final Random RANDOM = new Random();
  private String columnName = "testColumn";

  @Mock
  private CyNetworkView cyNetworkView;
  @Mock
  private CyNode node;
  @Mock
  private CyRow row;
  @Mock
  private View<CyNode> view;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldGetWeightedColor() {
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    Color[] colors = colorBrewer.getColorPalette(100);
    Class type = Integer.class;
    int value = 72;
    List<Integer> values = Arrays.asList(value, RANDOM.nextInt(100), RANDOM.nextInt(100),
            RANDOM.nextInt(100), RANDOM.nextInt(100), 100, 0);

    when(row.get(columnName, type)).thenReturn(value);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    ContinuousBrewerScaleMapper continuousMapper = new ContinuousBrewerScaleMapper(columnName, colorBrewer, values, Integer.class);
    continuousMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    verify(view).setLockedValue(any(), eq(colors[value - 1]));
  }

  @Test
  public void shouldGetDarkestColorOnTheColorScale() {
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    Color[] colors = colorBrewer.getColorPalette(100);
    Class type = Integer.class;
    Integer maxValue = 100;
    List<Integer> values = Arrays.asList(RANDOM.nextInt(100), RANDOM.nextInt(100),
            RANDOM.nextInt(100), RANDOM.nextInt(100), maxValue);

    when(row.get(columnName, type)).thenReturn(maxValue);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    ContinuousBrewerScaleMapper continuousMapper = new ContinuousBrewerScaleMapper(columnName, colorBrewer, values, Integer.class);
    continuousMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    verify(view).setLockedValue(any(), eq(colors[colors.length - 1]));
  }

  @Test
  public void shouldGetLightestColorOnTheColorScale() {
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    Color[] colors = colorBrewer.getColorPalette(100);
    Class type = Double.class;
    List<Integer> values = Arrays.asList(RANDOM.nextInt(100), RANDOM.nextInt(100),
            RANDOM.nextInt(100), RANDOM.nextInt(100), 100);
    Double minValue = values.stream().min(Integer::compare).map(Integer::doubleValue).get();

    when(row.get(columnName, type)).thenReturn(minValue);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    ContinuousBrewerScaleMapper continuousMapper = new ContinuousBrewerScaleMapper(columnName, colorBrewer, values, Double.class);
    continuousMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    verify(view).setLockedValue(any(), eq(colors[0]));
  }

  @Test
  public void shouldGetLightestColorOnTheColorScaleWithNegativeNumbers() {
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    Color[] colors = colorBrewer.getColorPalette(100);
    Class type = Double.class;
    List<Integer> values = Arrays.asList(RANDOM.nextInt(100) * -1, RANDOM.nextInt(100),
            RANDOM.nextInt(100) * -1, RANDOM.nextInt(100), 100);
    Double minValue = values.stream().min(Integer::compare).map(Integer::doubleValue).get();

    when(row.get(columnName, type)).thenReturn(minValue);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    ContinuousBrewerScaleMapper continuousMapper = new ContinuousBrewerScaleMapper(columnName, colorBrewer, values, Double.class);
    continuousMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    verify(view).setLockedValue(any(), eq(colors[0]));
  }

  @Test
  public void shouldGetDarkestColorOnTheColorScaleWithNegativeNumbers() {
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    Color[] colors = colorBrewer.getColorPalette(100);
    Class type = Integer.class;
    Integer maxValue = 100;
    List<Integer> values = Arrays.asList(RANDOM.nextInt(100), RANDOM.nextInt(100) * -1,
            RANDOM.nextInt(100), RANDOM.nextInt(100) * -1, maxValue);

    when(row.get(columnName, type)).thenReturn(maxValue);
    when(cyNetworkView.getNodeView(node)).thenReturn(view);

    ContinuousBrewerScaleMapper continuousMapper = new ContinuousBrewerScaleMapper(columnName, colorBrewer, values, Integer.class);
    continuousMapper.applyFilterMapping(Arrays.asList(cyNetworkView), node, row);

    verify(view).setLockedValue(any(), eq(colors[colors.length - 1]));
  }

  @Test
  public void shouldThrowExceptionWhenColorBrewerIsNotTypeSequential() {
    exception.expect(InvalidBrewerColorMapper.class);
    new ContinuousBrewerScaleMapper(columnName, ColorBrewer.Accent, Collections.emptyList(), Integer.class);
  }

  @Test
  public void shouldReturnDivergingMapType() {
    assertEquals(MapType.CONTINUOUS,
            new ContinuousBrewerScaleMapper(
                    columnName, ColorBrewer.Blues, Arrays.asList(1), Integer.class).getMapType());
  }
}
