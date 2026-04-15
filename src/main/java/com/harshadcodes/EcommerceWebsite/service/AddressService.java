package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.payload.AddressDTO;
import jakarta.validation.Valid;

import java.util.List;

public interface AddressService {
    AddressDTO saveAddress(@Valid AddressDTO addressDTO) throws Exception;

    List<AddressDTO> getAllAddresses() throws Exception;

    AddressDTO getAddressById(@Valid Long addressId) throws Exception;

    List<AddressDTO> getAddressByUser() throws Exception;

    AddressDTO updateAddress(@Valid Long addressId, AddressDTO addressDTO) throws Exception;

    String deleteAddress(@Valid Long addressId) throws Exception;
}
