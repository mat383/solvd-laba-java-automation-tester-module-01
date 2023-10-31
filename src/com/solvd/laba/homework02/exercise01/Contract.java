package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public abstract class Contract {
    /**
     * first day of contract
     */
    private LocalDate startDate;
    /**
     * last day of the contract
     */
    private LocalDate endDate;

    private String description;

    public Contract(LocalDate startDate, LocalDate endDate, String description) {
        if (startDate == null) {
            throw new IllegalArgumentException("Parameter 'startDate' cannot be null");
        }
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    /**
     * calculate owned money between dates based on clocked time
     * @param start
     * @param end
     * @param time
     * @return amount of money owned
     */
    public abstract BigDecimal calculateOwned(LocalDate start, LocalDate end, List<ClockedTime> time);

    public boolean isActive() {
        return isActive(LocalDate.now());
    }

    public boolean isActive(LocalDate date) {
        if (this.endDate == null) {
            return !this.startDate.isAfter(date);
        } else {
            return !(this.startDate.isAfter(date)
                    || this.endDate.isBefore(date));
        }
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("Parameter 'startDate' cannot be null");
        }
        if (startDate.isAfter(this.endDate)) {
            throw new IllegalArgumentException("Start time have to be before or equal to end time");
        }
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        if (endDate == null) {
            throw new IllegalArgumentException("Parameter 'endDate' cannot be null");
        }
        if (endDate.isBefore(this.startDate)) {
            throw new IllegalArgumentException("End time have to be after or equal to end time");
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
