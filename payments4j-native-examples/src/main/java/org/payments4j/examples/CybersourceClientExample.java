package org.payments4j.examples;


import com.cybersource.schemas.transaction_data.transactionprocessor.ITransactionProcessor;
import com.cybersource.schemas.transaction_data.transactionprocessor.TransactionProcessor;
import com.cybersource.schemas.transaction_data_1.BillTo;
import com.cybersource.schemas.transaction_data_1.CCAuthService;
import com.cybersource.schemas.transaction_data_1.Card;
import com.cybersource.schemas.transaction_data_1.Item;
import com.cybersource.schemas.transaction_data_1.PurchaseTotals;
import com.cybersource.schemas.transaction_data_1.ReplyMessage;
import com.cybersource.schemas.transaction_data_1.RequestMessage;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.version.Version;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSConstants;
import org.apache.ws.security.WSPasswordCallback;
import org.apache.ws.security.handler.WSHandlerConstants;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Cybersource Sample Client, calling their SOAP webservice.
 */
public class CybersourceClientExample {

  // Replace the MERCHANT_ID and MERCHANT_KEY with the appropriate values.
  private static final String MERCHANT_ID = "merchant_id";
  private static final String MERCHANT_KEY = "merchant_key";

  private static final String SERVER_URL = "https://ics2wstest.ic3.com/commerce/1.x/transactionProcessor/CyberSourceTransaction_1.56.wsdl";

  private static final String CLIENT_LIB_VERSION = Version.getCompleteVersionString() + "/1.5.10"; // CXF Version / WSS4J Version
  private static final String CLIENT_LIBRARY = "Java CXF WSS4J";
  private static final String CLIENT_ENV = System.getProperty("os.name") + "/" +
                                           System.getProperty("os.version") + "/" +
                                           System.getProperty("java.vendor") + "/" +
                                           System.getProperty("java.version");

  public static void main(String[] args) throws RemoteException, MalformedURLException {
    RequestMessage request = new RequestMessage();

    // To help Cybersource troubleshoot any problems that you may encounter,
    // include the following information about the client.
    addClientLibraryInfo(request);

    request.setMerchantID(MERCHANT_ID);

    // Internal Transaction Reference Code for the Merchant
    request.setMerchantReferenceCode("222222");

    // Here we are telling the client that we are going to run an AUTH.
    request.setCcAuthService(new CCAuthService());
    request.getCcAuthService().setRun("true");

    request.setBillTo(buildBillTo());
    request.setCard(buildCard());
    request.setPurchaseTotals(buildPurchaseTotals());

    request.getItem().add(buildItem("0", "12.34", "2"));
    request.getItem().add(buildItem("1", "56.78", "1"));

    ITransactionProcessor processor = new TransactionProcessor(new URL(SERVER_URL)).getPortXML();

    //  Add WS-Security Headers to the Request
    addSecurityValues(processor);

    ReplyMessage reply = processor.runTransaction(request);

    System.out.println("decision = " + reply.getDecision());
    System.out.println("reasonCode = " + reply.getReasonCode());
    System.out.println("requestID = " + reply.getRequestID());
    System.out.println("requestToken = " + reply.getRequestToken());
    System.out.println("ccAuthReply.reasonCode = " + reply.getCcAuthReply().getReasonCode());
  }

  private static void addClientLibraryInfo(RequestMessage request) {
    request.setClientLibrary(CLIENT_LIBRARY);
    request.setClientLibraryVersion(CLIENT_LIB_VERSION);
    request.setClientEnvironment(CLIENT_ENV);
  }

  private static Item buildItem(String id, String unitPrice, String quantity) {
    Item item = new Item();
    item.setId(new BigInteger(id));
    item.setUnitPrice(unitPrice);
    item.setQuantity(new BigInteger(quantity));
    return item;
  }

  private static PurchaseTotals buildPurchaseTotals() {
    PurchaseTotals purchaseTotals = new PurchaseTotals();
    purchaseTotals.setCurrency("USD");
    purchaseTotals.setGrandTotalAmount("100");
    return purchaseTotals;
  }

  private static Card buildCard() {
    Card card = new Card();
    card.setAccountNumber("4111111111111111");
    card.setExpirationMonth(new BigInteger("12"));
    card.setExpirationYear(new BigInteger("2020"));
    return card;
  }

  private static BillTo buildBillTo() {
    BillTo billTo = new BillTo();
    billTo.setFirstName("John");
    billTo.setLastName("Doe");
    billTo.setStreet1("1295 Charleston Road");
    billTo.setCity("Mountain View");
    billTo.setState("CA");
    billTo.setPostalCode("94043");
    billTo.setCountry("US");
    billTo.setEmail("null@cybersource.com");
    billTo.setIpAddress("10.7.111.111");
    return billTo;
  }

  private static void addSecurityValues(ITransactionProcessor processor) {
    Client client = ClientProxy.getClient(processor);
    Endpoint endpoint = client.getEndpoint();

    // We'll have to add the Username and Password properties to an OutInterceptor
    HashMap<String, Object> outHeaders = new HashMap<String, Object>();
    outHeaders.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
    outHeaders.put(WSHandlerConstants.USER, MERCHANT_ID);
    outHeaders.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
    outHeaders.put(WSHandlerConstants.PW_CALLBACK_CLASS, ClientPasswordHandler.class.getName());

    WSS4JOutInterceptor interceptor = new WSS4JOutInterceptor(outHeaders);
    endpoint.getOutInterceptors().add(interceptor);
  }

  public static class ClientPasswordHandler implements CallbackHandler {

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
      for (Callback callback : callbacks) {
        if (callback instanceof WSPasswordCallback) {
          WSPasswordCallback passwordCallback = (WSPasswordCallback) callback;
          passwordCallback.setPassword(MERCHANT_KEY);
        }
      }
    }
  }
}
