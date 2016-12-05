package org.umuc.swen.colorcast.model.mapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyle;
import org.cytoscape.view.vizmap.mappings.ContinuousMapping;
import org.cytoscape.view.vizmap.mappings.DiscreteMapping;
import org.jcolorbrewer.ColorBrewer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;
import org.umuc.swen.colorcast.CyActivator;

/**
 * Created by cwancowicz on 11/15/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class BrewerScaleMapperFactoryTest {

  private static final Random RANDOM = new Random();
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
  }

  @Test
  public void shouldCreateDiscreteFilterMapper() {
    Class type = String.class;
    String columnName = "test";
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    List<CyRow> rows = createCyRows(columnName, type);
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(DiscreteMapping.class));
    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);
    when(cyActivator.getVmfFactoryDiscrete()).thenReturn(visualMappingFunctionFactory);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.Pastel1, MapType.DISCRETE, cyActivator);
    assertNotNull(mapper);
    assertEquals(MapType.DISCRETE, mapper.getMapType());
  }

  @Test
  public void shouldCreateDivergentFilterMapper() {
    String columnName = "test";
    Class type = Double.class;
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    List<CyRow> rows = createCyRows(columnName, type);
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(ContinuousMapping.class));
    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(visualMappingFunctionFactory);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.RdBu, MapType.DIVERGING, cyActivator);
    assertNotNull(mapper);
    assertEquals(MapType.DIVERGING, mapper.getMapType());
  }

  @Test
  public void shouldCreateSequentialFilterMapper() {
    String columnName = "test";
    Class type = Double.class;
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    when(visualMappingFunctionFactory.createVisualMappingFunction(columnName, type, BasicVisualLexicon.NODE_FILL_COLOR))
            .thenReturn(mock(ContinuousMapping.class));
    List<CyRow> rows = createCyRows(columnName, type);

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);
    when(cyActivator.getVmfFactoryContinuous()).thenReturn(visualMappingFunctionFactory);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.BuGn, MapType.SEQUENTIAL, cyActivator);
    assertNotNull(mapper);
    assertEquals(MapType.SEQUENTIAL, mapper.getMapType());
  }

  private List<CyRow> createCyRows(String columnName, Class type) {
    int num = RANDOM.nextInt(100) + 1;
    List rows = new ArrayList();
    for (int i = 0; i < num; i++) {
      CyRow row = mock(CyRow.class);
      when(row.get(columnName, type)).thenReturn(getValue(type));
      rows.add(row);
    }
    return rows;
  }

  public Object getValue(Class type) {
    if (type == String.class) {
      return UUID.randomUUID();
    }
    else {
      return RANDOM.nextInt();
    }
  }
}
