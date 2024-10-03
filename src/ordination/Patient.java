package ordination;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private final String cprNr;
    private final String navn;
    private final double vægt;
    private List<Ordination> ordinationer = new ArrayList<>();

    public Patient(String cprNr, String navn, double vægt) {
        this.cprNr = cprNr;
        this.navn = navn;
        this.vægt = vægt;
    }

    public double getVægt() {
        return vægt;
    }

    @Override
    public String toString() {
        return navn + "  " + cprNr;
    }


    public void addOrdination(Ordination ordination) {
        ordinationer.add(ordination);
    }

    public List<Ordination> getOrdinationer() {
        return ordinationer;
    }
}
