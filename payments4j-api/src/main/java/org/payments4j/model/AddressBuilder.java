package org.payments4j.model;

public class AddressBuilder extends AbstractBaseModelBuilder<Address> {

  public AddressBuilder() {
    super(Address.class);
  }

  public AddressBuilder withFirstName(String firstName) {
    values.put("firstName", firstName);
    return this;
  }

  public AddressBuilder withLastName(String lastName) {
    values.put("lastName", lastName);
    return this;
  }

  public AddressBuilder withEmail(String email) {
    values.put("email", email);
    return this;
  }

  public AddressBuilder withCompany(String company) {
    values.put("company", company);
    return this;
  }

  public AddressBuilder withAddress1(String address1) {
    values.put("address1", address1);
    return this;
  }

  public AddressBuilder withAddress2(String address2) {
    values.put("address2", address2);
    return this;
  }

  public AddressBuilder withCity(String city) {
    values.put("city", city);
    return this;
  }

  public AddressBuilder withState(String state) {
    values.put("state", state);
    return this;
  }

  public AddressBuilder withCountryIsoCode(String countryIsoCode) {
    values.put("countryIsoCode", countryIsoCode);
    return this;
  }

  public AddressBuilder withPostalCode(String postalCode) {
    values.put("postalCode", postalCode);
    return this;
  }

  public AddressBuilder withPhone(String phone) {
    values.put("phone", phone);
    return this;
  }
}
