package com.harshadcodes.EcommerceWebsite.service;

import com.harshadcodes.EcommerceWebsite.exceptions.ResourceNotFoundException;
import com.harshadcodes.EcommerceWebsite.model.Address;
import com.harshadcodes.EcommerceWebsite.model.User;
import com.harshadcodes.EcommerceWebsite.payload.AddressDTO;
import com.harshadcodes.EcommerceWebsite.repositories.AddressRepository;
import com.harshadcodes.EcommerceWebsite.utils.AuthUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService{


    private final AuthUtils authUtils;
    private final ModelMapper modelMapper;
    private final AddressRepository addressRepository;

    @Transactional
    @Override
    public AddressDTO saveAddress(AddressDTO addressDTO) throws Exception {

        Address newAddress=modelMapper.map(addressDTO,Address.class);
        User user=authUtils.getLoggedinUser();
        if (user == null) {
            throw new Exception("User not logged in");
        }
        newAddress.setUser(user);
        user.getAddresses().add(newAddress);
        Address newSavedAddress=addressRepository.save(newAddress);
        return modelMapper.map(newSavedAddress,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddresses() throws Exception {
        List<Address> savedAddresses=addressRepository.findAll();

        if(savedAddresses.isEmpty()){
            throw new Exception("no Addresses found");
        }

        List<AddressDTO> addressDTOList=savedAddresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class) )
                .collect(Collectors.toList());

        return addressDTOList;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) throws Exception {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        User user=authUtils.getLoggedinUser();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new Exception("You are not allowed to access this address");
        }

        return modelMapper.map(address,AddressDTO.class);
    }

    @Transactional
    @Override
    public List<AddressDTO> getAddressByUser() throws Exception {
        User user=authUtils.getLoggedinUser();
        if(user==null){
            throw new Exception("user not logged in");
        }
        List<Address> savedUserAddresses=user.getAddresses();

        List<AddressDTO> addressDTOList=savedUserAddresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class))
                .collect(Collectors.toList());

        return addressDTOList;
    }

    @Transactional
    @Override
    public AddressDTO updateAddress(Long addressId, AddressDTO addressDTO) throws Exception {
        Address address=addressRepository.findById(addressId)
                .orElseThrow(()-> new ResourceNotFoundException("Address","addressId",addressId));

        User user=authUtils.getLoggedinUser();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new Exception("You cannot update this address");
        }
        address.setRoomNo(addressDTO.getRoomNo());
        address.setBuilding(addressDTO.getBuilding());
        address.setStreet(addressDTO.getStreet());
        address.setCity(addressDTO.getCity());
        address.setCountry(addressDTO.getCountry());
        address.setPincode(addressDTO.getPincode());

        Address updatedAddress=addressRepository.save(address);
        return modelMapper.map(updatedAddress,AddressDTO.class);
    }

    @Transactional
    @Override
    public String deleteAddress(Long addressId) throws Exception {

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "addressId", addressId));

        User user = authUtils.getLoggedinUser();
        if (!address.getUser().getId().equals(user.getId())) {
            throw new Exception("You cannot delete this address");
        }

        addressRepository.delete(address);
        return "Address deleted successfully with addressId " + addressId;
    }
}
