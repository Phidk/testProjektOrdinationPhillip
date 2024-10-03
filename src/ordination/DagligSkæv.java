package ordination;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DagligSkæv extends Ordination {
    private List<Dosis> doser = new ArrayList<>();

    public DagligSkæv(LocalDate startDato, LocalDate slutDato) {
        super(startDato, slutDato);
    }

    public void opretDosis(Dosis dosis) {
        this.doser.add(dosis);
    }

//    public void removeDosis(Dosis dosis) {
//        this.doser.remove(dosis);
//    }
//
    public List<Dosis> getDoser() {
        return doser;
    }

    @Override
    public double samletDosis() {
        return døgnDosis() * antalDage();
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
        return "Daglig skæv";
    }
}
