package com.solvd.laba.homework02.exercise01;

import java.util.Objects;

public class Company extends Entity {
    private String companyName;

    // TODO add other constructors
    public Company(String companyName) {
        super();
        if (companyName == null) {
            throw new IllegalArgumentException("companyName cannot be null");
        }
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return this.companyName;
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
        if (this.companyName.equals(other.getCompanyName())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.companyName);
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null) {
            throw new IllegalArgumentException("companyName cannot be null");
        }
        this.companyName = companyName;
    }

    @Override
    public String getFullName() {
        return this.companyName;
    }
}
