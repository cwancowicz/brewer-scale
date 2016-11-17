package org.umuc.swen.colorcast.model.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.jcolorbrewer.ColorBrewer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.mapping.BrewerScaleMapperFactory;
import org.umuc.swen.colorcast.model.mapping.ContinuousBrewerScaleMapper;
import org.umuc.swen.colorcast.model.mapping.MapType;

/**
 * Created by cwancowicz on 10/28/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(BrewerScaleMapperFactory.class)
public class ColorBrewerMapperUtilTest {

  private static final Random RANDOM = new Random();
  private static final Long NETWORK_ID = RANDOM.nextLong();
  @Mock
  private ContinuousBrewerScaleMapper continuousBrewerScaleMapper;
  @Mock
  private CyNode cyNode;
  @Mock
  private CyNetwork cyNetwork;
  @Mock
  private CyTable cyTable;
  @Mock
  private CyNetworkViewManager viewManager;
  @Mock
  private CyNetworkManager cyNetworkManager;
  @Mock
  private CyNetworkView cyNetworkView;
  @Mock
  private CyActivator cyActivator;

  private ColorBrewerMapperUtil colorBrewerMapperUtil;

  @Before
  public void setUp() {
    PowerMockito.mockStatic(BrewerScaleMapperFactory.class);
    colorBrewerMapperUtil = new ColorBrewerMapperUtil(cyActivator);
  }

  @Test
  public void shouldApplyFilterToEachRowInNetwork() {
    String columnName = "testColumnName";
    List<CyRow> rows = Arrays.asList(
            createMockRow(columnName, RANDOM.nextInt()),
            createMockRow(columnName, RANDOM.nextInt()),
            createMockRow(columnName, RANDOM.nextInt())
    );
    Set<CyNetwork> cyNetworks = new HashSet();
    cyNetworks.addAll(Arrays.asList(cyNetwork));
    ColorBrewer colorBrewer = ColorBrewer.Blues;
    MapType mapType = MapType.CONTINUOUS;
    List<CyNetworkView> cyNetworkViews = Arrays.asList(cyNetworkView);

    when(cyActivator.getNetworkManager()).thenReturn(cyNetworkManager);
    when(cyNetworkManager.getNetworkSet()).thenReturn(cyNetworks);
    when(cyActivator.getNetworkViewManager()).thenReturn(viewManager);
    when(viewManager.getNetworkViews(cyNetwork)).thenReturn(cyNetworkViews);
    when(cyNetwork.getDefaultNodeTable()).thenReturn(cyTable);
    when(cyTable.getAllRows()).thenReturn(rows);
    when(cyNetwork.getNode(any(Long.class))).thenReturn(cyNode);
    PowerMockito.when(BrewerScaleMapperFactory.createFilterMapper(cyNetwork, columnName, colorBrewer, mapType)).thenReturn(continuousBrewerScaleMapper);

    colorBrewerMapperUtil.applyFilterToNetworks(columnName, ColorBrewer.Blues, MapType.CONTINUOUS);

    rows.stream().forEach(row -> verify(continuousBrewerScaleMapper).applyFilterMapping(cyNetworkViews, cyNode, row));
    verify(cyNetworkView).updateView();
  }

  private CyRow createMockRow(String columnName, int value) {
    CyRow row = mock(CyRow.class);
    when(row.get(columnName, Object.class)).thenReturn(value);
    when(row.get(CyNetwork.SUID, Long.class)).thenReturn(NETWORK_ID);
    return row;
  }
}
