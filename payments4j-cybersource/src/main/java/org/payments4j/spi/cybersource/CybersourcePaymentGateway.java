package org.payments4j.spi.cybersource;

import com.cybersource.schemas.transaction_data.transactionprocessor.ITransactionProcessor;
import com.cybersource.schemas.transaction_data.transactionprocessor.TransactionProcessor;
import com.cybersource.schemas.transaction_data_1.PurchaseTotals;
import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.cybersource.schemas.transaction_data_1.RequestMessage;
import com.google.common.annotations.VisibleForTesting;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.payments4j.core.AbstractPaymentGateway;
import org.payments4j.core.TransactionResponse;
import org.payments4j.model.CreditCard;
import org.payments4j.model.Money;
import org.payments4j.model.Order;
import org.payments4j.spi.cybersource.converter.AddressConverter;
import org.payments4j.spi.cybersource.converter.CreditCardConverter;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.ws.security.WSConstants.PW_TEXT;
import static org.apache.ws.security.handler.WSHandlerConstants.ACTION;
import static org.apache.ws.security.handler.WSHandlerConstants.PASSWORD_TYPE;
import static org.apache.ws.security.handler.WSHandlerConstants.PW_CALLBACK_REF;
import static org.apache.ws.security.handler.WSHandlerConstants.USER;
import static org.apache.ws.security.handler.WSHandlerConstants.USERNAME_TOKEN;
import static org.payments4j.common.ParamUtil.requireOption;
import static org.payments4j.spi.cybersource.TransactionType.AUTHORIZE;
import static org.payments4j.spi.cybersource.TransactionType.CAPTURE;
import static org.payments4j.spi.cybersource.TransactionType.CREDIT;
import static org.payments4j.spi.cybersource.TransactionType.PURCHASE;
import static org.payments4j.spi.cybersource.TransactionType.REVERT;


/**
 * Implementation of <code>PaymentGateway</code> for Cybersource. For more details on the operations for Cybersource,
 * visit {@link http://apps.cybersource.com/library/documentation/dev_guides/CC_Svcs_SO_API/html/}.
 */
public class CybersourcePaymentGateway extends AbstractPaymentGateway {

  public static final URL HOSTNAME = initUrl("https://ics2ws.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.56.wsdl");
  public static final URL TEST_HOSTNAME = initUrl("https://ics2wstest.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.56.wsdl");

  private boolean test;
  private String username;
  private String transactionKey;

