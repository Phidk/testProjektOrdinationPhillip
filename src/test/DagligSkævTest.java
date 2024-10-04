package test;

import ordination.DagligSkæv;
import ordination.Dosis;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DagligSkævTest {

    // TC4:
    @Test
    public void testOpretDosis() {
        // Arrange:
        DagligSkæv dagligSkæv = new DagligSkæv(LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 7));

        // Act:
        dagligSkæv.opretDosis(LocalTime.of(8, 0), 2);

        // Assert:
        Dosis dosis = dagligSkæv.getDoser().get(0);
        assertEquals(LocalTime.of(8, 0), dosis.getTid());
        assertEquals(2, dosis.getAntal());
    }

    // TC5:
    @Test
    public void testGetDoser() {
        // Arrange:
        DagligSkæv dagligSkæv = new DagligSkæv(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 7));
        dagligSkæv.opretDosis(LocalTime.of(8, 0), 1);
        dagligSkæv.opretDosis(LocalTime.of(12, 0), 1);
        dagligSkæv.opretDosis(LocalTime.of(16, 0), 1);
        dagligSkæv.opretDosis(LocalTime.of(20, 0), 1);

        // Act:
        List<Dosis> doser = dagligSkæv.getDoser();

        // Assert:
        assertEquals(4, doser.size());
        assertEquals(LocalTime.of(8, 0), doser.get(0).getTid());
        assertEquals(1, doser.get(0).getAntal());
        assertEquals(LocalTime.of(12, 0), doser.get(1).getTid());
        assertEquals(1, doser.get(1).getAntal());
        assertEquals(LocalTime.of(16, 0), doser.get(2).getTid());
        assertEquals(1, doser.get(2).getAntal());
        assertEquals(LocalTime.of(20, 0), doser.get(3).getTid());
        assertEquals(1, doser.get(3).getAntal());
    }

    // TC6:
    @Test
    public void testGetType() {
        // Arrange:
        DagligSkæv dagligSkæv = new DagligSkæv(LocalDate.of(2023, 10, 1), LocalDate.of(2023, 10, 7));

        // Act:
        String type = dagligSkæv.getType();

        // Assert:
        assertEquals("Daglig skæv", type);
    }

    // TC7
    @Test
    public void testSamletDosis() {
        // Arrange:
        DagligSkæv dagligSkæv = new DagligSkæv(LocalDate.of(2023, 10, 3),
                LocalDate.of(2023, 10, 10));
        dagligSkæv.opretDosis(LocalTime.of(9, 30), 2);
        dagligSkæv.opretDosis(LocalTime.of(12, 0), 1);
        dagligSkæv.opretDosis(LocalTime.of(16, 0), 1);

        // Act:
        double expectedSamletDosis = (2 + 1 + 1) * dagligSkæv.antalDage();

        // Assert:
        assertEquals(expectedSamletDosis, dagligSkæv.samletDosis());
    }

    // TC8:
    @Test
    public void testDøgnDosis() {
        // Arrange:
        DagligSkæv dagligSkæv = new DagligSkæv(LocalDate.of(2023, 10, 1),
                LocalDate.of(2023, 10, 7));
        dagligSkæv.opretDosis(LocalTime.of(9, 30), 2);
        dagligSkæv.opretDosis(LocalTime.of(12, 0), 1);
        dagligSkæv.opretDosis(LocalTime.of(16, 0), 1);

        // Act:
        double actualDøgnDosis = dagligSkæv.døgnDosis();

        // Assert:
        assertEquals(4, actualDøgnDosis);
    }
}

