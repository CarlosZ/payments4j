package org.payments4j.examples;

import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.aim.Result;
import net.authorize.TransactionType;
import net.authorize.aim.Transaction;
import net.authorize.data.creditcard.CreditCard;

import java.math.BigDecimal;

/**
 * Authorize.net example using their anet-java-sdk directly.
 */
public class AuthorizeNetClientExample {

  // Replace the API_LOGIN_ID and TRANSACTION_KEY with the appropriate values.
  private static final String API_LOGIN_ID = "your_api_login_id";
  private static final String TRANSACTION_KEY = "your_transaction_key";

  public static void main(String[] args) {
    Merchant merchant = Merchant.createMerchant(Environment.SANDBOX, API_LOGIN_ID, TRANSACTION_KEY);

    CreditCard creditCard = CreditCard.createCreditCard();
    creditCard.setCreditCardNumber("4111 1111 1111 1111");
    creditCard.setExpirationMonth("12");
    creditCard.setExpirationYear("2015");

    // Create AUTH transaction
    Transaction authCaptureTransaction = merchant.createAIMTransaction(TransactionType.AUTH_CAPTURE, new BigDecimal(1.99));
    authCaptureTransaction.setCreditCard(creditCard);

    Result<Transaction> result = (Result<Transaction>) merchant.postTransaction(authCaptureTransaction);

    System.out.println(result.getTarget().getTransactionId());
    System.out.println("responseCode = " + result.getResponseCode());
    System.out.println("responseText = " + result.getResponseText());
    System.out.println("reasonResponseCode = " + result.getReasonResponseCode());
  }
}
