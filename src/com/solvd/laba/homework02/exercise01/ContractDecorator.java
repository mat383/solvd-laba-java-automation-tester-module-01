package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;

public abstract class ContractDecorator implements IContract {
    final protected IContract contract;

    public ContractDecorator(IContract contract) {
        this.contract = contract;
    }

    public abstract BigDecimal calculateTotalPrice(LegalCase legalCase);

    public String getDescription() {
        return contract.getDescription();
    }

}
