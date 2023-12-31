package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.IEntity;
import com.solvd.laba.homework02.exercise01.LegalCase;

import java.util.List;

public class LegalCasesClientFilter implements IListFilter<LegalCase> {
    private final IEntity client;

    public LegalCasesClientFilter(IEntity client) {
        this.client = client;
    }

    @Override
    public List<LegalCase> filter(List<LegalCase> legalCases) {
        return legalCases.stream()
                .filter(legalCase -> legalCase.haveClient(this.client))
                .toList();
    }
}
