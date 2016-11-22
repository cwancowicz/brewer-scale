package org.umuc.swen.colorcast.model.mapping;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyRow;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.CyActivator;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;
import org.umuc.swen.colorcast.model.exception.InvalidElement;
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
    ColorCastMapValidator.validateDiscreteMapper(colorBrewer);
    Set<Object> values = cyNetwork.getDefaultNodeTable().getAllRows()
            .stream()
            .map(row -> row.get(columnName, Object.class))
            .collect(Collectors.toSet());
    return new DiscreteBrewerScaleMapper(values, cyNetwork.getDefaultNodeTable().getColumn(columnName).getType(),
            colorBrewer, columnName, cyActivator);
  }

  private static FilterMapper createSequentialFilterMapper(String columnName, ColorBrewer colorBrewer,
                                                           CyNetwork cyNetwork, CyActivator cyActivator) {
    ColorCastMapValidator.validateSequentialMapper(colorBrewer);
    return new SequentialBrewerScaleMapper(columnName, colorBrewer,
            cyNetwork.getDefaultNodeTable().getAllRows().stream()
                    .map(row -> row.get(columnName, Object.class))
                    .collect(Collectors.toList()),
            cyNetwork.getDefaultNodeTable().getColumn(columnName).getType(), cyActivator
    );
  }

  private static FilterMapper createDivergentFilterMapper(String columnName, ColorBrewer colorBrewer,
                                                          CyNetwork cyNetwork, CyActivator cyActivator) {
    ColorCastMapValidator.validateDivergingMapper(colorBrewer);
    Class type = cyNetwork.getDefaultNodeTable().getColumn(columnName).getType();
    Double maxValue;

    if (type.getSuperclass() == Number.class) {
      maxValue = getMaxValue(type, cyNetwork, columnName);
    } else {
      throw new InvalidBrewerColorMapper(MapType.DIVERGING, InvalidElement.INVALID_DATA_TYPE);
    }

    return new DivergingBrewerScaleMapper(columnName, colorBrewer, maxValue, type, cyActivator);
  }

  private static <T extends Number> Double getMaxValue(Class<T> type, CyNetwork cyNetwork, String columnName) {
    CyRow cyRow = cyNetwork.getDefaultNodeTable().getAllRows()
            .stream()
            .filter(row -> Objects.nonNull(row.get(columnName, type)))
            .max((row1, row2) -> Double.valueOf(Math.abs(row1.get(columnName, type).doubleValue()))
                    .compareTo(Double.valueOf(Math.abs(row2.get(columnName, type).doubleValue()))))
            .orElseThrow(() -> new InvalidDataException(columnName));
    return Math.abs(cyRow.get(columnName, type).doubleValue());
  }
}
