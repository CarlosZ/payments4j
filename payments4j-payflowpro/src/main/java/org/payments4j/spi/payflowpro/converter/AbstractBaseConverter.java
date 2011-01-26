package org.payments4j.spi.payflowpro.converter;

import org.payments4j.spi.payflowpro.Pair;

import java.util.List;

/**
 *
 */
public abstract class AbstractBaseConverter {

  public abstract List<Pair> toParamsList();

  protected void addPair(List<Pair> params, String name, String value) {
    if (value != null) {
      params.add(new Pair(name, value));
    }
  }
}
