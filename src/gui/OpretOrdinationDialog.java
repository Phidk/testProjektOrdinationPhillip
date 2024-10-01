package gui;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import ordination.Lægemiddel;
import ordination.Patient;

import java.time.LocalTime;

public class OpretOrdinationDialog extends Stage {

	private final Patient patient;
	private final Lægemiddel lægemiddel;
	private final TypeOrdination type;

	private final DatePicker startDato = new DatePicker();
	private final DatePicker slutDato = new DatePicker();
	private final TextField txtStyk = new TextField();

	private final Button btnOpret = new Button("Opret");
	private final Button btnFortryd = new Button("Fortryd");

	private final Label lblError = new Label();
	private DagligFastPane dagligFastPane;
	private DagligSkævPane dagligSkævPane;

	public OpretOrdinationDialog(Patient patient, Lægemiddel lægemiddel, TypeOrdination type) {
	    
		this.patient = patient;
		this.lægemiddel = lægemiddel;
		this.type = type;

		this.initStyle(StageStyle.UTILITY);
		this.initModality(Modality.APPLICATION_MODAL);
		this.setResizable(false);
		this.setTitle("Ordinér medicin!");

		GridPane pane = new GridPane();
		Scene scene = new Scene(pane);
		this.initContent(pane);
		this.setScene(scene);
	}

	private void initContent(GridPane pane) {
		pane.setPadding(new Insets(20));
		pane.setHgap(20);
		pane.setVgap(10);
		pane.setGridLinesVisible(false);

		pane.add(new Label("Patient: " + patient), 0, 0, 2, 1);
		pane.add(new Label("Lægemiddel: "), 0, 1);
		pane.add(new Label(lægemiddel + ""), 1, 1);

		pane.add(new Label("Startdato for ordination: "), 0, 2);
		pane.add(startDato, 1, 2);
		pane.add(new Label("Slutdato for ordination: "), 0, 3);
		pane.add(slutDato, 1, 3);

		pane.add(new Label("Anbefalet antal enheder pr døgn "), 0, 4);
		String antal = String.format("%.2f",
				Controller.anbefaletDosisPrDøgn(patient, lægemiddel))
				+ lægemiddel.getEnhed();
		pane.add(new Label(antal), 1, 4);

		if (this.type.equals(TypeOrdination.PN)) {
			pane.add(new Label("Angiv antal enheder (styk):"), 0, 5);
			pane.add(txtStyk, 1, 5);
		} else if (this.type.equals(TypeOrdination.FAST)) {
			this.dagligFastPane = new DagligFastPane(300);
			pane.add(dagligFastPane, 0, 5, 2, 1);
		} else if (this.type.equals(TypeOrdination.SKÆV)) {
			this.dagligSkævPane = new DagligSkævPane();
			pane.add(dagligSkævPane, 0, 5, 2, 1);
		}

		btnOpret.setMinWidth(100);
		btnOpret.setOnAction(event -> opretAction());
		btnFortryd.setMinWidth(100);
		btnFortryd.setOnAction(event -> this.hide());

		HBox hbox = new HBox(10);
		hbox.alignmentProperty().set(Pos.BOTTOM_CENTER);
		hbox.getChildren().add(btnOpret);
		hbox.getChildren().add(btnFortryd);
		pane.add(hbox, 0, 7, 2, 1);

		lblError.setTextFill(Color.RED);
		pane.add(lblError, 0, 8, 2, 1);
	}

	private void opretAction() {
		if (this.type.equals(TypeOrdination.PN)) {
			opretPN();
		} else if (this.type.equals(TypeOrdination.FAST)) {
			opretFast();
		} else if (this.type.equals(TypeOrdination.SKÆV)) {
			opretSkæv();
		}
	}

	private void opretPN() {
		if (txtStyk.getText().isEmpty()) {
			lblError.setText("Angiv antal enheder");
		} else {
			double dose = Double.parseDouble(txtStyk.getText());
			if (dose <= 0) {
				lblError.setText("Dosis skal være et positivt tal");
				return;
			}
			try {
				Controller.opretPNOrdination(startDato.getValue(),
						slutDato.getValue(), patient, lægemiddel, dose);
			} catch (IllegalArgumentException e) {
				lblError.setText(e.getMessage());
				return;
			}
			this.hide();
		}
	}

	public void opretFast() {
		double[] doser = { 0, 0, 0, 0 };

		if (startDato.getValue() == null || slutDato.getValue() == null) {
			lblError.setText("Datoer skal angives");
			return;
		}

		try {
			parseField(dagligFastPane.getMorgen(), doser, 0);
			parseField(dagligFastPane.getMiddag(), doser, 1);
			parseField(dagligFastPane.getAften(), doser, 2);
			parseField(dagligFastPane.getNat(), doser, 3);

			Controller.opretDagligFastOrdination(startDato.getValue(),
					slutDato.getValue(), patient, lægemiddel, doser[0],
					doser[1], doser[2], doser[3]);
		} catch (IllegalArgumentException e) {
			lblError.setText(e.getMessage());
			return;
		}

		this.hide();
	}

	private void parseField(Double field, double[] doser, int index) {
		if (field != null) {
			double dose = field;
			if (dose >= 0) {
				doser[index] = dose;
			} else {
				throw new IllegalArgumentException(
						"Dosis skal være et positivt tal");
			}
		}
	}

	private void opretSkæv() {
		if (startDato.getValue() == null || slutDato.getValue() == null) {
			lblError.setText("Datoer skal angives");
			return;
		}

		String[] doser = dagligSkævPane.getDosisArray();
		try {
			Controller.opretDagligSkævOrdination(startDato.getValue(),
					slutDato.getValue(), patient, lægemiddel,
					makeKlokkeSlet(doser), makeAntal(doser));
		} catch (IllegalArgumentException e) {
			lblError.setText(e.getMessage());
			return;
		}
		this.hide();
	}

	private LocalTime[] makeKlokkeSlet(String[] model) {
		LocalTime[] resultat = new LocalTime[model.length];
		try {
			for (int i = 0; i < model.length; i++) {
				resultat[i] = LocalTime.parse(model[i].substring(0, 5));
			}
		} catch (RuntimeException e) {
			throw new IllegalArgumentException(
					"Klokkeslæt er ikke korrekt angivet");
		}
		return resultat;
	}

	private double[] makeAntal(String[] model) {
		double[] resultat = new double[model.length];
		try {
			for (int i = 0; i < model.length; i++) {
				double dosis = Double.parseDouble((model[i]).substring(6));
				resultat[i] = dosis;
			}
		} catch (RuntimeException e) {
			throw new IllegalArgumentException(
					"Der er ikke angivet korrekt antal");
		}
		return resultat;
	}
}
