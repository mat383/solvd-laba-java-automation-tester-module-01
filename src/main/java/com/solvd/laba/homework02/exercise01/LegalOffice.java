package com.solvd.laba.homework02.exercise01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

public class LegalOffice {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final ArrayList<LegalCase> cases = new ArrayList<>();
    // TODO add address book


    public LegalOffice(Collection<LegalCase> cases) {
        LOGGER.info("LegalOffice created");
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
                .toList();
    }

    public List<LegalCase> getClosedCases() {
        return this.cases.stream().
                filter(legalCase -> !legalCase.isOpened())
                .toList();
    }

    public List<LegalCase> getClientCases(IEntity client) {
        return this.cases.stream()
                .filter(legalCase -> legalCase.haveClient(client))
                .toList();
    }

    public void addCase(LegalCase legalCase) {
        LOGGER.info("Legal case with hash " + legalCase.hashCode() + "added to legalOffice");
        this.cases.add(legalCase);
    }

    public void removeCase(LegalCase legalCase) {
        LOGGER.info("Legal case with hash " + legalCase.hashCode() + "removed from legalOffice");
        this.cases.remove(legalCase);
    }

    public Set<IEntity> getClients() {
        return this.cases.stream()
                .flatMap(legalCase -> legalCase.getClients().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    public List<Appointment> getClientAppointments(IEntity client) {
        return this.cases.stream()
                .flatMap(legalCase -> legalCase.getClientAppointments(client).stream())
                .toList();
    }
}
