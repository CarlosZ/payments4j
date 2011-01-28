package org.payments4j.spi.cybersource;

import com.cybersource.schemas.transaction_data.transactionprocessor.ITransactionProcessor;
import com.cybersource.schemas.transaction_data_1.BillTo;
import com.cybersource.schemas.transaction_data_1.Card;
import com.cybersource.schemas.transaction_data_1.PurchaseTotals;
import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.cybersource.schemas.transaction_data_1.RequestMessage;
import com.cybersource.schemas.transaction_data_1.ShipTo;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.AddressBuilder;
import org.payments4j.model.CreditCard;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;
import org.payments4j.model.Order;
import org.payments4j.model.OrderBuilder;

import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.apache.ws.security.WSConstants.PW_TEXT;
import static org.apache.ws.security.handler.WSHandlerConstants.ACTION;
import static org.apache.ws.security.handler.WSHandlerConstants.PASSWORD_TYPE;
import static org.apache.ws.security.handler.WSHandlerConstants.PW_CALLBACK_REF;
import static org.apache.ws.security.handler.WSHandlerConstants.USER;
import static org.apache.ws.security.handler.WSHandlerConstants.USERNAME_TOKEN;
import static org.fest.assertions.Assertions.assertThat;
import static org.fest.assertions.MapAssert.entry;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class CybersourcePaymentGatewayTest {

  private static final String USERNAME = "some_username";
  private static final String KEY = "some_key";
  private static final String AUTHORIZATION_ID = "some_auth_id";

  private CybersourcePaymentGateway_ForTest gateway;
  private Money money;
  private CreditCard creditCard;
  @Mock
  private Endpoint mockEndpoint;
  @Mock
  private ITransactionProcessor mockTransactionProcessor;
  private List<Interceptor<? extends Message>> outInterceptors;
  private HashMap<String, Object> options;

  @Before
  public void setUp() throws Exception {
    gateway = new CybersourcePaymentGateway_ForTest(USERNAME, KEY);
    gateway.setTest(true);

    money = new MoneyBuilder().withAmount("10").withCurrency(Locale.US).build();
    creditCard = new CreditCardBuilder()
        .withNumber("4111111111111111")
        .withFirstName("John")
        .withLastName("Doe")
        .withMonth("01")
        .withYear("2012")
        .withType(MASTER_CARD)
        .withSecurityCode("123").build();
    Order order = new OrderBuilder()
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

    options = new HashMap<String, Object>();
    options.put("referenceNumber", "123");
    options.put("order", order);
    options.put("money", money);

    TransactionResponse transactionResponse = new TransactionResponse();
    transactionResponse.setCode(100);
    transactionResponse.setAuthorizationId("123");
    transactionResponse.setReasonCode(100);
    transactionResponse.setMessage("Verified");
    transactionResponse.setSuccessful(true);

    outInterceptors = new ArrayList<Interceptor<? extends Message>>();

    ReplyMessage replyMessage = new ReplyMessage();
    replyMessage.setDecision("Verified");
    replyMessage.setReasonCode(new BigInteger("100"));
    replyMessage.setRequestID("123");

    when(mockTransactionProcessor.runTransaction(isA(RequestMessage.class))).thenReturn(replyMessage);
    when(mockEndpoint.getOutInterceptors()).thenReturn(outInterceptors);
  }

  @After
  public void tearDown() throws Exception {
    assertThatSecurityValuesAreSet();
  }

  @Test
  public void testPurchase() throws Exception {

    gateway.purchase(money, creditCard, options);

    RequestMessage requestMessage = captureRequestMessage();
    assertThat(requestMessage.getCcAuthService().getRun()).isEqualTo("true");
    assertThat(requestMessage.getCcCaptureService().getRun()).isEqualTo("true");
    assertThatCardEquals(requestMessage.getCard());
    assertThatPurchaseTotalsEquals(requestMessage.getPurchaseTotals());
  }

  @Test
  public void testAuthorize() throws Exception {

    gateway.authorize(money, creditCard, options);

    RequestMessage requestMessage = captureRequestMessage();
    assertThat(requestMessage.getCcAuthService().getRun()).isEqualTo("true");
    assertThatCardEquals(requestMessage.getCard());
    assertThatPurchaseTotalsEquals(requestMessage.getPurchaseTotals());
    assertThatBillingAddressEquals(requestMessage.getBillTo());
    assertThatShippingAddressEquals(requestMessage.getShipTo());
  }

  @Test
  public void testCapture() throws Exception {

    gateway.capture(money, AUTHORIZATION_ID, options);

    RequestMessage requestMessage = captureRequestMessage();
    assertThat(requestMessage.getCcCaptureService().getRun()).isEqualTo("true");
    assertThatPurchaseTotalsEquals(requestMessage.getPurchaseTotals());
  }

  @Test
  public void testRevert() throws Exception {

    gateway.revert(AUTHORIZATION_ID, options);

    RequestMessage requestMessage = captureRequestMessage();
    assertThat(requestMessage.getCcAuthReversalService().getRun()).isEqualTo("true");
    assertThat(requestMessage.getCcAuthReversalService().getAuthRequestID()).isEqualTo(AUTHORIZATION_ID);
    assertThatPurchaseTotalsEquals(requestMessage.getPurchaseTotals());
  }

  @Test
  public void testCredit() throws Exception {

    gateway.credit(money, AUTHORIZATION_ID, options);

    RequestMessage requestMessage = captureRequestMessage();
    assertThat(requestMessage.getCcCreditService().getRun()).isEqualTo("true");
    assertThat(requestMessage.getCcCreditService().getCaptureRequestID()).isEqualTo(AUTHORIZATION_ID);
    assertThatPurchaseTotalsEquals(requestMessage.getPurchaseTotals());
  }

  private void assertThatBillingAddressEquals(BillTo billTo) {
    assertThat(billTo.getFirstName()).isEqualTo("John");
    assertThat(billTo.getLastName()).isEqualTo("Doe");
    assertThat(billTo.getEmail()).isEqualTo("some@email.com");
    assertThat(billTo.getStreet1()).isEqualTo("b_street 1");
    assertThat(billTo.getStreet2()).isEqualTo("b_street 2");
    assertThat(billTo.getCity()).isEqualTo("b_city");
    assertThat(billTo.getPostalCode()).isEqualTo("10000");
    assertThat(billTo.getState()).isEqualTo("DE");
    assertThat(billTo.getCountry()).isEqualTo("US");
  }


  private void assertThatShippingAddressEquals(ShipTo shipTo) {
    assertThat(shipTo.getFirstName()).isEqualTo("John");
    assertThat(shipTo.getLastName()).isEqualTo("Doe");
    assertThat(shipTo.getEmail()).isEqualTo("some@email.com");
    assertThat(shipTo.getStreet1()).isEqualTo("s_street 1");
    assertThat(shipTo.getStreet2()).isEqualTo("s_street 2");
    assertThat(shipTo.getCity()).isEqualTo("s_city");
    assertThat(shipTo.getPostalCode()).isEqualTo("10000");
    assertThat(shipTo.getState()).isEqualTo("DE");
    assertThat(shipTo.getCountry()).isEqualTo("US");
  }

  private void assertThatSecurityValuesAreSet() {
    Map<String, Object> interceptorProperties = ((WSS4JOutInterceptor) outInterceptors.get(0)).getProperties();
    assertThat(interceptorProperties).includes(
        entry(ACTION, USERNAME_TOKEN),
        entry(USER, USERNAME),
        entry(PASSWORD_TYPE, PW_TEXT));
    assertThat(interceptorProperties.get(PW_CALLBACK_REF)).isInstanceOf(ClientPasswordHandler.class);
  }

  private RequestMessage captureRequestMessage() {
    ArgumentCaptor<RequestMessage> captor = ArgumentCaptor.forClass(RequestMessage.class);
    verify(mockTransactionProcessor).runTransaction(captor.capture());
    return captor.getValue();
  }

  private void assertThatPurchaseTotalsEquals(PurchaseTotals purchaseTotals) {
    assertThat(purchaseTotals.getCurrency()).isEqualTo("USD");
    assertThat(purchaseTotals.getGrandTotalAmount()).isEqualTo("10.00");
  }

  private void assertThatCardEquals(Card card) {
    assertThat(card.getAccountNumber()).isEqualTo("4111111111111111");
    assertThat(card.getAccountNumber()).isEqualTo("4111111111111111");
    assertThat(card.getFullName()).isEqualTo("John Doe");
    assertThat(card.getExpirationMonth()).isEqualTo(new BigInteger("01"));
    assertThat(card.getExpirationYear()).isEqualTo(new BigInteger("2012"));
    assertThat(card.getCvNumber()).isEqualTo("123");
    assertThat(card.getCardType()).isEqualTo("002");
  }

  private class CybersourcePaymentGateway_ForTest extends CybersourcePaymentGateway {

    public CybersourcePaymentGateway_ForTest(String username, String transactionKey) {
      super(username, transactionKey);
    }

    @Override
    ITransactionProcessor buildProcessor(URL wsdlLocation) {
      assertThat(wsdlLocation).isEqualTo(TEST_HOSTNAME);
      return mockTransactionProcessor;
    }

    @Override
    Endpoint buildEndpoint(ITransactionProcessor processor) {
      return mockEndpoint;
    }
  }
}
