package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class UI {
    private final LegalOffice office;

    public UI(LegalOffice office) {
        this.office = office;
    }

    public LegalOffice getOffice() {
        return office;
    }

    public void start() {
        casesView();
    }

    /**
     * display all cases returned by getCasesForCasesView and allow performing some
     * operations on them
     */
    protected void casesView() {
        /**
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
                            "Case number: ", 1, relevantCases.size());
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
                            "Case number: ", 1, relevantCases.size());
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
        for (Entity client : legalCase.getClients()) {
            System.out.println("- " + client);
        }
        System.out.println();

        System.out.println("** Appointments");
        for (Appointment appointment : legalCase.getAppointments()) {
            System.out.println("- " + appointment);
        }
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

    protected LegalCase createCase() {
        System.out.println("\n* Creating case");

        System.out.println("** Creating description");
        String description = promptForString("description: ");

        System.out.println("** Creating clients");
        Entity client = createClient();

        System.out.println("** Creating contract");
        Contract contract = createContract();

        LegalCase newCase = new LegalCase(contract, description);
        newCase.addClient(client);

        return newCase;
    }

    protected Entity createClient() {
        // TODO add option to create Person or Company
        String id = promptForString("Id: ");
        String firstName = promptForString("First name: ");
        String lastName = promptForString("Last name: ");
        return new Person(id, firstName, lastName);
    }

    protected Contract createContract() {
        // TODO add more contract options
        BigDecimal flatPrice = promptForBigDecimal("Contract flat price: ");
        return new ContractFlatPrice(flatPrice);
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
        System.out.print(prompt);
        return new BigDecimal(scanner.next());
    }

    protected int selectNumericOption(String prompt, int minOption, int maxOption) {
        if (minOption > maxOption) {
            throw new IllegalArgumentException("minOption cannot be greater than maxOption");
        }
        int selection = minOption - 1;
        while (selection < minOption || maxOption < selection) {
            Scanner scanner = new Scanner(System.in);
            System.out.print(prompt);
            selection = scanner.nextInt();

            if (selection < minOption || maxOption < selection) {
                System.out.printf("Option out of bonds (%d-%d)\n", minOption, maxOption);
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