  public CybersourcePaymentGateway(String username, String transactionKey) {
    this.username = username;
    this.transactionKey = transactionKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doPurchase(Money money, CreditCard creditCard, Map<String, Object> options) {
    requireOption(options, "referenceNumber");
    requireOption(options, "order");
    ITransactionProcessor processor = buildTransactionProcessor();

    Order order = (Order) options.get("order");
    RequestMessage request = buildRequestMessage(PURCHASE, money, creditCard, order);
    request.setMerchantReferenceCode((String) options.get("referenceNumber"));

    return executeTransaction(processor, request);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doAuthorize(Money money, CreditCard creditCard, Map<String, Object> options) {
    requireOption(options, "referenceNumber");
    requireOption(options, "order");
    ITransactionProcessor processor = buildTransactionProcessor();

    Order order = (Order) options.get("order");
    RequestMessage request = buildRequestMessage(AUTHORIZE, money, creditCard, order);
    request.setMerchantReferenceCode((String) options.get("referenceNumber"));

    return executeTransaction(processor, request);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doCapture(Money money, String authorizationId, Map<String, Object> options) {
    requireOption(options, "referenceNumber");
    ITransactionProcessor processor = buildTransactionProcessor();

    RequestMessage request = buildRequestMessage(CAPTURE, money, null, null);
    request.setMerchantReferenceCode((String) options.get("referenceNumber"));
    request.getCcCaptureService().setAuthRequestID(authorizationId);

    return executeTransaction(processor, request);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doRevert(String transactionId, Map<String, Object> options) {
    requireOption(options, "referenceNumber");
    requireOption(options, "money");

    ITransactionProcessor processor = buildTransactionProcessor();

    RequestMessage request = buildRequestMessage(REVERT, null, null, null);
    request.setMerchantReferenceCode((String) options.get("referenceNumber"));
    request.setPurchaseTotals(buildPurchaseTotals((Money) options.get("money")));
    request.getCcAuthReversalService().setAuthRequestID(transactionId);

    return executeTransaction(processor, request);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doCredit(Money money, String transactionId, Map<String, Object> options) {
    requireOption(options, "referenceNumber");
    ITransactionProcessor processor = buildTransactionProcessor();

    RequestMessage request = buildRequestMessage(CREDIT, money, null, null);
    request.setMerchantReferenceCode((String) options.get("referenceNumber"));
    request.getCcCreditService().setCaptureRequestID(transactionId);

    return executeTransaction(processor, request);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doRecurring(Money money, CreditCard creditCard, Map<String, Object> options) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doStoreCreditCard(CreditCard creditCard, Map<String, Object> options) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TransactionResponse doEvictCreditCard(String creditCardId, Map<String, Object> options) {
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

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTest(boolean test) {
    this.test = test;
  }

  private TransactionResponse executeTransaction(ITransactionProcessor processor, RequestMessage request) {
    ReplyMessage replyMessage = processor.runTransaction(request);
    return buildTransactionResponse(replyMessage);
  }

  private RequestMessage buildRequestMessage(TransactionType transactionType,
                                             Money money,
                                             CreditCard creditCard,
                                             Order order) {
    RequestMessage request = new RequestMessage();
    transactionType.setServices(request);
    request.setMerchantID(username);
    if (creditCard != null) {
      request.setCard(new CreditCardConverter(creditCard).toCard());
    }
    if (money != null) {
      request.setPurchaseTotals(buildPurchaseTotals(money));
    }
    if (order != null) {
      request.setBillTo(new AddressConverter(order.getBillingAddress()).toBillTo());

      request.setShipTo(new AddressConverter(order.getShippingAddress()).toShipTo());
    }
    return request;
  }

  private ITransactionProcessor buildTransactionProcessor() {
    ITransactionProcessor processor = buildProcessor(test ? TEST_HOSTNAME : HOSTNAME);
    addSecurityProperties(processor, username, transactionKey);
    return processor;
  }

  private TransactionResponse buildTransactionResponse(ReplyMessage replyMessage) {
    TransactionResponse transactionResponse = new TransactionResponse();
    String message = replyMessage.getDecision();
    if (!replyMessage.getMissingField().isEmpty()) {
      message += (" | Missing Fields: " + replyMessage.getMissingField().toString());
    }
    if (!replyMessage.getInvalidField().isEmpty()) {
      message += (" | Invalid Fields: " + replyMessage.getInvalidField().toString());
    }
    transactionResponse.setMessage(message);
    transactionResponse.setCode(replyMessage.getReasonCode().intValue());
    transactionResponse.setReasonCode(replyMessage.getReasonCode().intValue());
    transactionResponse.setAuthorizationId(replyMessage.getRequestID());
    transactionResponse.setSuccessful(100 == replyMessage.getReasonCode().intValue());
    return transactionResponse;
  }

  private PurchaseTotals buildPurchaseTotals(Money money) {
    PurchaseTotals purchaseTotals = new PurchaseTotals();
    purchaseTotals.setGrandTotalAmount(format("%1$.2f", money.getAmount()));
    purchaseTotals.setCurrency(money.getCurrency().getCurrencyCode());
    return purchaseTotals;
  }

  private void addSecurityProperties(ITransactionProcessor processor, String username, String transactionKey) {
    Endpoint endpoint = buildEndpoint(processor);

    HashMap<String, Object> headers = new HashMap<String, Object>();
    headers.put(ACTION, USERNAME_TOKEN);
    headers.put(USER, username);
    headers.put(PASSWORD_TYPE, PW_TEXT);
    headers.put(PW_CALLBACK_REF, new ClientPasswordHandler(transactionKey));

    WSS4JOutInterceptor interceptor = new WSS4JOutInterceptor(headers);
    endpoint.getOutInterceptors().add(interceptor);
  }

  @VisibleForTesting Endpoint buildEndpoint(ITransactionProcessor processor) {
    return ClientProxy.getClient(processor).getEndpoint();
  }

  @VisibleForTesting ITransactionProcessor buildProcessor(URL wsdlLocation) {
    return new TransactionProcessor(wsdlLocation).getPortXML();
  }

  private static URL initUrl(String url) {
    try {
      return new URL(url);
    } catch (MalformedURLException e) {
      throw new RuntimeException(e);
    }
  }
}
