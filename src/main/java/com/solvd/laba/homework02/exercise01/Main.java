package com.solvd.laba.homework02.exercise01;

import com.solvd.laba.homework02.exercise01.tui.CasesView;
import com.solvd.laba.homework02.exercise01.tui.View;
import com.solvd.laba.homework02.exercise01.tui.Widgets;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {

    static {
        System.setProperty("log4j.configurationFile", "log4j2.xml");
    }

    private static final Logger LOGGER = LogManager.getLogger(Main.class.getName());

    public static void main(String[] args) {
        LOGGER.info("Main started");
        LegalOffice office = new LegalOffice();

        IEntity clientA = LegalOfficeGenerator.generatePerson();
        IEntity clientB = LegalOfficeGenerator.generatePerson();
        IEntity clientC = LegalOfficeGenerator.generateCompany();
        IEntity clientD = new Company("Some New Company", Company.Type.GENERAL_PARTNERSHIP, "SNC-001");
        ArrayList<IEntity> caseClients = new ArrayList<>();
        caseClients.add(clientA);
        caseClients.add(clientB);
        caseClients.add(clientC);
        caseClients.add(clientD);

        IContract caseContract = new ContractPerHour(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(400));
        // add discount

        BigDecimal discount = new BigDecimal("0.20");
        caseContract = new ContractDiscountDecorator(caseContract, discount);

        LegalCase caseA = new LegalCase(caseContract, "Case of missing water");
        office.addCase(caseA);
        caseA.addClient(clientA);
        caseA.addClient(clientB);
        caseA.addClient(clientC);
        caseA.addClient(clientD);


        LegalCase caseB = new LegalCase(caseContract, "Case of angry dog");
        caseB.addClient(clientA);
        office.addCase(caseB);


        TimeSpan consultationTime = new TimeSpan(
                LocalDateTime.of(2023, 10, 27,
                        11, 0, 0),
                LocalDateTime.of(2023, 10, 27,
                        12, 0, 0));
        LegalService consultation = new LegalService(LegalService.Type.CONSULTATION,
                consultationTime);
        consultation.setDescription("Initial consultation");
        consultation.setComplexity(1.3);
        caseA.addService(consultation);
        caseB.addService(consultation);


        TimeSpan researchATime = new TimeSpan(
                LocalDateTime.of(2023, 11, 2,
                        11, 0, 0),
                LocalDateTime.of(2023, 11, 2,
                        12, 0, 0));
        LegalService research = new LegalService(LegalService.Type.RESEARCH,
                consultationTime);
        research.setDescription("Researching water claims");
        research.setComplexity(2);
        caseA.addService(research);


        Address courtAddress = new Address("Poland", "Warsaw", "00-123",
                "Legal Street", "1a", "2b", true);
        LocalDateTime courtStart = LocalDateTime.of(2023, 11, 10, 12, 0, 0);
        LocalDateTime courtEnd = LocalDateTime.of(2023, 11, 10, 15, 0, 0);
        Appointment courtHearing = new Appointment(Appointment.Type.COURT,
                courtStart, courtEnd, courtAddress, "First Hearing", caseClients);
        caseA.addAppointment(courtHearing);


        Appointment caseBAppointmentA = LegalOfficeGenerator.generateAppointment();
        caseBAppointmentA.addParticipant(clientA);
        caseB.addAppointment(caseBAppointmentA);


        office.addCase(LegalOfficeGenerator.generateCase());
        office.addCase(LegalOfficeGenerator.generateCase());
        office.addCase(LegalOfficeGenerator.generateCase());
        office.addCase(LegalOfficeGenerator.generateCase());
        office.addCase(LegalOfficeGenerator.generateCase());

        // TODO add UI for selecting UI type / use args
        //UI ui = new ClientUI(office, clientB);
        //UI ui = new UI(office);
        //UI ui = new UI(LegalOfficeGenerator.generateLegalOffice());
        //UI ui = new FilteredUI(office, new LegalCasesClientFilter(clientA));
        View mainView = new CasesView(office, new Widgets());
        mainView.show();
    }


}
