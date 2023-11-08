package com.solvd.laba.homework02.exercise01;

import java.util.*;
import java.util.stream.Collectors;

public class LegalOffice {
    private final ArrayList<LegalCase> cases = new ArrayList<>();
    // TODO add address book


    public LegalOffice(Collection<LegalCase> cases) {
        this.cases.addAll(cases);
    }

    public LegalOffice() {
        this(Collections.emptyList());
    }

    public List<LegalCase> getCases() {
        return Collections.unmodifiableList(cases);
    }

    public List<LegalCase> getOpenCases() {
        return this.cases.stream().
                filter(LegalCase::isOpened)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<LegalCase> getClosedCases() {
        return this.cases.stream().
                filter(legalCase -> !legalCase.isOpened())
                .collect(Collectors.toUnmodifiableList());
    }

    public List<LegalCase> getClientCases(Entity client) {
        return this.cases.stream()
                .filter(legalCase -> legalCase.haveClient(client))
                .collect(Collectors.toUnmodifiableList());
    }

    public void addCase(LegalCase legalCase) {
        this.cases.add(legalCase);
    }

    public void removeCase(LegalCase legalCase) {
        this.cases.remove(legalCase);
    }

    public Set<Entity> getClients() {
        HashSet<Entity> clients = new HashSet<>();
        for (LegalCase legalCase : this.cases) {
            clients.addAll(cases.getFirst().getClients());
        }

        return Collections.unmodifiableSet(clients);
    }

    public List<Appointment> getClientAppointments(Entity client) {
        List<Appointment> appointments = new ArrayList<>();
        for (LegalCase legalCase : this.cases) {
            appointments.addAll(legalCase.getClientAppointments(client));
        }

        return Collections.unmodifiableList(appointments);
    }
}
