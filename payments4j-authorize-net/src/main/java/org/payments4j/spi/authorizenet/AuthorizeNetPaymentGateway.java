package org.payments4j.spi.authorizenet;

import net.authorize.*;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CardType;
import org.payments4j.core.PaymentGateway;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.Money;

import java.math.BigDecimal;
import java.util.Map;

import static net.authorize.TransactionType.*;

/**
 * Implementation of <code>PaymentGateway</code> for Authorize.net using their provided Java SDK.
 *
 * @link http://developer.authorize.net/downloads/
 */
public class AuthorizeNetPaymentGateway implements PaymentGateway {

  private String apiLoginId;
  private String transactionKey;
  private boolean test;

  public AuthorizeNetPaymentGateway(String apiLoginId, String transactionKey) {
    this.apiLoginId = apiLoginId;
    this.transactionKey = transactionKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse purchase(Money money, CreditCard creditCard, Map<String, Object> optionals) {
    return executeTransactionWithCreditCard(AUTH_CAPTURE, creditCard, money.getAmount());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse authorize(Money money, CreditCard creditCard, Map<String, Object> optionals) {
    return executeTransactionWithCreditCard(AUTH_ONLY, creditCard, money.getAmount());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse capture(Money money, String authorizationId, Map<String, Object> optionals) {
    return executeTransactionWithTransactionId(PRIOR_AUTH_CAPTURE, authorizationId, money.getAmount());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse revert(String transactionId, Map<String, Object> optionals) {
    return executeTransactionWithTransactionId(VOID, transactionId, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse credit(Money money, String transactionId, Map<String, Object> optionals) {
    Merchant merchant = buildMerchant(apiLoginId, transactionKey);
    Transaction transaction = merchant.createAIMTransaction(CREDIT, money.getAmount());
    transaction.setTransactionId(transactionId);
    net.authorize.data.creditcard.CreditCard creditCard = net.authorize.data.creditcard.CreditCard.createCreditCard();
    creditCard.setCreditCardNumber((String) optionals.get("creditCardNumber"));
    creditCard.setExpirationMonth((String) optionals.get("creditCardMonth"));
    creditCard.setExpirationYear((String) optionals.get("creditCardYear"));
    transaction.setCreditCard(creditCard);
    Result<?> result = merchant.postTransaction(transaction);
    return convert(result);
  }

  /**
   * {@inheritDoc}
   *
   * UNSUPPORTED
   */
  @Override
  public TransactionResponse recurring(Money money, CreditCard creditCard, Map<String, Object> optionals) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   *
   * UNSUPPORTED
   */
  @Override
  public TransactionResponse storeCreditCard(CreditCard creditCard, Map<String, Object> optionals) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   *
   * UNSUPPORTED
   */
  @Override
  public TransactionResponse evictCreditCard(String creditCardId, Map<String, Object> optionals) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsPurchase() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsAuthorize() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsCapture() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsRevert() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsCredit() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsStoreCreditCard() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean supportsEvictCreditCard() {
    return false;
  }

  private TransactionResponse executeTransactionWithTransactionId(TransactionType transactionType,
                                                                  String authorizationId,
                                                                  BigDecimal amount) {
    Merchant merchant = buildMerchant(apiLoginId, transactionKey);
    Transaction transaction = merchant.createAIMTransaction(transactionType, amount);
    transaction.setTransactionId(authorizationId);
    Result<?> result = merchant.postTransaction(transaction);
    return convert(result);
  }

  private TransactionResponse executeTransactionWithCreditCard(TransactionType transactionType,
                                                               CreditCard creditCard,
                                                               BigDecimal amount) {
    Merchant merchant = buildMerchant(apiLoginId, transactionKey);
    Transaction transaction = merchant.createAIMTransaction(transactionType, amount);
    transaction.setCreditCard(convert(creditCard));
    Result<?> result = merchant.postTransaction(transaction);
    return convert(result);
  }

  @Override
  public void setTest(boolean test) {
    this.test = test;
  }

  private TransactionResponse convert(Result<?> result) {
    TransactionResponse response = new TransactionResponse();
    Transaction transaction = (Transaction) result.getTarget();
    response.setCode(getLongResponseField(transaction, ResponseField.RESPONSE_CODE));
    response.setReasonCode(getLongResponseField(transaction, ResponseField.RESPONSE_REASON_CODE));
    response.setMessage(getStringResponseField(transaction, ResponseField.RESPONSE_REASON_TEXT));
    response.setAuthorizationId(getStringResponseField(transaction, ResponseField.AUTHORIZATION_CODE));
    response.setSuccessful(ResponseCode.APPROVED == ResponseCode.findByResponseCode((int) response.getCode()));
    return response;
  }

  private Long getLongResponseField(Transaction transaction, ResponseField field) {
    return Long.valueOf(getStringResponseField(transaction, field));
  }

  private String getStringResponseField(Transaction transaction, ResponseField field) {
    return transaction.getResponseMap().get(field);
  }

  private net.authorize.data.creditcard.CreditCard convert(CreditCard creditCard) {
    net.authorize.data.creditcard.CreditCard converted = net.authorize.data.creditcard.CreditCard.createCreditCard();
    converted.setCreditCardNumber(creditCard.getNumber());
    converted.setExpirationMonth(creditCard.getMonth());
    converted.setExpirationYear(creditCard.getYear());
    converted.setCardCodeVerification(creditCard.getSecurityCode());
    converted.setCardType(CardType.valueOf(creditCard.getType().name()));
    return converted;
  }

  Merchant buildMerchant(String apiLoginId, String transactionKey) {
    Environment environment = Environment.PRODUCTION;
    if (test) {
      environment = Environment.SANDBOX;
    }
    return Merchant.createMerchant(environment, apiLoginId, transactionKey);
  }
}
