package com.solvd.laba.homework02.exercise01.UI;

import com.solvd.laba.homework02.exercise01.LegalCase;
import com.solvd.laba.homework02.exercise01.LegalOffice;

import java.util.HashMap;
import java.util.List;

public class ViewCases implements Showable {

    private final LegalOffice office;
    private final ILegalCasesFilter casesFilter;

    public ViewCases(LegalOffice office, ILegalCasesFilter casesFilter) {
        if (office == null) {
            throw new IllegalArgumentException("office cannot be null");
        }
        if (casesFilter == null) {
            throw new IllegalArgumentException("casesFilter cannot be null");
        }
        this.office = office;
        this.casesFilter = casesFilter;
    }

    public ViewCases(LegalOffice office) {
        this(office, new LegalCasesNoFilter());
    }

    @Override
    public void show() {
        /*
         * list of all cases that should be shown
         * needs to be updated after any modification of cases
         */
        List<LegalCase> relevantCases = getCases();
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
            UI.enumerateCases(relevantCases);
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

    protected List<LegalCase> getCases() {
        return this.casesFilter.filter(this.office.getCases());
    }

}
