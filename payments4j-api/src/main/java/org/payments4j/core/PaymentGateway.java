package org.payments4j.core;

import org.payments4j.model.CreditCard;
import org.payments4j.model.Money;

import java.util.Map;

/**
 * <p>
 * Interface that all <code>PaymentGateway's</code> must implement.
 * </p>
 * <p>
 * Usually most <code>PaymentGateways</code> will support purchase, authorize, capture, revert(void) and credit
 * transactions.
 * </p>
 * <p>
 * Only some <code>PaymentGateways</code> support recurring transactions as well as storing/evicting credit card data.
 * </p>
 */
public interface PaymentGateway {

  /**
   * Runs a purchase(authorize+capture) transaction against the <code>PaymentGateway</code>.
   *
   * @param money Amount for the transaction.
   * @param creditCard Credit Card to charge the transaction to.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse purchase(Money money, CreditCard creditCard, Map<String, Object> options);

  /**
   * Runs an authorize transaction against the <code>PaymentGateway</code>.
   *
   * @param money Amount for the transaction.
   * @param creditCard Credit Card to authorize the transaction to.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse authorize(Money money, CreditCard creditCard, Map<String, Object> options);

  /**
   * Runs a capture transaction against the <code>PaymentGateway</code>.
   *
   * @param money Amount for the transaction.
   * @param authorizationId Id of previously run authorization transaction.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse capture(Money money, String authorizationId, Map<String, Object> options);

  /**
   * Runs a revert(void) transaction against the <code>PaymentGateway</code>.
   *
   * @param transactionId Identifier of the transaction to revert(void).
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse revert(String transactionId, Map<String, Object> options);

  /**
   * Runs a credit transaction against the <code>PaymentGateway</code>.
   *
   * @param money Amount for the transaction.
   * @param transactionId Identifier of the transaction to credit.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse credit(Money money, String transactionId, Map<String, Object> options);

  /**
   * Runs a recurrent purchase transaction against the <code>PaymentGateway</code>.
   *
   * @param money Amount for the transaction.
   * @param creditCard Credit Card to recurrently charge the transaction to.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse recurring(Money money, CreditCard creditCard, Map<String, Object> options);

  /**
   * Securely stores the Credit Card data in the <code>PaymentGateway</code>.
   *
   * @param creditCard Credit Card information to store.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse storeCreditCard(CreditCard creditCard, Map<String, Object> options);

  /**
   * Evicts a previously stored Credit Card data from the <code>PaymentGateway</code>.
   *
   * @param creditCardId Identifier of the Credit Card to evict.
   * @param options Optional Gateway-specific data, that will be sent to the Gateway service.
   *
   * @return The response from the gateway.
   */
  TransactionResponse evictCreditCard(String creditCardId, Map<String, Object> options);

  /**
   * Returns true if this PaymentGateway supports <code>purchase</code> transactions.
   *
   * @return True if this PaymentGateway supports <code>purchase</code> transactions.
   */
  boolean supportsPurchase();

  /**
   * Returns true if this PaymentGateway supports <code>authorize</code> transactions.
   *
   * @return True if this PaymentGateway supports <code>authorize</code> transactions.
   */
  boolean supportsAuthorize();

  /**
   * Returns true if this PaymentGateway supports <code>capture</code> transactions.
   *
   * @return True if this PaymentGateway supports <code>capture</code> transactions.
   */
  boolean supportsCapture();

  /**
   * Returns true if this PaymentGateway supports <code>revert, i.e., void</code> transactions.
   *
   * @return True if this PaymentGateway supports <code>revert, i.e., void</code> transactions.
   */
  boolean supportsRevert();

  /**
   * Returns true if this PaymentGateway supports <code>credit/code> transactions.
   *
   * @return True if this PaymentGateway supports <code>credit</code> transactions.
   */
  boolean supportsCredit();

  /**
   * Returns true if this PaymentGateway supports storing credit card information.
   *
   * @return True if this PaymentGateway supports storing credit card information.
   */
  boolean supportsStoreCreditCard();

  /**
   * Returns true if this PaymentGateway supports evicting credit card information.
   *
   * @return True if this PaymentGateway supports evicting credit card information.
   */
  boolean supportsEvictCreditCard();

  /**
   * Sets whether the transactions are run against the test/sandbox environments of the Payment Processor.
   *
   * @param test Flag to set the <code>PaymentGateway</code> to testing mode.
   */
  void setTest(boolean test);
}
