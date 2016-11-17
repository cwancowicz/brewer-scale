package org.umuc.swen.colorcast;

import java.util.Properties;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.osgi.framework.BundleContext;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;


public class CyActivator extends AbstractCyActivator {

  private BrewerPanelComponent brewerPanelComponent;
  private ControlPanelAction controlPanelAction;
  private CySwingApplication swingApplicationService;

  private CyNetworkManager networkManager;
  private CyNetworkViewManager networkViewManager;

  final private ColorBrewerMapperUtil colorBrewerMapperUtil;

  public CyActivator() {
    super();
    colorBrewerMapperUtil = new ColorBrewerMapperUtil(this);
  }

  public void start(BundleContext bundleContext) {
    getServices(bundleContext);
    registerServices(bundleContext);
  }

  public CyNetworkViewManager getNetworkViewManager() {
    return networkViewManager;
  }

  public CyNetworkManager getNetworkManager() {
    return networkManager;
  }

  private void getServices(BundleContext bundleContext) {
    swingApplicationService = getService(bundleContext, CySwingApplication.class);
    networkViewManager = getService(bundleContext, CyNetworkViewManager.class);
    networkManager = getService(bundleContext, CyNetworkManager.class);
  }

  private void registerServices(BundleContext bundleContext) {
    brewerPanelComponent = new BrewerPanelComponent(colorBrewerMapperUtil);
    controlPanelAction = new ControlPanelAction(swingApplicationService, brewerPanelComponent);
    registerService(bundleContext, brewerPanelComponent, CytoPanelComponent.class, new Properties());
    registerService(bundleContext, controlPanelAction, CyAction.class, new Properties());
  }
}

