package com.solvd.laba.homework02.exercise01;

import java.util.Objects;

public class Company implements IEntity {
    private final String name;

    public Company(String name) {
        if (name == null) {
            throw new IllegalArgumentException("name cannot be null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be empty");
        }

        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Company)) {
            return false;
        }

        Company other = (Company) obj;
        if (this.name.equals(other.getName())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.name);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFullName() {
        return this.name;
    }
}
