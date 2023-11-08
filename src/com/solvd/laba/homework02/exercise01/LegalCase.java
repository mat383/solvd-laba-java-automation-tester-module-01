package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LegalCase {
    private Contract contract;
    // TODO add case name
    private String description;
    private boolean isOpened;

    /**
     * all entities related to case
     */
    private final ArrayList<Entity> clients = new ArrayList<>();
    private final ArrayList<Appointment> appointments = new ArrayList<>();
    private final ArrayList<LegalService> services = new ArrayList<>();

    public LegalCase(Contract contract, String description, boolean isOpened, List<Entity> clients, List<Appointment> appointments) {
        if (contract == null) {
            throw new IllegalArgumentException("contract cannot be null");
        }
        this.contract = contract;
        this.description = description;
        this.isOpened = isOpened;
        for (Entity client : clients) {
            addClient(client);
        }
        this.appointments.addAll(appointments);
    }

    public LegalCase(Contract contract, String description, boolean isOpened) {
        this(contract, description, isOpened,
                Collections.emptyList(), Collections.emptyList());
    }

    public LegalCase(Contract contract, String description) {
        this(contract, description, true);
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

    public boolean isOpened() {
        return isOpened;
    }

    public void setOpened(boolean opened) {
        this.isOpened = opened;
    }

    public List<Entity> getClients() {
        return Collections.unmodifiableList(clients);
    }

    /**
     * checks whether client is uniqe to the case and adds it
     * use in constructor to safely add clients
     *
     * @param client
     */
    public final void addClient(Entity client) {
        if (haveClient(client)) {
            throw new IllegalArgumentException("Case already have client '" + client + "'");
        }
        this.clients.add(client);
    }

    public boolean haveClient(Entity client) {
        for (Entity e : this.clients) {
            if (e.equals(client)) {
                return true;
            }
        }
        return false;
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

    public List<Appointment> getClientAppointments(Entity client) {
        return this.appointments.stream()
                .filter(appointment -> appointment.getParticipants().contains(client))
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
