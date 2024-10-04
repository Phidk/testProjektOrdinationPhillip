package test;


import controller.Controller;
import ordination.Lægemiddel;
import ordination.PN;
import ordination.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import storage.Storage;

import java.time.DateTimeException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ControllerTest {
    private Storage storage;
    private Patient patient;
    private Lægemiddel lægemiddel;
    private LocalDate startDato;

    @BeforeEach
    void setUp() {
        storage = new Storage();
        Controller.setStorage(storage);
        patient = new Patient("123456-7890", "Jens Jensen", 30);
        lægemiddel = new Lægemiddel
                ("Acetylsalicylsyre", 1, 1, 1, "Styk");
        startDato = LocalDate.of(2024, 10, 3);
    }

    @Test
    void opretPNOrdination() {

        PN pn = Controller.opretPNOrdination
                (startDato, LocalDate.of(2024,10,4), patient, lægemiddel, 2);

        assertEquals(startDato, pn.getStartDato());
        assertEquals(LocalDate.of(2024, 10, 4), pn.getSlutDato());
        assertEquals(2, pn.getAntalEnheder());


        assertEquals(patient.getOrdinationer().getFirst(), pn);
        assertEquals(lægemiddel, pn.getLaegemiddel());
    }

    @Test
    void opretPNOrdinationDatoException() {
        startDato = LocalDate.of(2024, 11, 3);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> Controller.opretPNOrdination(startDato, LocalDate.of(2024, 10, 4), patient, lægemiddel, 2));

        assertEquals(exception.getMessage(), "Startdatoen må ikke være efter slutdatoen.");
    }

    @Test
    void opretPNOrdinationAntalException() {
        startDato = LocalDate.of(2024, 11, 3);

        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> Controller.opretPNOrdination(startDato, LocalDate.of(2024, 10, 4), patient, lægemiddel, 2));

        assertEquals(exception.getMessage(), "Startdatoen må ikke være efter slutdatoen.");
    }
}
