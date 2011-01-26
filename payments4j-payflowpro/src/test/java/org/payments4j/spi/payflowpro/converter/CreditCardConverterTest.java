package org.payments4j.spi.payflowpro.converter;

import org.payments4j.model.CreditCard;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.spi.payflowpro.Pair;

import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
public class CreditCardConverterTest extends AbstractBaseConverterTest {

  @Override
  protected Pair[] expectedParams() {
    return new Pair[]{
        new Pair("ACCT", "4111111111111111"),
        new Pair("FIRSTNAME", "John"),
        new Pair("LASTNAME", "Doe"),
        new Pair("EXPDATE", "0112"),
        new Pair("CVV2", "123"),
        new Pair("TENDER", "C")
    };
  }

  @Override
  protected AbstractBaseConverter buildConverter() {
    CreditCard creditCard = new CreditCardBuilder()
        .withNumber("4111111111111111")
        .withFirstName("John")
        .withLastName("Doe")
        .withMonth("01")
        .withYear("12")
        .withType(MASTER_CARD)
        .withSecurityCode("123").build();
    return new CreditCardConverter(creditCard);
  }
}
