package it.infocamere.sipert.distrivoci.view;

import java.util.ArrayList;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class OverviewDistriVociController {
	
	@FXML
	private TabPane tabPane;
	
    @FXML
    private TableView<Tabella> tabelledbTable;
    
    @FXML
    private TextArea textAreaPreviewDelete;
    
    @FXML
    private TextArea textAreaPreviewInsert;
    
    @FXML
    private TableView<Voce> vociTable;
    
    @FXML
    private TableView<Schema> schemiTable;
    
    @FXML
    private TableView<DeleteStatement> deleteStatementTable;
    
    @FXML
    private TableColumn<DeleteStatement, String> codiceTabDeleteColumn;
    
    @FXML
    private TableColumn<DeleteStatement, String> statementDeleteColumn;
    
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
    	
        // Initializza la lista degli statement di delete (2 colonne - codice tabella e relativo statement) 
    	codiceTabDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
    	statementDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());
    	
    	//
    	tabelledbTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	vociTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	schemiTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
        // Listener per la selezione del dettaglio dello sattement di delete e dei relativi statement di insert
    	deleteStatementTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showInsertsDetails(newValue));
    	
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> handlePreviewElaborazione(nv));
    }
    
    private Object showInsertsDetails(DeleteStatement newValue) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setMain(Main main) {
        this.main = main;

        // aggiunta di una observable list alla table
        tabelledbTable.setItems(main.getTabelleDB());
        
        // aggiunta di una observable list alla table
        vociTable.setItems(main.getVociData());
        
        // aggiunta di una observable list alla table
        schemiTable.setItems(main.getSchemi());

        // aggiunta di una observable list alla table delle sql delete statement
        deleteStatementTable.setItems(main.getDeleteStatement());
        
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
    
    @FXML
    private void handlePreviewElaborazione(Tab nv) {
    	
    	if ("Preview Elaborazione".equalsIgnoreCase(nv.getText())) {
    		if (isInputValidForPreviewElaborazione()) {
    			fillPreviewElaborazione();
    		} else {
    			SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    			selectionModel.select(0);
    		}
    		
    	}
   
        //System.out.println("click on Exit button");
    }
    
    private void fillPreviewElaborazione() {
		// TODO Auto-generated method stub
		    	
    	ObservableList<Tabella> listaTabelleSelezionate = tabelledbTable.getSelectionModel().getSelectedItems();
    	ObservableList<Voce> listaVociSelezionate = vociTable.getSelectionModel().getSelectedItems();
    	ObservableList<Schema> listaSchemiSelezionati = schemiTable.getSelectionModel().getSelectedItems();
    	
    	ArrayList<String> listDelete = new ArrayList<String>();
    	String textArea = "";
    	
    	for (int i = 0; i < listaTabelleSelezionate.size(); i++) {
    		String deleteString = "";
    		deleteString = "DELETE FROM "; 
    		Tabella tab = listaTabelleSelezionate.get(i);
    		DeleteStatement ds = new DeleteStatement();
    		ds.setCodice(tab.getCodice());
    		deleteString += tab.getCodice() + " WHERE CDVOCEXX  IN('";
    		for (int x = 0; x < listaVociSelezionate.size(); x++) {
    			Voce voce = listaVociSelezionate.get(x);
    			if (x > 0) {
    				deleteString += " , '";
    			}
    			deleteString += voce.getCodice() + "'";
    			if (x == listaVociSelezionate.size() - 1) {
    				deleteString += ");";
    				listDelete.add(deleteString);
    				System.out.println(deleteString);
    				
    			}
    		}
    		ds.setDeleteStatement(deleteString);
    		main.addDeleteStatement(ds);
    	}  
    	deleteStatementTable.setItems(main.getDeleteStatement());
	}

	private boolean isInputValidForPreviewElaborazione() {
        String errorMessage = "";

        int selectedIndexTabelle = tabelledbTable.getSelectionModel().getSelectedIndex();
        int selectedIndexVoci = vociTable.getSelectionModel().getSelectedIndex();
        int selectedIndexSchemi = schemiTable.getSelectionModel().getSelectedIndex();
        if (selectedIndexTabelle < 0) {
        	errorMessage += "Seleziona almeno una Tabella dalla lista!\n";
        }
        if (selectedIndexVoci < 0) {
        	errorMessage += "Seleziona almeno una Voce dalla lista!\n";
        }
        if (selectedIndexSchemi < 0) {
        	errorMessage += "Seleziona almeno uno Schema dalla lista!\n";
        }

		if (errorMessage.length() == 0) {
			return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi", "Per cortesia, correggi i campi non validi sul tab Parametri", errorMessage, main.getStagePrincipale());
			return false;
		}
    }
    
	public void showAlert(AlertType type, String title, String headerText, String text, Stage stage) {
		
		Alert alert = new Alert(type);

		alert.setTitle(title);
		alert.setHeaderText(headerText);
		alert.setContentText(text);

		if (stage != null) alert.initOwner(stage);
		
		alert.showAndWait();
	}
    
}
