package com.solvd.laba.homework02.exercise01.tui;

import com.solvd.laba.homework02.exercise01.LegalCase;

import java.util.List;

@FunctionalInterface
public interface ILegalCasesFilter {
    public List<LegalCase> filter(List<LegalCase> legalCases);
}
