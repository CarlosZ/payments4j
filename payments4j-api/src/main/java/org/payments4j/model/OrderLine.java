package org.payments4j.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 */
public class OrderLine {

  private String id;
  private Money unitPrice;
  private long quantity;
  private String description;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Money getUnitPrice() {
    return unitPrice;
  }

  public void setUnitPrice(Money unitPrice) {
    this.unitPrice = unitPrice;
  }

  public long getQuantity() {
    return quantity;
  }

  public void setQuantity(long quantity) {
    this.quantity = quantity;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.id)
        .append(this.unitPrice)
        .append(this.quantity)
        .append(this.description)
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

    OrderLine that = (OrderLine) o;

    return new EqualsBuilder()
        .append(this.id, that.id)
        .append(this.unitPrice, that.unitPrice)
        .append(this.quantity, that.quantity)
        .append(this.description, that.description)
        .isEquals();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", this.id)
        .append("unitPrice", this.unitPrice)
        .append("quantity", this.quantity)
        .append("description", this.description)
        .toString();
  }
}
