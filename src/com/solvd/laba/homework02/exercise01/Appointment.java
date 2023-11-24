package com.solvd.laba.homework02.exercise01;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Appointment {
    public enum Type {
        COURT,
        CONSULTATION
    }

    private Appointment.Type type;
    private TimeSpan timeSpan;
    private Address location;
    private String details;
    private List<IEntity> participants;

    public Appointment(Appointment.Type type, LocalDateTime start, LocalDateTime end,
                       Address location, String details, List<IEntity> participants) {
        this.type = type;
        this.location = location;
        this.timeSpan = new TimeSpan(start, end);
        if (this.timeSpan.isInfinite()) {
            throw new TimeSpanIsInfiniteException("Appointment TimeSpan have to be finite");
        }
        this.details = details;
        this.participants = new ArrayList<>(participants.size());

        for (IEntity entity : participants) {
            addParticipant(entity);
        }

    }

    public Appointment(Appointment.Type type, LocalDateTime start, LocalDateTime end,
                       Address location, String details) {
        this(type, start, end, location, details, Collections.emptyList());
    }


    @Override
    public String toString() {
        return "(" + getStart() + " - " + getEnd() + ")"
                + " " + this.type
                + " " + this.details
                + " at " + this.location;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Appointment)) {
            return false;
        }
        Appointment other = (Appointment) obj;
        if (this.type.equals(other.getType())
                && this.location.equals(other.getLocation())
                && this.details.equals(other.getDetails())
                && this.participants.equals(other.getParticipants())) {
            return true;
        }

        return false;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public LocalDateTime getStart() {
        return this.timeSpan.getStart().get();
    }

    public void setStart(LocalDateTime start) {
        if (start == null) {
            throw new TimeSpanIsInfiniteException("Appointment timeSpan cannot be infinite (start cannot be null).");
        }
        this.timeSpan.setStart(start);
    }

    public LocalDateTime getEnd() {
        return this.timeSpan.getEnd().get();
    }

    public void setEnd(LocalDateTime end) {
        if (end == null) {
            throw new TimeSpanIsInfiniteException("Appointment timeSpan cannot be infinite (end cannot be null).");
        }
        this.timeSpan.setEnd(end);
    }

    public Duration getDuration() {
        return this.timeSpan.duration();
    }

    public boolean startsAfter(LocalDateTime dateTime) {
        return getStart().isAfter(dateTime);
    }


    public boolean startsAfterNow() {
        return startsAfter(LocalDateTime.now());
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public List<IEntity> getParticipants() {
        return Collections.unmodifiableList(participants);
    }

    // used in constructor
    public final void addParticipant(IEntity participant) {
        if (haveParticipant(participant)) {
            throw new EntityAlreadyAddedException("Appointments already have participant '" + participant + "'");
        }
        this.participants.add(participant);
    }

    public void removeParticipant(IEntity participant) {
        this.participants.remove(participant);
    }

    // used in constructor
    public final boolean haveParticipant(IEntity participant) {
        return this.participants.contains(participant);
    }
}
