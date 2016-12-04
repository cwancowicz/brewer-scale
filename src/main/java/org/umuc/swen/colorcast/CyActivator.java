package org.umuc.swen.colorcast;

import java.awt.Component;
import java.util.Properties;
import org.cytoscape.application.swing.CyAction;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.osgi.framework.BundleContext;
import org.umuc.swen.colorcast.model.util.ColorBrewerMapperUtil;


public class CyActivator extends AbstractCyActivator {

  private ColorBrewerMapperUtil colorBrewerMapperUtil;

  private ColorCastCyAction colorCastCyAction;
  private CySwingApplication swingApplicationService;
  private CyNetworkManager networkManager;
  private CyNetworkViewManager networkViewManager;
  private VisualMappingManager visualMappingManager;
  private VisualStyleFactory visualStyleFactory;
  private VisualMappingFunctionFactory vmfFactoryContinuous;
  private VisualMappingFunctionFactory vmfFactoryDiscrete;
  private VisualMappingFunctionFactory vmfFactoryPassthrough;

  public CyActivator() {
    super();
  }

  public void start(BundleContext bundleContext) {
    getServices(bundleContext);
    colorBrewerMapperUtil = new ColorBrewerMapperUtil(this);
    registerServices(bundleContext);
  }

  public CyNetworkViewManager getNetworkViewManager() {
    return networkViewManager;
  }

  public CyNetworkManager getNetworkManager() {
    return networkManager;
  }

  public VisualMappingManager getVisualMappingManager() {
    return visualMappingManager;
  }

  public VisualStyleFactory getVisualStyleFactory() {
    return visualStyleFactory;
  }

  public VisualMappingFunctionFactory getVmfFactoryContinuous() {
    return vmfFactoryContinuous;
  }

  public VisualMappingFunctionFactory getVmfFactoryDiscrete() {
    return vmfFactoryDiscrete;
  }

  public VisualMappingFunctionFactory getVmfFactoryPassthrough() {
    return vmfFactoryPassthrough;
  }

  private Component getRootComponent() {
    return this.swingApplicationService.getJFrame();
  }

  private void getServices(BundleContext bundleContext) {
    swingApplicationService = getService(bundleContext, CySwingApplication.class);
    networkViewManager = getService(bundleContext, CyNetworkViewManager.class);
    networkManager = getService(bundleContext, CyNetworkManager.class);
    visualMappingManager = getService(bundleContext, VisualMappingManager.class);
    visualStyleFactory = getService(bundleContext, VisualStyleFactory.class);
    vmfFactoryContinuous = getService(bundleContext, VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
    vmfFactoryDiscrete = getService(bundleContext, VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
    vmfFactoryPassthrough = getService(bundleContext, VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
  }

  private void registerServices(BundleContext bundleContext) {
    colorCastCyAction = new ColorCastCyAction(getRootComponent(), colorBrewerMapperUtil);
    registerService(bundleContext, colorCastCyAction, CyAction.class, new Properties());
  }
}

