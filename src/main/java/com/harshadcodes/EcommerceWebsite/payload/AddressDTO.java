package com.harshadcodes.EcommerceWebsite.payload;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

    private String roomNo;
    private String building;
    private String street;
    private String city;
    private String pincode;
    private String  country;



}
