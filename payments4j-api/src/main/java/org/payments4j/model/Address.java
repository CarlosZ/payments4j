package org.payments4j.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class Address {

  private String firstName;
  private String lastName;
  private String email;
  private String company;
  private String address1;
  private String address2;
  private String city;
  private String state;
  private String countryIsoCode;
  private String postalCode;
  private String phone;

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCompany() {
    return company;
  }

  public void setCompany(String company) {
    this.company = company;
  }

  public String getAddress1() {
    return address1;
  }

  public void setAddress1(String address1) {
    this.address1 = address1;
  }

  public String getAddress2() {
    return address2;
  }

  public void setAddress2(String address2) {
    this.address2 = address2;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getCountryIsoCode() {
    return countryIsoCode;
  }

  public void setCountryIsoCode(String countryIsoCode) {
    this.countryIsoCode = countryIsoCode;
  }

  public String getPostalCode() {
    return postalCode;
  }

  public void setPostalCode(String postalCode) {
    this.postalCode = postalCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.firstName)
        .append(this.lastName)
        .append(this.email)
        .append(this.company)
        .append(this.address1)
        .append(this.address2)
        .append(this.city)
        .append(this.state)
        .append(this.countryIsoCode)
        .append(this.postalCode)
        .append(this.phone)
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

    Address that = (Address) o;

    return new EqualsBuilder()
        .append(this.firstName, that.firstName)
        .append(this.lastName, that.lastName)
        .append(this.email, that.email)
        .append(this.company, that.company)
        .append(this.address1, that.address1)
        .append(this.address2, that.address2)
        .append(this.city, that.city)
        .append(this.state, that.state)
        .append(this.countryIsoCode, that.countryIsoCode)
        .append(this.postalCode, that.postalCode)
        .append(this.phone, that.phone)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("firstName", this.firstName)
        .append("lastName", this.lastName)
        .append("email", this.email)
        .append("company", this.company)
        .append("address1", this.address1)
        .append("address2", this.address2)
        .append("city", this.city)
        .append("state", this.state)
        .append("countryIsoCode", this.countryIsoCode)
        .append("postalCode", this.postalCode)
        .append("phone", this.phone)
        .toString();
  }
}
