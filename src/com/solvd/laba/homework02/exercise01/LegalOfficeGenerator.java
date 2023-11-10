package com.solvd.laba.homework02.exercise01;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;


/* legal office structure:
- legalOffice
  - legalCases
    - contract
    - description
    - isOpened
    - clients
    - Appointments
      - timespan
      - address
      - participants
    - services
      - type
      - timespan
      - description
 */

public class LegalOfficeGenerator {
    // minimum and maximum duration (in hours) values for generating random timespan
    // used for appointments and services
    static final int MIN_TIMESPAN_DURATION_HOURS = 1;
    static final int MAX_TIMESPAN_DURATION_HOURS = 4;
    // minimum and maximum values for generating random timespan
    // used for appointments and services
    static final int MIN_TIMESPAN_HOUR_START = 10;
    static final int MAX_TIMESPAN_HOUR_START = 15;

    // minimum and maximum values for generating random day of month
    // used for appointments and services
    static final int MIN_TIMESPAN_DAY_OF_MONTH = 1;
    static final int MAX_TIMESPAN_DAY_OF_MONTH = 20;

    // minimum and maximum values for generating random month
    // used for appointments and services
    static final int MIN_TIMESPAN_MONTHS_OFFSET = 1;
    static final int MAX_TIMESPAN_MONTHS_OFFSET = 4;


    public static LegalOffice generateLegalOffice() {
        LegalOffice generatedOffice = new LegalOffice();

        return generatedOffice;
    }

    public static Appointment generateAppointment() {
        return generateAppointment(Collections.<Entity>emptyList());
    }

    public static Appointment generateAppointment(List<Entity> possibleParticipants) {
        // TODO randomly generate description
        Random random = new Random();
        Appointment.Type type = randomElement(Appointment.Type.values());
        TimeSpan timeSpan = generateTimeSpan(true);
        Address location = generateAddress();

        Appointment generatedAppointment = new Appointment(type,
                timeSpan.getStart(), timeSpan.getEnd(),
                location, "Some random appointment");

        // add random participants from possible participants
        List<Entity> shuffledPossibleParticipants = new ArrayList<>(possibleParticipants);
        Collections.shuffle(shuffledPossibleParticipants);
        // set initially to true to add at least one participant (if exists)
        boolean addParticipant = true;
        for (Entity participant : shuffledPossibleParticipants) {
            if (addParticipant && !generatedAppointment.haveParticipant(participant)) {
                generatedAppointment.addParticipant(participant);
            }
            addParticipant = random.nextBoolean();
        }

        return generatedAppointment;

    }

    public static TimeSpan generateTimeSpan(boolean inFuture) {
        Random random = new Random();
        int hour = random.nextInt(MIN_TIMESPAN_HOUR_START, MAX_TIMESPAN_HOUR_START + 1);
        int duration = random.nextInt(MIN_TIMESPAN_DURATION_HOURS, MAX_TIMESPAN_DURATION_HOURS + 1);
        int day = random.nextInt(MIN_TIMESPAN_DAY_OF_MONTH, MAX_TIMESPAN_DAY_OF_MONTH + 1);
        int month_offset = random.nextInt(MIN_TIMESPAN_MONTHS_OFFSET, MAX_TIMESPAN_MONTHS_OFFSET + 1);

        LocalDateTime start = LocalDateTime.now();
        // set month
        if (inFuture) {
            start = start.plusMonths(month_offset);
        } else {
            start = start.minusMonths(month_offset);
        }
        // set rest
        start = start.withDayOfMonth(day)
                .withHour(hour)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);
        LocalDateTime end = start.plusHours(duration);

        return new TimeSpan(start, end);
    }

    public static Address generateAddress() {
        Random random = new Random();
        final String[] CITIES = {"Warsaw", "Cracow"};
        final String[] STREETS = {"New Street", "Old Street", "Legal Street", "Main Street"};
        // postfix added to street and apartment number
        final String[] NUMBER_POSTFIX = {"a", "b", "c", "d", "", "", ""};

        String country = "Poland";
        String city = randomElement(CITIES);
        String postalCode = Integer.toString(random.nextInt(10, 100))
                + "-" + Integer.toString(random.nextInt(100, 1_000));
        String street = randomElement(STREETS);
        String streetNumber = Integer.toString(random.nextInt(1, 40))
                + randomElement(NUMBER_POSTFIX);
        String apartmentNumber = Integer.toString(random.nextInt(1, 40))
                + randomElement(NUMBER_POSTFIX);

        return new Address(country, city, postalCode,
                street, streetNumber, apartmentNumber);
    }


    public static <T> T randomElement(T[] elements) {
        Random random = new Random();
        int randomIndex = random.nextInt(0, elements.length);
        return elements[randomIndex];
    }
}
