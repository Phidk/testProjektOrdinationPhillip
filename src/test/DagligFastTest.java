package test;

import ordination.DagligFast;
import ordination.Lægemiddel;
import ordination.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

class DagligFastTest {

    //Test af samletDosis()
    //TC1
    @Test
    public void testSamletDosisEnDag() {
        DagligFast o = new DagligFast(LocalDate.of(2024, 10, 03), LocalDate.of(2024, 10, 03), 1, 1, 1, 1);
        double samletDosis = o.samletDosis();
        assertEquals(samletDosis, 4);
    }
    //TC2
    @Test
    public void testSamletDosistreDage() {
        DagligFast o = new DagligFast(LocalDate.of(2024, 10, 03), LocalDate.of(2024, 10, 10), 1, 2, 3, 4);
        double samletDosis = o.samletDosis();
        assertEquals(samletDosis, 80);
    }

    //Test af døgnDosis()
    //TC3
    @Test
    public void testDøgnDosis() {
        DagligFast o = new DagligFast(LocalDate.of(2024, 10, 03), LocalDate.of(2024, 12, 03), 1, 2, 3, 4);
        double døgnDosis = o.døgnDosis();
        assertEquals(døgnDosis, 10);
    }
}