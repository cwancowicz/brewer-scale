package org.umuc.swen.capstone.brewer.model.exception;

/**
 * Created by cwancowicz on 10/21/16.
 */
public enum InvalidElement {

  INVALID_DATA_TYPE("invalid data type in column"),
  EXPECTED_QUALITATIVE_PALETTE("invalid palette type, expected Qualitative Palette"),
  EXPECTED_SEQUENTIAL_PALETTE("invalid palette type, expected Sequential Palette"),
  EXPECTED_DIVERGING_PALETTE("invalid palette type, expected Diverging Palette"),
  UNSUPPORTED_MAP_TYPE("invalid map type");

  private String element;

  InvalidElement(String element) {
    this.element = element;
  }

  public String getElement() {
    return this.element;
  }
}