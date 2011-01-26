package org.payments4j.spi.payflowpro.converter;

import org.payments4j.spi.payflowpro.Pair;

/**
 *
 */
public class CredentialsConverterTest extends AbstractBaseConverterTest {

  @Override
  protected AbstractBaseConverter buildConverter() {
    return new CredentialsConverter("some_username", "some_password");
  }

  @Override
  protected Pair[] expectedParams() {
    return new Pair[]{
        new Pair("USER", "some_username"),
        new Pair("VENDOR", "some_username"),
        new Pair("PWD", "some_password"),
        new Pair("PARTNER", "PayPal")
    };
  }
}
