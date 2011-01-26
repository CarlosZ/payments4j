package org.payments4j.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.amount)
        .append(this.currency)
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

    Money that = (Money) o;

    return new EqualsBuilder()
        .append(this.amount, that.amount)
        .append(this.currency, that.currency)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("amount", this.amount)
        .append("currency", this.currency)
        .toString();
  }
}
