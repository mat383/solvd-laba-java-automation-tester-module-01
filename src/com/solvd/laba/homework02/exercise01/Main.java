package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Address officeAddress = new Address("Poland", "Warsaw", "00-012",
                "ul. Nowa", "1a", "12b");
        Address courtAddress = new Address("Poland", "Warsaw", "00-012",
                "ul. Nowa", "1a", "12b");


        LocalDate caseStart = LocalDate.now().minusDays(12);
        LocalDate caseEnd   = LocalDate.now().plusDays(12);

        LocalDateTime consultationStart = LocalDateTime.of(2023, 10, 11, 11, 0);
        LocalDateTime consultationEnd = LocalDateTime.of(2023, 10, 11, 12, 0);
        ClockedTime consultationInOffice = new ClockedTime(consultationStart, consultationEnd,
                "Initial consultation", false);

        Entity workerA = new Person("Worker", "A");
        Entity workerB = new Person("Worker", "B");
        Entity workerC = new Person("Worker", "C");
        Entity contractorA = new Person("Contractor", "C");
        contractorA.addContract(
                new SinglePaymentContract(caseStart, caseEnd,
                        "Hired PI to investigate r", new BigDecimal("3.14")));

        ArrayList<Entity> entities = new ArrayList<Entity>();
        entities.add(new Person("John", "Smith"));
        entities.add(new Person("Worker", "001"));
        entities.add(new Person("Worker", "002"));
        entities.add(new Person("Contractor", "001"));
        entities.add(new Person("Client", "001"));
        entities.add(new Person("Client", "002"));
        entities.add(new Person("Client", "003"));
        entities.add(new Company("BigCompany001"));

        Appointment ap1 = new Appointment(LocalDateTime.now().plusDays(12),
                LocalDateTime.now().plusDays(12).plusHours(1), officeAddress,
                "Legal consultation", entities);

        System.out.println("entities:");
        for( Entity e : entities ) {
            System.out.println("- " + e.getFullName());
        }
    }
}