package test;


import controller.Controller;
import ordination.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControllerTest {
    private Storage storage;
    private Patient patient;
    private Lægemiddel lægemiddel;
    private LocalDate startDato;
    private LocalDate slutDato;
    private LocalTime[] kl;
    private double[] an;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        Controller.setStorage(storage);
        patient = new Patient("123456-7890", "Jens Jensen", 30);
        lægemiddel = new Lægemiddel("Acetylsalicylsyre",
                1, 1, 1, "Styk");
        startDato = LocalDate.of(2024, 10, 3);
        slutDato = LocalDate.of(2024, 11, 3);
        kl = new LocalTime[]
                {LocalTime.parse("08:00"), LocalTime.parse("14:00"), LocalTime.parse("20:00")};
        an = new double[]{1, 1, 1};
    }

    @Test
    void opretPNOrdination() {

        PN pn = Controller.opretPNOrdination
                (startDato, LocalDate.of(2024,10,4), patient, lægemiddel, 2);

        assertEquals(startDato, pn.getStartDato());
        assertEquals(LocalDate.of(2024, 10, 4), pn.getSlutDato());
        assertEquals(2, pn.getAntalEnheder());
        assertEquals(pn, patient.getOrdinationer().getFirst());
        assertEquals(pn.getLaegemiddel(), lægemiddel);
    }

    @Test
    void opretPNOrdinationDatoException() {
        startDato = LocalDate.of(2024, 11, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> Controller.opretPNOrdination
                (startDato, LocalDate.of(2024, 10, 4), patient, lægemiddel, 2));

        assertEquals("Startdatoen må ikke være efter slutdatoen.", exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void opretPNOrdinationAntalException() {

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> Controller.opretPNOrdination(startDato, LocalDate.of(2024, 10, 4), patient, lægemiddel, 0));


        assertEquals("Antal må ikke være nul eller under.", exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void opretDagligFastOrdination() {

        DagligFast dagligFast = Controller.opretDagligFastOrdination(startDato, slutDato, patient, lægemiddel,
                1, 1,1,1);

        assertEquals(dagligFast.getStartDato(), startDato);
        assertEquals(dagligFast.getSlutDato(), slutDato);
        assertEquals(4, dagligFast.døgnDosis());
        assertEquals(dagligFast.getLaegemiddel(), lægemiddel);
        assertEquals(dagligFast, patient.getOrdinationer().getFirst());
        assertEquals(128, dagligFast.samletDosis());
    }

    @Test
    void opretDagligFastOrdinationMedDatoException() {
        startDato = LocalDate.of(2024, 11, 3);
        slutDato = LocalDate.of(2024, 10, 3);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.opretDagligFastOrdination(startDato, slutDato, patient, lægemiddel,
                        0, 0, 0, 1));

        assertEquals("Startdatoen må ikke være efter slutdatoen.", exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void opretDagligFastOrdinationMedDosisException() {

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.opretDagligFastOrdination(startDato, slutDato, patient, lægemiddel,
                        0, 0, 0, 0));

        assertEquals("Mindst en dosis skal være over 0.", exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void opretDagligSkævOrdination() {
        slutDato = LocalDate.of(2024, 10, 5);

        DagligSkæv dagligSkæv = Controller.opretDagligSkævOrdination(startDato, slutDato, patient, lægemiddel,
                kl, an);

        assertEquals(dagligSkæv.getStartDato(), startDato);
        assertEquals(dagligSkæv.getSlutDato(), slutDato);
        assertEquals(dagligSkæv, patient.getOrdinationer().getFirst());
        assertEquals(dagligSkæv.getLaegemiddel(), lægemiddel);
        assertEquals(3, dagligSkæv.døgnDosis());
        assertEquals(9, dagligSkæv.samletDosis());
        assertEquals(LocalTime.of(8,00), dagligSkæv.getDoser().get(0).getTid());
        assertEquals(LocalTime.of(14,00), dagligSkæv.getDoser().get(1).getTid());
        assertEquals(LocalTime.of(20,00), dagligSkæv.getDoser().get(2).getTid());
    }

    @Test
    void opretDagligSkævOrdinationMedDatoException() {

        slutDato = LocalDate.of(2024, 10, 2);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.opretDagligSkævOrdination(startDato, slutDato, patient, lægemiddel, kl, an));

        assertEquals("Startdatoen må ikke være efter slutdatoen.", exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void opretDagligSkævOrdinationMedArraySizeException() {
        an = new double[]{1, 1};

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.opretDagligSkævOrdination(startDato, slutDato, patient, lægemiddel, kl, an));

        assertEquals("Antal klokkeslæt og enheder skal være ens." ,exception.getMessage());
        assertEquals(0, patient.getOrdinationer().size());
    }

    @Test
    void ordinationPNAnvendt() {
        slutDato = LocalDate.of(2024, 10, 13);
        PN ordination = Controller.opretPNOrdination(startDato, slutDato, patient, lægemiddel, 2);

        Controller.anvendOrdinationPN(ordination, startDato);


        assertEquals(startDato, ordination.getAnvendelsesDatoer().getFirst());
    }

    @Test
    void ordinationPNAnvendtSidsteDato() {
        slutDato = LocalDate.of(2024, 10, 13);
        PN ordination = Controller.opretPNOrdination(startDato, slutDato, patient, lægemiddel, 2);

        Controller.anvendOrdinationPN(ordination, slutDato);

        assertEquals(slutDato, ordination.getAnvendelsesDatoer().getLast());
    }

    @Test
    void ordinationPNAnvendtFørDatoException() {
        slutDato = LocalDate.of(2024, 10, 13);
        PN ordination = Controller.opretPNOrdination(startDato, slutDato, patient, lægemiddel, 2);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.anvendOrdinationPN(ordination, LocalDate.of(2024, 10, 2)));

        assertEquals("Datoen er udenfor ordinationens gyldighedsperiode.", exception.getMessage());
    }

    @Test
    void ordinationPNAnvendtEfterDatoException() {
        slutDato = LocalDate.of(2024, 10, 13);
        PN ordination = Controller.opretPNOrdination(startDato, slutDato, patient, lægemiddel, 2);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                Controller.anvendOrdinationPN(ordination, LocalDate.of(2024, 10, 14)));

        assertEquals("Datoen er udenfor ordinationens gyldighedsperiode.", exception.getMessage());
    }

    @Test
    void anbefaletDosisPrDøgn24Kg() {
        patient = new Patient("123456-7890", "Jens Jensen", 24);
        lægemiddel = new Lægemiddel("Acetylsalicylsyre",
                1, 3, 7, "Styk");

        assertEquals(24, Controller.anbefaletDosisPrDøgn(patient, lægemiddel));
    }
    @Test
    void anbefaletDosisPrDøgn25Kg() {
        patient = new Patient("123456-7890", "Jens Jensen", 25);
        lægemiddel = new Lægemiddel("Acetylsalicylsyre",
                1, 3, 7, "Styk");

        assertEquals(75, Controller.anbefaletDosisPrDøgn(patient, lægemiddel));
    }

    @Test
    void anbefaletDosisPrDøgn120Kg() {
        patient = new Patient("123456-7890", "Jens Jensen", 120);
        lægemiddel = new Lægemiddel("Acetylsalicylsyre",
                1, 3, 7, "Styk");

        assertEquals(360, Controller.anbefaletDosisPrDøgn(patient, lægemiddel));
    }

    @Test
    void anbefaletDosisPrDøgn121Kg() {
        patient = new Patient("123456-7890", "Jens Jensen", 121);
        lægemiddel = new Lægemiddel("Acetylsalicylsyre",
                1, 3, 7, "Styk");

        assertEquals(847, Controller.anbefaletDosisPrDøgn(patient, lægemiddel));
    }
}
