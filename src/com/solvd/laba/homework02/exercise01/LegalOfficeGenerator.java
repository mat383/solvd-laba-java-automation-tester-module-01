package com.solvd.laba.homework02.exercise01;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;


public class LegalOfficeGenerator {
    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    // minimum and maximum duration (in hours) values for generating random timespan
    // used for appointments and services
    private static final int MIN_TIMESPAN_DURATION_HOURS = 1;
    private static final int MAX_TIMESPAN_DURATION_HOURS = 4;
    // minimum and maximum values for generating random timespan
    // used for appointments and services
    private static final int MIN_TIMESPAN_HOUR_START = 10;
    private static final int MAX_TIMESPAN_HOUR_START = 15;

    // minimum and maximum values for generating random day of month
    // used for appointments and services
    private static final int MIN_TIMESPAN_DAY_OF_MONTH = 1;
    private static final int MAX_TIMESPAN_DAY_OF_MONTH = 20;

    // minimum and maximum values for generating random month
    // used for appointments and services
    private static final int MIN_TIMESPAN_MONTHS_OFFSET = 1;
    private static final int MAX_TIMESPAN_MONTHS_OFFSET = 4;

    // values for generating service complexity
    private static final double NON_STANDARD_SERVICE_COMPLEXITY_CHANCE = 0.3;
    private static final double MIN_SERVICE_COMPLEXITY = 0.4;
    private static final double MAX_SERVICE_COMPLEXITY = 3;

    // values for generating price modifier
    private static final double NON_STANDARD_PRICE_CHANCE = 0.3;
    private static final double MIN_PRICE_MODIFIER = 0.4;
    private static final double MAX_PRICE_MODIFIER = 3;

    private static final Random RANDOM_GENERATOR = new Random();


    public static LegalOffice generateLegalOffice() {
        LegalOffice generatedOffice = new LegalOffice();
        int legalCasesNumber = RANDOM_GENERATOR.nextInt(4, 14);
        List<LegalCase> legalCases = generateMany(
                legalCasesNumber,
                LegalOfficeGenerator::generateCase);

        for (LegalCase legalCase : legalCases) {
            generatedOffice.addCase(legalCase);
        }

        return generatedOffice;
    }

    public static LegalCase generateCase() {
        Contract contract = generateContact();
        String description = generateLegalCaseDescription();
        boolean isOpened = RANDOM_GENERATOR.nextBoolean();

        // generate clients
        // it might add less than clientsNumber if duplicates are generated
        int clientsNumber = RANDOM_GENERATOR.nextInt(1, 4);
        List<Entity> clients = new ArrayList<>(clientsNumber);
        for (int i = 0; i < clientsNumber; ++i) {
            Entity generatedClient = generateEntity();
            if (!clients.contains(generatedClient)) {
                clients.add(generatedClient);
            } else {
                LOGGER.info("generateEntity generated duplicate of already generated client");
            }
        }

        // generate appointments (only if case is opened)
        int appointmentsNumber = isOpened
                ? RANDOM_GENERATOR.nextInt(3, 8)
                : 0;
        List<Appointment> appointments = generateMany(
                appointmentsNumber,
                () -> LegalOfficeGenerator.generateAppointment(clients));

        // generate services
        int servicesNumber = RANDOM_GENERATOR.nextInt(3, 10);
        List<LegalService> services = generateMany(
                servicesNumber,
                LegalOfficeGenerator::generateLegalService);

        // create case
        LegalCase generatedCase = new LegalCase(contract, description,
                isOpened, clients, appointments);

        // add services
        for (LegalService legalService : services) {
            generatedCase.addService(legalService);
        }

        return generatedCase;
    }

    public static String generateLegalCaseDescription() {
        final String[] PREFIXES = {"Strange case of", "Case of", "Curious case of", "New case of"};
        final String[] FIRST_PART = {"water", "dog", "city", "possession", "pen"};
        final String[] LAST_PART = {"which was missing", "which was stolen", "which was lost", "which was accused of many things"};
        return randomElement(PREFIXES) + " " + randomElement(FIRST_PART) + " " + randomElement(LAST_PART);
    }

