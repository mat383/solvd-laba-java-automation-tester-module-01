package com.solvd.laba.homework02.exercise01;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Entity class models any legal entity that legal office conducts business with.
 */
public abstract class Entity {
    private ArrayList<Contract> contracts;

    public Entity(Contract[] contractcs) {
        this.contracts = new ArrayList<Contract>(Arrays.asList(contractcs));
    }

    public Entity(Contract contractc) {
        this.contracts = new ArrayList<Contract>();
        this.contracts.add(contractc);
    }

    public Entity() {
        this.contracts = new ArrayList<Contract>();
    }

    public ArrayList<Contract> getContracts() {
        return this.contracts;
    }

    public void addContract(Contract contract) {
        this.contracts.add(contract);
    }

    // TODO add methods for removing from list

    public abstract String getFullName();

}
