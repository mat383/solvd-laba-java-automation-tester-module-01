package com.solvd.laba.homework02.exercise01.ui;

import com.solvd.laba.homework02.exercise01.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class UI {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final LegalOffice office;

    public UI(LegalOffice office) {
        this.office = office;
    }

    public LegalOffice getOffice() {
        return office;
    }

    public void show() {
        LOGGER.info("UI Started");
        casesView();
    }

    /**
     * display all cases returned by getCasesForCasesView and allow performing some
     * operations on them
     */
    protected void casesView() {
        LOGGER.debug("Entering casesView");
        /*
         * list of all cases that should be shown
         * needs to be updated after any modification of cases
         */
        List<LegalCase> relevantCases = getCasesForCasesView();
        char selection = ' ';
        final char ACTION_QUIT = 'q';
        final char ACTION_DETAILS = 'd';
        final char ACTION_ADD_CASE = 'a';
        final char ACTION_REMOVE_CASE = 'r';
        final HashMap<Character, String> actions = new HashMap<>();
        actions.put(ACTION_QUIT, "quit");
        actions.put(ACTION_DETAILS, "show case details");
        actions.put(ACTION_ADD_CASE, "add case");
        actions.put(ACTION_REMOVE_CASE, "remove case");

        while (selection != ACTION_QUIT) {
            System.out.println();
            enumerateCases(relevantCases);
            selection = selectCharOptionWithDescription(actions);
            LOGGER.debug("Selected option " + selection + " in cases view");

            switch (selection) {
                case ACTION_QUIT:
                    System.out.println("quitting");
                    break;

                case ACTION_DETAILS:
                    if (relevantCases.isEmpty()) {
                        System.out.println("No cases");
                        break;
                    }
                    System.out.println("Showing case details");
                    int caseNumber = selectNumericOption(
                            "Case number: ", new IntegerInRangeValidator(1, relevantCases.size()));
                    caseDetailsView(relevantCases.get(caseNumber - 1));
                    relevantCases = getCasesForCasesView();
                    break;

                case ACTION_ADD_CASE:
                    System.out.println("Adding case");
                    this.office.addCase(createCase());
                    relevantCases = getCasesForCasesView();
                    break;

                case ACTION_REMOVE_CASE:
                    if (relevantCases.isEmpty()) {
                        System.out.println("No cases");
                        break;
                    }
                    System.out.println("Removing case");
                    // TODO add method to select element from list
                    int caseToDeleteNumber = selectNumericOption(
                            "Case number: ", new IntegerInRangeValidator(1, relevantCases.size()));
                    office.removeCase(relevantCases.get(caseToDeleteNumber - 1));
                    relevantCases = getCasesForCasesView();
                    break;

                default:
                    System.out.printf("Choice doesn't match any action: '%c'\n", selection);
            }
        }
    }

    /**
     * used in casesView to update list of relevant cases after adding or removing cases
     *
     * @return list of cases to be displayed in casesView
     */
    protected List<LegalCase> getCasesForCasesView() {
        return office.getCases();
    }

    public void caseDetailsView(LegalCase legalCase) {
        System.out.println("\n* Case details");
        System.out.println(legalCase.getDescription());
        System.out.println();

        System.out.println("** Clients");
        for (IEntity client : legalCase.getClients()) {
            System.out.println("- " + client);
        }
        System.out.println();

        System.out.println("** Appointments");
        listAppointments(legalCase.getAppointments());
        System.out.println();

        System.out.println("** Services provided");
        for (LegalService service : legalCase.getServices()) {
            System.out.println("- " + service);
        }
        System.out.println();

        System.out.println("** Total cost: " + legalCase.totalPrice());
        System.out.println();


    }

    protected void listCases(List<LegalCase> cases) {
        System.out.println("Cases:");
        for (LegalCase legalCase : cases) {
            System.out.printf("- (%s) %s\n",
                    legalCase.isOpened() ? "open" : "closed",
                    legalCase.getDescription());
        }
    }

    protected void enumerateCases(List<LegalCase> cases) {
        System.out.println("Cases:");
        int index = 1;
        for (LegalCase legalCase : cases) {
            System.out.printf("%02d. (%s) %s\n",
                    index,
                    legalCase.isOpened() ? "open" : "closed",
                    legalCase.getDescription());
            ++index;
        }
    }

    protected void listAppointments(List<Appointment> appointments) {
        for (Appointment appointment : appointments) {
            System.out.printf("- %s\n", appointment);
        }
    }

    protected LegalCase createCase() {
        System.out.println("\n* Creating case");

        System.out.println("** Creating description");
        String description = promptForString("description: ");

        System.out.println("** Creating contract");
        ContractWithDescription contract = createContract();

        LegalCase newCase = new LegalCase(contract, description);

        System.out.println("** Creating clients");
        addClientsToCase(newCase);

        System.out.println("** Adding appointment");
        Address appointmentAddress = null;
        while ( appointmentAddress == null ) {
            try {
                appointmentAddress = promptForAddress("provide address: ");
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

        return newCase;
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
                case ACTION_ADD_EXISTING:
                    List<IEntity> existingClients = new ArrayList<>(this.office.getClients());
                    for (int i = 0; i < existingClients.size(); ++i) {
                        System.out.printf("%02d. %s\n", i + 1, existingClients.get(i));
                    }
                    // displayed numbers start at 1, so index = selectedNumber - 1
                    int selectedClientNumber = selectNumericOption("Select client to add: ", new IntegerInRangeValidator(1, existingClients.size()));
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
            option = selectCharOptionWithDescription(createClientsOptions);
        }
    }

    protected IEntity createClient() {
        // TODO add option to create Person or Company
        String id = promptForString("Id: ");
        String firstName = promptForString("First name: ");
        String lastName = promptForString("Last name: ");
        return new Person(id, firstName, lastName);
    }

    protected ContractWithDescription createContract() {
        // TODO add more contract options
        BigDecimal flatPrice = promptForBigDecimal("Contract flat price: ");
        return new ContractFlatPrice(flatPrice);
    }

    protected Address promptForAddress(String prompt) throws AddressDoesntExistException {
        System.out.println(prompt);
        Scanner scanner = new Scanner(System.in);

        System.out.print("Country: ");
        String country = scanner.nextLine();

        System.out.print("City: ");
        String city = scanner.nextLine();

        System.out.print("Postal Code: ");
        String postalCode = scanner.nextLine();

        System.out.print("Street: ");
        String street = scanner.nextLine();

        System.out.print("Street Number: ");
        String streetNumber = scanner.nextLine();

        System.out.print("Appartment Number: ");
        String apartmentNumber = scanner.nextLine();

        return new Address(country, city, postalCode, street, streetNumber, apartmentNumber);
    }

    protected String promptForString(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.nextLine();
    }

    protected char promptForChar(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return scanner.next().charAt(0);
    }

    protected BigDecimal promptForBigDecimal(String prompt) {
        Scanner scanner = new Scanner(System.in);
        BigDecimal input = null;
        while (input == null) {
            try {
                System.out.print(prompt);
                input = new BigDecimal(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Wrong input type. Please provide numeric option");
            }
        }
        return input;
    }

    protected int selectNumericOption(String prompt, IValidator<Integer> validator) {
        Integer selection = null;
        while (selection == null || !validator.isValid(selection)) {
            try {
                Scanner scanner = new Scanner(System.in);
                System.out.print(prompt);
                selection = scanner.nextInt();
                if (!validator.isValid(selection)) {
                    System.out.println("Option out of bonds");
                }
            } catch (InputMismatchException e) {
                System.out.println("Wrong input type. Please provide numeric option");
            }
        }
        return selection;
    }

    /**
     * returns selected option as char
     *
     * @param options map of options (Character) with descriptions (String)
     * @return Character corresponding to selected options
     */
    protected char selectCharOptionWithDescription(Map<Character, String> options) {
        if (options.isEmpty()) {
            throw new IllegalArgumentException("options Map cannot be empty");
        }
        Character selectedOption = null;
        while (selectedOption == null || !options.containsKey(selectedOption)) {
            if (selectedOption != null && !options.containsKey(selectedOption)) {
                System.out.println("Selected invalid option '" + selectedOption + "'");
            }
            System.out.println("Select option:");
            for (Map.Entry<Character, String> entry : options.entrySet()) {
                System.out.printf("- '%c' - %s\n", entry.getKey(), entry.getValue());
            }
            selectedOption = promptForChar("Choice: ");
        }

        return selectedOption;
    }
}
