package org.payments4j.core;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

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

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(successful)
        .append(test)
        .append(authorizationId)
        .append(message)
        .append(code)
        .append(reasonCode)
        .append(avsResponse)
        .append(cvvResponse)
        .toHashCode();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    TransactionResponse that = (TransactionResponse) o;

    return new EqualsBuilder()
        .append(this.successful, that.successful)
        .append(this.test, that.test)
        .append(this.authorizationId, that.authorizationId)
        .append(this.message, that.message)
        .append(this.code, that.code)
        .append(this.reasonCode, that.reasonCode)
        .append(this.avsResponse, that.avsResponse)
        .append(this.avsResponse, that.avsResponse)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("successful", successful)
        .append("test", test)
        .append("authorizationId", authorizationId)
        .append("message", message)
        .append("code", code)
        .append("reasonCode", reasonCode)
        .append("avsResponse", avsResponse)
        .append("cvvResponse", cvvResponse)
        .toString();
  }
}
