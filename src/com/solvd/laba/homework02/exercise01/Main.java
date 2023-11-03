package com.solvd.laba.homework02.exercise01;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        LegalOffice office = new LegalOffice();

        Entity clientA = new Person("Client", "A");
        Entity clientB = new Company("Company B");
        ArrayList<Entity> caseClients = new ArrayList<>();
        caseClients.add(clientA);
        caseClients.add(clientB);

        Contract caseContract = new ContractPerHour(
                BigDecimal.valueOf(100),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(200),
                BigDecimal.valueOf(400));

        LegalCase caseA = new LegalCase(caseContract, "Case of missing water");
        office.addCase(caseA);
        caseA.addClient(clientA);
        caseA.addClient(clientB);

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

        Address courtAddress = new Address("Poland", "Warsaw", "00-123",
                "Legal Street", "1a", "2b");
        LocalDateTime courtStart = LocalDateTime.of(2023, 11, 10, 12, 00, 00);
        LocalDateTime courtEnd = LocalDateTime.of(2023, 11, 10, 15, 00, 00);
        Appointment courtHearing = new Appointment(Appointment.Type.COURT,
                courtStart, courtEnd, courtAddress, "First Hearing", caseClients);
        caseA.addAppointment(courtHearing);

        
        UI officeUI = new UI(office);
        officeUI.start();

    }
}
