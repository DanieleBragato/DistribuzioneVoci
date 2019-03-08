package it.infocamere.sipert.distrivoci.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.db.QueryDB;
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Distribuzione;
import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.util.Constants;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class OverviewDistriVociController {

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
	private TableView<Schema> schemiPartenzaTable;
	
	@FXML
	private TableView<StoricoDistribuzione> storicoDistribuzioneTable;

	@FXML
	private TableColumn<StoricoDistribuzione, String> dataTabStoricoDistribuzioneColumn;

	@FXML
	private TableColumn<StoricoDistribuzione, String> noteTabStoricoDistribuzioneColumn;
	
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
	private TableColumn<Schema, String> codiceSchemaPartenzaColumn;
	@FXML
	private TableColumn<Schema, String> descrizioneSchemaPartenzaColumn;

	@FXML
	private Label labelAnteprimaInsert;

	@FXML
	private TextField filterField;
	@FXML
	private Button bntSelectAll;
	@FXML
	private Button bntDeselectAll;

    @FXML
    private Label labelInfoEsecuzione;
    
    @FXML
    private ProgressBar bar;
    
    @FXML
    private AnchorPane anchorPaneDX; 
    
    @FXML
    private AnchorPane anchorPaneSX;
    
    @FXML
    private Button bntDistribuzioneVoci; 
	
	// Referimento al main
	private Main main;

	private Model model;

	private Task copyWorkerForGenInserts;
	
	private Task copyWorkerForUpdateDB;
	
	private ObservableList<DeleteStatement> newlistaDelete = FXCollections.observableArrayList();
	
	private ObservableList<StoricoDistribuzione> newlistaStoricoDistribuzione = FXCollections.observableArrayList();

	protected GenericResultsDTO risultatiDTO;

	public OverviewDistriVociController() {

	}

	/**
	 * Initialializza la classe controller, chiamato automaticamente dopo il
	 * caricamento del file fxml
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

		// Initializza la lista degli schemi di partenza con 2 colonne - codice e
		// descrizione
		codiceSchemaPartenzaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaPartenzaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista degli statement di delete (2 colonne - codice tabella e
		// relativo statement)
		codiceTabDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());

		// Initializza la lista dello storico delle distribuzioni (2 colonne - data e note della distribuzione
		dataTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().dataOraDistribuzioneProperty());
		noteTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
		
		tabelledbTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		vociTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		schemiTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		// Listener per la selezione del dettaglio dello schema di partenza
		schemiPartenzaTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> saveInfoSchemaPartenza(newValue));

		// Listener per la selezione del dettaglio dello statement di delete e dei
		// relativi statement di insert
		deleteStatementTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInsertsDetails(newValue));

		// Listener per la selezione del dettaglio dello storico distribuzioni 

		storicoDistribuzioneTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInfoDistribuzioneDetails(newValue));
		
	}

	private void saveInfoSchemaPartenza(Schema newValue) {

		if (newValue != null && newValue.getCodice() != null && newValue.getCodice().length() > 0) {
			SchemaDTO schemaDTO = searchSchemaDTO(newValue.getCodice());
			main.setSchemaDtoOrigine(schemaDTO);
		}
	}

	private void showInsertsDetails(DeleteStatement newValue) {

		if (newValue != null && newValue.getInsertsListFromSchemaOrigine() != null
				&& newValue.getInsertsListFromSchemaOrigine().size() > 0) {
			String textArea = "";
			for (String insertStatement : newValue.getInsertsListFromSchemaOrigine()) {
				textArea = textArea + insertStatement + "\n";
			}
			textAreaPreviewInsert.setText(textArea);
		}

	}
	
	private void showInfoDistribuzioneDetails(StoricoDistribuzione newValue) {
		
	}

	public void setMain(Main main) {
		this.main = main;

		// aggiunta di una observable list alla table
		tabelledbTable.setItems(main.getTabelleDB());

		// aggiunta di una observable list alla table
		vociTable.setItems(main.getVociData());

		// aggiunta di una observable list alla table
		schemiTable.setItems(main.getSchemi());

		// aggiunta di una observable list alla table
		schemiPartenzaTable.setItems(main.getSchemiPartenza());

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
		// Otherwise, sorting the TableView would have no effect.
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
	 * chiamata quando l'utente fa click sul bottone Nuova Tabella, per impostare i
	 * dettagli della nuova tabella
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
	 * chiamata quando l'utente fa click sul bottone Nuova Voce, per impostare i
	 * dettagli della nuova voce
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
		// System.out.println("click on Exit button");

	}

	@FXML
	private void handleDeselectAllSchemi(ActionEvent event) {

		schemiTable.getSelectionModel().clearSelection();
		// System.out.println("click on Exit button");

	}

	public void setTreeCellFactory(TreeView<String> tree) {
		tree.setCellFactory(param -> new TreeCell<String>() {
			@Override
			public void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				// setDisclosureNode(null);

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
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxSchemiPartenza");
					VboxNonVisibile("#vboxPreView");
				}
				if ("Voci".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxSchemi");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxSchemiPartenza");
					VboxNonVisibile("#vboxPreView");
					VboxVisibile("#vboxVoci");
				}
				if ("Schemi sui quali distribuire".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxSchemiPartenza");
					VboxNonVisibile("#vboxPreView");
					VboxVisibile("#vboxSchemi");
				}
				if ("Anteprima e Distribuzione".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxSchemiPartenza");
					VboxNonVisibile("#vboxSchemi");
					VboxVisibile("#vboxPreView");
			    	bar.setVisible(true);
					handleAnteprimaDuistribuzione();
				}
				if ("Schemi di Partenza".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxPreView");
					VboxNonVisibile("#vboxSchemi");
					VboxVisibile("#vboxSchemiPartenza");
				}
				if ("Distribuzioni".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxPreView");
					VboxNonVisibile("#vboxSchemi");
					VboxNonVisibile("#vboxSchemiPartenza");
				}
				if ("Ripristino".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxNonVisibile("#vboxStorico");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxPreView");
					VboxNonVisibile("#vboxSchemi");
					VboxNonVisibile("#vboxSchemiPartenza");
				}
				if ("Distribuzioni".equalsIgnoreCase(newValue.getValue())) {
					VboxNonVisibile("#vboxTabelle");
					VboxVisibile("#vboxStorico");
					VboxNonVisibile("#vboxVoci");
					VboxNonVisibile("#vboxPreView");
					VboxNonVisibile("#vboxSchemi");
					VboxNonVisibile("#vboxSchemiPartenza");
				}

				System.out.println(newValue.getValue());
			}
		});
	}

	private void VboxVisibile(String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(true);
	}

	private void VboxNonVisibile(String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(false);
	}

	private void handleAnteprimaDuistribuzione() {

		if (isInputValidForAnteprimaDuistribuzione()) {
			try {
				previewDistribuzione();
			} catch (Exception e) {
				VboxNonVisibile("#vboxPreView");
				showAlert(AlertType.ERROR, "Error", "", e.toString(), null);
			}
		} else {
			VboxNonVisibile("#vboxPreView");
		}

		// System.out.println("click on Exit button");
	}

	private void fillAnteprimaDuistribuzione() {

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
			
			if (tab.getCodice().toUpperCase().contains(Constants.ESPADE) || tab.getCodice().contains(Constants.ESPAFM)) {
				whereCondition = Constants.PREFIX_WHERE_CONDITION_WITH_CDTPFORM;
			} else {
				whereCondition = Constants.PREFIX_WHERE_CONDITION;	
			}
			
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
			main.addDeleteStatement(manageDeleteStatementGenInserts(deleteStatement));
		}
		deleteStatementTable.setItems(main.getDeleteStatement());
	}

	private DeleteStatement manageDeleteStatementGenInserts(DeleteStatement deleteStatement) {
		
		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();

		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;

		System.out.println("selectStatement = " + selectStatement);

		DeleteStatement deleteStatementOutput = deleteStatement;
		
		QueryDB queryDB = new QueryDB();
		queryDB.setQuery(selectStatement);
		
		if (main.getSchemaDtoOrigine() != null) {
			risultatiDTO = model.runQueryForGenerateInserts(main.getSchemaDtoOrigine(), queryDB, tableName);
			deleteStatementOutput.setInsertsListFromSchemaOrigine((ArrayList<String>) risultatiDTO.getListString());
			deleteStatementOutput.setCodiceSchemaOrigine(main.getSchemaDtoOrigine().getSchemaUserName());
		} else {
			showAlert(AlertType.ERROR, "Error", "",
					"Non trovati dati di connessione relativi allo schema " + Constants.SETEUR7ES, null);
		}
		return deleteStatementOutput;
	}
	
	private DeleteStatement manageDeleteStatement(DeleteStatement deleteStatement) {
		
		System.out.println("classe OverviewDistriVociController metodo manageDeleteStatement");

		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();

		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;

		System.out.println("selectStatement = " + selectStatement);

		DeleteStatement deleteStatementOutput = deleteStatement;

		QueryDB queryDB = new QueryDB();
		queryDB.setQuery(selectStatement);

		QueryDB deleteDB = new QueryDB();
		deleteDB.setQuery(deleteStatement.getDeleteStatement());

		List<QueryDB> listOfInserts = new ArrayList<QueryDB>();

		for (int i = 0; i < deleteStatement.getInsertsListFromSchemaOrigine().size(); i++) {
			QueryDB insertDB = new QueryDB();
			insertDB.setQuery(deleteStatement.getInsertsListFromSchemaOrigine().get(i));
			listOfInserts.add(insertDB);
		}

		ObservableList<Schema> listaSchemiSuiQualiDistribuire = schemiTable.getSelectionModel().getSelectedItems();
		ArrayList<Distribuzione> listaDistribuzione = new ArrayList<Distribuzione>(
				listaSchemiSuiQualiDistribuire.size());

		int contatoreRigheDistribuite = 0;
		
		for (int y = 0; y < listaSchemiSuiQualiDistribuire.size(); y++) {
			
			Schema schema = listaSchemiSuiQualiDistribuire.get(y);

			Distribuzione distribuzione = new Distribuzione();
			distribuzione.setCodiceSchema(schema.getCodice());
			distribuzione.setContatoreInsertGeneratePerBackup(Constants.ZERO);
			distribuzione.setContatoreRigheCancellate(Constants.ZERO);
			distribuzione.setContatoreRigheDistribuite(Constants.ZERO);

			SchemaDTO schemaDTO = searchSchemaDTO(schema.getCodice());

			if (schemaDTO != null) {
				// GENERAZIONE DELLE INSERTS PER IL BACKUP 
				risultatiDTO = model.runQueryForGenerateInserts(schemaDTO, queryDB, tableName);
				distribuzione.setContatoreRigheCancellate(Constants.ZERO);
				distribuzione.setContatoreRigheDistribuite(Constants.ZERO);
				distribuzione.setContatoreInsertGeneratePerBackup(risultatiDTO.getListString().size());
				distribuzione.setListaInsertGeneratePerBackup(risultatiDTO.getListString());
				// ESECUZIONE DELLA DELETE 
				risultatiDTO = model.runUpdate(schemaDTO, deleteDB);
				distribuzione.setContatoreRigheCancellate(risultatiDTO.getRowsUpdated());
				// ESECUZIONE DELLE INSERT
				for (int x = 0; x < listOfInserts.size(); x++) {
					risultatiDTO = model.runUpdate(schemaDTO, listOfInserts.get(x));
					if (risultatiDTO.getRowsUpdated() == 1) contatoreRigheDistribuite++;
				}
				distribuzione.setContatoreRigheDistribuite(contatoreRigheDistribuite);
				
			} else {
				showAlert(AlertType.ERROR, "Error", "",
						"Non trovati dati di connessione relativi allo schema " + schema.getCodice(), null);
			}
			listaDistribuzione.add(distribuzione);
		}
		deleteStatementOutput.setListaDistribuzione(listaDistribuzione);

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


	private void showAlerAnteprimaOK() {
		
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Anteprima di Distribuzione");
		alert.setContentText("Anteprima predisposta correttamente");

		alert.showAndWait();
		
		bntDistribuzioneVoci.setDisable(false);
		
	}
	
    private void showAlertEstrazioneOK() {
    	
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("Information Dialog");
		alert.setHeaderText("Distribuzione conclusa regolarmente");
		alert.setContentText("Tutto OK!");

		alert.showAndWait();
		
		bntDistribuzioneVoci.setDisable(true);
	}
	
	private boolean isInputValidForAnteprimaDuistribuzione() {
		String errorMessage = "";

		int selectedIndexTabelle = tabelledbTable.getSelectionModel().getSelectedIndex();
		int selectedIndexVoci = vociTable.getSelectionModel().getSelectedIndex();
		int selectedIndexSchemi = schemiTable.getSelectionModel().getSelectedIndex();
		int selectedIndexSchemiPartenza = schemiPartenzaTable.getSelectionModel().getSelectedIndex();

		if (selectedIndexSchemiPartenza < 0) {
			errorMessage += "Seleziona uno Schema di Partenza\n";
		}
		if (selectedIndexTabelle < 0) {
			errorMessage += "Seleziona almeno una Tabella\n";
		}
		if (selectedIndexVoci < 0) {
			errorMessage += "Seleziona almeno una Voce\n";
		}
		if (selectedIndexSchemi < 0) {
			errorMessage += "Seleziona almeno uno Schema sul quale distribuire\n";
		}

		if (errorMessage.length() == 0) {
			return isSchemiSelezionatiOK();
			// return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi",
					"Per cortesia, imposta i Parametri necessari per l'Anteprima di Distribuzione", errorMessage,
					main.getStagePrincipale());
			return false;
		}
	}

	private boolean isSchemiSelezionatiOK() {

		ObservableList<Schema> listaSchemiSelezionati = schemiTable.getSelectionModel().getSelectedItems();

		for (Schema s : listaSchemiSelezionati) {
			SchemaDTO schemaDTO = searchSchemaDTO(s.getCodice());
			if (schemaDTO != null) {
				if (!model.testConnessioneDB(schemaDTO)) {
					showAlert(AlertType.ERROR, "Error", "", "Non riuscita connessione allo schema " + s.getCodice(),
							null);
					return false;
				}
			} else {
				showAlert(AlertType.ERROR, "Error", "",
						"Non trovati dati di connessione relativi allo schema " + s.getCodice(), null);
				return false;
			}
		}
		return true;
	}

	private void previewDistribuzione() {
		
		System.out.println("OverviewDistriVociController metodo previewDistribuzione");
		
		textAreaPreviewInsert.setText("");

		labelAnteprimaInsert.setText(Constants.INSERT_LABEL_CONTENTS + main.getSchemaDtoOrigine().getSchemaUserName());
		
		disabledView(true);
		
		bar.setProgress(0);
		
		copyWorkerForGenInserts = createWorkerForGenInserts();
			
//        labelInfoEsecuzione.textProperty().unbind();
//        labelInfoEsecuzione.textProperty().bind(copyWorkerForGenInserts.messageProperty());
		
        bar.progressProperty().unbind();
        bar.progressProperty().bind(copyWorkerForGenInserts.progressProperty());
				
        copyWorkerForGenInserts.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("newValue " + newValue);
            }
        });
        
		Thread backgroundThread = new Thread(copyWorkerForGenInserts, "queryDB-anteprima-thread");
		backgroundThread.setDaemon(true);
		backgroundThread.start();
		
		copyWorkerForGenInserts.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				System.out.println("OverviewDistriVociController task copyWorkerForGenInserts - setOnFailed - no eccezione" );
				copyWorkerForGenInserts.cancel(true);
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(true);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
			} else {
				System.out.println("OverviewDistriVociController task copyWorkerForGenInserts - setOnFailed - eccezione = " + exception.toString());
			}
		});
		
	}
	
	/**
	 * chiamata quando l'utente fa click sul bottone Distribuzione Voci
	 */
	@FXML
	private void distribuzione() {
		
		System.out.println("OverviewDistriVociController metodo distribuzione");
		
		disabledView(true);
		bar.setVisible(true);
		
		bar.setProgress(0);
		
		copyWorkerForUpdateDB = createWorkerForExecuteUpdateDB();
		
        labelInfoEsecuzione.textProperty().unbind();
        labelInfoEsecuzione.textProperty().bind(copyWorkerForUpdateDB.messageProperty());

        bar.progressProperty().unbind();
        bar.progressProperty().bind(copyWorkerForUpdateDB.progressProperty());

        copyWorkerForUpdateDB.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("newValue " + newValue);
            }
        });
        
		Thread backgroundThreadUpdateDB = new Thread(copyWorkerForUpdateDB, "updateDataBase-thread");
		backgroundThreadUpdateDB.setDaemon(true);
		backgroundThreadUpdateDB.start();
		
		copyWorkerForUpdateDB.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteUpdateDB - setOnFailed - no eccezione");
				copyWorkerForUpdateDB.cancel(true);
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(false);
				bar.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
			} else {
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteUpdateDB - setOnFailed - eccezione = " + exception.toString());
			}
		});
		
	}

    private void disabledView(boolean b) {
    	
    	anchorPaneDX.setDisable(b);
    	anchorPaneSX.setDisable(b);
    	
    	bntDistribuzioneVoci.setDisable(b);
    	
    }
	
	public Task createWorkerForGenInserts() {

		System.out.println("OverviewDistriVociController metodo createWorkerForGenInserts");
		
		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					System.out.println("Canceling...");
				}
				
				fillAnteprimaDuistribuzione();
				
				return true;
			}

			@Override
			protected void succeeded() {
				System.out.println("OverviewDistriVociController metodo createWorkerForGenInserts - succeeded");
				super.succeeded();
				updateMessage("Done!");
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(false);
				bar.setVisible(false);
				showAlerAnteprimaOK();
			}
		};
	}

	
	public Task createWorkerForExecuteUpdateDB() {

		System.out.println("OverviewDistriVociController metodo createWorkerForExecuteUpdateDB");
		
		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					System.out.println("Canceling...");
				}
				
				for (DeleteStatement deleteStatement : main.getDeleteStatement()) {
					newlistaDelete.add(manageDeleteStatement(deleteStatement));
					System.out.println("SQL Statement " + deleteStatement.getDeleteStatement() + " Elaborato!");

				}
				
				main.getDeleteStatement().clear();
				main.setDeleteStatement(newlistaDelete);
				deleteStatementTable.setItems(main.getDeleteStatement());
				
				return true;
			}

			@Override
			protected void succeeded() {
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteUpdateDB - succeeded");
				super.succeeded();
				updateMessage("Done!");
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(false);
				bar.setVisible(false);
				showAlertEstrazioneOK();
				aggiornaStoricoDistribuzione();
			}
		};
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	private void aggiornaStoricoDistribuzione() {
		
		StoricoDistribuzione storicoDistribuzione = new StoricoDistribuzione();
		ArrayList<DeleteStatement> listaDeleteStatement = new ArrayList<DeleteStatement>();
		for (DeleteStatement deletestatement : newlistaDelete) {
			listaDeleteStatement.add(deletestatement);
			
		}
		storicoDistribuzione.setListaDeleteStatement(listaDeleteStatement);
		storicoDistribuzione.setNote("inserire gestione");
		storicoDistribuzione.setDataOraDistribuzione(new Date().toString());
		
		main.addStoricoDistribuzione(storicoDistribuzione);
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
