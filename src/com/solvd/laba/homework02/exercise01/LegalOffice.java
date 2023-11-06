package com.solvd.laba.homework02.exercise01;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class LegalOffice {
    private final ArrayList<LegalCase> cases = new ArrayList<>();
    // TODO add address book


    public LegalOffice(Collection<LegalCase> cases) {
        this.cases.addAll(cases);
    }

    public LegalOffice() {
        this(Collections.emptyList());
    }

    public List<LegalCase> getCases() {
        return Collections.unmodifiableList(cases);
    }

    public List<LegalCase> getOpenCases() {
        return this.cases.stream().
                filter(LegalCase::isOpened)
                .collect(Collectors.toUnmodifiableList());
    }

    public List<LegalCase> getClosedCases() {
        return this.cases.stream().
                filter(legalCase -> !legalCase.isOpened())
                .collect(Collectors.toUnmodifiableList());
    }

    public void addCase(LegalCase legalCase) {
        this.cases.add(legalCase);
    }

    public void removeCase(LegalCase legalCase) {
        this.cases.remove(legalCase);
    }
}
