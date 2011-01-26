package org.payments4j.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Set;

/**
 *
 */
public class Order {

  private String id;
  private String ipAddress;
  private String customerName;
  private String customerEmail;
  private String invoiceNumber;
  private String merchant;
  private String description;
  private Address billingAddress;
  private Address shippingAddress;
  private Set<OrderLine> orderLines;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getIpAddress() {
    return ipAddress;
  }

  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  public String getCustomerName() {
    return customerName;
  }

  public void setCustomerName(String customerName) {
    this.customerName = customerName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public String getInvoiceNumber() {
    return invoiceNumber;
  }

  public void setInvoiceNumber(String invoiceNumber) {
    this.invoiceNumber = invoiceNumber;
  }

  public String getMerchant() {
    return merchant;
  }

  public void setMerchant(String merchant) {
    this.merchant = merchant;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Address getBillingAddress() {
    return billingAddress;
  }

  public void setBillingAddress(Address billingAddress) {
    this.billingAddress = billingAddress;
  }

  public Address getShippingAddress() {
    return shippingAddress;
  }

  public void setShippingAddress(Address shippingAddress) {
    this.shippingAddress = shippingAddress;
  }

  public Set<OrderLine> getOrderLines() {
    return orderLines;
  }

  public void setOrderLines(Set<OrderLine> orderLines) {
    this.orderLines = orderLines;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.id)
        .append(this.ipAddress)
        .append(this.customerName)
        .append(this.customerEmail)
        .append(this.invoiceNumber)
        .append(this.merchant)
        .append(this.description)
        .append(this.billingAddress)
        .append(this.shippingAddress)
        .append(this.orderLines)
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

    Order that = (Order) o;

    return new EqualsBuilder()
        .append(this.id, that.id)
        .append(this.ipAddress, that.ipAddress)
        .append(this.customerName, that.customerName)
        .append(this.customerEmail, that.customerEmail)
        .append(this.invoiceNumber, that.invoiceNumber)
        .append(this.merchant, that.merchant)
        .append(this.description, that.description)
        .append(this.billingAddress, that.billingAddress)
        .append(this.shippingAddress, that.shippingAddress)
        .append(this.orderLines, that.orderLines)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", this.id)
        .append("ipAddress", this.ipAddress)
        .append("customerName", this.customerName)
        .append("customerEmail", this.customerEmail)
        .append("invoiceNumber", this.invoiceNumber)
        .append("merchant", this.merchant)
        .append("description", this.description)
        .append("billingAddress", this.billingAddress)
        .append("shippingAddress", this.shippingAddress)
        .append("orderLines", this.orderLines)
        .toString();
  }
}
