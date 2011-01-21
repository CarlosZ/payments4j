package org.payments4j.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class MoneyBuilder extends AbstractBaseModelBuilder<Money> {

  public MoneyBuilder() {
    super(Money.class);
  }

  public MoneyBuilder withAmount(String amount) {
    values.put("amount", new BigDecimal(amount));
    return this;
  }

  public MoneyBuilder withAmount(BigDecimal amount) {
    values.put("amount", amount);
    return this;
  }

  public MoneyBuilder withCurrency(Currency currency) {
    values.put("currency", currency);
    return this;
  }

  public MoneyBuilder withCurrency(Locale locale) {
    values.put("currency", Currency.getInstance(locale));
    return this;
  }

  @Override
  public Money build() {
    BigDecimal amount;
    Object amountObj = values.get("amount");
    if (amountObj == null) {
      throw new IllegalArgumentException("You must specify an amount value for the Money object.");
    }
    if (amountObj instanceof String) {
      amount = new BigDecimal((String) amountObj);
    } else {
      amount = (BigDecimal) amountObj;
    }

    Currency currency;
    Object currencyObj = values.get("currency");
    if (currencyObj == null) {
      throw new IllegalArgumentException("You must specify a currency for the Money object.");
    }
    if (currencyObj instanceof Locale) {
      currency = Currency.getInstance((Locale) currencyObj);
    } else {
      currency = (Currency) currencyObj;
    }

    return new Money(amount, currency);
  }
}
