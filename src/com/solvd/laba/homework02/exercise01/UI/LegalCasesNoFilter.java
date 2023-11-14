package com.solvd.laba.homework02.exercise01.UI;

import com.solvd.laba.homework02.exercise01.LegalCase;

import java.util.List;

public class LegalCasesNoFilter implements ILegalCasesFilter {
    @Override
    public List<LegalCase> filter(List<LegalCase> legalCases) {
        return legalCases;
    }
}
