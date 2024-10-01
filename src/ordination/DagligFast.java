package ordination;

import java.lang.reflect.Array;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class DagligFast extends Ordination{
    private final Dosis[] doser = new Dosis[4];

    public DagligFast(LocalDate startDato, LocalDate slutDato, Patient patient,
                      double morgenAntal, double middagAntal, double aftenAntal, double natAntal) {

        super(startDato, slutDato, patient);

        doser[0] = new Dosis(LocalTime.of(8, 00), morgenAntal);
        doser[1] = new Dosis(LocalTime.of(12, 00), middagAntal);
        doser[2] = new Dosis(LocalTime.of(18, 00), aftenAntal);
        doser[3] = new Dosis(LocalTime.of(00, 00), natAntal);
    }

    @Override
    public double samletDosis() {
        double dagligDosis = 0;

        for (Dosis dosis : doser) {
            dagligDosis += dosis.getAntal();
        }

        return dagligDosis * antalDage();
    }

    @Override
    public double døgnDosis() {
        double dagligDosis = 0;

        for (Dosis dosis : doser) {
            dagligDosis += dosis.getAntal();
        }

        return dagligDosis;
    }

    @Override
    public String getType() {
        return "Daglig fast";
    }

    public void addDose(int index, Dosis dosis) {
        if (index >= 0 && index < 4) {
            this.doser[index] = dosis;
        } else {
            throw new IndexOutOfBoundsException("Index must be between 0 and 3.");
        }
    }

    public void removeDose(int index) {
        if (index >= 0 && index < 4) {
            this.doser[index] = null;
        } else {
            throw new IndexOutOfBoundsException("Index must be between 0 and 3.");
        }
    }


    public Dosis[] getDoser() {
        return doser;
    }
}
