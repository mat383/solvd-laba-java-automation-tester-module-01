package com.solvd.laba.homework02.exercise01;

import java.util.Objects;

public class Person extends Entity {

    private String firstName;
    private String lastName;

    // TODO add other constructors
    public Person(String firstName, String lastName) {
        super();
        // TODO add checking for empty name string
        if (firstName == null || lastName == null) {
            throw new IllegalArgumentException("Neither firstName nor lastName can be null");
        }
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.firstName, this.lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("firstName cannot be null");
        }
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        if (lastName == null) {
            throw new IllegalArgumentException("lastName cannot be null");
        }
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


}
