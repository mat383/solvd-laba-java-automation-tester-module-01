package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LegalCase {
    Contract contract;
    // TODO add case name
    String description;
    boolean open;

    /**
     * all entities related to case
     */
    private final ArrayList<Entity> clients = new ArrayList<>();
    private final ArrayList<Appointment> appointments = new ArrayList<>();
    private final ArrayList<LegalService> services = new ArrayList<>();

    public LegalCase(Contract contract, String description, boolean open, List<Entity> clients, List<Appointment> appointments) {
        if (contract == null) {
            throw new IllegalArgumentException("contract cannot be null");
        }
        this.contract = contract;
        this.description = description;
        this.open = open;
        this.clients.addAll(clients);
        this.appointments.addAll(appointments);
    }

    public LegalCase(Contract contract, String description, boolean open) {
        if (contract == null) {
            throw new IllegalArgumentException("contract cannot be null");
        }
        this.contract = contract;
        this.description = description;
        this.open = open;
    }

    public LegalCase(Contract contract, String description) {
        if (contract == null) {
            throw new IllegalArgumentException("contract cannot be null");
        }
        this.contract = contract;
        this.description = description;
        this.open = true;
    }


    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
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

    public List<Entity> getClients() {
        return Collections.unmodifiableList(clients);
    }

    public void addClient(Entity client) {
        this.clients.add(client);
    }

    public void removeClient(Entity client) {
        this.clients.remove(client);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public List<Appointment> getFutureAppointments() {
        return this.appointments.stream()
                .filter(Appointment::inFuture)
                .collect(Collectors.toUnmodifiableList());
    }

    public void addAppointment(Appointment appointment) {
        this.appointments.add(appointment);
    }

    public void removeAppointment(Appointment appointment) {
        this.appointments.remove(appointment);
    }


    public List<LegalService> getServices() {
        return Collections.unmodifiableList(services);
    }

    public void addService(LegalService service) {
        this.services.add(service);
    }

    public void removeService(LegalService service) {
        this.services.remove(service);
    }

    public BigDecimal totalPrice() {
        return this.contract.calculateTotalPrice(this);
    }
}
