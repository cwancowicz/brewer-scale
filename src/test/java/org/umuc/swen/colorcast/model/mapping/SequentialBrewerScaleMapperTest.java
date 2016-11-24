package org.umuc.swen.colorcast.model.mapping;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.jcolorbrewer.ColorBrewer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;

/**
 * Created by cwancowicz on 10/28/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class SequentialBrewerScaleMapperTest {

  private static final Random random = new Random();
  private static final String columnName = "test";

  @Mock
  private CyActivator cyActivator;
  @Mock
  private VisualMappingManager visualMappingManager;
  @Mock
  private VisualMappingFunctionFactory visualMappingFunctionFactory;
  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    when(cyActivator.getVisualMappingManager()).thenReturn(visualMappingManager);
    when(visualMappingManager.getCurrentVisualStyle()).thenReturn(mock(VisualStyle.class));
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(visualMappingFunctionFactory);
  }

  @Test
  public void shouldCreateContinuousMappingVisualMappingFunction() {
    Class type = Integer.class;
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(ContinuousMapping.class));
    SequentialBrewerScaleMapper sequentialBrewerScalemapper = new SequentialBrewerScaleMapper(columnName, ColorBrewer.Blues,
            type, Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt()), cyActivator);
    assertTrue(sequentialBrewerScalemapper.createVisualMappingFunction() instanceof ContinuousMapping);
  }

  @Test
  public void shouldReturnSequentialMapType() {
    Class type = Integer.class;
    ContinuousMapping continuousMapping = mock(ContinuousMapping.class);
    VisualMappingFunctionFactory vmfFactory = mock(VisualMappingFunctionFactory.class);
    when(vmfFactory.createVisualMappingFunction(columnName, Integer.class, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(continuousMapping);
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(vmfFactory);
    assertEquals(MapType.SEQUENTIAL, new SequentialBrewerScaleMapper(columnName, ColorBrewer.Blues,
            type, Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt()), cyActivator).getMapType());
  }

  @Test
  @Ignore
  public void shouldThrowExceptionWhenValuesIsEmpty() {
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, Integer.class, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(ContinuousMapping.class));
    exception.expect(InvalidDataException.class);
    new SequentialBrewerScaleMapper(columnName, ColorBrewer.Accent, String.class, Arrays.asList("something"), cyActivator);
  }

  @Test
  public void shouldCreateBoundaryRangeValues() {
    Class type = Integer.class;
    ContinuousMapping continuousMapping = mock(ContinuousMapping.class);
    VisualMappingFunctionFactory vmfFactory = mock(VisualMappingFunctionFactory.class);
    when(vmfFactory.createVisualMappingFunction(columnName, Integer.class, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(continuousMapping);
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(vmfFactory);
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    List<Integer> values = Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt());
    SequentialBrewerScaleMapper mapper = new SequentialBrewerScaleMapper(columnName, colorBrewer,
            type, values, cyActivator);
    verify(continuousMapping, times(colorBrewer.getMaximumColorCount())).addPoint(any(), any());
  }
}
