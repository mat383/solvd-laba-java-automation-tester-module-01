package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public class PerHourContract extends Contract {
        private BigDecimal paymentPerHour;

        PerHourContract(LocalDate start, LocalDate end, String description, BigDecimal paymentPerHour) {
            super(start, end, description);
            if (paymentPerHour == null) {
                throw new IllegalArgumentException("Parameter 'paymentAmount' cannot be null");
            }
            this.paymentPerHour = paymentPerHour;
        }

        public BigDecimal getPaymentPerHour() {
            return paymentPerHour;
        }

        public void setPaymentPerHour(BigDecimal paymentPerHour) {
            this.paymentPerHour = paymentPerHour;
        }

        @Override
        public BigDecimal calculateOwned(LocalDate start, LocalDate end, List<ClockedTime> time) {
            long totalDuration = 0;
            for( ClockedTime c : time ) {
                totalDuration += c.duration().toHours();
            }

            return this.paymentPerHour.multiply(new BigDecimal(totalDuration));
        }
}
