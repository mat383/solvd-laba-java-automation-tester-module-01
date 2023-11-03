package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;


public abstract class Contract {

    private String description;

    public Contract(String description) {
        this.description = description;
    }

    public Contract() {
    }

    /**
     * calculate owned money between dates based on clocked time
     */
    public abstract BigDecimal calculateTotalPrice(LegalCase legalCase);

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
