package org.payments4j.examples;


import paypal.payflow.*;

/**
 * PayFlow example using their SDK to call their service.
 */
public class PayFlowClientExample {

  private static final String MERCHANT_USERNAME = "your_merchant_username";
  private static final String MERCHANT_PASSWORD = "your_merchant_password";
  private static final String PARTNER = "PayPal";

  public static void main(String[] args) {

    Invoice invoice = new Invoice();

    invoice.setAmt(new Currency(25.12));
    invoice.setPoNum("PO12345");
    invoice.setInvNum("INV12345");
    invoice.setBillTo(buildBuildTo());
    invoice.addLineItem(buildLineItem("Desc 1"));
    invoice.addLineItem(buildLineItem("Desc 2"));

    CardTender card = buildCardTender();

    UserInfo user = new UserInfo(MERCHANT_USERNAME, MERCHANT_USERNAME, PARTNER, MERCHANT_PASSWORD);

    PayflowConnectionData connection = new PayflowConnectionData("pilot-payflowpro.paypal.com", 443);

    // Create AUTH transaction.
    AuthorizationTransaction trans = new AuthorizationTransaction(user,
                                                                  connection,
                                                                  invoice,
                                                                  card,
                                                                  PayflowUtility.getRequestId());

    Response resp = trans.submitTransaction();

    TransactionResponse transactionResponse = resp.getTransactionResponse();

    System.out.println("result = " + transactionResponse.getResult());
    System.out.println("pnref = " + transactionResponse.getPnref());
    System.out.println("respmsg = " + transactionResponse.getRespMsg());
    System.out.println("authcode = " + transactionResponse.getAuthCode());
    System.out.println("avsaddr = " + transactionResponse.getAvsAddr());
    System.out.println("avszip = " + transactionResponse.getAvsZip());
    System.out.println("iavs = " + transactionResponse.getIavs());
    System.out.println("cvv2match = " + transactionResponse.getCvv2Match());
    // If value is true, then the Request ID has not been changed and the original response
    // of the original transction is returned.
    System.out.println("duplicate = " + transactionResponse.getDuplicate());

    // Get the Fraud Response parameters.
    FraudResponse fraudResp = resp.getFraudResponse();
    System.out.println("prefpsmsg = " + fraudResp.getPreFpsMsg());
    System.out.println("postfpsmsg = " + fraudResp.getPostFpsMsg());
  }

  private static LineItem buildLineItem(String description) {
    LineItem lineItem = new LineItem();
    lineItem.setDesc(description);
    return lineItem;
  }

  private static BillTo buildBuildTo() {
    BillTo bill = new BillTo();
    bill.setStreet("123 Main St.");
    bill.setZip("12345");
    return bill;
  }

  private static CardTender buildCardTender() {
    CreditCard cc = new CreditCard("5105105105105100", "0112");
    cc.setCvv2("123");

    return new CardTender(cc);
  }
}
