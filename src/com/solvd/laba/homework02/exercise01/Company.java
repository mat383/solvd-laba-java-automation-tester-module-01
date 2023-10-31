package com.solvd.laba.homework02.exercise01;

public class Company extends Entity {
    private String companyName;

    // TODO add other constructors
    public Company(String companyName) {
        super();
        if( companyName == null ) {
            throw new IllegalArgumentException("companyName cannot be null");
        }
        this.companyName = companyName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if( companyName == null ) {
            throw new IllegalArgumentException("companyName cannot be null");
        }
        this.companyName = companyName;
    }

    @Override
    public String getFullName() {
        return this.companyName;
    }
}
