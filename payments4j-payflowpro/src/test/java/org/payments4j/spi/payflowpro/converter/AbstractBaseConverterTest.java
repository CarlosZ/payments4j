package org.payments4j.spi.payflowpro.converter;

import org.junit.Test;
import org.payments4j.spi.payflowpro.Pair;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

/**
 *
 */
public abstract class AbstractBaseConverterTest {

  @Test
  public void testToParamsList() throws Exception {
    AbstractBaseConverter converter = buildConverter();

    List<Pair> params = converter.toParamsList();

    assertThat(params).containsOnly((Object[])expectedParams());
  }

  protected abstract Pair[] expectedParams();

  protected abstract AbstractBaseConverter buildConverter();
}
