package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class CaseDetailsView implements View {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final LegalOffice legalOffice;
    private final LegalCase legalCase;
    private final IListFilter casesFilter;
    private final Widgets widgets;

    public CaseDetailsView(LegalOffice legalOffice, LegalCase legalCase, Widgets widgets, IListFilter casesFilter) {
        this.legalOffice = legalOffice;
        this.legalCase = legalCase;
        this.casesFilter = casesFilter;
        this.widgets = widgets;

        if (legalOffice == null) {
            throw new IllegalArgumentException("legalOffice cannot be null");
        }
        if (legalCase == null) {
            throw new IllegalArgumentException("legalCase cannot be null");
        }
        if (casesFilter == null) {
            throw new IllegalArgumentException("casesFilter cannot be null");
        }
        if (widgets == null) {
            throw new IllegalArgumentException("widgets cannot be null");
        }
    }

    public CaseDetailsView(LegalOffice legalOffice, LegalCase legalCase, Widgets widgets) {
        this(legalOffice, legalCase, widgets, new LegalCasesNoFilter());
    }

    @Override
    public void show() {
        LOGGER.info("Showing CaseDetailsView for %s".formatted(legalCase));

        Actions action = null;

        while (action != Actions.QUIT) {
            System.out.println();
            printDetails();

            action = Actions.valueOf(
                    this.widgets.selectCharOptionWithDescription(Actions.actionsMap()));

            LOGGER.debug("Selected option " + action + " in cases view");

            switch (action) {
                case QUIT:
                    LOGGER.info("quitting CasesView");
                    System.out.println("quitting");
                    break;

                case SERVICE_FROM_APPOINTMENT:
                    actionServiceFromAppointment();
                    break;

                default:
                    System.out.printf("Choice doesn't match any action: '%s'\n", action);

            }
        }
    }

    private void actionServiceFromAppointment() {
        LOGGER.info("action: create service from appointment");
        if (this.legalCase.getAppointments().isEmpty()) {
            System.out.println("No appointments to convert");
            return;
        }
        System.out.println("Select appointment from which to create service:");
        this.widgets.enumerateList("", this.legalCase.getAppointments(), Appointment::toString);
        Appointment appointmentToUse = this.widgets.selectFromList("Select appointment: ", this.legalCase.getAppointments());
        this.legalCase.addService(appointmentToUse.createService());
    }

    private void printDetails() {
        System.out.println("\n* Case details");
        System.out.println(this.legalCase.getDescription());
        System.out.println();

        System.out.println("** Clients");
        for (IEntity client : this.legalCase.getClients()) {
            System.out.println("- " + client);
        }
        System.out.println();

        System.out.println("** Appointments");
        this.widgets.listList("", this.legalCase.getAppointments(), Appointment::toString);
        System.out.println();

        System.out.println("** Services provided");
        for (LegalService service : this.legalCase.getServices()) {
            System.out.println("- " + service);
        }
        System.out.println();

        System.out.println("** Total cost: " + legalCase.totalPrice());
        System.out.println();
    }

    private enum Actions {
        QUIT("quit", 'q'),
        SERVICE_FROM_APPOINTMENT("create service from appointment", 's');

        private final String readableName;
        private final char selectionKey;

        Actions(String readableName, char selectionKey) {
            this.readableName = readableName;
            this.selectionKey = selectionKey;
        }

        public static Actions valueOf(char selectionKey) {
            return Arrays.stream(Actions.values())
                    .filter(a -> a.selectionKey == selectionKey)
                    .findFirst()
                    .orElseThrow(() -> new NoSuchElementException("No action with key '%c'".formatted(selectionKey)));
        }

        public static Map<Character, String> actionsMap() {
            return Arrays.stream(Actions.values())
                    .collect(Collectors.toMap(a -> a.selectionKey, a -> a.readableName));
        }
    }
}
