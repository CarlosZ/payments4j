package org.payments4j.spi.cybersource;

import org.apache.ws.security.WSPasswordCallback;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.security.auth.callback.Callback;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientPasswordHandlerTest {

  @Mock
  private WSPasswordCallback mockPasswordCallback;
  @Mock
  private Callback mockOtherCallback;

  @Test
  public void testHandle() throws Exception {
    ClientPasswordHandler handler = new ClientPasswordHandler("some_key");

    handler.handle(new Callback[]{mockPasswordCallback, mockOtherCallback });

    verify(mockPasswordCallback).setPassword("some_key");
    verifyZeroInteractions(mockOtherCallback);
  }
}
