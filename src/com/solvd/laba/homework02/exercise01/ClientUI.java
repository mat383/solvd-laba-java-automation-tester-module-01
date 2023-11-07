package com.solvd.laba.homework02.exercise01;

import java.util.List;

public final class ClientUI extends UI {
    private final Entity client;

    public ClientUI(LegalOffice office, Entity client) {
        super(office);
        if (client == null) {
            throw new IllegalArgumentException("client have to be reference to valid object, cannot be null");
        }
        this.client = client;
    }

    public Entity getClient() {
        return client;
    }

    @Override
    public void start() {
        casesView();
    }

    @Override
    protected List<LegalCase> getCasesForCasesView() {
        return this.getOffice().getCasesWithClient(this.client);
    }
}
