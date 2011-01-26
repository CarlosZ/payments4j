package org.payments4j.spi.authorizenet;

import org.junit.Ignore;
import org.junit.Test;
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

  @Override
  @Test
  @Ignore("Because Authorize.net doesn't send back real tranId when the account is in test mode. " +
          "Enable it when you can to test against a live account.")
  public void testAuthVoid() throws Exception {
    super.testAuthVoid();
  }

  @Override
  @Test
  @Ignore("Because Authorize.net doesn't send back real tranId when the account is in test mode. " +
          "Enable it when you can to test against a live account.")
  public void testAuthCaptureCredit() throws Exception {
    super.testAuthCaptureCredit();
  }
}
