package org.payments4j.core;

/**
 *
 */
public class AvsResponse {

  private String code;
  private String message;
  private String streetMatch;
  private String postalMatch;

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getStreetMatch() {
    return streetMatch;
  }

  public void setStreetMatch(String streetMatch) {
    this.streetMatch = streetMatch;
  }

  public String getPostalMatch() {
    return postalMatch;
  }

  public void setPostalMatch(String postalMatch) {
    this.postalMatch = postalMatch;
  }
}
