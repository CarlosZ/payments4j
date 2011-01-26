package org.payments4j.spi.payflowpro.converter;

import org.payments4j.spi.payflowpro.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class CredentialsConverter extends AbstractBaseConverter {

  private String username;
  private String password;

  public CredentialsConverter(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public List<Pair> toParamsList() {
    List<Pair> paramsList = new ArrayList<Pair>();
    paramsList.add(new Pair("USER", username));
    paramsList.add(new Pair("VENDOR", username));
    paramsList.add(new Pair("PWD", password));
    paramsList.add(new Pair("PARTNER", "PayPal"));
    return paramsList;
  }
}
