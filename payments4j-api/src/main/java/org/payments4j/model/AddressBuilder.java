package org.payments4j.model;

public class AddressBuilder extends AbstractBaseModelBuilder<Address> {

  public AddressBuilder() {
    super(Address.class);
  }

  public AddressBuilder withName(String name) {
    values.put("name", name);
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
