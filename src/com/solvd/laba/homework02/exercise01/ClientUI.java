package com.solvd.laba.homework02.exercise01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ClientUI extends UI {
    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final Entity client;

    public ClientUI(LegalOffice office, Entity client) {
        super(office);
        if (client == null) {
            throw new IllegalArgumentException("client have to be reference to valid object, cannot be null");
        }
        this.client = client;
    }

    public Entity getClient() {
        return client;
    }


    @Override
    public void start() {
        clientDashboardView();
    }

    private void clientDashboardView() {
        HashMap<Character, String> actions = new HashMap<>();
        final char ACTION_CASES = 'c';
        actions.put(ACTION_CASES, "manage client cases");
        final char ACTION_QUIT = 'q';
        actions.put(ACTION_QUIT, "quit");
        char selectedAction = ' ';

        do {
            System.out.println("Viewing for client: " + this.client);
            System.out.println();

            System.out.println("* Upcoming appointments");
            listAppointments(this.getOffice().getClientAppointments(this.client));
            System.out.println();

            System.out.println("* Related cases");
            listCases(this.getOffice().getClientCases(this.client));
            System.out.println();

            selectedAction = selectCharOptionWithDescription(actions);
            switch (selectedAction) {
                case ACTION_CASES:
                    casesView();
                    break;

                case ACTION_QUIT:
                    System.out.println("quiting");
                    break;

                default:
                    System.out.printf("Choice doesn't match any action: '%c'\n", selectedAction);

            }
        } while (selectedAction != ACTION_QUIT);

    }

    @Override
    protected List<LegalCase> getCasesForCasesView() {
        return this.getOffice().getClientCases(this.client);
    }

    // TODO a lot of code duplication, figure out a better way
    @Override
    protected void addClientsToCase(LegalCase legalCase) {
        Map<Character, String> createClientsOptions = new HashMap<>();
        final char ACTION_ADD = 'a';
        final char ACTION_LIST = 'l';
        final char ACTION_QUIT = 'q';
        createClientsOptions.put(ACTION_ADD, "Add another client");
        createClientsOptions.put(ACTION_LIST, "List added clients");
        createClientsOptions.put(ACTION_QUIT, "Finish adding clients");

        // automativally add client for which view is created
        try {
            legalCase.addClient(this.client);
        } catch (IllegalArgumentException e) {
            LOGGER.info("Trying to add already added client to case");
            // do nothing, client already added
        }
        System.out.println("Adding client " + this.client + " to case");

        // separate each action with empty line
        System.out.println();
        char option = selectCharOptionWithDescription(createClientsOptions);

        // quit adding when user chooses to AND there is at least one client added
        while (option != ACTION_QUIT
                || legalCase.getClients().isEmpty()) {
            switch (option) {
                case ACTION_ADD:
                    try {
                        legalCase.addClient(createClient());
                    } catch (IllegalArgumentException e) {
                        LOGGER.warn("Trying to add already added client to case");
                        System.out.println("This case already have this client");
                    }
                    break;
                case ACTION_LIST:
                    if (legalCase.getClients().isEmpty()) {
                        System.out.println("No clients added yet");
                    } else {
                        System.out.println("Clients:");
                    }
                    for (Entity client : legalCase.getClients()) {
                        System.out.printf("- %s\n", client);
                    }
                    break;
                case ACTION_QUIT:
                    if (legalCase.getClients().isEmpty()) {
                        System.out.println("Cannot quit because no client was added");
                    }
                    break;
            }

            // separate each action with empty line
            System.out.println();
            option = selectCharOptionWithDescription(createClientsOptions);
        }
    }
}
