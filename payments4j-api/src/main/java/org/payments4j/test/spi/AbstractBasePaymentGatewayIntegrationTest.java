package org.payments4j.test.spi;

import org.junit.Before;
import org.junit.Test;
import org.payments4j.core.PaymentGateway;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;

import java.util.HashMap;
import java.util.Locale;
import java.util.Properties;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
public abstract class AbstractBasePaymentGatewayIntegrationTest {

  private Money money;
  private CreditCard creditCard;
  private PaymentGateway gateway;
  protected Properties credentials;

  @Before
  public void setUp() throws Exception {
    if (credentials == null) {
      credentials = new Properties();
      credentials.load(this.getClass().getResourceAsStream("/credentials.properties"));
    }
    int amount = new Random(System.nanoTime()).nextInt(30);
    money = new MoneyBuilder().withAmount(String.valueOf(amount)).withCurrency(Locale.US).build();
    creditCard = new CreditCardBuilder()
        .withFirstName("Joe")
        .withLastName("Doe")
        .withMonth("11")
        .withYear("13")
        .withNumber("4111111111111111")
        .withSecurityCode("132")
        .withType(MASTER_CARD)
        .build();
    this.gateway = buildGateway();
  }

  protected abstract PaymentGateway buildGateway();

  @Test
  public void testAuthCaptureCredit() throws Exception {
    TransactionResponse authResponse = gateway.authorize(money, creditCard, null);

    assertThat(authResponse.isSuccessful()).isTrue();

    TransactionResponse captureResponse = gateway.capture(money, authResponse.getAuthorizationId(), null);

    assertThat(captureResponse.isSuccessful()).isTrue();

    TransactionResponse creditResponse = gateway.credit(money, captureResponse.getAuthorizationId(), null);

    assertThat(creditResponse.isSuccessful()).isTrue();
  }

  @Test
  public void testPurchaseCredit() throws Exception {
    TransactionResponse purchaseResponse = gateway.purchase(money, creditCard, null);

    assertThat(purchaseResponse.isSuccessful()).isTrue();

    HashMap<String, Object> optionals = new HashMap<String, Object>();
    optionals.put("creditCardNumber", "4111111111111111");
    optionals.put("creditCardMonth", "11");
    optionals.put("creditCardYear", "2013");
    TransactionResponse creditResponse = gateway.credit(money,
                                                        purchaseResponse.getAuthorizationId(),
                                                        optionals);

    assertThat(creditResponse.isSuccessful()).isTrue();
  }

  @Test
  public void testAuthVoid() throws Exception {
    TransactionResponse authResponse = gateway.authorize(money, creditCard, null);

    assertThat(authResponse.isSuccessful()).isTrue();

    TransactionResponse revertResponse = gateway.revert(authResponse.getAuthorizationId(), null);

    assertThat(revertResponse.isSuccessful()).isTrue();
  }
}