    public static Contract generateContact() {
        boolean flatPrice = RANDOM_GENERATOR.nextBoolean();

        if (flatPrice) {
            String description = "Flat price contract";
            BigDecimal price = BigDecimal.valueOf(RANDOM_GENERATOR.nextInt(100, 1_000));
            return new ContractFlatPrice(description, price);
        } else {
            String description = "Per hour contract";
            BigDecimal priceConsult = BigDecimal.valueOf(RANDOM_GENERATOR.nextInt(50, 500));
            BigDecimal priceHearing = BigDecimal.valueOf(RANDOM_GENERATOR.nextInt(50, 500));
            BigDecimal priceResearch = BigDecimal.valueOf(RANDOM_GENERATOR.nextInt(50, 500));
            BigDecimal priceAdvice = BigDecimal.valueOf(RANDOM_GENERATOR.nextInt(50, 500));
            return new ContractPerHour(description, priceConsult, priceHearing, priceResearch, priceAdvice);
        }
    }

    public static Entity generateEntity() {
        boolean isCompany = RANDOM_GENERATOR.nextBoolean();

        if (isCompany) {
            return generateCompany();
        } else {
            return generatePerson();
        }
    }

    public static Company generateCompany() {
        final String[] COMPANY_FIRST_PART = {"New", "Green", "Fast", "Traditional"};
        final String[] COMPANY_SECOND_PART = {"Industries", "Corporation", "Company"};
        String name = randomElement(COMPANY_FIRST_PART) + " " + randomElement(COMPANY_SECOND_PART);
        return new Company(name);
    }

    public static Person generatePerson() {
        final String[] FIRST_NAME = {"John", "Martha", "Jack", "Elisabeth", "Elijah", "Sophia"};
        final String[] LAST_NAME = {"London", "Smith", "Lee", "Gonzales", "Keller"};
        String id = Long.toString(RANDOM_GENERATOR.nextLong(1_000_000_000_000_000_000L, 2_000_000_000_000_000_000L));
        return new Person(id, randomElement(FIRST_NAME), randomElement(LAST_NAME));
    }


    public static LegalService generateLegalService() {
        // TODO randomly generate description
        LegalService.Type type = randomElement(LegalService.Type.values());
        TimeSpan timeSpan = generateTimeSpan(false);
        String description = "Randomly generated service";
        String annotation = "";

        boolean nonStandardComplexity = randomBoolean(NON_STANDARD_SERVICE_COMPLEXITY_CHANCE);
        double complexity = LegalService.STANDARD_COMPLEXITY;
        if (nonStandardComplexity) {
            complexity = RANDOM_GENERATOR.nextDouble(
                    MIN_SERVICE_COMPLEXITY,
                    MAX_SERVICE_COMPLEXITY);
        }

        boolean nonStandardPrice = randomBoolean(NON_STANDARD_PRICE_CHANCE);
        double priceModifier = LegalService.UNMODIFIED_PRICE;
        if (nonStandardPrice) {
            priceModifier = RANDOM_GENERATOR.nextDouble(
                    MIN_PRICE_MODIFIER,
                    MAX_PRICE_MODIFIER);
        }

        return new LegalService(type, timeSpan, description, annotation, complexity, priceModifier);
    }


    public static Appointment generateAppointment() {
        return generateAppointment(Collections.<Entity>emptyList());
    }

    public static Appointment generateAppointment(List<Entity> possibleParticipants) {
        // TODO randomly generate description
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
            addParticipant = RANDOM_GENERATOR.nextBoolean();
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
        final String[] CITIES = {"Warsaw", "Cracow"};
        final String[] STREETS = {"New Street", "Old Street", "Legal Street", "Main Street"};
        // postfix added to street and apartment number
        final String[] NUMBER_POSTFIX = {"a", "b", "c", "d", "", "", ""};

        String country = "Poland";
        String city = randomElement(CITIES);
        String postalCode = Integer.toString(RANDOM_GENERATOR.nextInt(10, 100))
                + "-" + Integer.toString(RANDOM_GENERATOR.nextInt(100, 1_000));
        String street = randomElement(STREETS);
        String streetNumber = Integer.toString(RANDOM_GENERATOR.nextInt(1, 40))
                + randomElement(NUMBER_POSTFIX);
        String apartmentNumber = Integer.toString(RANDOM_GENERATOR.nextInt(1, 40))
                + randomElement(NUMBER_POSTFIX);

        return new Address(country, city, postalCode,
                street, streetNumber, apartmentNumber);
    }


    public static <T> T randomElement(T[] elements) {
        int randomIndex = RANDOM_GENERATOR.nextInt(0, elements.length);
        return elements[randomIndex];
    }

    public static boolean randomBoolean(double truthChance) {
        return RANDOM_GENERATOR.nextDouble(0, 1) < truthChance;
    }

    private static <T> List<T> generateMany(int number, Supplier<T> generator) {
        List<T> generatedList = new ArrayList<>(number);
        for (int i = 0; i < number; i++) {
            generatedList.add(generator.get());
        }
        return generatedList;
    }
}
