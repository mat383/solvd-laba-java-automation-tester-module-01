package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Widgets {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());


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
