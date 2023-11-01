package com.solvd.laba.homework02.exercise01;

import java.util.ArrayList;
import java.util.List;

public class LegalCase {
    String description;
    boolean open;

    /**
     * all entities related to case
     */
    private ArrayList<Entity> entities;
    private ArrayList<Appointment> appointments;
    private ArrayList<ClockedTime> clockedTimes;

    public LegalCase(String description, boolean open, List<Entity> entities, List<Appointment> appointments, List<ClockedTime> clockedTimes) {
        if(description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        this.description = description;
        this.open = open;
        this.entities = new ArrayList<>(entities);
        this.appointments = new ArrayList<>(appointments);
        this.clockedTimes = new ArrayList<>(clockedTimes);
    }
    public LegalCase(String description, boolean open) {
        if(description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        this.description = description;
        this.open = open;
        this.entities = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.clockedTimes = new ArrayList<>();
    }

    public LegalCase(String description) {
        if(description == null) {
            throw new IllegalArgumentException("description cannot be null");
        }
        this.description = description;
        this.open = true;
        this.entities = new ArrayList<>();
        this.appointments = new ArrayList<>();
        this.clockedTimes = new ArrayList<>();
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }

    public ArrayList<ClockedTime> getClockedTimes() {
        return clockedTimes;
    }

    public void addClockedTime(ClockedTime clockedTime) {
        this.clockedTimes.add(clockedTimes);
    }

    public void removeClockedTime(ClockedTime clockedTime) {
        this.clockedTimes.remove(clockedTimes);
    }
}
