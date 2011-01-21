package org.payments4j.model;

public class CreditCardBuilder extends AbstractBaseModelBuilder<CreditCard> {

  public CreditCardBuilder() {
    super(CreditCard.class);
  }

  public CreditCardBuilder withFirstName(String firstName) {
    values.put("firstName", firstName);
    return this;
  }

  public CreditCardBuilder withLastName(String lastName) {
    values.put("lastName", lastName);
    return this;
  }

  public CreditCardBuilder withNumber(String number) {
    values.put("number", number);
    return this;
  }

  public CreditCardBuilder withMonth(String month) {
    values.put("month", month);
    return this;
  }

  public CreditCardBuilder withYear(String year) {
    values.put("year", year);
    return this;
  }

  public CreditCardBuilder withType(CreditCard.Type type) {
    values.put("type", type);
    return this;
  }

  public CreditCardBuilder withSecurityCode(String securityCode) {
    values.put("securityCode", securityCode);
    return this;
  }
}
