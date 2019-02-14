package it.infocamere.sipert.distrivoci.view;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;

public class OverviewController {
	
    @FXML
    private TableView<Tabella> tabelledbTable;
    
    @FXML
    private TableView<Voce> vociTable;
    
    @FXML
    private TableColumn<Tabella, String> codiceTabColumn;
    @FXML
    private TableColumn<Tabella, String> descrizioneTabColumn;
    
    @FXML
    private TableColumn<Voce, String> codiceVoceColumn;
    @FXML
    private TableColumn<Voce, String> descrizioneVoceColumn;
    
    // Referimento al main 
    private Main main;
    
    public OverviewController() {
    	
    }
    
    /**
     * Initialializza la classe controller, chiamato automaticamente dopo il caricamento del file fxml
     */
    @FXML
    private void initialize() {
        // Initializza la lista delle tabelle con 2 colonne - codice e descrizione 
    	codiceTabColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
    	descrizioneTabColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
        
        // Initializza la lista delle voci con 2 colonne - codice e descrizione 
    	codiceVoceColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
    	descrizioneVoceColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
    	
    	
        // Listener per la selezione delle modifiche e della visualizzazione del dettaglio della query quando viene cambiata
//    	tabelledbTable.getSelectionModel().selectedItemProperty().addListener(
//                (observable, oldValue, newValue) -> showQueryDetails(newValue));
    }
    
    public void setMain(Main main) {
        this.main = main;

        // aggiunta di una observable list alla table
        tabelledbTable.setItems(main.getTabelleDB());
        
        // aggiunta di una observable list alla table
        vociTable.setItems(main.getVociData());
        
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Cancella
     */
    @FXML
    private void handleDeleteTabDB() {
        int selectedIndex = tabelledbTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	tabelledbTable.getItems().remove(selectedIndex);
        } else {
            // Nessuna selezione
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getStagePrincipale());
            alert.setTitle("Nessuna Selezione");
            alert.setHeaderText("Nessuna Tabella Selezionata");
            alert.setContentText("Per cortesia, seleziona una tabella dalla lista");
            
            alert.showAndWait();
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Nuova Tabella, per impostare i dettagli della nuova tabella
     */
    @FXML
    private void handleNewTabDB() {
    	Tabella tempTabella = new Tabella();
        boolean okClicked = main.showTabellaEditDialog(tempTabella);
        if (okClicked) {
            main.getTabelleDB().add(tempTabella);
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Edit tabella
     */
    @FXML
    private void handleEditTabDB() {
        Tabella selectedTabDB = tabelledbTable.getSelectionModel().getSelectedItem();
        if (selectedTabDB != null) {
        	main.showTabellaEditDialog(selectedTabDB);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getStagePrincipale());
            alert.setTitle("No Selection");
            alert.setHeaderText("nessuna query selezionata");
            alert.setContentText("Per cortesia, seleziona una query dalla lista ");
            
            alert.showAndWait();
        }
    }
    
    
    /**
     * chiamata quando l'utente fa click sul bottone Cancella Voce
     */
    @FXML
    private void handleDeleteVoce() {
        int selectedIndex = vociTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
        	vociTable.getItems().remove(selectedIndex);
        } else {
            // Nessuna selezione
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getStagePrincipale());
            alert.setTitle("Nessuna Selezione");
            alert.setHeaderText("Nessuna Voce Selezionata");
            alert.setContentText("Per cortesia, seleziona una Voce dalla lista");
            
            alert.showAndWait();
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Nuova Voce, per impostare i dettagli della nuova voce
     */
    @FXML
    private void handleNewVoce() {
    	Voce tempVoce = new Voce();
        boolean okClicked = main.showVoceEditDialog(tempVoce);
        if (okClicked) {
            main.getVociData().add(tempVoce);
        }
    }
    
    /**
     * chiamata quando l'utente fa click sul bottone Edit voce
     */
    @FXML
    private void handleEditVoce() {
        Voce selectedVoce = vociTable.getSelectionModel().getSelectedItem();
        if (selectedVoce != null) {
        	main.showVoceEditDialog(selectedVoce);
        } else {
            // Nothing selected.
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(main.getStagePrincipale());
            alert.setTitle("No Selection");
            alert.setHeaderText("nessuna voce selezionata");
            alert.setContentText("Per cortesia, seleziona una voce dalla lista ");
            
            alert.showAndWait();
        }
    }
       
}
