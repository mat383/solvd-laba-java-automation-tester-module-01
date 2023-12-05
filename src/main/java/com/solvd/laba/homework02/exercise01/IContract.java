package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;

public interface IContract {
    public abstract BigDecimal calculateTotalPrice(LegalCase legalCase);

    public String getDescription();
}

