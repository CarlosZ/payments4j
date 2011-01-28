package org.payments4j.spi.cybersource.converter;

import com.cybersource.schemas.transaction_data_1.BillTo;
import com.cybersource.schemas.transaction_data_1.ShipTo;
import org.payments4j.model.Address;

/**
 *
 */
public class AddressConverter {

  private Address address;

  public AddressConverter(Address address) {
    this.address = address;
  }

  public BillTo toBillTo() {
    BillTo billTo = new BillTo();
    billTo.setFirstName(address.getFirstName());
    billTo.setLastName(address.getLastName());
    billTo.setEmail(address.getEmail());
    billTo.setStreet1(address.getAddress1());
    billTo.setStreet2(address.getAddress2());
    billTo.setCity(address.getCity());
    billTo.setPostalCode(address.getPostalCode());
    billTo.setState(address.getState());
    billTo.setCountry(address.getCountryIsoCode());
    billTo.setPhoneNumber(address.getPhone());
    return billTo;
  }

  public ShipTo toShipTo() {
    ShipTo shipTo = new ShipTo();
    shipTo.setFirstName(address.getFirstName());
    shipTo.setLastName(address.getLastName());
    shipTo.setEmail(address.getEmail());
    shipTo.setStreet1(address.getAddress1());
    shipTo.setStreet2(address.getAddress2());
    shipTo.setCity(address.getCity());
    shipTo.setPostalCode(address.getPostalCode());
    shipTo.setState(address.getState());
    shipTo.setCountry(address.getCountryIsoCode());
    shipTo.setPhoneNumber(address.getPhone());
    return shipTo;
  }
}
