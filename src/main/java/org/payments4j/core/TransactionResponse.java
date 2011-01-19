package org.payments4j.core;

/**
 * <p>
 * Response returned by a <code>PaymentGateway</code> after a transaction is run.
 * </p>
 */
public class TransactionResponse {

  private boolean successful;
  private boolean test;
  private String authorizationId;
  private String message;
  private long code;
  private long reasonCode;
  private AvsResponse avsResponse;
  private CvvResponse cvvResponse;

  public boolean isSuccessful() {
    return successful;
  }

  public void setSuccessful(boolean successful) {
    this.successful = successful;
  }

  public boolean isTest() {
    return test;
  }

  public void setTest(boolean test) {
    this.test = test;
  }

  public String getAuthorizationId() {
    return authorizationId;
  }

  public void setAuthorizationId(String authorizationId) {
    this.authorizationId = authorizationId;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getCode() {
    return code;
  }

  public void setCode(long code) {
    this.code = code;
  }

  public long getReasonCode() {
    return reasonCode;
  }

  public void setReasonCode(long reasonCode) {
    this.reasonCode = reasonCode;
  }

  public AvsResponse getAvsResponse() {
    return avsResponse;
  }

  public void setAvsResponse(AvsResponse avsResponse) {
    this.avsResponse = avsResponse;
  }

  public CvvResponse getCvvResponse() {
    return cvvResponse;
  }

  public void setCvvResponse(CvvResponse cvvResponse) {
    this.cvvResponse = cvvResponse;
  }
}
