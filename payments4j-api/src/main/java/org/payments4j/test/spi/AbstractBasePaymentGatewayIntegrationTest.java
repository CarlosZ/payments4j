package org.payments4j.test.spi;

import org.junit.Before;
import org.junit.Test;
import org.payments4j.core.PaymentGateway;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import static org.fest.assertions.Assertions.assertThat;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
public abstract class AbstractBasePaymentGatewayIntegrationTest {

  protected Money money;
  protected CreditCard creditCard;
  protected PaymentGateway gateway;
  protected Properties credentials;

  @Before
  public void setUp() throws Exception {
    if (credentials == null) {
      credentials = new Properties();
      credentials.load(this.getClass().getResourceAsStream("/credentials.properties"));
    }
    int amount = new Random(System.nanoTime()).nextInt(30);
    money = new MoneyBuilder().withAmount(String.valueOf(amount)).withCurrency(Locale.US).build();
    creditCard = getCreditCard();
    this.gateway = buildGateway();
  }

  protected abstract PaymentGateway buildGateway();

  protected CreditCard getCreditCard() {
    return new CreditCardBuilder()
        .withFirstName("Joe")
        .withLastName("Doe")
        .withMonth("11")
        .withYear("2013")
        .withNumber("4111111111111111")
        .withSecurityCode("132")
        .withType(MASTER_CARD)
        .build();
  }

  protected Map<String, Object> getCreditOptions() {
    return null;
  }

  protected Map<String, Object> getCaptureOptions() {
    return null;
  }

  protected Map<String, Object> getAuthOptions() {
    return null;
  }

  protected Map<String, Object> getPurchaseOptions() {
    return null;
  }

  protected Map<String, Object> getRevertOptions() {
    return null;
  }

  @Test
  public void testAuthCaptureCredit() throws Exception {
    TransactionResponse authResponse = gateway.authorize(money, creditCard, getAuthOptions());

    assertThat(authResponse.isSuccessful()).as("TransactionResponse = " + authResponse).isTrue();

    TransactionResponse captureResponse = gateway.capture(money, authResponse.getAuthorizationId(), getCaptureOptions());

    assertThat(captureResponse.isSuccessful()).as("TransactionResponse = " + captureResponse).isTrue();

    TransactionResponse creditResponse = gateway.credit(money, captureResponse.getAuthorizationId(), getCreditOptions());

    assertThat(creditResponse.isSuccessful()).as("TransactionResponse = " + creditResponse).isTrue();
  }

  @Test
  public void testPurchaseCredit() throws Exception {
    TransactionResponse purchaseResponse = gateway.purchase(money, creditCard, getPurchaseOptions());

    assertThat(purchaseResponse.isSuccessful()).as("TransactionResponse = " + purchaseResponse).isTrue();


    TransactionResponse creditResponse = gateway.credit(money,
                                                        purchaseResponse.getAuthorizationId(),
                                                        getCreditOptions());

    assertThat(creditResponse.isSuccessful()).as("TransactionResponse = " + creditResponse).isTrue();
  }

  @Test
  public void testAuthRevert() throws Exception {
    TransactionResponse authResponse = gateway.authorize(money, creditCard, getAuthOptions());

    assertThat(authResponse.isSuccessful()).as("TransactionResponse = " + authResponse).isTrue();

    TransactionResponse revertResponse = gateway.revert(authResponse.getAuthorizationId(), getRevertOptions());

    assertThat(revertResponse.isSuccessful()).as("TransactionResponse = " + revertResponse).isTrue();
  }
}
