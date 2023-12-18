package com.solvd.laba.homework02.exercise01;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

public class Company implements IEntity {
    private String name;
    private Type companyType;
    private final String id;

    public Company(String name, Type companyType, String id) {
        if (StringUtils.isBlank(name)) {
            throw new IllegalArgumentException("name cannot be blank nor null");
        }
        if (companyType == null) {
            throw new IllegalArgumentException("companyType cannot be null");
        }
        if (StringUtils.isBlank(id)) {
            throw new IllegalArgumentException("id cannot be blank nor null");
        }

        this.name = name;
        this.companyType = companyType;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.name
                + " " + this.companyType.getAbbreviation()
                + " (" + this.id + ")";
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
        if (this.id.equals(other.getId())) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getCompanyType() {
        return companyType;
    }

    public void setCompanyType(Type companyType) {
        this.companyType = companyType;
    }

    public String getId() {
        return id;
    }


    @Override
    public String getFullName() {
        return this.name;
    }

    public enum Type {
        GENERAL_PARTNERSHIP("General Partnership", "GP"),
        LIMITED_PARTNERSHIP("Limited Partnership", "LP"),
        JOINT_VENTURE("Joint Venture", "JV"),
        LIMITED_LIABILITY_COMPANY("Limited Liability Company", "LLC"),
        STANDARD_CORPORATION("Standard Corporation", "C-Corp"),
        SMALL_CORPORATION("Small Corporation", "S-Corp"),
        QUALIFIED_SUBCHAPTER_S_SUBSIDIARY("Qualified Subchapter S Subsidiary", "QSSS");

        private final String fullName;
        private final String abbreviation;

        Type(String fullName, String abbreviation) {
            this.fullName = fullName;
            this.abbreviation = abbreviation;
        }

        @Override
        public String toString() {
            return this.fullName;
        }

        public String getAbbreviation() {
            return this.abbreviation;
        }
    }
}
