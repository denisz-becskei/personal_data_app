package com.deniszbecskei.personaldata.person;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Person {
    private Identifier identifier;
    private HumanName name;
    private ContactPoint telecom;
    private String gender;
    private Date birthDate;
    private Address address;
    private int photo;
    private String managingOrganization;
    private boolean active;

    private String birthDateString;

    public Person() { }

    public Identifier getIdentifier() {
        return identifier;
    }

    public HumanName getName() {
        return name;
    }

    public ContactPoint getTelecom() {
        return telecom;
    }

    public String getGender() { return gender; }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getBirthDateString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(this.birthDate);
    }

    public Address getAddress() {
        return address;
    }

    public int getPhoto() {
        return photo;
    }

    public String getManagingOrganization() {
        return managingOrganization;
    }

    public boolean isActive() {
        return active;
    }

    public void setName(HumanName name) {
        this.name = name;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public void setIdentifier(Identifier identifier) {
        this.identifier = identifier;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setTelecom(ContactPoint telecom) {
        this.telecom = telecom;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setManagingOrganization(String managingOrganization) {
        this.managingOrganization = managingOrganization;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setBirthDateString(String birthDateString) {
        this.birthDateString = birthDateString;
    }

    @Override
    public String toString() {
        return "Person{" +
                "identifier='" + identifier + '\'' +
                ", name=" + name +
                ", telecom=" + telecom +
                ", gender='" + gender + '\'' +
                ", birthDate=" + birthDate +
                ", address=" + address +
                ", photo=" + photo +
                ", managingOrganization='" + managingOrganization + '\'' +
                ", active=" + active +
                '}';
    }
}
