package com.solvd.laba.homework02.exercise01;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment extends TimeSpan {
    public enum Type {
        COURT,
        CONSULTATION
    }

    private Appointment.Type type;
    private Address location;
    private String details;
    private ArrayList<IEntity> participants = new ArrayList<>();

    public Appointment(Appointment.Type type, LocalDateTime start, LocalDateTime end,
                       Address location, String details, List<IEntity> participants) {
        this(type, start, end, location, details);
        this.participants.addAll(participants);
    }

    public Appointment(Appointment.Type type, LocalDateTime start, LocalDateTime end,
                       Address location, String details) {
        super(start, end);
        this.type = type;
        this.location = location;
        this.details = details;
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

    public ArrayList<IEntity> getParticipants() {
        return participants;
    }

    public void addParticipant(IEntity participant) {
        if (haveParticipant(participant)) {
            throw new IllegalArgumentException("Appointments already have participant '" + participant + "'");
        }
        this.participants.add(participant);
    }

    public void removeParticipant(IEntity participant) {
        this.participants.remove(participant);
    }

    public boolean haveParticipant(IEntity participant) {
        return this.participants.contains(participant);
    }
}
