package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cytoscape.model.CyRow;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidDataException;
import org.umuc.swen.colorcast.model.exception.InvalidElement;

/**
 * {@link FilterMapper} that maps a Sequential Color Scheme to nodes.
 *
 * Created by cwancowicz on 9/29/16.
 */
public class ContinuousBrewerScaleMapper<T extends Number> extends AbstractBrewerScaleMapper {

  private final Double maxValue;
  private final Double offset;
  private final int colorScale;
  private final List<Color> colors;
  private final Class<T> type;

  public ContinuousBrewerScaleMapper(String columnName, ColorBrewer colorBrewer, List<T> values, Class<T> type) {
    super(colorBrewer, columnName);
    this.colorScale = 100;
    this.colors = Arrays.asList(colorBrewer.getColorPalette(this.colorScale));
    this.offset = getOffsetValue(values);
    this.maxValue = getMaxValue(values) + offset;
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
    return Math.max(0, (int) Math.ceil(((value.doubleValue() + offset) / maxValue) * colorScale) - 1);
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

  private double getMaxValue(List<T> values) {
    return values.stream()
            .filter(Objects::nonNull)
            .map(value -> Double.valueOf(value.doubleValue()))
            .max((d1, d2) -> d1.compareTo(d2))
            .map(Math::abs)
            .orElseThrow(() -> new InvalidDataException(columnName));
  }

  private double getOffsetValue(List<T> values) {
    // get offset to bring scale back to zero.
    // For example, if the min value in the entire set is -10 and max value is 15,
    // the offset will be +10. This offset is added to each value when finding the
    // bucket the value belongs to.
    // -10 (value) + 10 (offset) / 15 (maxValue) * 100 (colorScale) = 0
    // This would assign -10 the lightest color in the scale which is in bucket 0
    return values.stream()
            .filter(Objects::nonNull)
            .map(value -> Double.valueOf(value.doubleValue()))
            .min((d1, d2) -> d1.compareTo(d2))
            .map(min -> min * -1)
            .orElseThrow(() -> new InvalidDataException(columnName));
  }
}
