package ordination;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DagligFast {
    private final Dosis[] doser = new Dosis[4];


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
