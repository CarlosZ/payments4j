package org.payments4j.spi.authorizenet;

import org.payments4j.core.PaymentGateway;
import org.payments4j.test.spi.AbstractBasePaymentGatewayIntegrationTest;

/**
 *
 */
public class AuthorizeNetPaymentGatewayIntegrationTest extends AbstractBasePaymentGatewayIntegrationTest {

  protected PaymentGateway buildGateway() {
    PaymentGateway gateway = new AuthorizeNetPaymentGateway(credentials.getProperty("authorizenet.apiLoginId"),
                                                            credentials.getProperty("authorizenet.transactionKey"));
    gateway.setTest(true);
    return gateway;
  }
}
