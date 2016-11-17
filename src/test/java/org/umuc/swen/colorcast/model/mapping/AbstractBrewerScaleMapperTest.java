package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Optional;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.View;
import org.cytoscape.view.presentation.property.BasicVisualLexicon;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by cwancowicz on 10/25/16.
 */
public class AbstractBrewerScaleMapperTest {

  private AbstractBrewerScaleMapper abstractBrewerScaleMapper;

  private CyNetworkView cyNetworkView;
  private CyNode cyNode;
  private CyRow cyRow;
  private View<CyNode> view;

  @Before
  public void setUp() {
    abstractBrewerScaleMapper = mock(AbstractBrewerScaleMapper.class);
    cyNetworkView = mock(CyNetworkView.class);
    cyNode = mock(CyNode.class);
    cyRow = mock(CyRow.class);
    view = mock(View.class);
  }

  @Test
  public void shouldApplyColorToNode() {
    Color blue = Color.BLUE;
    when(cyNetworkView.getNodeView(cyNode)).thenReturn(view);
    when(abstractBrewerScaleMapper.getColor(cyRow)).thenReturn(Optional.of(blue));
    doCallRealMethod().when(abstractBrewerScaleMapper).applyFilterMapping(any(), any(), any());
    abstractBrewerScaleMapper.applyFilterMapping(Arrays.asList(cyNetworkView), cyNode, cyRow);
    verify(view).setLockedValue(BasicVisualLexicon.NODE_FILL_COLOR, blue);
  }
}
