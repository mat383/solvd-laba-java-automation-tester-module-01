package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LegalCase {
    private IContract contract;
    // TODO add case name
    private String description;
    private boolean isOpened;

    /**
     * all entities related to case
     */
    private final List<IEntity> clients = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<LegalService> services = new ArrayList<>();

    public LegalCase(IContract contract, String description, boolean isOpened, List<IEntity> clients, List<Appointment> appointments) {
        if (contract == null) {
            throw new IllegalArgumentException("contract cannot be null");
        }
        this.contract = contract;
        this.description = description;
        this.isOpened = isOpened;
        for (IEntity client : clients) {
            addClient(client);
        }
        this.appointments.addAll(appointments);
    }

    public LegalCase(IContract contract, String description, boolean isOpened) {
        this(contract, description, isOpened,
                Collections.emptyList(), Collections.emptyList());
    }

    public LegalCase(IContract contract, String description) {
        this(contract, description, true);
    }


    public IContract getContract() {
        return contract;
    }

    public void setContract(IContract contract) {
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

    public List<IEntity> getClients() {
        return Collections.unmodifiableList(clients);
    }

    /**
     * checks whether client is uniqe to the case and adds it
     * use in constructor to safely add clients
     *
     * @param client
     */
    public final void addClient(IEntity client) {
        if (haveClient(client)) {
            throw new EntityAlreadyAddedException("Case already have client '" + client + "'");
        }
        this.clients.add(client);
    }

    public final boolean haveClient(IEntity client) {
        return this.clients.contains(client);
    }

    public void removeClient(IEntity client) {
        this.clients.remove(client);
    }

    public List<Appointment> getAppointments() {
        return Collections.unmodifiableList(appointments);
    }

    public List<Appointment> getFutureAppointments() {
        return this.appointments.stream()
                .filter(Appointment::startsAfterNow)
                .toList();
    }

    public List<Appointment> getClientAppointments(IEntity client) {
        return this.appointments.stream()
                .filter(appointment -> appointment.getParticipants().contains(client))
                .toList();
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
