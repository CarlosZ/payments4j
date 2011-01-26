package org.payments4j.spi.payflowpro;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.junit.Test;
import org.payments4j.core.TransactionResponse;

import java.io.UnsupportedEncodingException;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public class PayFlowResponseHandlerTest {

  @Test
  public void testHandleResponse() throws Exception {
    TransactionResponse expectedTransaction = buildExpectedTransactionResponse(0, "Verified", true);

    HttpResponse httpResponse = buildHttpResponse(
        "PNREF=123&PPREF=456&RESULT=0&CVV2MATCH=Y&RESPMSG=Verified&AVSADDR=Y&AVSZIP=N");

    PayFlowResponseHandler handler = new PayFlowResponseHandler();

    TransactionResponse transactionResponse = handler.handleResponse(httpResponse);

    assertThat(transactionResponse).isEqualTo(expectedTransaction);
  }

  @Test
  public void testHandleResponse_Failed() throws Exception {
    TransactionResponse expectedTransaction = buildExpectedTransactionResponse(4, "Invalid Amount", false);

    HttpResponse httpResponse = buildHttpResponse(
        "PNREF=123&PPREF=456&RESULT=4&CVV2MATCH=Y&RESPMSG=Invalid Amount&AVSADDR=Y&AVSZIP=N");

    PayFlowResponseHandler handler = new PayFlowResponseHandler();

    TransactionResponse transactionResponse = handler.handleResponse(httpResponse);

    assertThat(transactionResponse).isEqualTo(expectedTransaction);
  }

  private TransactionResponse buildExpectedTransactionResponse(int code, String message, boolean successful) {
    TransactionResponse expectedTransaction = new TransactionResponse();
    expectedTransaction.setCode(code);
    expectedTransaction.setAuthorizationId("123");
    expectedTransaction.setReasonCode(code);
    expectedTransaction.setMessage(message);
    expectedTransaction.setSuccessful(successful);
    return expectedTransaction;
  }

  private HttpResponse buildHttpResponse(String responseString) throws UnsupportedEncodingException {
    HttpResponse httpResponse = new BasicHttpResponse(new BasicStatusLine(HttpVersion.HTTP_1_1, 200, ""), null, null);
    httpResponse.setEntity(new StringEntity(responseString));
    httpResponse.setStatusCode(200);
    return httpResponse;
  }
}
