package com.solvd.laba.homework02.exercise01;

import java.nio.file.attribute.UserPrincipalLookupService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Appointment extends TimeSpan {
    private Address location;
    private String details;
    private ArrayList<Entity> participants;

    public Appointment(LocalDateTime start, LocalDateTime end,
                       Address location, String details, List<Entity> participants) {
        super(start, end);
        this.location = location;
        this.details = details;
        this.participants = new ArrayList<>(participants);
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

    public ArrayList<Entity> getParticipants() {
        return participants;
    }

    public void addParticipant(Entity participant) {
        this.participants.add(participant);
    }
    public void removeParticipant(Entity participant) {
        this.participants.remove(participant);
    }
}
