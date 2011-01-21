package org.payments4j.model;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

/**
 *
 */
public class Money {

  private BigDecimal amount;
  private Currency currency;

  public Money(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public Money(String amount, Locale locale) {
    this(new BigDecimal(amount), Currency.getInstance(locale));
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }
}
