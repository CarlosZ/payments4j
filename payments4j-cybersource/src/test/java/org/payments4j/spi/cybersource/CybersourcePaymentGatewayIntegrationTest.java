package org.payments4j.spi.cybersource;

import org.junit.Before;
import org.payments4j.core.PaymentGateway;
import org.payments4j.model.AddressBuilder;
import org.payments4j.model.CreditCard;
import org.payments4j.model.Order;
import org.payments4j.model.OrderBuilder;
import org.payments4j.test.spi.AbstractBasePaymentGatewayIntegrationTest;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class CybersourcePaymentGatewayIntegrationTest extends AbstractBasePaymentGatewayIntegrationTest {

  private Order order;

  @Override
  protected PaymentGateway buildGateway() {
    CybersourcePaymentGateway gateway = new CybersourcePaymentGateway(credentials.getProperty("cybersource.username"),
                                                                      credentials.getProperty(
                                                                          "cybersource.transactionKey"));
    gateway.setTest(true);
    return gateway;
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    order = new OrderBuilder()
        .withBillingAddress(new AddressBuilder()
                                .withFirstName("John")
                                .withLastName("Doe")
                                .withEmail("some@email.com")
                                .withAddress1("b_street 1")
                                .withAddress2("b_street 2")
                                .withCity("b_city")
                                .withPostalCode("10000")
                                .withState("DE")
                                .withCountryIsoCode("US")
                                .build())
        .withShippingAddress(new AddressBuilder()
                                 .withFirstName("John")
                                 .withLastName("Doe")
                                 .withEmail("some@email.com")
                                 .withAddress1("s_street 1")
                                 .withAddress2("s_street 2")
                                 .withCity("s_city")
                                 .withPostalCode("10000")
                                 .withState("DE")
                                 .withCountryIsoCode("US")
                                 .build())
        .build();
  }

  @Override
  protected Map<String, Object> getAuthOptions() {
    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("referenceNumber", "123");
    options.put("order", order);
    return options;
  }

  @Override
  protected Map<String, Object> getCaptureOptions() {
    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("referenceNumber", "456");
    return options;
  }

  @Override
  protected Map<String, Object> getPurchaseOptions() {
    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("referenceNumber", "789");
    options.put("order", order);
    return options;
  }

  @Override
  protected Map<String, Object> getCreditOptions() {
    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("referenceNumber", "159");
    return options;
  }

  @Override
  protected Map<String, Object> getRevertOptions() {
    HashMap<String, Object> options = new HashMap<String, Object>();
    options.put("referenceNumber", "753");
    options.put("money", money);
    return options;
  }

  @Override
  protected CreditCard getCreditCard() {
    CreditCard creditCard = super.getCreditCard();
    creditCard.setNumber("5555555555554444");
    return creditCard;
  }
}
