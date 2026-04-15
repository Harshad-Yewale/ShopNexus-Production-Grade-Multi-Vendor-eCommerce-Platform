package com.harshadcodes.EcommerceWebsite.controller;


import com.harshadcodes.EcommerceWebsite.payload.AddressDTO;
import com.harshadcodes.EcommerceWebsite.service.AddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;


    @PostMapping("/addresses")
    public ResponseEntity<AddressDTO> createAddress(@Valid @RequestBody AddressDTO addressDTO) throws Exception {
        AddressDTO savedAddressDTO=addressService.saveAddress(addressDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedAddressDTO);
    }

    @GetMapping("/addresses")
    public ResponseEntity<List<AddressDTO>> getAllAddresses() throws Exception {
        List<AddressDTO> addressDTOList =addressService.getAllAddresses();
        return ResponseEntity.status(HttpStatus.OK).body(addressDTOList);
    }

    @GetMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> getAddressById(@PathVariable Long addressId) throws Exception {
        AddressDTO addressDTO=addressService.getAddressById(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(addressDTO);
    }

    @GetMapping("/users/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressByUser() throws Exception {
        List<AddressDTO> addressDTOList=addressService.getAddressByUser();
        return ResponseEntity.status(HttpStatus.OK).body(addressDTOList);
    }

    @PutMapping("/addresses/{addressId}")
    public ResponseEntity<AddressDTO> updateAddress(@PathVariable Long addressId,
                                                    @Valid @RequestBody AddressDTO addressDTO) throws Exception {
        AddressDTO updatedAddressDTO=addressService.updateAddress(addressId,addressDTO);
        return ResponseEntity.status(HttpStatus.OK).body(updatedAddressDTO);
    }

    @DeleteMapping("/addresses/{addressId}")
    public ResponseEntity<String> deleteAddress(@PathVariable Long addressId) throws Exception {
        String message=addressService.deleteAddress(addressId);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }
}
