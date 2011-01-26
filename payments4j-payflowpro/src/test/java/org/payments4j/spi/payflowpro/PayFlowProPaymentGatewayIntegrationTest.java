package org.payments4j.spi.payflowpro;

import org.payments4j.core.PaymentGateway;
import org.payments4j.test.spi.AbstractBasePaymentGatewayIntegrationTest;

/**
 *
 */
public class PayFlowProPaymentGatewayIntegrationTest extends AbstractBasePaymentGatewayIntegrationTest {

  @Override
  protected PaymentGateway buildGateway() {
    PayFlowProPaymentGateway gateway = new PayFlowProPaymentGateway(credentials.getProperty("payflowpro.username"),
                                                                    credentials.getProperty("payflowpro.password"));
    gateway.setTest(true);
    return gateway;
  }
}
