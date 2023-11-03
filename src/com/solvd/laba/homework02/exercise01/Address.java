package com.solvd.laba.homework02.exercise01;

import java.util.Objects;

public class Address {
    private String country;
    private String city;
    private String postalCode;
    private String street;
    private String streetNumber;
    private String apartmentNumber;

    public Address(String country, String city, String postalCode,
                   String street, String streetNumber, String apartmentNumber) {
        this.country = country;
        this.city = city;
        this.postalCode = postalCode;
        this.street = street;
        this.streetNumber = streetNumber;
        this.apartmentNumber = apartmentNumber;

        if (!this.isAddresValid()) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
    }

    @Override
    public String toString() {
        return this.country
                + ", " + this.city
                + " " + this.postalCode
                + ", street: " + this.street
                + " " + this.streetNumber
                + " / " + this.apartmentNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Address)) {
            return false;
        }
        Address other = (Address) obj;
        if (this.country.equals(other.getCountry())
                && this.city.equals(other.getCity())
                && this.postalCode.equals(other.getPostalCode())
                && this.street.equals(other.getStreet())
                && this.streetNumber.equals(other.getStreetNumber())
                && this.apartmentNumber.equals(other.getApartmentNumber())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.country, this.city, this.postalCode,
                this.street, this.streetNumber, this.apartmentNumber);
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        if (!isAddresValid(country, this.city, this.postalCode,
                this.street, this.streetNumber, this.apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        if (!isAddresValid(this.country, city, this.postalCode,
                this.street, this.streetNumber, this.apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        if (!isAddresValid(this.country, this.city, postalCode,
                this.street, this.streetNumber, this.apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        if (!isAddresValid(this.country, this.city, this.postalCode,
                street, this.streetNumber, this.apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.street = street;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        if (!isAddresValid(this.country, this.city, this.postalCode,
                this.street, streetNumber, this.apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.streetNumber = streetNumber;
    }

    public String getApartmentNumber() {
        return apartmentNumber;
    }

    public void setApartmentNumber(String apartmentNumber) {
        if (!isAddresValid(this.country, this.city, this.postalCode,
                this.street, this.streetNumber, apartmentNumber)) {
            throw new IllegalArgumentException("Passed address is invalid");
        }
        this.apartmentNumber = apartmentNumber;
    }

    private boolean isAddresValid() {
        // TODO consider implementing
        return isAddresValid(this.country, this.city, this.postalCode,
                this.street, this.streetNumber, this.apartmentNumber);
    }

    private boolean isAddresValid(String country, String city, String postalCode,
                                  String street, String streetNumber, String apartmentNumber) {
        // TODO consider implementing
        return true;
    }
}
