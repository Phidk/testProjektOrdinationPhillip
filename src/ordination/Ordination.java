package ordination;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class Ordination {
    private LocalDate startDato;
    private LocalDate slutDato;
    private Patient patient;
    private Lægemiddel lægemiddel;

    public Ordination(LocalDate startDato, LocalDate slutDato, Patient patient) {
        this.startDato = startDato;
        this.slutDato = slutDato;
        this.patient = patient;
//        patient.addOrdination(this);
    }

    public LocalDate getStartDato() {
        return startDato;
    }

    public LocalDate getSlutDato() {
        return slutDato;
    }


    public void setLægemiddel(Lægemiddel lægemiddel) {
        this.lægemiddel = lægemiddel;
    }

    /** Note: Nullable param lægemiddel. */
    public Lægemiddel getLaegemiddel() {
        return lægemiddel;
    }

    /**
     * Returner antal hele dage mellem startdato og slutdato
     * (begge dage inklusive).
     */
    public int antalDage() {
        return (int) ChronoUnit.DAYS.between(startDato, slutDato) + 1;
    }

    @Override
    public String toString() {
        return startDato.toString();
    }

    /** Returner den totale dosis, der er givet i den periode, ordinationen er gyldig. */
    public abstract double samletDosis();

    /** Returner den gennemsnitlige dosis givet per dag. */
    public abstract double døgnDosis();

    /** Returner ordinationstypen som en String. */
    public abstract String getType();
}
