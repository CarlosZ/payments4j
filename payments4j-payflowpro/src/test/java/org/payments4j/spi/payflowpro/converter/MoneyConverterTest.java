package org.payments4j.spi.payflowpro.converter;

import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;
import org.payments4j.spi.payflowpro.Pair;

import java.util.Locale;

/**
 *
 */
public class MoneyConverterTest extends AbstractBaseConverterTest {

  @Override
  protected AbstractBaseConverter buildConverter() {
    Money money = new MoneyBuilder().withAmount("10").withCurrency(Locale.US).build();
    return new MoneyConverter(money);
  }

  protected Pair[] expectedParams() {
    return new Pair[] {
        new Pair("AMT", "10.00"),
        new Pair("CURRENCY", "USD"),
    };
  }
}
