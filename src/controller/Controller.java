package controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import ordination.*;
import storage.Storage;

public abstract class Controller {
    //Kommentar!
    private static Storage storage;

    public static void setStorage(Storage storage) {
        Controller.storage = storage;
    }

    /**
     * Opret og returner en PN ordination.
     * Hvis startDato er efter slutDato, kastes en IllegalArgumentException,
     * og ordinationen oprettes ikke.
     * Pre: antal > 0.
     */
    public static PN opretPNOrdination(
            LocalDate startDato, LocalDate slutDato, Patient patient, Lægemiddel lægemiddel,
            double antal) {

        if (startDato.isAfter(slutDato)) {
            throw new IllegalArgumentException("Startdatoen må ikke være efter slutdatoen.");
        }

        PN pn = new PN(startDato, slutDato, patient, antal);
        pn.setLægemiddel(lægemiddel);
//        patient.addOrdination(pn);

        return pn;
    }

    /**
     * Opret og returner en DagligFast ordination.
     * Hvis startDato er efter slutDato, kastes en IllegalArgumentException,
     * og ordinationen oprettes ikke.
     * Pre: morgenAntal, middagAntal, aftenAntal, natAntal >= 0
     */
    public static DagligFast opretDagligFastOrdination(
            LocalDate startDato, LocalDate slutDato, Patient patient, Lægemiddel lægemiddel,
            double morgenAntal, double middagAntal, double aftenAntal, double natAntal) {

        if (startDato.isAfter(slutDato)) {
            throw new IllegalArgumentException("Startdatoen må ikke være efter slutdatoen.");
        }

        DagligFast dagligFast = new DagligFast(startDato, slutDato, patient,
                morgenAntal, middagAntal, aftenAntal, natAntal);
        dagligFast.setLægemiddel(lægemiddel);
//        patient.addOrdination(dagligFast);

        return dagligFast;
    }

    /**
     * Opret og returner en DagligSkæv ordination.
     * Hvis startDato er efter slutDato, kastes en IllegalArgumentException,
     * og ordinationen oprettes ikke.
     * Hvis antallet af elementer i klokkeSlet og antalEnheder er forskellige,
     * kastes en IllegalArgumentException, og ordinationen oprettes ikke.
     * Pre: I antalEnheder er alle tal >= 0.
     */
    public static DagligSkæv opretDagligSkævOrdination(
            LocalDate startDen, LocalDate slutDen, Patient patient, Lægemiddel lægemiddel,
            LocalTime[] klokkeSlet, double[] antalEnheder) {

        if (startDen.isAfter(slutDen)) {
            throw new IllegalArgumentException("Startdatoen må ikke være efter slutdatoen.");
        }

        if (klokkeSlet.length != antalEnheder.length) {
            throw new IllegalArgumentException("Antal klokkeslæt og enheder skal være ens.");
        }

        DagligSkæv dagligSkæv = new DagligSkæv(startDen, slutDen, patient);

        for (int i = 0; i < klokkeSlet.length; i++) {
            dagligSkæv.addDosis(new Dosis(klokkeSlet[i], antalEnheder[i]));
        }

        dagligSkæv.setLægemiddel(lægemiddel);
//        patient.addOrdination(dagligSkæv);

        return dagligSkæv;    }

    /**
     * Tilføj en dato for anvendelse af PN ordinationen.
     * Hvis datoen ikke er indenfor ordinationens gyldighedsperiode,
     * kastes en IllegalArgumentException.
     */
    public static void anvendOrdinationPN(PN ordination, LocalDate dato) {
        if (dato.isBefore(ordination.getStartDato()) || dato.isAfter(ordination.getSlutDato())) {
            throw new IllegalArgumentException("Datoen er udenfor ordinationens gyldighedsperiode.");
        }
        ordination.anvendDosis(dato);
    }

