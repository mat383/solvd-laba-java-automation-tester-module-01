package com.solvd.laba.homework02.exercise01;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

public class TimeSpan {
    private Optional<LocalDateTime> start;
    private Optional<LocalDateTime> end;

    public TimeSpan(LocalDateTime start, LocalDateTime end) {
        if ((start != null && end != null)
                && start.isAfter(end)) {
            throw new TimeSpanHasNegativeDurationError("Start time have to be before end time");
        }
        this.start = Optional.ofNullable(start);
        this.end = Optional.ofNullable(end);
    }


    public Optional<LocalDateTime> getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        if ((start != null && this.end.isPresent())
                && start.isAfter(this.end.get())) {
            throw new TimeSpanHasNegativeDurationError("Start time have to be before end time");
        }
        this.start = Optional.ofNullable(start);
    }

    public final boolean hasStart() {
        return this.start.isPresent();
    }

    public Optional<LocalDateTime> getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        if ((this.start.isPresent() && end != null)
                && this.start.get().isAfter(end)) {
            throw new TimeSpanHasNegativeDurationError("Start time have to be before end time");
        }
        this.end = Optional.ofNullable(end);
    }

    public final boolean hasEnd() {
        return this.end.isPresent();
    }

    public final boolean isInfinite() {
        return !(hasStart() && hasEnd());
    }

    /**
     * returns duration of TimeSpan, requires that underlying interval is finite
     * otherwise throws TimeSpanIsInfinite
     *
     * @return
     */
    public Duration duration() {
        if (isInfinite()) {
            throw new TimeSpanIsInfiniteException("To get duration TimeSpan has to be finite.");
        }
        return Duration.between(this.start.get(), this.end.get());
    }


    public boolean startsAfter(LocalDateTime dateTime) {
        if (!hasStart()) {
            return false;
        } else {
            return this.start.get().isAfter(dateTime);
        }
    }


    public boolean startsAfterNow() {
        return startsAfter(LocalDateTime.now());
    }
}
