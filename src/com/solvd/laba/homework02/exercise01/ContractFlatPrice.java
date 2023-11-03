package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;

public class ContractFlatPrice extends Contract {
    private BigDecimal fee;

    public ContractFlatPrice(String description, BigDecimal fee) {
        super(description);
        this.fee = fee;
    }

    public ContractFlatPrice(BigDecimal fee) {
        this.fee = fee;
    }

    public ContractFlatPrice(String fee) {
        this.fee = new BigDecimal(fee);
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    @Override
    public BigDecimal calculateTotalPrice(LegalCase legalCase) {
        return this.fee;
    }
}
