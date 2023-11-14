package com.solvd.laba.homework02.exercise01;

import java.util.Objects;

public class Person implements IEntity {

    private final String id;
    private String firstName;
    private String lastName;

    // TODO add other constructors
    public Person(String id, String firstName, String lastName) {
        super();
        // TODO add checking for empty name string
        if (firstName == null) {
            throw new IllegalArgumentException("firstName cannot be null");
        }
        if (lastName == null) {
            throw new IllegalArgumentException("lastName cannot be null");
        }
        if (firstName.isEmpty() && lastName.isEmpty()) {
            throw new IllegalArgumentException("firstName and lastName cannot both be empty");
        }
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        if (id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be empty");
        }
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " (" + this.id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Person)) {
            return false;
        }

        Person other = (Person) obj;
        if (this.id.equals(other.getId())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null) {
            throw new IllegalArgumentException("firstName cannot be null");
        }
        if (firstName.isEmpty() && lastName.isEmpty()) {
            throw new IllegalArgumentException("firstName and lastName cannot both be empty");
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
        if (firstName.isEmpty() && lastName.isEmpty()) {
            throw new IllegalArgumentException("firstName and lastName cannot both be empty");
        }
        this.lastName = lastName;
    }

    @Override
    public String getFullName() {
        return this.firstName + " " + this.lastName;
    }


}
