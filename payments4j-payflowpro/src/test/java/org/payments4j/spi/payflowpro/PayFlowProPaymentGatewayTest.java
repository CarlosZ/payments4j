package org.payments4j.spi.payflowpro;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;

import java.io.IOException;
import java.util.Locale;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class PayFlowProPaymentGatewayTest {

  private static final String USERNAME = "LOGIN_ID";
  private static final String PASSWORD = "PASSWORD";

  private PayFlowProPaymentGateway_ForTest gateway;
  private Money money;
  private CreditCard creditCard;
  @Mock
  private HttpPost mockHttpPost;
  @Mock
  private HttpClient mockHttpClient;
  private TransactionResponse transactionResponse;

  @Before
  public void setUp() throws Exception {
    gateway = new PayFlowProPaymentGateway_ForTest(USERNAME, PASSWORD);
    gateway.setTest(true);

    money = new MoneyBuilder().withAmount("10").withCurrency(Locale.US).build();
    creditCard = new CreditCardBuilder()
        .withNumber("4111111111111111")
        .withFirstName("John")
        .withLastName("Doe")
        .withMonth("01")
        .withYear("12")
        .withType(MASTER_CARD)
        .withSecurityCode("123").build();

    transactionResponse = new TransactionResponse();
    transactionResponse.setCode(0);
    transactionResponse.setAuthorizationId("123");
    transactionResponse.setReasonCode(0);
    transactionResponse.setMessage("Verified");

    when(mockHttpClient.execute(mockHttpPost, new PayFlowResponseHandler())).thenReturn(transactionResponse);
  }

  @Test
  public void testPurchase() throws Exception {
    TransactionResponse response = gateway.purchase(money, creditCard, null);

    assertThatParamListEquals(
        "AMT[5]=10.00&CURRENCY[3]=USD&ACCT[16]=4111111111111111&EXPDATE[4]=0112&CVV2[3]=123&FIRSTNAME[4]=John&" +
        "LASTNAME[3]=Doe&TENDER[1]=C&USER[8]=LOGIN_ID&VENDOR[8]=LOGIN_ID&PWD[8]=PASSWORD&PARTNER[6]=PayPal&" +
        "VERBOSITY[6]=MEDIUM&TRXTYPE[1]=S");
    assertThat(response).isEqualTo(transactionResponse);
  }

  @Test
  public void testAuthorize() throws Exception {
    TransactionResponse response = gateway.authorize(money, creditCard, null);

    assertThatParamListEquals(
        "AMT[5]=10.00&CURRENCY[3]=USD&ACCT[16]=4111111111111111&EXPDATE[4]=0112&CVV2[3]=123&FIRSTNAME[4]=John&" +
        "LASTNAME[3]=Doe&TENDER[1]=C&USER[8]=LOGIN_ID&VENDOR[8]=LOGIN_ID&PWD[8]=PASSWORD&PARTNER[6]=PayPal&" +
        "VERBOSITY[6]=MEDIUM&TRXTYPE[1]=A");

    assertThat(response).isEqualTo(transactionResponse);
  }

  @Test
  public void testCapture() throws Exception {
    TransactionResponse response = gateway.capture(money, "SOME_ID", null);

    assertThatParamListEquals(
        "AMT[5]=10.00&CURRENCY[3]=USD&USER[8]=LOGIN_ID&VENDOR[8]=LOGIN_ID&" +
        "PWD[8]=PASSWORD&PARTNER[6]=PayPal&ORIGID[7]=SOME_ID&VERBOSITY[6]=MEDIUM&TRXTYPE[1]=D");

    assertThat(response).isEqualTo(transactionResponse);
  }

  @Test
  public void testRevert() throws Exception {
    TransactionResponse response = gateway.revert("SOME_ID", null);

    assertThatParamListEquals(
        "USER[8]=LOGIN_ID&VENDOR[8]=LOGIN_ID&PWD[8]=PASSWORD&PARTNER[6]=PayPal&" +
        "ORIGID[7]=SOME_ID&VERBOSITY[6]=MEDIUM&TRXTYPE[1]=V");

    assertThat(response).isEqualTo(transactionResponse);
  }

  @Test
  public void testCredit() throws Exception {
    TransactionResponse response = gateway.credit(money, "SOME_ID", null);

    assertThatParamListEquals(
        "USER[8]=LOGIN_ID&VENDOR[8]=LOGIN_ID&PWD[8]=PASSWORD&PARTNER[6]=PayPal&" +
        "ORIGID[7]=SOME_ID&VERBOSITY[6]=MEDIUM&TRXTYPE[1]=C");

    assertThat(response).isEqualTo(transactionResponse);
  }

  private void assertThatParamListEquals(String paramsList) throws IOException {
    ArgumentCaptor<HttpEntity> entityArgumentCaptor = ArgumentCaptor.forClass(HttpEntity.class);
    verify(mockHttpPost).setEntity(entityArgumentCaptor.capture());
    HttpEntity entity = entityArgumentCaptor.getValue();
    assertThat(EntityUtils.toString(entity)).isEqualTo(paramsList);
  }

  private class PayFlowProPaymentGateway_ForTest extends PayFlowProPaymentGateway {

    public PayFlowProPaymentGateway_ForTest(String username, String password) {
      super(username, password);
    }

    @Override
    HttpPost buildHttpPost(String url) {
      assertThat(url).isEqualTo(TEST_HOST_ADDRESS);
      return mockHttpPost;
    }

    @Override
    HttpClient buildHttpClient() {
      return mockHttpClient;
    }
  }
}
