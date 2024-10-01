// File Path: C:\Users\Phidk\IdeaProjects\testProjektOrdinationPhillip\src\ordination\PN.java
package ordination;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PN extends Ordination {
    private final double antalEnheder;
    private final List<LocalDate> anvendelsesDatoer = new ArrayList<>();

    public PN(LocalDate startDato, LocalDate slutDato, Patient patient, double antalEnheder) {
        super(startDato, slutDato, patient);
        this.antalEnheder = antalEnheder;
    }

    public double getAntalEnheder() {
        return antalEnheder;
    }

    @Override
    public double samletDosis() {
        return antalEnheder * anvendelsesDatoer.size();
    }

    @Override
    public double døgnDosis() {
        return samletDosis() / antalDage();
    }

    @Override
    public String getType() {
        return "PN";
    }

    /**
     * Registrer datoen for en anvendt dosis.
     */
    public void anvendDosis(LocalDate dato) {
        if (dato.isBefore(getStartDato()) || dato.isAfter(getSlutDato())) {
            throw new IllegalArgumentException("Datoen er udenfor ordinationens gyldighedsperiode.");
        }
        anvendelsesDatoer.add(dato);
    }

    /** Returner antal gange ordinationen er anvendt. */
    public int antalGangeAnvendt() {
        return anvendelsesDatoer.size();
    }
}

