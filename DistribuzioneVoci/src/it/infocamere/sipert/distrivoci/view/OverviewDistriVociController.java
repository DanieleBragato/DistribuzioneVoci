package it.infocamere.sipert.distrivoci.view;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class OverviewDistriVociController {
	
    @FXML
    private TableView<Tabella> tabelledbTable;
    
    @FXML
    private TableView<Voce> vociTable;
    
    @FXML
    private TableView<Schema> schemiTable;
    
    @FXML
    private TableColumn<Tabella, String> codiceTabColumn;
    @FXML
    private TableColumn<Tabella, String> descrizioneTabColumn;
    
    @FXML
    private TableColumn<Voce, String> codiceVoceColumn;
    @FXML
    private TableColumn<Voce, String> descrizioneVoceColumn;
    
    @FXML
    private TableColumn<Schema, String> codiceSchemaColumn;
    @FXML
    private TableColumn<Schema, String> descrizioneSchemaColumn;
	@FXML
	private TextField filterField;
    @FXML
    private Button bntSelectAll;
    @FXML
    private Button bntDeselectAll;
    
    // Referimento al main 
    private Main main;
    
    public OverviewDistriVociController() {
    	
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
    	
        // Initializza la lista degli schemi con 2 colonne - codice e descrizione 
    	codiceSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
    	descrizioneSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
    	
    	//
    	tabelledbTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	vociTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	schemiTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
    	

		
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
        
        // aggiunta di una observable list alla table
        schemiTable.setItems(main.getSchemi());
        
    }
    
    public void setFilter() {
    
		// 1. Wrap the ObservableList in a FilteredList (initially display all data).
		FilteredList<Schema> filteredData = new FilteredList<>(main.getSchemi(), p -> true);
    	
		// 2. Set the filter Predicate whenever the filter changes.
		filterField.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(schema -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}
				
				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				
				if (schema.getCodice().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches first name.
				} else if (schema.getDescrizione().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});
		
		// 3. Wrap the FilteredList in a SortedList. 
		SortedList<Schema> sortedData = new SortedList<>(filteredData);
		
		// 4. Bind the SortedList comparator to the TableView comparator.
		// 	  Otherwise, sorting the TableView would have no effect.
		sortedData.comparatorProperty().bind(schemiTable.comparatorProperty());
		
		// 5. Add sorted (and filtered) data to the table.
		schemiTable.setItems(sortedData);
    	
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
            alert.setHeaderText("Nessuna Tabella selezionata");
            alert.setContentText("Per cortesia, seleziona una Tabella dalla lista ");
            
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
    
    @FXML
    private void handleSelectAllSchemi(ActionEvent event) {
    	
    	schemiTable.getSelectionModel().selectAll();
        //System.out.println("click on Exit button");
       
    }
       
    @FXML
    private void handleDeselectAllSchemi(ActionEvent event) {
    	
    	schemiTable.getSelectionModel().clearSelection();
        //System.out.println("click on Exit button");
       
    }
}
