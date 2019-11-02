package com.app.ecommerce.models;

public class UserAlt {
    public String name, shopName, shopLocation, NID, email, phone;

    public UserAlt(){

    }

    public UserAlt(String name, String shopName, String shopLocation, String phone, String NID, String email) {
        this.name = name;
        this.shopName = shopName;
        this.shopLocation = shopLocation;
        this.NID = NID;
        this.email = email;
        this.phone = phone;
    }
}