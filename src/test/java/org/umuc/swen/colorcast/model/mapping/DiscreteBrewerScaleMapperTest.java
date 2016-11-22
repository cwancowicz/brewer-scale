package org.umuc.swen.colorcast.model.mapping;

import java.util.Arrays;
import java.util.Random;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.jcolorbrewer.ColorBrewer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.runners.MockitoJUnitRunner;
import org.umuc.swen.colorcast.CyActivator;

/**
 * Created by cwancowicz on 10/15/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DiscreteBrewerScaleMapperTest {

  private static final Random random = new Random();
  private static String columnName = "testing";

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
    when(cyActivator.getVmfFactoryDiscrete()).thenReturn(visualMappingFunctionFactory);
  }

  @Test
  public void shouldCreateDiscreteMappingVisualMappingFunction() {
    Class type = Integer.class;
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(DiscreteMapping.class));
    DiscreteBrewerScaleMapper discreteBrewerScaleMapper = new DiscreteBrewerScaleMapper(columnName, ColorBrewer.Pastel1,
            type, Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt()), cyActivator);
    assertTrue(discreteBrewerScaleMapper.createVisualMappingFunction() instanceof DiscreteMapping);
  }

  @Test
  public void shouldReturnDiscreteMapType() {
    Class type = Integer.class;
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(DiscreteMapping.class));
    assertEquals(MapType.DISCRETE, new DiscreteBrewerScaleMapper(columnName, ColorBrewer.Blues,
            type, Arrays.asList(random.nextInt(), random.nextInt(), random.nextInt()), cyActivator).getMapType());
  }
}
