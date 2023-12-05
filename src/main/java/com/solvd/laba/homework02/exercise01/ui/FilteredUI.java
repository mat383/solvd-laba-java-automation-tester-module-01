package com.solvd.laba.homework02.exercise01.ui;

import com.solvd.laba.homework02.exercise01.LegalCase;
import com.solvd.laba.homework02.exercise01.LegalOffice;

import java.util.List;

public class FilteredUI extends UI {
    private final ILegalCasesFilter filter;

    public FilteredUI(LegalOffice office, ILegalCasesFilter filter) {
        super(office);
        this.filter = filter;
    }

    @Override
    protected List<LegalCase> getCasesForCasesView() {
        return this.filter.filter(getOffice().getCases());
    }
}
