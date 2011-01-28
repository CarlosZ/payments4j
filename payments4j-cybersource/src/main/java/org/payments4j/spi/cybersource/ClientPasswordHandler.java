package org.payments4j.spi.cybersource;

import org.apache.ws.security.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

/**
* <code>CallbackHandler</code> implementation that set's the password for CXF web services clients.
*/
public class ClientPasswordHandler implements CallbackHandler {

  private String key;

  public ClientPasswordHandler(String key) {
    this.key = key;
  }

  @Override
  public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for (Callback callback : callbacks) {
      if (callback instanceof WSPasswordCallback) {
        WSPasswordCallback passwordCallback = (WSPasswordCallback) callback;
        passwordCallback.setPassword(key);
      }
    }
  }
}
