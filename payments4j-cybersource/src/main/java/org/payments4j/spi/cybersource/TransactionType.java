package org.payments4j.spi.cybersource;

import com.cybersource.schemas.transaction_data_1.CCAuthReversalService;
import com.cybersource.schemas.transaction_data_1.CCAuthService;
import com.cybersource.schemas.transaction_data_1.CCCaptureService;
import com.cybersource.schemas.transaction_data_1.CCCreditService;
import com.cybersource.schemas.transaction_data_1.RequestMessage;

/**
*
*/
enum TransactionType {

  PURCHASE {
    @Override
    public void setServices(RequestMessage request) {
      AUTHORIZE.setServices(request);
      CAPTURE.setServices(request);
    }
  },
  AUTHORIZE{
    @Override
    public void setServices(RequestMessage request) {
      request.setCcAuthService(new CCAuthService());
      request.getCcAuthService().setRun("true");
    }
  },
  CAPTURE{
    @Override
    public void setServices(RequestMessage request) {
      request.setCcCaptureService(new CCCaptureService());
      request.getCcCaptureService().setRun("true");
    }
  },
  REVERT{
    @Override
    public void setServices(RequestMessage request) {
      request.setCcAuthReversalService(new CCAuthReversalService());
      request.getCcAuthReversalService().setRun("true");
    }
  },
  CREDIT{
    @Override
    public void setServices(RequestMessage request) {
      request.setCcCreditService(new CCCreditService());
      request.getCcCreditService().setRun("true");
    }
  };

  public abstract void setServices(RequestMessage request);
}
