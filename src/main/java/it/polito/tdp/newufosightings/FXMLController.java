package it.polito.tdp.newufosightings;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.newufosightings.model.Model;
import it.polito.tdp.newufosightings.model.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

//controller turno A --> switchare al branch master_turnoB per turno B

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextArea txtResult;

    @FXML
    private TextField txtAnno;

    @FXML
    private Button btnSelezionaAnno;

    @FXML
    private ComboBox<String> cmbBoxForma;

    @FXML
    private Button btnCreaGrafo;

    @FXML
    private TextField txtT1;

    @FXML
    private TextField txtAlfa;

    @FXML
    private Button btnSimula;

    @FXML
    void doCreaGrafo(ActionEvent event) {

    	txtResult.clear();
    	int anno = 0;
    	String shape = this.cmbBoxForma.getValue();
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		txtResult.appendText("Puoi inserire solo un numero intero per l'anno");
    		return;
    	}
    	if(shape==null) {
    		txtResult.appendText("Devi selezionare una shape");
    		return;
    	}
    	this.model.creaGrafo(anno, shape);
    	txtResult.appendText("GRAFO CREATO CON SUCCESSO!\nNUMERO VERTICI: "+this.model.getVerticiSize()+"\nNUMERO ARCHI: "+this.model.getArchiSize()+"\n");
    	txtResult.appendText("ELENCO STATI CON PESI TOTALI  DEI VICINI :\n");
    	
    	for(State s : this.model.setPesoTotale()) {
    		txtResult.appendText(s.toString()+"  ---  "+s.getTotalePesi()+"\n");
    	}
    }

    @FXML
    void doSelezionaAnno(ActionEvent event) {

    	txtResult.clear();
    	int anno = 0;
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		txtResult.appendText("Puoi inserire solo un numero intero per l'anno");
    		return;
    	}
    	List<String> shapes = this.model.getShapeAnno(anno);
    	Collections.sort(shapes);
    	this.cmbBoxForma.getItems().addAll(shapes);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	int anno = 0;
    	String shape = this.cmbBoxForma.getValue();
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    		
    	}catch(NumberFormatException nfe) {
    		nfe.printStackTrace();
    		txtResult.appendText("Puoi inserire solo un numero intero per l'anno");
    		return;
    	}
    	if(shape==null) {
    		txtResult.appendText("Devi selezionare una shape");
    		return;
    	}
    	this.model.init(Integer.parseInt(this.txtT1.getText()), Integer.parseInt(this.txtAlfa.getText()), anno, shape);
    	this.model.run();
    	Map<State, Double> ris = this.model.getMappa();
        for (Map.Entry<State, Double> entry : ris.entrySet()) {
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue()+"\n");
        }
    }

    @FXML
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSelezionaAnno != null : "fx:id=\"btnSelezionaAnno\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert cmbBoxForma != null : "fx:id=\"cmbBoxForma\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtT1 != null : "fx:id=\"txtT1\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert txtAlfa != null : "fx:id=\"txtAlfa\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'NewUfoSightings.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
	}
}
