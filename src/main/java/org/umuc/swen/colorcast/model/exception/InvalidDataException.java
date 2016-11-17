package org.umuc.swen.colorcast.model.exception;

/**
 * Created by cwancowicz on 10/21/16.
 */
public class InvalidDataException extends BrewerColorAppException {
  public InvalidDataException(String columnName) {
    super(String.format("Invalid data found for column [%s]", columnName));
  }
}