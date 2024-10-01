package gui;

import controller.Controller;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import ordination.DagligSkæv;
import ordination.Dosis;
import ordination.Ordination;
import ordination.PN;

import java.time.LocalDate;

public class OrdinationDetailsPane extends GridPane {
    private final TextField txtStarttid;
    private final TextField txtSluttid;
    private final TextField txtLægemiddel;
    private final TextField txtDøgndosis;
    private final TextField txtTotalDosis;
    private final TextField txtType = new TextField();

    // Daglig fast
    private final DagligFastPane fastPane = new DagligFastPane(240);

    // Daglig skæv
    private final TextArea textAreaSkæv = new TextArea();

    // PN
    private final GridPane pnPane = new GridPane();
    private final TextField txtDosis = new TextField();
    private final TextField txtAnvendt = new TextField();
    private final DatePicker datePicker = new DatePicker();
    private PN pn;

    private final Label lblError = new Label();

    public OrdinationDetailsPane() {
        setHgap(20);
        setVgap(10);
        setGridLinesVisible(false);

        txtStarttid = new TextField();
        txtStarttid.setEditable(false);
        txtSluttid = new TextField();
        txtSluttid.setEditable(false);
        txtLægemiddel = new TextField();
        txtLægemiddel.setEditable(false);
        txtDøgndosis = new TextField();
        txtDøgndosis.setEditable(false);
        txtTotalDosis = new TextField();
        txtTotalDosis.setEditable(false);

        Label lblType = new Label("Type");
        lblType.setMinWidth(90);
        this.add(lblType, 0, 0);
        this.add(new Label("Starttid"), 0, 1);
        this.add(new Label("Sluttid"), 0, 2);
        this.add(new Label("Lægemiddel"), 0, 3);
        this.add(new Label("Døgndosis"), 0, 4);
        this.add(new Label("Total dosis"), 0, 5);

        this.add(txtType, 1, 0);
        this.add(txtStarttid, 1, 1);
        this.add(txtSluttid, 1, 2);
        this.add(txtLægemiddel, 1, 3);
        this.add(txtDøgndosis, 1, 4);
        this.add(txtTotalDosis, 1, 5);

        // Daglig fast
        fastPane.makeReadOnly();

        // Daglig skæv
        textAreaSkæv.setMaxWidth(200);
        textAreaSkæv.setEditable(false);

        // PN
        pnPane.setHgap(20);
        pnPane.setVgap(10);
        pnPane.setGridLinesVisible(false);
        pnPane.add(new Label("Dosis"), 0, 1);
        pnPane.add(new Label("Givet"), 0, 2);
        pnPane.add(txtDosis, 1, 1);
        pnPane.add(txtAnvendt, 1, 2);
        pnPane.add(datePicker, 0, 3);
        Button btnAnvend = new Button("Anvend ordination");
        pnPane.add(btnAnvend, 1, 3);
        datePicker.setMaxWidth(90);

        btnAnvend.setOnAction(event -> actionAnvend());
        
        lblError.setTextFill(Color.RED);
        pnPane.add(lblError, 0, 8, 2, 1);
    }

    private void actionAnvend() {
        lblError.setText("");
        LocalDate anvendtDato = datePicker.getValue();
        try {
            Controller.anvendOrdinationPN(pn, anvendtDato);
        }
        catch (IllegalArgumentException e) {
            lblError.setText(e.getMessage());
            return;
        }
        txtAnvendt.setText(pn.antalGangeAnvendt() + " gange");
        txtDosis.setText(pn.getAntalEnheder() + "");

        txtDøgndosis.setText(pn.doegnDosis() + " "
            + pn.getLaegemiddel().getEnhed());
        txtTotalDosis.setText(pn.samletDosis() + " "
            + pn.getLaegemiddel().getEnhed());
    }

    public void clear() {
        txtType.clear();
        txtStarttid.clear();
        txtSluttid.clear();
        txtLægemiddel.clear();
        txtDøgndosis.clear();
        txtTotalDosis.clear();
        getChildren().remove(fastPane);
        getChildren().remove(textAreaSkæv);
        getChildren().remove(pnPane);
    }

    public void setOrdination(Ordination ordination) {
        txtType.setText(ordination.getType());
        txtStarttid.setText(ordination.getStartDato().toString());
        txtSluttid.setText(ordination.getSlutDato().toString());
        txtLægemiddel.setText(ordination.getLaegemiddel().toString());
        txtDøgndosis.setText(ordination.døgnDosis() + "");
        txtTotalDosis.setText(ordination.samletDosis() + "");
    }

    public void setFast(Dosis morgen, Dosis middag, Dosis aften, Dosis nat) {
        this.add(fastPane, 0, 6, 2, 1);
        if (morgen != null) {
            fastPane.setMorgen(morgen.getAntal() + "");
        }
        if (middag != null) {
            fastPane.setMiddag(middag.getAntal() + "");
        }
        if (aften != null) {
            fastPane.setAften(aften.getAntal() + "");
        }
        if (nat != null) {
            fastPane.setNat(nat.getAntal() + "");
        }
    }

    public void setSkæv(DagligSkæv skæv) {
        textAreaSkæv.clear();
        this.add(textAreaSkæv, 0, 6, 2, 1);
        for (Dosis d : skæv.getDoser()) {
            textAreaSkæv.appendText(d.toString() + "\n");
        }
    }

    public void setPN(PN pn) {
        this.pn = pn;
        this.add(pnPane, 0, 6, 2, 1);
        txtDosis.setText(pn.getAntalEnheder() + "");
        txtAnvendt.setText(pn.antalGangeAnvendt() + " antal");
    }
}
