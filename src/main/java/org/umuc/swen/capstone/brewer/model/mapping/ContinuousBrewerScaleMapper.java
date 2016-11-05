package org.umuc.swen.capstone.brewer.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cytoscape.model.CyRow;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.capstone.brewer.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.capstone.brewer.model.exception.InvalidDataException;
import org.umuc.swen.capstone.brewer.model.exception.InvalidElement;

/**
 * Created by cwancowicz on 9/29/16.
 */
public class ContinuousBrewerScaleMapper<T extends Number> extends AbstractBrewerScaleMapper {

  private final Double maxValue;
  private final int colorScale;
  private final List<Color> colors;
  private final Class<T> type;

  public ContinuousBrewerScaleMapper(String columnName, ColorBrewer colorBrewer, List<T> values, Class<T> type) {
    super(colorBrewer, columnName);
    this.colorScale = 100;
    this.colors = Arrays.asList(colorBrewer.getColorPalette(this.colorScale));
    this.maxValue = values.stream()
            .filter(Objects::nonNull)
            .map(value -> Double.valueOf(Math.abs(value.doubleValue())))
            .max((d1, d2) -> d1.compareTo(d2))
            .orElseThrow(() -> new InvalidDataException(columnName));
    this.type = type;
  }

  @Override
  protected Optional<Color> getColor(CyRow row) {
    if (Objects.isNull(row.get(columnName, type))) {
      return Optional.empty();
    }
    return Optional.of(colors.get(getBucket(row.get(columnName, type))));
  }

  /**
   * Returns the bucket this value belongs to
   * @param value
   * @return
   */
  private Integer getBucket(T value) {
    return Math.max(0, (int) Math.ceil((Math.abs(value.doubleValue()) / maxValue) * colorScale) - 1);
  }

  @Override
  protected void validateColorBrewer(ColorBrewer colorBrewer) {
    if (!Arrays.asList(ColorBrewer.getSequentialColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(getMapType(), InvalidElement.EXPECTED_SEQUENTIAL_PALETTE);
    }
  }

  @Override
  public MapType getMapType() {
    return MapType.CONTINUOUS;
  }
}
