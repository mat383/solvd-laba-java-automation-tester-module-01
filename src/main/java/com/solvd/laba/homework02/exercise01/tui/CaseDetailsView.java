package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.IEntity;
import com.solvd.laba.homework02.exercise01.LegalCase;
import com.solvd.laba.homework02.exercise01.LegalOffice;
import com.solvd.laba.homework02.exercise01.LegalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CaseDetailsView implements View {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final LegalOffice legalOffice;
    private final LegalCase legalCase;
    private final ILegalCasesFilter casesFilter;
    private final Widgets widgets;

    public CaseDetailsView(LegalOffice legalOffice, LegalCase legalCase, ILegalCasesFilter casesFilter, Widgets widgets) {
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
        this(legalOffice, legalCase, new LegalCasesNoFilter(), widgets);
    }

    @Override
    public void show() {
        LOGGER.info("Showing CaseDetailsView for %s".formatted(legalCase));

        System.out.println("\n* Case details");
        System.out.println(legalCase.getDescription());
        System.out.println();

        System.out.println("** Clients");
        for (IEntity client : legalCase.getClients()) {
            System.out.println("- " + client);
        }
        System.out.println();

        System.out.println("** Appointments");
        this.widgets.listAppointments(legalCase.getAppointments());
        System.out.println();

        System.out.println("** Services provided");
        for (LegalService service : legalCase.getServices()) {
            System.out.println("- " + service);
        }
        System.out.println();

        System.out.println("** Total cost: " + legalCase.totalPrice());
        System.out.println();
    }

}
