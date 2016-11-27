package org.umuc.swen.colorcast.model.mapping;

import java.util.Arrays;
import java.util.Random;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.jcolorbrewer.ColorBrewer;
import static org.junit.Assert.assertEquals;
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
 * Created by cwancowicz on 10/25/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class DivergingBrewerScaleMapperTest {

  private static final Random random = new Random();
  @Mock
  private CyActivator cyActivator;
  @Mock
  private VisualMappingManager visualMappingManager;

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Before
  public void setUp() {
    when(cyActivator.getVisualMappingManager()).thenReturn(visualMappingManager);
    when(visualMappingManager.getCurrentVisualStyle()).thenReturn(mock(VisualStyle.class));
  }

  @Test
  public void shouldReturnDivergingMapType() {
    VisualMappingFunctionFactory vmfFactory = mock(VisualMappingFunctionFactory.class);
    when(vmfFactory.createVisualMappingFunction("", Integer.class, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(ContinuousMapping.class));
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(vmfFactory);
    assertEquals(MapType.DIVERGING, new DivergingBrewerScaleMapper("", ColorBrewer.BrBG, Integer.class,
            Arrays.asList(random.nextInt(), random.nextInt()), cyActivator).getMapType());
  }
}
