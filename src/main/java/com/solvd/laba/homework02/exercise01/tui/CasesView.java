package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class CasesView implements View {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());

    private final LegalOffice legalOffice;
    private final ILegalCasesFilter casesFilter;
    private final Widgets widgets;

    public CasesView(LegalOffice legalOffice, ILegalCasesFilter casesFilter, Widgets widgets) {
        this.legalOffice = legalOffice;
        this.casesFilter = casesFilter;
        this.widgets = widgets;

        if (legalOffice == null) {
            throw new IllegalArgumentException("legalOffice cannot be null");
        }
        if (casesFilter == null) {
            throw new IllegalArgumentException("casesFilter cannot be null");
        }
        if (widgets == null) {
            throw new IllegalArgumentException("widgets cannot be null");
        }
    }

    public CasesView(LegalOffice legalOffice, Widgets widgets) {
        this(legalOffice, new LegalCasesNoFilter(), widgets);
    }

    @Override
    public void show() {
        LOGGER.debug("Showing CasesView");
        /*
         * list of all cases that should be shown
         * needs to be updated after any modification of cases
         */
        List<LegalCase> relevantCases = getCasesForCasesView();
        Actions action = null;

        while (action != Actions.ACTION_QUIT) {
            System.out.println();
            this.widgets.enumerateCases(relevantCases);
            action = Actions.of(
                    this.widgets.selectCharOptionWithDescription(Actions.actionsMap()));

            LOGGER.debug("Selected option " + action + " in cases view");

            switch (action) {
                case ACTION_QUIT:
                    LOGGER.info("quitting CasesView");
                    System.out.println("quitting");
                    break;

                case ACTION_DETAILS:
                    actionDetails(relevantCases);
                    relevantCases = getCasesForCasesView();
                    break;

                case ACTION_ADD_CASE:
                    actionAddCase();
                    relevantCases = getCasesForCasesView();
                    break;

                case ACTION_REMOVE_CASE:
                    actionRemoveCase(relevantCases);
                    relevantCases = getCasesForCasesView();
                    break;

                case ACTION_UPCOMING_APPOINTMENTS:
                    actionUpcomingAppointments(relevantCases);
                    relevantCases = getCasesForCasesView();
                    break;

                default:
                    System.out.printf("Choice doesn't match any action: '%s'\n", action);
            }
        }
    }


    private void actionAddCase() {
        LOGGER.info("action: create new case");
        System.out.println("\n* Creating case");

        System.out.println("** Creating description");
        String description = this.widgets.promptForString("description: ");

        System.out.println("** Creating contract");
        ContractWithDescription contract = createContract();

        LegalCase newCase = new LegalCase(contract, description);

        System.out.println("** Creating clients");
        addClientsToCase(newCase);

        System.out.println("** Adding appointment");
        Address appointmentAddress = null;
        while (appointmentAddress == null) {
            try {
                appointmentAddress = this.widgets.promptForAddress("provide address: ");
            } catch (AddressDoesntExistException e) {
                System.out.println("please provide valid address");
            }
        }
        LocalDateTime start = LocalDateTime.now()
                .plusWeeks(1)
                .withHour(13);
        LocalDateTime end = start.plusHours(1);
        Appointment appointment = new Appointment(
                Appointment.Type.CONSULTATION,
                start, end, appointmentAddress,
                "Initial consultation");
        newCase.addAppointment(appointment);

        this.legalOffice.addCase(newCase);
    }

    private void actionDetails(List<LegalCase> relevantCases) {
        LOGGER.info("action: case details");
        if (relevantCases.isEmpty()) {
            System.out.println("No cases");
            return;
        }

        System.out.println("Showing case details");
        int caseNumber = this.widgets.selectNumericOption(
                "Case number: ", new IntegerInRangeValidator(1, relevantCases.size()));
        LegalCase selectedCase = relevantCases.get(caseNumber - 1);

        CaseDetailsView caseDetailsView = new CaseDetailsView(this.legalOffice, selectedCase,
                this.casesFilter, this.widgets);
        caseDetailsView.show();
    }

    private void actionRemoveCase(List<LegalCase> relevantCases) {
        LOGGER.info("action: removing case");
        if (relevantCases.isEmpty()) {
            System.out.println("No cases");
            return;
        }
        System.out.println("Removing case");
        // TODO add method to select element from list
        int caseToDeleteNumber = this.widgets.selectNumericOption(
                "Case number: ", new IntegerInRangeValidator(1, relevantCases.size()));
        legalOffice.removeCase(relevantCases.get(caseToDeleteNumber - 1));
    }

    private void actionUpcomingAppointments(List<LegalCase> relevantCases) {
        LOGGER.info("action: upcoming appointments");
        System.out.println("Upcoming appointments:");
        relevantCases.stream()
                .flatMap(legalCase -> legalCase.getFutureAppointments().stream())
                .sorted(Comparator.comparing(Appointment::getStart))
                .forEachOrdered(appointment -> System.out.println("- " + appointment.toString()));
    }


    /**
     * prompts for and adds clients to case, used by createCase
     *
     * @param legalCase case to create and add clients for
     */
    protected void addClientsToCase(LegalCase legalCase) {
        Map<Character, String> createClientsOptions = new HashMap<>();
        final char ACTION_ADD = 'a';
        createClientsOptions.put(ACTION_ADD, "Add another client");
        final char ACTION_ADD_EXISTING = 'e';
        createClientsOptions.put(ACTION_ADD_EXISTING, "Add existing client");
        final char ACTION_LIST = 'l';
        createClientsOptions.put(ACTION_LIST, "List added clients");
        final char ACTION_QUIT = 'q';
        createClientsOptions.put(ACTION_QUIT, "Finish adding clients");

        // separate each action with empty line
        System.out.println();
        char option = this.widgets.selectCharOptionWithDescription(createClientsOptions);

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
                case ACTION_ADD_EXISTING:
                    List<IEntity> existingClients = new ArrayList<>(this.legalOffice.getClients());
                    for (int i = 0; i < existingClients.size(); ++i) {
                        System.out.printf("%02d. %s\n", i + 1, existingClients.get(i));
                    }
                    // displayed numbers start at 1, so index = selectedNumber - 1
                    int selectedClientNumber = this.widgets.selectNumericOption("Select client to add: ", new IntegerInRangeValidator(1, existingClients.size()));
                    IEntity selectedClient = existingClients.get(selectedClientNumber - 1);

                    try {
                        legalCase.addClient(selectedClient);
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
                    for (IEntity client : legalCase.getClients()) {
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
            option = this.widgets.selectCharOptionWithDescription(createClientsOptions);
        }
    }

    protected IEntity createClient() {
        // TODO add option to create Person or Company
        String id = this.widgets.promptForString("Id: ");
        String firstName = this.widgets.promptForString("First name: ");
        String lastName = this.widgets.promptForString("Last name: ");
        return new Person(id, firstName, lastName);
    }

    protected ContractWithDescription createContract() {
        // TODO add more contract options
        BigDecimal flatPrice = this.widgets.promptForBigDecimal("Contract flat price: ");
        return new ContractFlatPrice(flatPrice);
    }

    private List<LegalCase> getCasesForCasesView() {
        return this.casesFilter.filter(this.legalOffice.getCases());
    }


    private enum Actions {
        ACTION_QUIT("quit", 'q'),
        ACTION_DETAILS("how case details", 'd'),
        ACTION_ADD_CASE("add case", 'a'),
        ACTION_REMOVE_CASE("remove case", 'r'),
        ACTION_UPCOMING_APPOINTMENTS("show upcoming appointments", 'u');

        private final String readableName;
        private final char selectionKey;

        Actions(String readableName, char selectionKey) {
            this.readableName = readableName;
            this.selectionKey = selectionKey;
        }

        public static Actions of(char selectionKey) {
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
