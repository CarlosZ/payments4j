package org.payments4j.spi.authorizenet;

import org.junit.Ignore;
import org.junit.Test;
import org.payments4j.core.PaymentGateway;
import org.payments4j.test.spi.AbstractBasePaymentGatewayIntegrationTest;

import java.util.HashMap;
import java.util.Map;

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
  public void testAuthRevert() throws Exception {
    super.testAuthRevert();
  }

  @Override
  @Test
  @Ignore("Because Authorize.net doesn't send back real tranId when the account is in test mode. " +
          "Enable it when you can to test against a live account.")
  public void testAuthCaptureCredit() throws Exception {
    super.testAuthCaptureCredit();
  }

  @Override
  protected Map<String, Object> getCreditOptions() {
    Map<String, Object> options = new HashMap<String, Object>();
    options.put("creditCardNumber", "4111111111111111");
    options.put("creditCardMonth", "11");
    options.put("creditCardYear", "2013");
    return options;
  }
}
