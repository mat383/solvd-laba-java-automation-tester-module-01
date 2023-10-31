package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class SinglePaymentContract extends Contract{
    private BigDecimal paymentAmount;

    SinglePaymentContract(LocalDate start, LocalDate end, String description, BigDecimal paymentAmount) {
        super(start, end, description);
        if (paymentAmount == null) {
            throw new IllegalArgumentException("Parameter 'paymentAmount' cannot be null");
        }
        this.paymentAmount = paymentAmount;
    }

    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(BigDecimal paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    @Override
    public BigDecimal calculateOwned(LocalDate start, LocalDate end, List<ClockedTime> time) {
        // check if contract was active in selected timespan
        if ( isActive(start) || isActive(end)) {
            return this.paymentAmount;
        } else {
            return new BigDecimal(0);
        }
    }
}
