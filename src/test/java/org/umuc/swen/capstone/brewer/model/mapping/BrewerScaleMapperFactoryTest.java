package org.umuc.swen.capstone.brewer.model.mapping;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.jcolorbrewer.ColorBrewer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.umuc.swen.capstone.brewer.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.capstone.brewer.model.exception.InvalidDataException;

/**
 * Created by cwancowicz on 11/15/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class BrewerScaleMapperFactoryTest {

  private static final Random RANDOM = new Random();

  @Rule
  public ExpectedException exception = ExpectedException.none();

  @Test
  public void shouldCreateDiscreteFilterMapper() {
    String columnName = "test";
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    List<CyRow> rows = createCyRows(columnName, Integer.class);

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.Pastel1, MapType.DISCRETE);
    assertNotNull(mapper);
    assertEquals(MapType.DISCRETE, mapper.getMapType());
    rows.stream().forEach(row -> verify(row).get(columnName, Object.class));
  }

  @Test
  public void shouldCreateDivergentFilterMapper() {
    String columnName = "test";
    Class type = Double.class;
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    List<CyRow> rows = createCyRows(columnName, type);

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.RdBu, MapType.DIVERGING);
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
    List<CyRow> rows = createCyRows(columnName, Object.class);

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);

    FilterMapper mapper = BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
        ColorBrewer.BuGn, MapType.CONTINUOUS);
    assertNotNull(mapper);
    assertEquals(MapType.CONTINUOUS, mapper.getMapType());
  }

  @Test
  public void shouldThrowExceptionWhenCreatingDivergentAndColumnNotNumeric() {
    String columnName = "test";
    Class invalidType = Object.class;
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    List<CyRow> rows = Collections.emptyList();

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(invalidType);

    exception.expect(InvalidBrewerColorMapper.class);
    BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
            ColorBrewer.RdBu, MapType.DIVERGING);
  }

  @Test
  public void shouldThrowExceptionWhenCreatingDivergentAndColumnInvalidData() {
    String columnName = "test";
    Class type = Integer.class;
    CyNetwork cyNetwork = mock(CyNetwork.class);
    CyTable cyTable = mock(CyTable.class);
    CyColumn cyColumn = mock(CyColumn.class);
    List<CyRow> rows = Collections.emptyList();

    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyTable.getColumn(columnName)).thenReturn(cyColumn);
    when(cyColumn.getType()).thenReturn(type);

    exception.expect(InvalidDataException.class);
    BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName,
            ColorBrewer.RdBu, MapType.DIVERGING);
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
