/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.genes;

import java.net.URL;
import java.util.ResourceBundle;

import java.util.List;

import it.polito.tdp.genes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model ;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnContaArchi"
    private Button btnContaArchi; // Value injected by FXMLLoader

    @FXML // fx:id="btnRicerca"
    private Button btnRicerca; // Value injected by FXMLLoader

    @FXML // fx:id="txtSoglia"
    private TextField txtSoglia; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doContaArchi(ActionEvent event) {

    	if(this.model.isGrafoLoaded()) {
    		try {
    			double soglia = Double.parseDouble(this.txtSoglia.getText());
    			if( soglia>=this.model.getPesoMinimo() && soglia<=this.model.getPesoMassimo()) {
    				this.txtResult.appendText("Soglia: " + soglia + " --> #Archi di peso maggiore: " + this.model.getNumArchiPesoMaggiore(soglia) + "; #Archi di peso minore: "+ this.model.getNumArchiPesoMinore(soglia)+ "\n");
    			}else
    				this.txtResult.appendText("\nERRORE:Inserire un valore numerico ne campo 'soglia', tra " + this.model.getPesoMinimo() + " e "+ this.model.getPesoMassimo()+ "\n\n");
    		}catch (NumberFormatException ne) {
    			this.txtResult.setText("Inserire un valore numerico ne campo 'soglia', tra " + this.model.getPesoMinimo() + " e "+ this.model.getPesoMassimo());
    		}
    	}
		else
			this.txtResult.setText("Errore implementazione del grafo.");
    }

    @FXML
    void doRicerca(ActionEvent event) {
    	if(this.model.isGrafoLoaded()) {
    		try {
    			double soglia = Double.parseDouble(this.txtSoglia.getText());
    			if( soglia>=this.model.getPesoMinimo() && soglia<=this.model.getPesoMassimo()) {
    				List<Integer> percorso = this.model.ricerca(soglia);
    				if(percorso.size()>0) {
    					this.txtResult.appendText("Percorso trovato di dimensione " + percorso.size() + " di peso " + this.model.getLunghezza(percorso) + "\n");
    					for(Integer i : percorso)
    						this.txtResult.appendText("> Nodo: " + i + "\n");
    				}else
    					this.txtResult.appendText("Non è stato possibile determinare un percorso per il valore di soglia.\n");
    			}else
    				this.txtResult.appendText("\nERRORE: Inserire un valore numerico ne campo 'soglia', tra " + this.model.getPesoMinimo() + " e "+ this.model.getPesoMassimo()+ "\n\n");
    		}catch (NumberFormatException ne) {
    			this.txtResult.appendText("\nERRORE: Inserire un valore numerico ne campo 'soglia', tra " + this.model.getPesoMinimo() + " e "+ this.model.getPesoMassimo());
    		}
    	}
		else
			this.txtResult.setText("Errore implementazione del grafo.");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnContaArchi != null : "fx:id=\"btnContaArchi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnRicerca != null : "fx:id=\"btnRicerca\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtSoglia != null : "fx:id=\"txtSoglia\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model ;
		if(this.model.isGrafoLoaded()) {
			this.txtResult.setText("Grafo Correttamente Creato: " + this.model.getNumberOfVertex() + " vertici; " + this.model.gerNumberOfEdges() + " archi\n");
			this.txtResult.appendText("Di cui:\n");
			this.txtResult.appendText(">Arco di peso minimo: " + this.model.getPesoMinimo() + "\n");
			this.txtResult.appendText(">Arco di peso massimo: " + this.model.getPesoMassimo() + "\n");
		}
		else
			this.txtResult.setText("Errore implementazione del grafo.");
	}
}
