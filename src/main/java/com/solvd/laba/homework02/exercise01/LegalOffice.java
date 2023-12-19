package com.solvd.laba.homework02.exercise01;

import com.solvd.laba.homework02.exercise01.util.ArrayBasedLinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LegalOffice {

    private static final Logger LOGGER = LogManager.getLogger(LegalOffice.class.getName());
    private final List<LegalCase> cases = new ArrayBasedLinkedList<>();
    /**
     * saved address to simplify creating appointments
     */
    private Map<String, Address> addressBook;


    public LegalOffice(Collection<LegalCase> cases) {
        LOGGER.info("LegalOffice created");
        this.cases.addAll(cases);
        this.addressBook = new HashMap<>();
    }

    public LegalOffice() {
        this(Collections.emptyList());
    }

    public List<LegalCase> getCases() {
        return Collections.unmodifiableList(cases);
    }

    public List<LegalCase> getOpenCases() {
        return this.cases.stream().
                filter(LegalCase::isOpened)
                .toList();
    }

    public List<LegalCase> getClosedCases() {
        return this.cases.stream().
                filter(legalCase -> !legalCase.isOpened())
                .toList();
    }

    public List<LegalCase> getClientCases(IEntity client) {
        return this.cases.stream()
                .filter(legalCase -> legalCase.haveClient(client))
                .toList();
    }

    public void addCase(LegalCase legalCase) {
        LOGGER.info("Legal case with hash " + legalCase.hashCode() + "added to legalOffice");
        this.cases.add(legalCase);
    }

    public void removeCase(LegalCase legalCase) {
        LOGGER.info("Legal case with hash " + legalCase.hashCode() + "removed from legalOffice");
        this.cases.remove(legalCase);
    }

    public Set<IEntity> getClients() {
        return this.cases.stream()
                .flatMap(legalCase -> legalCase.getClients().stream())
                .collect(Collectors.toUnmodifiableSet());
    }

    public List<Appointment> getClientAppointments(IEntity client) {
        return this.cases.stream()
                .flatMap(legalCase -> legalCase.getClientAppointments(client).stream())
                .toList();
    }

    /**
     * returns first client entity that fulfils specified criteria
     *
     * @param predicate criterion for selecting a client
     * @return
     */
    public Optional<IEntity> findClient(Predicate<IEntity> predicate) {
        return getClients().stream()
                .filter(predicate)
                .findFirst();
    }


    public void putToAddressBook(String name, Address address) {
        this.addressBook.put(name, address);
    }

    public Address getFromAddressBook(String name) {
        return this.addressBook.get(name);
    }

    public Map<String, Address> getAddressBook() {
        return this.addressBook;
    }

    /**
     * removes address from addresBook and returns it
     *
     * @param name
     * @return removed address
     */
    public Address removeFromAddressBook(String name) {
        return this.addressBook.remove(name);
    }

    public boolean loadAddressBook(String file) {
        boolean loadSuccesfull = true;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Map.Entry<String, Address> entry = decodeCSVLine(line);
                this.addressBook.put(entry.getKey(), entry.getValue());
            }
        } catch (IOException | AddressDoesntExistException e) {
            loadSuccesfull = false;
            LOGGER.error("Error while reading file \"" + file + "\": " + e);
        }
        return loadSuccesfull;
    }

    public boolean loadAddressBook(URI file) {
        String path = Paths.get(file).toString();
        return loadAddressBook(path);
    }

    public boolean saveAddressBook(String file) {
        boolean saveSuccesfull = true;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Map.Entry<String, Address> entry : this.addressBook.entrySet()) {
                writer.write(encodeCSVLine(entry) + "\n");
            }
        } catch (IOException e) {
            saveSuccesfull = false;
            LOGGER.error("Error while writing to file \"" + file + "\": " + e);
        }
        return saveSuccesfull;
    }

    private String encodeStringForCSV(String text) {
        return text.replaceAll("\\\\", "\\\\\\\\").replaceAll(",", "\\\\,");
    }

    private String decodeStringForCSV(String text) {
        return text.replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\,", ",");
    }

    private String encodeCSVLine(Map.Entry<String, Address> entry) {
        Address address = entry.getValue();
        return encodeStringForCSV(entry.getKey()) +
                ", " + encodeStringForCSV(address.getCountry()) +
                ", " + encodeStringForCSV(address.getCity()) +
                ", " + encodeStringForCSV(address.getPostalCode()) +
                ", " + encodeStringForCSV(address.getStreet()) +
                ", " + encodeStringForCSV(address.getStreetNumber()) +
                ", " + encodeStringForCSV(address.getApartmentNumber());
    }

    private Map.Entry<String, Address> decodeCSVLine(String line) throws IOException, AddressDoesntExistException {
        // split into fields
        String[] fields = line.split("(?<!\\\\)(\\\\\\\\)*,");
        if (fields.length != 7) {
            throw new IOException("bad input format for line: \"" + line + "\", fields number: " + fields.length);
        }

        // decode
        for (int i = 0; i < fields.length; i++) {
            fields[i] = decodeStringForCSV(fields[i]).trim();
        }

        String name = fields[0];
        Address address = new Address(fields[1], fields[2], fields[3], fields[4], fields[5], fields[6]);

        return new AbstractMap.SimpleImmutableEntry<>(name, address);
    }

}
