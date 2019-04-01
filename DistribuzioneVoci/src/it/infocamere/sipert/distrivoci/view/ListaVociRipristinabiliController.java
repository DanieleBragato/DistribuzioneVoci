package it.infocamere.sipert.distrivoci.view;

import java.util.Optional;

import org.apache.log4j.Logger;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ListaVociRipristinabiliController {

    @FXML // fx:id="vboxVoci"
    private VBox vboxVoci; // Value injected by FXMLLoader

    @FXML // fx:id="vociTable"
    private TableView<Voce> vociTable; // Value injected by FXMLLoader

    @FXML // fx:id="codiceVoceColumn"
    private TableColumn<Voce, String> codiceVoceColumn; // Value injected by FXMLLoader

    @FXML // fx:id="descrizioneVoceColumn"
    private TableColumn<Voce, String> descrizioneVoceColumn; // Value injected by FXMLLoader
    
    @FXML // fx:id="ripristinoVoceColumn"
    private TableColumn<Voce, String> ripristinoVoceColumn; // Value injected by FXMLLoader
    
    @FXML
    Label labelTitolo;
    
    /**
     * i dati nel formato di observable list di Voce.
     */
    private ObservableList<Voce> voci = FXCollections.observableArrayList();

    private boolean exitClicked = false;
    private Stage dialogStage;
	private Main main;
	
	static Logger logger = Logger.getLogger(ListaVociRipristinabiliController.class);
	
	private StoricoDistribuzione storicoDistribuzione;

	private boolean ripristinoVoce;
	
    public void setMain(Main main) {
        this.main = main;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    private boolean handleExit(ActionEvent event) {

    	if (this.ripristinoVoce) {
    		return checkSelezioneVoce();
    	} else {
	        exitClicked = true;
	        dialogStage.close();
	        return exitClicked;
    	}
    	
    }
    
    private boolean checkSelezioneVoce() {

		Voce selectedVoce = vociTable.getSelectionModel().getSelectedItem();
		if (selectedVoce != null) {
			
			if (selectedVoce.getDataOraRipristino() != null) {
				String contenuto = "Voce " + selectedVoce.getCodice() + " già ripristinata";
				showAlert(AlertType.WARNING, "Voce non valida", contenuto,
						"Per cortesia, seleziona una Voce non ripristinata",
						main.getStagePrincipale());
			} else {
				main.setVoceDaRipristinare(selectedVoce);
		        exitClicked = true;
		        dialogStage.close();
			}
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Conferma");
			alert.setHeaderText("Attenzione non hai selezionato alcuna Voce");
			alert.setContentText("Chiusura senza selezione?");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
				main.setVoceDaRipristinare(null);
		        exitClicked = true;
		        dialogStage.close();
			} 
		}
        return exitClicked;
	}

	public boolean isexitClicked() {
        return exitClicked;
    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert vboxVoci != null : "fx:id=\"vboxVoci\" was not injected: check your FXML file 'ListaVociRipristinabili.fxml'.";
        assert vociTable != null : "fx:id=\"vociTable\" was not injected: check your FXML file 'ListaVociRipristinabili.fxml'.";
        assert codiceVoceColumn != null : "fx:id=\"codiceVoceColumn\" was not injected: check your FXML file 'ListaVociRipristinabili.fxml'.";
        assert descrizioneVoceColumn != null : "fx:id=\"descrizioneVoceColumn\" was not injected: check your FXML file 'ListaVociRipristinabili.fxml'.";
        
        assert ripristinoVoceColumn != null : "fx:id=\"ripristinoVoceColumn\" was not injected: check your FXML file 'ListaVociRipristinabili.fxml'.";
        
		// Initializza la lista delle voci con 2 colonne - codice e descrizione
        codiceVoceColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneVoceColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
		ripristinoVoceColumn.setCellValueFactory(cellData -> cellData.getValue().dataOraRipristinoProperty());

    }
    
    public void setStoricoDistribuzione(StoricoDistribuzione storicoDistribuzione) {    	
        
        if (storicoDistribuzione != null) {
        	this.storicoDistribuzione = storicoDistribuzione;
        	
        	labelTitolo.setText("Distribuzione del " + this.storicoDistribuzione.getDataOraDistribuzione());
        	
        	//ArrayList<Voce> voci = new ArrayList<Voce>();
        	for (Voce voce : this.storicoDistribuzione.getElencoVoci()) {
        		voci.add(voce);
        	}
        	vociTable.setItems(voci);
//        	String contenuto = "         DISTRIBUZIONE del " + this.storicoDistribuzione.getDataOraDistribuzione();
//			if (this.storicoDistribuzione.getDataOraRipristino() != null) {
//				contenuto += " RIPRISTINATA il " + this.storicoDistribuzione.getDataOraRipristino(); 
//			}
//			labelTitle.setText(contenuto);
//			
//			String contenuto2 = "         SCHEMA di PARTENZA " + this.storicoDistribuzione.getSchemaPartenza() + " - NOTE: " + this.storicoDistribuzione.getNote();
//			labelTitle2.setText(contenuto2);
//			
//			// aggiunta di una observable list alla table
//			if (this.storicoDistribuzione.getElencoSchemi() != null && this.storicoDistribuzione.getElencoSchemi().size() > 0) {
//				schemi.addAll(this.storicoDistribuzione.getElencoSchemi());
//				schemiTable.setItems(schemi);				
//			}
        }
    }
  
    public void setScope(String scope) {    	
        
        if (scope != null && Constants.VOCI_PER_RIPRISTINO.equalsIgnoreCase(scope)) {
        	this.ripristinoVoce = true;
        } else {
        	this.ripristinoVoce = false;
        }
    
       }
        	
	public void showAlert(AlertType type, String title, String headerText, String text, Stage stage) {

		Alert alert = new Alert(type);

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);

		if (stage != null)
			alert.initOwner(stage);

		alert.showAndWait();
	}
    
}
