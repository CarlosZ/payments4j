package org.payments4j.spi.payflowpro.converter;

import org.payments4j.model.CreditCard;
import org.payments4j.spi.payflowpro.Pair;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 *
 */
public class CreditCardConverter extends AbstractBaseConverter {

  private CreditCard creditCard;

  public CreditCardConverter(CreditCard creditCard) {
    this.creditCard = creditCard;
  }

  public List<Pair> toParamsList() {
    List<Pair> params = new ArrayList<Pair>();
    addPair(params, "ACCT", creditCard.getNumber());
    addPair(params, "EXPDATE", format("%s%s", creditCard.getMonth(), creditCard.getYear().substring(2)));
    addPair(params, "CVV2", creditCard.getSecurityCode());
    addPair(params, "FIRSTNAME", creditCard.getFirstName());
    addPair(params, "LASTNAME", creditCard.getLastName());
    addPair(params, "TENDER", "C");
    return params;
  }
}
