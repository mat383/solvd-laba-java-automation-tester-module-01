package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.util.List;
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

    private void casesView() {
        char selection = ' ';
        final char ACTION_QUIT = 'q';
        final char ACTION_DETAILS = 'd';
        final char ACTION_ADD_CASE = 'a';
        final char ACTION_REMOVE_CASE = 'r';

        while (selection != ACTION_QUIT) {
            System.out.println();
            enumerateCases(office.getCases());
            System.out.println("Select action:");
            System.out.printf("- '%c' - quit\n", ACTION_QUIT);
            System.out.printf("- '%c' - case details\n", ACTION_DETAILS);
            System.out.printf("- '%c' - add case\n", ACTION_ADD_CASE);
            System.out.printf("- '%c' - remove case\n", ACTION_REMOVE_CASE);
            System.out.print("your choice: ");
            Scanner scanner = new Scanner(System.in);
            selection = scanner.next().charAt(0);
            System.out.println();

            switch (selection) {
                case ACTION_QUIT:
                    System.out.println("quitting");
                    break;

                case ACTION_DETAILS:
                    if (office.getCases().size() < 1) {
                        System.out.println("No cases");
                        break;
                    }
                    System.out.println("Showing case details");
                    int caseNumber = promptSelectNumericOption(
                            "Case number: ", 1, office.getCases().size());
                    caseDetailsView(office.getCases().get(caseNumber - 1));
                    break;

                case ACTION_ADD_CASE:
                    System.out.println("Adding case");
                    this.office.addCase(createCase());
                    break;

                case ACTION_REMOVE_CASE:
                    if (office.getCases().size() < 1) {
                        System.out.println("No cases");
                        break;
                    }
                    System.out.println("Removing case");
                    int caseToDeleteNumber = promptSelectNumericOption(
                            "Case number: ", 1, office.getCases().size());
                    office.removeCase(office.getCases().get(caseToDeleteNumber - 1));
                    break;

                default:
                    System.out.printf("Choice doesn't match any action: '%c'\n", selection);
            }
        }
    }

    public void caseDetailsView(LegalCase legalCase) {
        System.out.println("\n* Case details");
        System.out.println(legalCase.getDescription());
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
                    legalCase.isOpen() ? "open" : "closed",
                    legalCase.getDescription());
        }
    }

    protected void enumerateCases(List<LegalCase> cases) {
        System.out.println("Cases:");
        int index = 1;
        for (LegalCase legalCase : cases) {
            System.out.printf("%02d. (%s) %s\n",
                    index,
                    legalCase.isOpen() ? "open" : "closed",
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
        String firstName = promptForString("First name: ");
        String lastName = promptForString("Last name: ");
        return new Person(firstName, lastName);
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

    protected BigDecimal promptForBigDecimal(String prompt) {
        Scanner scanner = new Scanner(System.in);
        System.out.print(prompt);
        return new BigDecimal(scanner.next());
    }

    protected int promptSelectNumericOption(String prompt, int minOption, int maxOption) {
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
}
