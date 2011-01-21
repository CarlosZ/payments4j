package org.payments4j.model;

/**
 *
 */
public class CreditCard {

  public enum Type {
    VISA, MASTER_CARD, AMERICAN_EXPRESS, DISCOVER, JCP, CARTE_BLANCHE
  }

  private String firstName;
  private String lastName;
  private String number;
  private String month;
  private String year;
  private Type type;
  private String securityCode;

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getNumber() {
    return number;
  }

  public void setNumber(String number) {
    this.number = number;
  }

  public String getMonth() {
    return month;
  }

  public void setMonth(String month) {
    this.month = month;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    this.year = year;
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public String getSecurityCode() {
    return securityCode;
  }

  public void setSecurityCode(String securityCode) {
    this.securityCode = securityCode;
  }

  public boolean isValid() {
    return false;
  }

  public boolean isExpired() {
    return true;
  }

  public String getDisplayNumber() {
    return "";
  }

  public String getLastDigits() {
    return "";
  }
}
