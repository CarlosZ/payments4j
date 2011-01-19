package org.payments4j.model;

import java.math.BigDecimal;
import java.util.Currency;

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

  public BigDecimal getAmount() {
    return amount;
  }

  public Currency getCurrency() {
    return currency;
  }
}
