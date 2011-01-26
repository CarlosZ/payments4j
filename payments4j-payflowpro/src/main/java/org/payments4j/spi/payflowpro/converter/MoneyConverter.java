package org.payments4j.spi.payflowpro.converter;

import org.payments4j.model.Money;
import org.payments4j.spi.payflowpro.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 *
 */
public class MoneyConverter extends AbstractBaseConverter {

  private Money money;

  public MoneyConverter(Money money) {
    this.money = money;
  }

  public List<Pair> toParamsList() {
    List<Pair> params = new ArrayList<Pair>();
    addPair(params, "AMT", format("%1$#.2f", money.getAmount()));
    addPair(params, "CURRENCY", money.getCurrency().getCurrencyCode());
    return params;
  }
}
