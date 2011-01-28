package org.payments4j.spi.authorizenet;

import net.authorize.Merchant;
import net.authorize.ResponseField;
import net.authorize.Result;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CreditCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCardBuilder;
import org.payments4j.model.Money;
import org.payments4j.model.MoneyBuilder;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static net.authorize.TransactionType.*;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.payments4j.model.CreditCard.Type.MASTER_CARD;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthorizeNetPaymentGatewayTest {

  private static final String LOGIN_ID = "LOGIN_ID";
  private static final String TRANSACTION_KEY = "TRANSACTION_KEY";

  @Mock
  private Merchant mockMerchant;
  private AuthorizeNetPaymentGateway_ForTest gateway;
  private Money money;
  private org.payments4j.model.CreditCard creditCard;

  @Before
  public void setUp() throws Exception {
    gateway = new AuthorizeNetPaymentGateway_ForTest(LOGIN_ID, TRANSACTION_KEY);
    money = new MoneyBuilder().withAmount("10").withCurrency(Locale.US).build();
    creditCard = new CreditCardBuilder()
        .withNumber("4111111111111111")
        .withFirstName("John")
        .withLastName("Doe")
        .withMonth("01")
        .withYear("2012")
        .withType(MASTER_CARD)
        .withSecurityCode("123").build();
  }

  @Test
  public void testPurchase() throws Exception {
    Transaction mockTransaction = setUpTransactionMock(AUTH_CAPTURE, new BigDecimal("10"));

    TransactionResponse transactionResponse = gateway.purchase(money, creditCard, null);

    verifyTransactionResult(mockTransaction, transactionResponse);
  }

  @Test
  public void testAuthorize() throws Exception {
    Transaction mockTransaction = setUpTransactionMock(AUTH_ONLY, new BigDecimal("10"));

    TransactionResponse transactionResponse = gateway.authorize(money, creditCard, null);

    verifyTransactionResult(mockTransaction, transactionResponse);
  }

  @Test
  public void testCapture() throws Exception {
    Transaction mockTransaction = setUpTransactionMock(PRIOR_AUTH_CAPTURE, new BigDecimal("10"));

    TransactionResponse transactionResponse = gateway.capture(money, "SOME_AUTH_ID", null);

    verifyTransactionResultWithTransactionId(mockTransaction, transactionResponse);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testRevert() throws Exception {
    Transaction mockTransaction = setUpTransactionMock(VOID, null);

    TransactionResponse transactionResponse = gateway.revert("SOME_AUTH_ID", null);

    verifyTransactionResultWithTransactionId(mockTransaction, transactionResponse);
  }

  @Test
  public void testCredit() throws Exception {
    Transaction mockTransaction = setUpTransactionMock(CREDIT, new BigDecimal("10"));
    HashMap<String, Object> optionals = new HashMap<String, Object>();
    optionals.put("creditCardNumber", "4111111111111111");
    optionals.put("creditCardMonth", "11");
    optionals.put("creditCardYear", "2010");

    TransactionResponse transactionResponse = gateway.credit(money, "SOME_AUTH_ID", optionals);

    assertThat(gateway.buildMerchant_wasCalled).isTrue();
    verify(mockTransaction).setTransactionId("SOME_AUTH_ID");
    CreditCard value = captureCreditCard(mockTransaction);
    assertThat(value.getCreditCardNumber()).isEqualTo("4111111111111111");
    assertThat(value.getExpirationMonth()).isEqualTo("11");
    assertThat(value.getExpirationYear()).isEqualTo("2010");
    verifyTransactionResponse(transactionResponse, "01", "some reason", "01", "2222");
  }

  private void verifyTransactionResultWithTransactionId(Transaction mockTransaction,
                                                        TransactionResponse transactionResponse) {
    assertThat(gateway.buildMerchant_wasCalled).isTrue();
    verify(mockTransaction).setTransactionId("SOME_AUTH_ID");
    verifyTransactionResponse(transactionResponse, "01", "some reason", "01", "2222");
  }

  private void verifyTransactionResult(Transaction mockTransaction, TransactionResponse transactionResponse) {
    assertThat(gateway.buildMerchant_wasCalled).isTrue();
    CreditCard value = captureCreditCard(mockTransaction);
    assertThatCreditCardEquals(value, creditCard);
    verifyTransactionResponse(transactionResponse, "01", "some reason", "01", "2222");
  }

  @SuppressWarnings("unchecked")
  private Transaction setUpTransactionMock(TransactionType transactionType, BigDecimal amount) {
    Transaction mockTransaction = mock(Transaction.class);
    when(mockMerchant.createAIMTransaction(transactionType, amount)).thenReturn(mockTransaction);
    Result<Transaction> mockResult = buildMockResult("01", "some reason", "01", "2222");
    when(mockMerchant.postTransaction(mockTransaction)).thenReturn((Result) mockResult);
    return mockTransaction;
  }

  private CreditCard captureCreditCard(Transaction mockTransaction) {
    ArgumentCaptor<CreditCard> captor = ArgumentCaptor.forClass(CreditCard.class);
    verify(mockTransaction).setCreditCard(captor.capture());
    return captor.getValue();
  }

  private void verifyTransactionResponse(TransactionResponse transactionResponse,
                                         String code,
                                         String message,
                                         String reasonCode, String authId) {
    assertThat(transactionResponse.getCode()).isEqualTo(Long.valueOf(code));
    assertThat(transactionResponse.getMessage()).isEqualTo(message);
    assertThat(transactionResponse.getReasonCode()).isEqualTo(Long.valueOf(reasonCode));
    assertThat(transactionResponse.getAuthorizationId()).isEqualTo(authId);
    assertThat(transactionResponse.isSuccessful()).isTrue();
  }

  private Result<Transaction> buildMockResult(String code, String message, String reasonCode, String authId) {
    @SuppressWarnings("unchecked")
    Result<Transaction> mockResult = mock(Result.class);
    Transaction mockResultTransaction = mock(Transaction.class);
    Map<ResponseField, String> responseMap = new HashMap<ResponseField, String>();
    responseMap.put(ResponseField.RESPONSE_CODE, code);
    responseMap.put(ResponseField.RESPONSE_REASON_TEXT, message);
    responseMap.put(ResponseField.RESPONSE_REASON_CODE, reasonCode);
    responseMap.put(ResponseField.AUTHORIZATION_CODE, authId);
    when(mockResultTransaction.getResponseMap()).thenReturn(responseMap);
    when(mockResult.getTarget()).thenReturn(mockResultTransaction);
    return mockResult;
  }

  private void assertThatCreditCardEquals(CreditCard value, org.payments4j.model.CreditCard creditCard) {
    assertThat(value.getCreditCardNumber()).isEqualTo(creditCard.getNumber());
    assertThat(value.getExpirationYear()).isEqualTo(creditCard.getYear());
    assertThat(value.getExpirationMonth()).isEqualTo(creditCard.getMonth());
    assertThat(value.getCardCodeVerification()).isEqualTo(creditCard.getSecurityCode());
    assertThat(value.getCardType().name()).isEqualToIgnoringCase(creditCard.getType().name());
  }

  private class AuthorizeNetPaymentGateway_ForTest extends AuthorizeNetPaymentGateway {

    private boolean buildMerchant_wasCalled;

    public AuthorizeNetPaymentGateway_ForTest(String apiLoginId, String transactionKey) {
      super(apiLoginId, transactionKey);
    }

    @Override
    Merchant buildMerchant(String apiLoginId, String transactionKey) {
      assertThat(apiLoginId).isEqualTo(LOGIN_ID);
      assertThat(transactionKey).isEqualTo(transactionKey);
      buildMerchant_wasCalled = true;
      return mockMerchant;
    }
  }
}
