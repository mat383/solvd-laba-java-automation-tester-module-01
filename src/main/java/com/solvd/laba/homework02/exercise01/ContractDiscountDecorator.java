package com.solvd.laba.homework02.exercise01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;

public class ContractDiscountDecorator extends ContractDecorator {
    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    /**
     * formula used to calculate discount: price * (1 - discount)
     * could be used to increase price if set to nevative value
     */
    private final BigDecimal discount;

    /**
     * modify contract's price by given discount. formula: price * (1 - discount)
     *
     * @param contract contract to modify price
     * @param discount by what modify price. formula: price * (1 - discount)
     */
    public ContractDiscountDecorator(IContract contract, BigDecimal discount) {
        super(contract);
        this.discount = discount;
    }

    @Override
    public BigDecimal calculateTotalPrice(LegalCase legalCase) {
        BigDecimal modifier = BigDecimal.valueOf(1).subtract(this.discount);
        BigDecimal unmodifiedCost = contract.calculateTotalPrice(legalCase);
        BigDecimal modifiedCost = unmodifiedCost.multiply(modifier);
        LOGGER.info("Calculating contract with discount(" + this.discount
                + "): before: " + unmodifiedCost
                + " after: " + modifiedCost
                + " modifier: " + modifier);
        return modifiedCost;
    }
}
