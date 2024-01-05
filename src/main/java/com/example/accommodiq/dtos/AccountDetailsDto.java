package com.example.accommodiq.dtos;

import com.example.accommodiq.domain.Account;

public class AccountDetailsDto {
    private String email;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;

    public AccountDetailsDto(Account account) {
        this.email = account.getEmail();
        this.firstName = account.getUser().getFirstName();
        this.lastName = account.getUser().getLastName();
        this.address = account.getUser().getAddress();
        this.phoneNumber = account.getUser().getPhoneNumber();
    }

    public AccountDetailsDto() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void putDetailsIntoAccount(Account accountToManage) {
        if (this.firstName != null) accountToManage.getUser().setFirstName(this.firstName);
        if (this.lastName != null) accountToManage.getUser().setLastName(this.lastName);
        if (this.address != null) accountToManage.getUser().setAddress(this.address);
        if (this.phoneNumber != null) accountToManage.getUser().setPhoneNumber(this.phoneNumber);

    }
}
