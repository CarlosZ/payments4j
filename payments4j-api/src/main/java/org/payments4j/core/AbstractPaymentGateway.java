package org.payments4j.core;

import org.payments4j.model.CreditCard;
import org.payments4j.model.Money;
import org.perf4j.StopWatch;
import org.perf4j.slf4j.Slf4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.payments4j.common.ParamUtil.requestId;

/**
 *
 */
public abstract class AbstractPaymentGateway implements PaymentGateway {

  protected static Logger LOGGER;
  protected static StopWatch STOP_WATCH;

  protected AbstractPaymentGateway() {
    LOGGER = LoggerFactory.getLogger(this.getClass());
    STOP_WATCH = new Slf4JStopWatch(LOGGER);
    STOP_WATCH.stop();
    if (LOGGER.isTraceEnabled()) {
      LOGGER.warn("DANGER WILL Robinson: You have set the logger level to TRACE, \n" +
                  "which means credit card information will be logged!!\n" +
                  "PCI-DSS guidelines recommend not to store unencrypted credit card data.");
    }
  }

  @Override
  public TransactionResponse purchase(Money money, CreditCard creditCard, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "purchase");
    logParam("money", requestId, money);
    logParam("creditCard", requestId, creditCard);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doPurchase(money, creditCard, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "purchase");
    return transactionResponse;
  }

  protected abstract TransactionResponse doPurchase(Money money, CreditCard creditCard, Map<String, Object> options);

  @Override
  public TransactionResponse authorize(Money money, CreditCard creditCard, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "authorize");
    logParam("money", requestId, money);
    logParam("creditCard", requestId, creditCard);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doAuthorize(money, creditCard, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "authorize");
    return transactionResponse;
  }

  protected abstract TransactionResponse doAuthorize(Money money, CreditCard creditCard, Map<String, Object> options);

  @Override
  public TransactionResponse capture(Money money, String authorizationId, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "capture");
    logParam("money", requestId, money);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doCapture(money, authorizationId, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "capture");
    return transactionResponse;
  }

  protected abstract TransactionResponse doCapture(Money money, String authorizationId, Map<String, Object> options);

  @Override
  public TransactionResponse revert(String transactionId, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "revert");
    logParam("transactionId", requestId, transactionId);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doRevert(transactionId, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "revert");
    return transactionResponse;
  }

  protected abstract TransactionResponse doRevert(String transactionId, Map<String, Object> options);

  @Override
  public TransactionResponse credit(Money money, String transactionId, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "credit");
    logParam("money", requestId, money);
    logParam("transactionId", requestId, transactionId);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doCredit(money, transactionId, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "credit");
    return transactionResponse;
  }

  protected abstract TransactionResponse doCredit(Money money, String transactionId, Map<String, Object> options);

  @Override
  public TransactionResponse recurring(Money money, CreditCard creditCard, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "recurring");
    logParam("money", requestId, money);
    logParam("creditCard", requestId, creditCard);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doRecurring(money, creditCard, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "recurring");
    return transactionResponse;
  }

  protected abstract TransactionResponse doRecurring(Money money, CreditCard creditCard, Map<String, Object> options);

  @Override
  public TransactionResponse storeCreditCard(CreditCard creditCard, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "storeCreditCard");
    logParam("creditCard", requestId, creditCard);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doStoreCreditCard(creditCard, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "storeCreditCard");
    return transactionResponse;
  }

  protected abstract TransactionResponse doStoreCreditCard(CreditCard creditCard, Map<String, Object> options);

  @Override
  public TransactionResponse evictCreditCard(String creditCardId, Map<String, Object> options) {
    String requestId = requestId();
    options = initOptions(options);
    options.put("requestId", requestId);
    logOperationStart(requestId, "evictCreditCard");
    logParam("creditCardId", requestId, creditCardId);
    logParam("options", requestId, options);

    TransactionResponse transactionResponse = doEvictCreditCard(creditCardId, options);

    logResponse(requestId, transactionResponse);
    logOperationEnd(requestId, "evictCreditCard");
    return transactionResponse;
  }

  private Map<String, Object> initOptions(Map<String, Object> options) {
    if (options == null) {
      options = new HashMap<String, Object>();
    }
    return options;
  }

  protected abstract TransactionResponse doEvictCreditCard(String creditCardIdd, Map<String, Object> options);

  private void logResponse(String requestId, TransactionResponse transactionResponse) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("[requestId={}] Transaction response [{}].", requestId, transactionResponse);
    }
  }

  private void logParam(String name, String requestId, Object value) {
    if (LOGGER.isTraceEnabled()) {
      LOGGER.trace("[requestId={}] DANGEROUS PARAMETER [" + name + "={}].", requestId, value);
    }
  }

  private void logOperationStart(String requestId, String operation) {
    String message = format("[requestId=%s] Executing %s operation.", requestId, operation);
    STOP_WATCH.setTag(message);
    LOGGER.info(message);
  }

  private void logOperationEnd(String requestId, String operation) {
    STOP_WATCH.stop();
    LOGGER.info("[requestId={}] Finished executing " + operation + " operation.", requestId);
  }
}
