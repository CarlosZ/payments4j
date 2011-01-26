package org.payments4j.spi.payflowpro;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.WrapDynaBean;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.payments4j.core.TransactionResponse;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

/**
 * Response Handler for Apache's HTTP Client, that parses the response into
 * a <code>TransactionResponse</code>
 */
public class PayFlowResponseHandler implements ResponseHandler<TransactionResponse> {

  private static final Multimap<String, String> nameToProperty = new ImmutableMultimap.Builder<String, String>()
      .putAll("PNREF", "authorizationId")
      .putAll("RESULT", "code", "reasonCode")
      .putAll("RESPMSG", "message")
      .build();

  @Override
  public TransactionResponse handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
    BasicResponseHandler basicResponseHandler = new BasicResponseHandler();
    String responseString = basicResponseHandler.handleResponse(response);
    TransactionResponse transactionResponse = buildTransactionResponse(responseString);
    if (transactionResponse.getCode() == 0) {
      transactionResponse.setSuccessful(true);
    }
    return transactionResponse;
  }

  private TransactionResponse buildTransactionResponse(String responseString) {
    TransactionResponse transactionResponse = new TransactionResponse();
    WrapDynaBean dynaBean = new WrapDynaBean(transactionResponse);
    String[] pairs = responseString.split("&");

    for (String pair : pairs) {
      String[] parts = pair.split("=");
      setProperties(dynaBean, parts[0], parts[1]);
    }

    return (TransactionResponse) dynaBean.getInstance();
  }

  private void setProperties(WrapDynaBean dynaBean, String name, String value) {
    Collection<String> properties = nameToProperty.get(name);
    if (properties != null && !properties.isEmpty()) {
      for (String property : properties) {
        try {
          Class propertyType = PropertyUtils.getPropertyType(dynaBean.getInstance(), property);
          if (propertyType == String.class) {
            dynaBean.set(property, value);
          } else {
            dynaBean.set(property, ConvertUtils.convert(value, propertyType));
          }
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        } catch (InvocationTargetException e) {
          e.printStackTrace();
        } catch (NoSuchMethodException e) {
          e.printStackTrace();
        }

      }
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    return true;
  }

  @Override
  public int hashCode() {
    return nameToProperty.hashCode();
  }
}
