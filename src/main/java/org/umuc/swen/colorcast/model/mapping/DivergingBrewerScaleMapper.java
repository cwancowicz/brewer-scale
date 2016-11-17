package org.umuc.swen.colorcast.model.mapping;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.cytoscape.model.CyRow;
import org.jcolorbrewer.ColorBrewer;
import org.umuc.swen.colorcast.model.exception.InvalidBrewerColorMapper;
import org.umuc.swen.colorcast.model.exception.InvalidElement;

/**
 * Created by cwancowicz on 10/17/16.
 */
public class DivergingBrewerScaleMapper<T extends Number> extends AbstractBrewerScaleMapper {

  private final Double maxValue;
  private final Integer colorScale;
  private final Class<T> type;
  private List<Color> positiveColors;
  private List<Color> negativeColors;
  private Color zeroValueColor;

  public DivergingBrewerScaleMapper(String columnName, ColorBrewer colorBrewer, Double maxValue, Class<T> type) {
    super(colorBrewer, columnName);
    this.maxValue = maxValue;
    this.colorScale = 100;
    this.type = type;
    initializeColorScales();
  }

  @Override
  public MapType getMapType() {
    return MapType.DIVERGING;
  }

  @Override
  protected Optional<Color> getColor(CyRow row) {
    if (Objects.isNull(row.get(columnName, type))) {
      return Optional.empty();
    }

    T value = row.get(columnName, type);

    if (isZero(value)) {
      return Optional.of(zeroValueColor);
    } else if (isPositive(value)) {
      return Optional.of(positiveColors.get(getBucket(value)));
    } else {
      return Optional.of(negativeColors.get(getBucket(value)));
    }
  }

  @Override
  protected void validateColorBrewer(ColorBrewer colorBrewer) {
    if (!Arrays.asList(ColorBrewer.getDivergingColorPalettes(false)).contains(colorBrewer)) {
      throw new InvalidBrewerColorMapper(getMapType(), InvalidElement.EXPECTED_DIVERGING_PALETTE);
    }
  }

  /**
   * Returns the bucket this value belongs to.
   * i.e. if we have 5 colors in the positive scale and the value is 25% of the max value then we should return
   * bucket 1:
   * <p>
   * |_| |_| |_| |_| |_|
   *  0   1   2   3   4
   *
   * @param value
   * @return
   */
  private Integer getBucket(T value) {
    return (int) Math.ceil((Math.abs(value.doubleValue()) / maxValue) * colorScale) - 1;
  }

  private boolean isPositive(T value) {
    return value.doubleValue() >= 0;
  }

  private boolean isZero(T value) {
    return value.doubleValue() == 0;
  }

  private void initializeColorScales() {
    // create a color scale with 100 "negative colors" 1 color for zero and 100 "positive colors"
    Color[] colors = colorBrewer.getColorPalette((colorScale * 2) + 1);
    // create the negative scale based on the first 100 colors
    this.negativeColors = Arrays.asList(Arrays.copyOfRange(colors, 0, colorScale));
    // create the positive scale based on the last 100 colors
    this.positiveColors = Arrays.asList(Arrays.copyOfRange(colors, colorScale + 1, colors.length));
    // use the middle color in the scale for zero
    this.zeroValueColor = colors[colorScale];
    // we reverse the negative scale to make it easier to apply a value to a bucket
    Collections.reverse(negativeColors);
  }
}
