package org.payments4j.model;

public class OrderLineBuilder extends AbstractBaseModelBuilder<OrderLine> {

  public OrderLineBuilder() {
    super(OrderLine.class);
  }

  public OrderLineBuilder withId(String id) {
    values.put("id", id);
    return this;
  }

  public OrderLineBuilder withUnitPrice(Money unitPrice) {
    values.put("unitPrice", unitPrice);
    return this;
  }

  public OrderLineBuilder withQuantity(long quantity) {
    values.put("quantity", quantity);
    return this;
  }

  public OrderLineBuilder withDescription(String description) {
    values.put("description", description);
    return this;
  }
}
