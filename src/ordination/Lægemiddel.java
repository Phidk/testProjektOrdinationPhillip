package ordination;

public class Lægemiddel {
    private final String navn;
    private final double enhedPrKgPrDøgnLet;    // faktor der anvendes hvis patient vejer < 25 kg
    private final double enhedPrKgPrDøgnNormal; // faktor der anvendes hvis 25 kg <= patient vægt <= 120 kg
    private final double enhedPrKgPrDøgnTung;   // faktor der anvendes hvis patient vægt > 120 kg
    private final String enhed;

    public Lægemiddel(
            String navn, double enhedPrKgPrDøgnLet, double enhedPrKgPrDøgnNormal,
            double enhedPrKgPrDøgnTung, String enhed) {
        this.navn = navn;
        this.enhedPrKgPrDøgnLet = enhedPrKgPrDøgnLet;
        this.enhedPrKgPrDøgnNormal = enhedPrKgPrDøgnNormal;
        this.enhedPrKgPrDøgnTung = enhedPrKgPrDøgnTung;
        this.enhed = enhed;
    }

    public String getEnhed() {
        return enhed;
    }

    public double getEnhedPrKgPrDøgnLet() {
        return enhedPrKgPrDøgnLet;
    }

    public double getEnhedPrKgPrDøgnNormal() {
        return enhedPrKgPrDøgnNormal;
    }

    public double getEnhedPrKgPrDøgnTung() {
        return enhedPrKgPrDøgnTung;
    }

    @Override
    public String toString() {
        return navn;
    }
}
