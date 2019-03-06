package it.infocamere.sipert.distrivoci.view;

import java.util.ArrayList;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.db.QueryDB;
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Distribuzione;
import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.util.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
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
    
	private Model model;
	
//	private boolean estrazioneDatiTerminataCorrettamente;
//
//	private boolean erroreSuScritturaFileRisultati;
//
//	private boolean nessunDatoEstratto;
//
//	private Task copyWorker;

	protected GenericResultsDTO risultatiDTO;
    
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
    	
    	tabelledbTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	vociTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	schemiTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    	
        // Listener per la selezione del dettaglio dello sattement di delete e dei relativi statement di insert
    	deleteStatementTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showInsertsDetails(newValue));
    	
//		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, ov, nv) -> handleTabPane(nv));
    }
    
    private void showInsertsDetails(DeleteStatement newValue) {
    	
    	if (newValue != null && newValue.getInsertsListFromSchemaOrigine() != null && newValue.getInsertsListFromSchemaOrigine().size() > 0) {
    		String textArea = "";
    		for (String insertStatement : newValue.getInsertsListFromSchemaOrigine()) {
    			textArea = textArea + insertStatement + "\n";
    		}
    		textAreaPreviewInsert.setText(textArea);
    	}
		
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

    public void setTreeCellFactory(TreeView<String> tree) {
        tree.setCellFactory(param -> new TreeCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                //setDisclosureNode(null);

                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    setText(item);
                }
            }

        });

        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	if ("Tabelle".equalsIgnoreCase(newValue.getValue())) {
            		VboxVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxVoci");
            		VboxNonVisibile("#vboxSchemi");
            		VboxNonVisibile("#vboxPreView");
            	}
            	if ("Voci".equalsIgnoreCase(newValue.getValue())) {
            		VboxNonVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxSchemi");
            		VboxNonVisibile("#vboxPreView");
            		VboxVisibile("#vboxVoci");
            	}
            	if ("Schemi sui quali distribuire".equalsIgnoreCase(newValue.getValue())) {
            		VboxNonVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxVoci");
            		VboxNonVisibile("#vboxPreView");
            		VboxVisibile("#vboxSchemi");
            	}
            	if ("Anteprima".equalsIgnoreCase(newValue.getValue())) {
            		VboxNonVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxVoci");
            		VboxNonVisibile("#vboxSchemi");
            		VboxVisibile("#vboxPreView");
            	}
            	
                System.out.println(newValue.getValue());
            }
        });
    }
    
    private void VboxVisibile (String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(true);
    }
    private void VboxNonVisibile (String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(false);
    }
    
    private void handleTabPane(Tab nv) {
    	if ("          PREVIEW ELABORAZIONE          ".equalsIgnoreCase(nv.getText())) {
    		handlePreviewElaborazione(nv);
    	}
//    	if ("          ELABORAZIONE          ".equalsIgnoreCase(nv.getText())) {
//    		handleElaborazione(nv);
//    	}
    }
    
    private void handlePreviewElaborazione(Tab nv) {
    	
		if (isInputValidForPreviewElaborazione()) {
			try {
				fillPreviewElaborazione();
			} catch (Exception e) {
				showAlert(AlertType.ERROR, "Error", "", e.toString(), null);
				SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
				selectionModel.select(0);
			}
		} else {
			SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
			selectionModel.select(0);
		}
    		
   
        //System.out.println("click on Exit button");
    }
    
    private void fillPreviewElaborazione() {
		    	
    	textAreaPreviewInsert.setText("");
    	
    	boolean fromSchemaOrigine = true;
    	
    	ObservableList<Tabella> listaTabelleSelezionate = tabelledbTable.getSelectionModel().getSelectedItems();
    	ObservableList<Voce> listaVociSelezionate = vociTable.getSelectionModel().getSelectedItems();
    	
    	ArrayList<String> listDelete = new ArrayList<String>();
    	
    	main.clearDeleteStatement();
    	
    	for (int i = 0; i < listaTabelleSelezionate.size(); i++) {
    		String deleteString = "";
    		String whereCondition = "";
    		deleteString = "DELETE FROM "; 
    		Tabella tab = listaTabelleSelezionate.get(i);
    		DeleteStatement deleteStatement = new DeleteStatement();
    		deleteStatement.setCodice(tab.getCodice());
    		whereCondition = Constants.PREFIX_WHERE_CONDITION;
    		deleteString += tab.getCodice();
    		for (int x = 0; x < listaVociSelezionate.size(); x++) {
    			Voce voce = listaVociSelezionate.get(x);
    			if (x > 0) {
    				whereCondition += " , '";
    			}
    			whereCondition += voce.getCodice() + "'";
    			if (x == listaVociSelezionate.size() - 1) {
    				whereCondition += ")";
    				listDelete.add(deleteString + whereCondition);
    				System.out.println(deleteString + whereCondition);
    				
    			}
    		}
    		deleteStatement.setWhereCondition(whereCondition);
    		deleteStatement.setDeleteStatement(deleteString + whereCondition);
    		main.addDeleteStatement(generateInsertStatement(deleteStatement, fromSchemaOrigine));
    	}  
    	deleteStatementTable.setItems(main.getDeleteStatement());
	}

    
	private DeleteStatement generateInsertStatement(DeleteStatement deleteStatement, boolean fromSchemaOrigine) {
		
		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();
		
		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;
		System.out.println("selectStatement = " + selectStatement);
		
		DeleteStatement deleteStatementOutput = deleteStatement;
		
    	QueryDB queryDB = new QueryDB();
    	queryDB.setQuery(selectStatement);
    	
    	if (fromSchemaOrigine) {

			if (main.getSchemaDtoOrigine() != null) {
				risultatiDTO = model.runQueryForGenerateInserts (main.getSchemaDtoOrigine(), queryDB, tableName);
				deleteStatementOutput.setInsertsListFromSchemaOrigine((ArrayList<String>) risultatiDTO.getListString());
				deleteStatementOutput.setCodiceSchemaOrigine(main.getSchemaDtoOrigine().getSchemaUserName());
				
//				copyWorker = createWorker(schemaDTO, queryDB, tableName);
//
//				Thread backgroundThread = new Thread(copyWorker, "queryDataBase-thread");
//				backgroundThread.setDaemon(true);
//				backgroundThread.start();
//				
//				copyWorker.setOnFailed(e -> {
//					Throwable exception = ((Task) e.getSource()).getException();
//					if (exception != null) {
//						copyWorker.cancel(true);
//						showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
//					}
//				});
				
			} else {
				showAlert(AlertType.ERROR, "Error", "", "Non trovati dati di connessione relativi allo schema " + Constants.SETEUR7ES, null);
			}
    	} else {
    		
    		ObservableList<Schema> listaSchemiSuiQualiDistribuire = schemiTable.getSelectionModel().getSelectedItems();
    		ArrayList<Distribuzione> listaDistribuzione = new ArrayList<Distribuzione>(listaSchemiSuiQualiDistribuire.size());
    		
    		for (Schema schema : listaSchemiSuiQualiDistribuire) {
    			
        		Distribuzione distribuzione = new Distribuzione();
        		distribuzione.setCodiceSchema(schema.getCodice());
        		distribuzione.setContatoreInsertGeneratePerBackup(Constants.ZERO);
				distribuzione.setContatoreRigheCancellate(Constants.ZERO);
				distribuzione.setContatoreRigheDistribuite(Constants.ZERO);
    			
    			SchemaDTO schemaDTO = searchSchemaDTO(schema.getCodice());
    			
    			if (schemaDTO != null) {
    				risultatiDTO = model.runQueryForGenerateInserts (schemaDTO, queryDB, tableName);
    				distribuzione.setContatoreRigheCancellate(Constants.ZERO);
    				distribuzione.setContatoreRigheDistribuite(Constants.ZERO);
    				distribuzione.setContatoreInsertGeneratePerBackup(risultatiDTO.getListString().size());
    				distribuzione.setListaInsertGeneratePerBackup(risultatiDTO.getListString());
    			} else {
    				showAlert(AlertType.ERROR, "Error", "", "Non trovati dati di connessione relativi allo schema " + schema.getCodice(), null);
    			}
    			listaDistribuzione.add(distribuzione);
    		}
    		deleteStatementOutput.setListaDistribuzione(listaDistribuzione);
    	}
		
		return deleteStatementOutput;
	}
	
	private SchemaDTO searchSchemaDTO(String codiceSchema) {
		
		for (SchemaDTO schemaDTO : main.getListSchemi()) {
			if (schemaDTO.getSchemaUserName().equalsIgnoreCase(codiceSchema)) {
				return schemaDTO;
			}
		}
		return null;
	}

	public Task createWorker(SchemaDTO schema, QueryDB queryDB, String tableName) {
		
        return new Task() {
            @Override
            protected Object call() throws Exception {
				
				if (this.isCancelled()) {
					System.out.println("Canceling...");
				} 
				
				boolean createListOfLinkedHashMap = false; 
				boolean createListOfInsert = true;
				
				risultatiDTO = model.runQueryForGenerateInserts (schema, queryDB, tableName); 

//				if (risultatiDTO.getListLinkedHashMap().size() > 0) {
//					estrazioneDatiTerminataCorrettamente = true;
//				} else {
//					erroreSuScritturaFileRisultati = true;
//				}
				return true;
			}
            
            @Override protected void succeeded() {
            	System.out.println("sono dentro il metodo succeeded del Task");
                super.succeeded();
                updateMessage("Done!");
                if (risultatiDTO != null) {
                	System.out.println("nr. di righe estratte dalla query = " + risultatiDTO.getListLinkedHashMap().size());
                }
            }
         };

    }

	private boolean isInputValidForPreviewElaborazione() {
        String errorMessage = "";

        int selectedIndexTabelle = tabelledbTable.getSelectionModel().getSelectedIndex();
        int selectedIndexVoci = vociTable.getSelectionModel().getSelectedIndex();
        int selectedIndexSchemi = schemiTable.getSelectionModel().getSelectedIndex();
        if (selectedIndexTabelle < 0) {
        	errorMessage += "Seleziona almeno una Tabella\n";
        }
        if (selectedIndexVoci < 0) {
        	errorMessage += "Seleziona almeno una Voce\n";
        }
        if (selectedIndexSchemi < 0) {
        	errorMessage += "Seleziona almeno uno Schema\n";
        }

		if (errorMessage.length() == 0) {
			return isSchemiSelezionatiOK();  
			//return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi", "Per cortesia, correggi i campi non validi sul tab PARAMETRI", errorMessage, main.getStagePrincipale());
			return false;
		}
    }
	
	private boolean isSchemiSelezionatiOK() {
		
		ObservableList<Schema> listaSchemiSelezionati = schemiTable.getSelectionModel().getSelectedItems();

		for (Schema s : listaSchemiSelezionati) {
			SchemaDTO schemaDTO = searchSchemaDTO(s.getCodice());
			if (schemaDTO != null) {
				if (!model.testConnessioneDB(schemaDTO)) {
					showAlert(AlertType.ERROR, "Error", "", "Non riuscita connessione allo schema " + s.getCodice(), null);
					return false;
				}
			} else {
				showAlert(AlertType.ERROR, "Error", "", "Non trovati dati di connessione relativi allo schema " + s.getCodice(), null);
				return false;
			}	
		}
		return true;
	}
	
    private void handleElaborazione(Tab nv) {
    	
		if (isInputValidForElaborazione()) {
			try {
				elaborazione();
			} catch (Exception e) {
				showAlert(AlertType.ERROR, "Error", "", e.toString(), null);
    			SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
    			selectionModel.select(0);
			}
		} else {
			SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
			selectionModel.select(0);
		}
   
        //System.out.println("click on Exit button");
    }
	
	private boolean isInputValidForElaborazione() {
		// TODO Auto-generated method stub
		return false;
	}
    
    /**
     * chiamata quando l'utente fa click sul bottone Cancella
     */
    @FXML
    private void elaborazione() {
		// TODO  >> per ogni deleteStatement di Main aggiorno la relativa lista delle distribuzioni:
		boolean fromSchemaOrigine = false;
		ObservableList<DeleteStatement> newlistaDelete =  FXCollections.observableArrayList();
		for (DeleteStatement deleteStatement : main.getDeleteStatement()) {
			//	a) per ogni elemento della lista delle distribuzioni genero l'elenco delle insert di backup ed aggiorno il relativo contatore
			//deleteStatement;
			//main.addDeleteStatement(
			newlistaDelete.add(generateInsertStatement(deleteStatement, fromSchemaOrigine));
			System.out.println("SQL Statement " + deleteStatement.getDeleteStatement() + " Elaborato!" );
			
		}
		main.getDeleteStatement().clear();
		main.setDeleteStatement(newlistaDelete);
		
		deleteStatementTable.setItems(main.getDeleteStatement());
		//              	b) a fronte dell'esecuzione della Delete aggiorno il relativo contatore del nr. di righe cancellate
		//					c) a fronte dell'inserimento dei nuovi valori aggiorno il contatore del nr. di righe inserite
		
		
	}

	public void setModel(Model model) {
		this.model = model;
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
