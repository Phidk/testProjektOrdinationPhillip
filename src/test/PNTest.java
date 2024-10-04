package test;

import controller.Controller;
import ordination.PN;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PNTest {
    private PN pn;
    @BeforeEach
    public void setup() {
        pn = new PN(LocalDate.of(2024, 10, 3), LocalDate.of(2024, 12, 3), 3);
    }
    //TC9
    @Test
    public void testSamletDosis() {
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 4));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 5));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 7));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 9));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 10));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 13));

        double samletDosis = pn.samletDosis();
        assertEquals(samletDosis, 24, 000.1);
    }
    //TC10
    @Test
    public void testDøgnDosisKronoligiskeAnvendelser() {
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 4));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 5));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 7));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 9));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 10));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 13));

        double døgnDosis = pn.døgnDosis();
        assertEquals(døgnDosis, 2.4, 000.1);
    }
    //TC11
    @Test
    public void testDøgnDosisIkkeKronoligiskeAnvendelser() {
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 5));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 4));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 7));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 8));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 13));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 10));
        Controller.anvendOrdinationPN(pn, LocalDate.of(2024, 10, 12));

        double døgnDosis = pn.døgnDosis();
        assertEquals(døgnDosis, 2.4, 000.1);
    }
}