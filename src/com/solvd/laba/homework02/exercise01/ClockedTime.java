package com.solvd.laba.homework02.exercise01;

import java.time.LocalDateTime;

public class ClockedTime extends TimeSpan {
    private String description;
    private boolean overtime;

    public ClockedTime(LocalDateTime start, LocalDateTime end, String description, boolean overtime) {
        super(start, end);
        this.description = description;
        this.overtime = overtime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOvertime() {
        return overtime;
    }

    public void setOvertime(boolean overtime) {
        this.overtime = overtime;
    }

}
