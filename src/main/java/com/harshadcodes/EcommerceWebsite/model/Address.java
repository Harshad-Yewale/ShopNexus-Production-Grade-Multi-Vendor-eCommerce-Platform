package com.harshadcodes.EcommerceWebsite.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "address_tbl")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;


    @Column(name = "room_no")
    private String roomNo;

    @Column(name = "building_name")
    @NotBlank(message = "Building name cannot be blank")
    private String building;

    @Column(name = "street_name")
    @NotBlank(message = "street name cannot be blank")
    private String street;

    @Column(name = "city_name")
    @NotBlank(message = "city name cannot be blank")
    private String city;

    @Column(name = "pincode")
    @NotBlank(message = "pincode name cannot be blank")
    private String pincode;

    @Column(name = "country_name")
    @NotBlank(message = "country name cannot be blank")
    private String  country;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String roomNo, String building, String street, String city, String pincode, String country) {
        this.roomNo = roomNo;
        this.building = building;
        this.street = street;
        this.city = city;
        this.pincode = pincode;
        this.country = country;
    }
}
