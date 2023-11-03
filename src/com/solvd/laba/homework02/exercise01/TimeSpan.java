package com.solvd.laba.homework02.exercise01;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeSpan {

    private LocalDateTime start;
    private LocalDateTime end;

    public TimeSpan(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Neither start nor end time can be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time have to be before end time");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        if (start == null) {
            throw new IllegalArgumentException("Parameter 'start' cannot be null");
        }
        if (start.isAfter(this.end)) {
            throw new IllegalArgumentException("Start time have to be before end time");
        }
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        if (end == null) {
            throw new IllegalArgumentException("Parameter 'end' cannot be null");
        }
        if (this.start.isAfter(end)) {
            throw new IllegalArgumentException("Start time have to be before end time");
        }
        this.end = end;
    }

    public Duration duration() {
        return Duration.between(this.start, this.end);
    }

    /**
     * checks if TimeSpan starts after relativeTo parameter
     */
    public boolean inFuture(LocalDateTime relativeTo) {
        return this.start.isAfter(relativeTo);
    }

    /**
     * checks if TimeSpan starts after current time
     */
    public boolean inFuture() {
        return inFuture(LocalDateTime.now());
    }
}
