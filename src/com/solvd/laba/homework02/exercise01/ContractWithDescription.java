package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;


public abstract class ContractWithDescription implements IContract {

    private String description = "";

    public ContractWithDescription(String description) {
        this.description = description;
    }

    public ContractWithDescription() {
        this("");
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
