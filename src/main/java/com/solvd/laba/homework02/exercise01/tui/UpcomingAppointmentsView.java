package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.Appointment;
import com.solvd.laba.homework02.exercise01.LegalCase;
import com.solvd.laba.homework02.exercise01.LegalOffice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;

public class UpcomingAppointmentsView implements View {
    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final LegalOffice legalOffice;
    private final IListFilter casesFilter;
    private final Widgets widgets;

    public UpcomingAppointmentsView(LegalOffice legalOffice, Widgets widgets, IListFilter casesFilter) {
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

    public UpcomingAppointmentsView(LegalOffice legalOffice, Widgets widgets) {
        this(legalOffice, widgets, new LegalCasesNoFilter());
    }

    @Override
    public void show() {
        LOGGER.info("Showing upcoming appointments");
        List<LegalCase> relevantCases = casesFilter.filter(this.legalOffice.getCases());
        List<Appointment> upcomingAppointments = relevantCases.stream()
                .flatMap(legalCase -> legalCase.getFutureAppointments().stream())
                .sorted(Comparator.comparing(Appointment::getStart))
                .toList();

        this.widgets.listList("Upcoming appointments", upcomingAppointments, Appointment::toString);

    }
}
