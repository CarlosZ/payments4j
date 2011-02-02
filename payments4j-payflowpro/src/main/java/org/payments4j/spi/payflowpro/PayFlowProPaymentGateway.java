package org.payments4j.spi.payflowpro;

import com.google.common.base.Joiner;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.payments4j.core.AbstractPaymentGateway;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.Money;
import org.payments4j.spi.payflowpro.converter.AbstractBaseConverter;
import org.payments4j.spi.payflowpro.converter.CredentialsConverter;
import org.payments4j.spi.payflowpro.converter.CreditCardConverter;
import org.payments4j.spi.payflowpro.converter.MoneyConverter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Payment Gateway for PayFlowPro. Implemented using the HTTPS interface.
 */
public class PayFlowProPaymentGateway extends AbstractPaymentGateway {

  public static final String HOST_ADDRESS = "https://payflowpro.paypal.com:443";
  public static final String TEST_HOST_ADDRESS = "https://pilot-payflowpro.paypal.com:443";

  private CredentialsConverter credentialsConverter;
  private boolean test;

  public PayFlowProPaymentGateway(String username, String password) {
    this.credentialsConverter = new CredentialsConverter(username, password);
  }

  @Override
  public TransactionResponse doPurchase(Money money, CreditCard creditCard, Map<String, Object> options) {
    List<Pair> paramsList = buildParamList(new MoneyConverter(money),
                                           new CreditCardConverter(creditCard),
                                           credentialsConverter);
    return executeTransaction(paramsList, "S");
  }

  @Override
  public TransactionResponse doAuthorize(Money money, CreditCard creditCard, Map<String, Object> options) {
    List<Pair> paramsList = buildParamList(new MoneyConverter(money),
                                           new CreditCardConverter(creditCard),
                                           credentialsConverter);
    return executeTransaction(paramsList, "A");
  }

  @Override
  public TransactionResponse doCapture(Money money, String authorizationId, Map<String, Object> options) {
    List<Pair> paramsList = buildParamList(new MoneyConverter(money),
                                           credentialsConverter);
    paramsList.add(new Pair("ORIGID", authorizationId));
    return executeTransaction(paramsList, "D");
  }

  @Override
  public TransactionResponse doRevert(String transactionId, Map<String, Object> options) {
    List<Pair> paramsList = buildParamList(credentialsConverter);
    paramsList.add(new Pair("ORIGID", transactionId));
    return executeTransaction(paramsList, "V");
  }

  @Override
  public TransactionResponse doCredit(Money money, String transactionId, Map<String, Object> options) {
    List<Pair> paramsList = buildParamList(credentialsConverter);
    paramsList.add(new Pair("ORIGID", transactionId));
    return executeTransaction(paramsList, "C");
  }

  @Override
  public TransactionResponse doRecurring(Money money, CreditCard creditCard, Map<String, Object> options) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TransactionResponse doStoreCreditCard(CreditCard creditCard, Map<String, Object> options) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TransactionResponse doEvictCreditCard(String creditCardId, Map<String, Object> options) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean supportsPurchase() {
    return true;
  }

  @Override
  public boolean supportsAuthorize() {
    return true;
  }

  @Override
  public boolean supportsCapture() {
    return true;
  }

  @Override
  public boolean supportsRevert() {
    return true;
  }

  @Override
  public boolean supportsCredit() {
    return true;
  }

  @Override
  public boolean supportsStoreCreditCard() {
    return false;
  }

  @Override
  public boolean supportsEvictCreditCard() {
    return false;
  }

  @Override
  public void setTest(boolean test) {
    this.test = test;
  }

  private TransactionResponse executeTransaction(List<Pair> paramsList, String transactionType) {
    HttpPost post = buildHttpPost(test ? TEST_HOST_ADDRESS : HOST_ADDRESS);


    paramsList.add(new Pair("VERBOSITY", "MEDIUM"));
    paramsList.add(new Pair("TRXTYPE", transactionType));

    post.setEntity(toStringEntity(paramsList));
    try {
      HttpClient httpClient = buildHttpClient();
      return httpClient.execute(post, new PayFlowResponseHandler());
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  private List<Pair> buildParamList(AbstractBaseConverter... converters) {
    List<Pair> paramsList = new ArrayList<Pair>();
    for (AbstractBaseConverter converter : converters) {
      paramsList.addAll(converter.toParamsList());
    }
    return paramsList;
  }

  private StringEntity toStringEntity(List<Pair> paramsList) {
    String params = Joiner.on("&").join(paramsList);
    try {
      return new StringEntity(params, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      // This is a dumb exception, and should never occur
      return null;
    }
  }

  HttpClient buildHttpClient() {
    return new DefaultHttpClient();
  }

  HttpPost buildHttpPost(String url) {
    return new HttpPost(url);
  }
}
