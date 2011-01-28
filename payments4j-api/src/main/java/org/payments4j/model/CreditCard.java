package org.payments4j.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 */
public class CreditCard {

  public enum Type {
    VISA, MASTER_CARD, AMERICAN_EXPRESS, DISCOVER, JCP, CARTE_BLANCHE, DINERS_CLUB, EN_ROUTE, LASER, MAESTRO, SOLO, SWITCH
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
    checkArgument(Integer.valueOf(month) >= 1 && Integer.valueOf(month) <= 12,
                  "The expiration month must be between 1 and 12.");
    this.month = month;
  }

  public String getYear() {
    return year;
  }

  public void setYear(String year) {
    checkArgument(Integer.valueOf(year) >= 1950 && Integer.valueOf(year) <= 3000,
                  "The expiration year must be between 1950 and 3000.");
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
    checkArgument(securityCode.length() >= 2 && securityCode.length() <= 4,
                  "The security code length must be between 2 and 4 characters.");
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.firstName)
        .append(this.lastName)
        .append(this.number)
        .append(this.month)
        .append(this.year)
        .append(this.type)
        .append(this.securityCode)
        .toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    CreditCard that = (CreditCard) o;

    return new EqualsBuilder()
        .append(this.firstName, that.firstName)
        .append(this.lastName, that.lastName)
        .append(this.number, that.number)
        .append(this.month, that.month)
        .append(this.year, that.year)
        .append(this.type, that.type)
        .append(this.securityCode, that.securityCode).isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("firstName", this.firstName)
        .append("lastName", this.lastName)
        .append("number", this.number)
        .append("month", this.month)
        .append("year", this.year)
        .append("type", this.type)
        .append("securityCode", this.securityCode)
        .toString();
  }
}