    /**
     * Returner den anbefalede dosis pr. døgn af lægemidlet til patienten
     * (afhænger af patientens vægt).
     */
    public static double anbefaletDosisPrDøgn(Patient patient, Lægemiddel lægemiddel) {
        double vægt = patient.getVægt();
        double dosis;

        if (vægt < 25) {
            dosis = lægemiddel.getEnhedPrKgPrDøgnLet();
        } else if (vægt <= 120) {
            dosis = lægemiddel.getEnhedPrKgPrDøgnNormal();
        } else {
            dosis = lægemiddel.getEnhedPrKgPrDøgnTung();
        }

        return vægt * dosis;

    }

    /** Returner antal ordinationer for det givne vægtinterval og det givne lægemiddel. */
    public static int antalOrdinationerPrVægtPrLægemiddel(
            double vægtStart, double vægtSlut, Lægemiddel lægemiddel) {

        int count = 0;
        List<Patient> patienter = storage.getAllPatienter();

        for (Patient patient : patienter) {
            if (patient.getVægt() >= vægtStart && patient.getVægt() <= vægtSlut) {
                for (Ordination ordination : patient.getOrdinationer()) {
                    if (ordination.getLaegemiddel().equals(lægemiddel)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    public static List<Patient> getAllPatienter() {
        return storage.getAllPatienter();
    }

    public static List<Lægemiddel> getAllLægemidler() {
        return storage.getAllLægemidler();
    }

    public static Patient opretPatient(String cpr, String navn, double vægt) {
        Patient p = new Patient(cpr, navn, vægt);
        storage.storePatient(p);
        return p;
    }

    public static Lægemiddel opretLægemiddel(
            String navn, double enhedPrKgPrDøgnLet, double enhedPrKgPrDøgnNormal,
            double enhedPrKgPrDøgnTung, String enhed) {
        Lægemiddel lm = new Lægemiddel(navn, enhedPrKgPrDøgnLet,
                enhedPrKgPrDøgnNormal, enhedPrKgPrDøgnTung, enhed);
        storage.storeLægemiddel(lm);
        return lm;
    }

    public static void initStorage() {
        Patient jane = opretPatient("121256-0512", "Jane Jensen", 63.4);
        Patient finn = opretPatient("070985-1153", "Finn Madsen", 83.2);
        opretPatient("050972-1233", "Hans Jørgensen", 89.4);
        opretPatient("011064-1522", "Ulla Nielsen", 59.9);
        Patient ib = opretPatient("090149-2529", "Ib Hansen", 87.7);

        Lægemiddel acetylsalicylsyre = opretLægemiddel(
                "Acetylsalicylsyre", 0.1, 0.15,
                0.16, "Styk");
        Lægemiddel paracetamol = opretLægemiddel(
                "Paracetamol", 1, 1.5,
                2, "Ml");
        Lægemiddel fucidin = opretLægemiddel(
                "Fucidin", 0.025, 0.025,
                0.025, "Styk");
        opretLægemiddel(
                "Methotrexate", 0.01, 0.015,
                0.02, "Styk");

        opretPNOrdination(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-12"),
                jane, paracetamol, 123);

        opretPNOrdination(LocalDate.parse("2019-02-12"), LocalDate.parse("2019-02-14"),
                jane, acetylsalicylsyre, 3);

        opretPNOrdination(LocalDate.parse("2019-01-20"), LocalDate.parse("2019-01-25"),
                ib, fucidin, 5);

        opretPNOrdination(LocalDate.parse("2019-01-01"), LocalDate.parse("2019-01-12"),
                jane, paracetamol, 123);

        opretDagligFastOrdination(LocalDate.parse("2019-01-10"), LocalDate.parse("2019-01-12"),
                finn, fucidin, 2, 0, 1, 0);

        LocalTime[] kl = {
                LocalTime.parse("12:00"), LocalTime.parse("12:40"),
                LocalTime.parse("16:00"), LocalTime.parse("18:45")};
        double[] an = {0.5, 1, 2.5, 3};
        opretDagligSkævOrdination(LocalDate.parse("2019-01-23"), LocalDate.parse("2019-01-24"),
                finn, fucidin, kl, an);
    }
}