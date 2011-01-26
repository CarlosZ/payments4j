package org.payments4j.spi.payflowpro;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import static java.lang.String.format;

/**
 *
 */
public class Pair {

  private String name;
  private String value;

  public Pair(String name, String value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder()
        .append(this.name)
        .append(this.value)
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

    Pair that = (Pair) o;

    return new EqualsBuilder()
        .append(this.name, that.name)
        .append(this.value, that.value)
        .isEquals();
  }

  @Override
  public String toString() {
    return format("%s[%s]=%s", name, value.length(), value);
  }
}
