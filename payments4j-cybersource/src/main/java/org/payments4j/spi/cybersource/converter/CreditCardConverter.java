package org.payments4j.spi.cybersource.converter;

import com.cybersource.schemas.transaction_data_1.Card;
import com.google.common.collect.ImmutableMap;
import org.payments4j.model.CreditCard;

import java.math.BigInteger;
import java.util.Map;

import static org.payments4j.model.CreditCard.Type.AMERICAN_EXPRESS;
import static org.payments4j.model.CreditCard.Type.CARTE_BLANCHE;
import static org.payments4j.model.CreditCard.Type.DINERS_CLUB;
import static org.payments4j.model.CreditCard.Type.DISCOVER;
import static org.payments4j.model.CreditCard.Type.EN_ROUTE;
import static org.payments4j.model.CreditCard.Type.JCP;
import static org.payments4j.model.CreditCard.Type.LASER;
import static org.payments4j.model.CreditCard.Type.MAESTRO;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;
import static org.payments4j.model.CreditCard.Type.SOLO;
import static org.payments4j.model.CreditCard.Type.VISA;

/**
 *
 */
public class CreditCardConverter {

  private static final Map<CreditCard.Type, String> CARD_TYPE_TO_CODE =
      new ImmutableMap.Builder<CreditCard.Type, String>()
          .put(VISA, "001")
          .put(MASTER_CARD, "002")
          .put(AMERICAN_EXPRESS, "003")
          .put(DISCOVER, "004")
          .put(DINERS_CLUB, "005")
          .put(CARTE_BLANCHE, "006")
          .put(JCP, "007")
          .put(EN_ROUTE, "014")
          .put(MAESTRO, "024")
          .put(SOLO, "032")
          .put(LASER, "035")
          .build();

  private CreditCard creditCard;

  public CreditCardConverter(CreditCard creditCard) {
    this.creditCard = creditCard;
  }

  public Card toCard() {
    Card card = new Card();
    card.setAccountNumber(creditCard.getNumber());
    card.setFullName(creditCard.getFirstName() + " " + creditCard.getLastName());
    card.setCardType(CARD_TYPE_TO_CODE.get(creditCard.getType()));
    card.setExpirationMonth(new BigInteger(creditCard.getMonth()));
    card.setExpirationYear(new BigInteger(creditCard.getYear()));
    card.setCvNumber(creditCard.getSecurityCode());
    return card;
  }
}
