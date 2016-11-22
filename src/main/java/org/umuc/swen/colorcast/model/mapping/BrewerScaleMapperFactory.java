package org.umuc.swen.colorcast.model.mapping;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.cytoscape.model.CyNetwork;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;
import org.umuc.swen.colorcast.model.validation.ColorCastMapValidator;

/**
 * Created by cwancowicz on 10/20/16.
 */
public class BrewerScaleMapperFactory {

  /**
   * Creates a {@link FilterMapper} of type {@link MapType} for applying the {@link ColorBrewer}
   * palette to the {@link CyNetwork}.
   *
   * @param cyNetwork   {@link CyNetwork}
   * @param columnName  {@link String}
   * @param colorBrewer {@link ColorBrewer}
   * @param mapType     {@link MapType}
   * @return {@link FilterMapper}
   */
  public static FilterMapper createFilterMapper(CyNetwork cyNetwork, String columnName,
                                                ColorBrewer colorBrewer, MapType mapType, CyActivator cyActivator) {
    switch (mapType) {
      case DISCRETE:
        return createDiscreteFilterMapper(columnName, colorBrewer, cyNetwork, cyActivator);
      case SEQUENTIAL:
        return createSequentialFilterMapper(columnName, colorBrewer, cyNetwork, cyActivator);
      case DIVERGING:
        return createDivergentFilterMapper(columnName, colorBrewer, cyNetwork, cyActivator);
      default:
        throw new InternalError(String.format("Unsupported Map Type [%s]", mapType));
    }
  }

  private static FilterMapper createDiscreteFilterMapper(String columnName, ColorBrewer colorBrewer,
                                                         CyNetwork cyNetwork, CyActivator cyActivator) {
    Class type = getType(cyNetwork, columnName);
    try {
      ColorCastMapValidator.validateDiscreteMapper(colorBrewer, type);
    } catch (IllegalArgumentException e) {
      throw new InvalidDataException(columnName);
    }
    return new DiscreteBrewerScaleMapper(columnName, colorBrewer, type, getValues(type, cyNetwork, columnName),
            cyActivator);
  }

  private static FilterMapper createSequentialFilterMapper(String columnName, ColorBrewer colorBrewer,
                                                           CyNetwork cyNetwork, CyActivator cyActivator) {
    Class type = getType(cyNetwork, columnName);
    try {
      ColorCastMapValidator.validateSequentialMapper(colorBrewer, type);
    } catch (IllegalArgumentException e) {
      throw new InvalidDataException(columnName);
    }
    return new SequentialBrewerScaleMapper(columnName, colorBrewer, type,
            getValues(type, cyNetwork, columnName), cyActivator);
  }

  private static FilterMapper createDivergentFilterMapper(String columnName, ColorBrewer colorBrewer,
                                                          CyNetwork cyNetwork, CyActivator cyActivator) {
    Class type = getType(cyNetwork, columnName);
    try {
      ColorCastMapValidator.validateDivergingMapper(colorBrewer, type);
    } catch (IllegalArgumentException e) {
      throw new InvalidDataException(columnName);
    }
    return new DivergingBrewerScaleMapper(columnName, colorBrewer, type, getValues(type, cyNetwork, columnName), cyActivator);
  }

  private static <T> List<T> getValues(Class<T> type, CyNetwork network, String columnName) {
    return network.getDefaultNodeTable().getAllRows()
            .stream()
            .filter(row -> Objects.nonNull(row.get(columnName, type)))
            .map(row -> row.get(columnName, type))
            .collect(Collectors.toList());
  }

  private static Class getType(CyNetwork cyNetwork, String columnName) {
    return cyNetwork.getDefaultNodeTable().getColumn(columnName).getType();
  }
}
