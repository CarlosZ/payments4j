package org.payments4j.model;

import java.util.Set;

public class OrderBuilder extends AbstractBaseModelBuilder<Order> {

  public OrderBuilder() {
    super(Order.class);
  }

  public OrderBuilder withId(String id) {
    values.put("id", id);
    return this;
  }

  public OrderBuilder withIpAddress(String ipAddress) {
    values.put("ipAddress", ipAddress);
    return this;
  }

  public OrderBuilder withCustomerName(String customerName) {
    values.put("customerName", customerName);
    return this;
  }

  public OrderBuilder withCustomerEmail(String customerEmail) {
    values.put("customerEmail", customerEmail);
    return this;
  }

  public OrderBuilder withInvoiceNumber(String invoiceNumber) {
    values.put("invoiceNumber", invoiceNumber);
    return this;
  }

  public OrderBuilder withMerchant(String merchant) {
    values.put("merchant", merchant);
    return this;
  }

  public OrderBuilder withDescription(String description) {
    values.put("description", description);
    return this;
  }

  public OrderBuilder withBillingAddress(Address billingAddress) {
    values.put("billingAddress", billingAddress);
    return this;
  }

  public OrderBuilder withShippingAddress(Address shippingAddress) {
    values.put("shippingAddress", shippingAddress);
    return this;
  }

  public OrderBuilder withOrderLines(Set<OrderLine> orderLines) {
    values.put("orderLines", orderLines);
    return this;
  }
}
