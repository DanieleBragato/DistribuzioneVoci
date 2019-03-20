package it.infocamere.sipert.distrivoci.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

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
	private TableColumn<StoricoDistribuzione, String> ripristinoTabStoricoDistribuzioneColumn;
	
	@FXML
	private TableColumn<StoricoDistribuzione, String> noteTabStoricoDistribuzioneColumn;
	
	@FXML
	private TableColumn<StoricoDistribuzione, String> schemaPartenzaTabStoricoDistribuzioneColumn;
	
	@FXML
	private TableColumn dummyTabStoricoDistribuzioneColumn;
	
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
    
    @FXML
    private VBox vboxRipristino;

    @FXML
    private TableView<Schema> schemiRipristinoTable;

    @FXML
    private TableColumn<Schema, String> codiceSchemaRipristinoColumn;

    @FXML
    private TableColumn<Schema, String> descrizioneSchemaRipristinoColumn;

    @FXML
    private TableView<DeleteStatement> deleteStatementRipristinoTable;

    @FXML
    private TableColumn<DeleteStatement, String> codiceTabRipristinoDeleteColumn;

    @FXML
    private TableColumn<DeleteStatement, String> statementRipristinoDeleteColumn;

    @FXML
    private Label labelRipristinoInsert;

    @FXML
    private TextArea textAreaRipristinoInsert;

    @FXML
    private ProgressBar barRipristino;

    @FXML
    private Button bntRipristinoDistribuzioneVoci;
    
    @FXML
    private Label labelRipristino;

	
	// Referimento al main
	private Main main;

	private Model model;

	private Task copyWorkerForGenInserts;
	
	private Task copyWorkerForExecuteDistribution;
	
	private Task copyWorkerForExecuteRipristino;
	
	private String nota;
	
	private ObservableList<DeleteStatement> newlistaDelete = FXCollections.observableArrayList();
	
	private ObservableList<DeleteStatement> newlistaDeleteForRipristino = FXCollections.observableArrayList();
	
	private ObservableList<StoricoDistribuzione> newlistaStoricoDistribuzione = FXCollections.observableArrayList();

	private ArrayList<Schema> listaSchemiPerStoricoDistribuzione = new ArrayList<Schema>();
	
	private String codiceSchemaRipristinoSelezionato;
	
	private StoricoDistribuzione distribuzioneRipristinabile;

	private int indiceDistribuzioneRipristinabile;
	
	protected GenericResultsDTO risultatiDTO;

	public OverviewDistriVociController() {

	}

	/**
	 * Initialializza la classe controller, chiamato automaticamente dopo il
	 * caricamento del file fxml
	 */
	@FXML
	private void initialize() {
		
		 assert labelRipristino != null : "fx:id=\"labelRipristino\" was not injected: check your FXML file 'OverviewDistriVoci.fxml'.";
		 assert labelRipristinoInsert != null : "fx:id=\"labelRipristinoInsert\" was not injected: check your FXML file 'OverviewDistriVoci.fxml'.";
		
		// Initializza la lista delle tabelle con 2 colonne - codice e descrizione
		codiceTabColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneTabColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista delle voci con 2 colonne - codice e descrizione
		codiceVoceColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneVoceColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista degli schemi con 2 colonne - codice e descrizione
		codiceSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista degli schemi per il ripristino con 2 colonne - codice e descrizione
		codiceSchemaRipristinoColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaRipristinoColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());
		
		// Initializza la lista degli schemi di partenza con 2 colonne - codice e
		// descrizione
		codiceSchemaPartenzaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaPartenzaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista degli statement di delete (2 colonne - codice tabella e
		// relativo statement)
		codiceTabDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());

		
		// Initializza la lista degli statement di delete per il RIPRISTINO (2 colonne - codice tabella e
		// relativo statement)
		codiceTabRipristinoDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementRipristinoDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());
		
		// Initializza la lista dello storico delle distribuzioni (2 colonne - data e note della distribuzione
		dataTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().dataOraDistribuzioneProperty());
		dataTabStoricoDistribuzioneColumn.setSortType(TableColumn.SortType.DESCENDING);
		
		ripristinoTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().dataOraRipristinoProperty());
		
		noteTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
		schemaPartenzaTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().schemaPartenzaProperty());
	
		dummyTabStoricoDistribuzioneColumn.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
		
		Callback<TableColumn<StoricoDistribuzione, String>, TableCell<StoricoDistribuzione, String>> cellFactory = //
				new Callback<TableColumn<StoricoDistribuzione, String>, TableCell<StoricoDistribuzione, String>>() {
					@Override
					public TableCell call(final TableColumn<StoricoDistribuzione, String> param) {
						
						final TableCell<StoricoDistribuzione, String> cell = new TableCell<StoricoDistribuzione, String>() {

							final Button btn = new Button("Dettaglio");

							@Override
							public void updateItem(String item, boolean empty) {
								super.updateItem(item, empty);
								if (empty) {
									setGraphic(null);
									setText(null);
								} else {
									btn.setOnAction(event -> {
										StoricoDistribuzione storicoDistribuzione = getTableView().getItems()
												.get(getIndex());
										System.out.println(storicoDistribuzione.getDataOraDistribuzione() + "   "
												+ storicoDistribuzione.getNote());
										handleApriDettaglioDistribuzione(storicoDistribuzione);
									});
									setGraphic(btn);
									setText(null);
								}
							}
						};
						return cell;
					}
				};
		dummyTabStoricoDistribuzioneColumn.setCellFactory(cellFactory);
		
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

		storicoDistribuzioneTable.getSortOrder().add(dataTabStoricoDistribuzioneColumn);
		
		storicoDistribuzioneTable.sort();
			
		// Listener per la selezione del dettaglio degli statement di delete Ripristino 
		schemiRipristinoTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDeletesRipristinoDetails(newValue));
		
		// Listener per la selezione del dettaglio dello statement di delete e dei
		// relativi statement di insert per RIPRISTINO
		deleteStatementRipristinoTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInsertsDetailsForRipristino(newValue));
		
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
			String contenuto = Constants.INSERT_LABEL_CONTENTS + main.getSchemaDtoOrigine().getSchemaUserName();
			if (newValue.getInsertsListFromSchemaOrigine() != null
					&& newValue.getInsertsListFromSchemaOrigine().size() > 0) {
				contenuto = contenuto + " - " + newValue.getInsertsListFromSchemaOrigine().size() + " statement";
				for (String insertStatement : newValue.getInsertsListFromSchemaOrigine()) {
					textArea = textArea + insertStatement + "\n";
				}				
			}
			labelAnteprimaInsert.setText(contenuto);
			textAreaPreviewInsert.setText(textArea);
		}

	}

	private void showDeletesRipristinoDetails(Schema newValue) {
		
		if (newValue != null && newValue.getCodice() != null) {
			codiceSchemaRipristinoSelezionato = newValue.getCodice();
			newlistaDeleteForRipristino.clear();
			textAreaRipristinoInsert.setText("");
			for (int i = 0; i < distribuzioneRipristinabile.getListaDeleteStatement().size(); i++) {
				DeleteStatement deleteStatement = distribuzioneRipristinabile.getListaDeleteStatement().get(i);
				for (int y = 0; y < deleteStatement.getListaDistribuzione().size(); y++) {
					Distribuzione distr = deleteStatement.getListaDistribuzione().get(y);
					if (distr.getCodiceSchema().equalsIgnoreCase(codiceSchemaRipristinoSelezionato)) {
						newlistaDeleteForRipristino.add(deleteStatement);
					}
				}
			}
			deleteStatementRipristinoTable.setItems(newlistaDeleteForRipristino);
		}
	}
	
	private void showInsertsDetailsForRipristino(DeleteStatement newValue) {
		
		// NB >> un solo schema selezionabile 
		ObservableList<Schema> schemaSelezionato = schemiRipristinoTable.getSelectionModel().getSelectedItems();
		
		String textArea = "";
		
		if (newValue != null) {
			// cerco la distribuzione corrispondente allo schema selezionato
			for (Distribuzione distribuzione : newValue.getListaDistribuzione()) {
				if (distribuzione.getCodiceSchema().equalsIgnoreCase(schemaSelezionato.get(Constants.ZERO).getCodice())) {
					for (String insertStatement : distribuzione.getListaInsertGeneratePerBackup()) {
						textArea = textArea + insertStatement + "\n";
					}					
				}
			}
			textAreaRipristinoInsert.setText(textArea);
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

		// aggiunta di una observable list alla table
		schemiPartenzaTable.setItems(main.getSchemiPartenza());

		// aggiunta di una observable list alla table delle sql delete statement
		deleteStatementTable.setItems(main.getDeleteStatement());

		// aggiunta di una observable list alla table dello storico delle distribuzioni
		storicoDistribuzioneTable.setItems(main.getStoricoDistribuzione());
		
		impostaDistribuzioneRipristinabile();		
	}

	private void impostaDistribuzioneRipristinabile() {
		
		distribuzioneRipristinabile = null;
		indiceDistribuzioneRipristinabile = 0;
		
		if (this.main.getStoricoDistribuzione().size() > 0) {
			for (int i = 0 ; i < this.main.getStoricoDistribuzione().size() ; i++) {
				if (this.main.getStoricoDistribuzione().get(i).getDataOraRipristino() == null) {
					distribuzioneRipristinabile = this.main.getStoricoDistribuzione().get(i);
					indiceDistribuzioneRipristinabile = i;
					break;
				}
			}
		}
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
			showAlert(AlertType.WARNING, "Nessuna Selezione", "Nessuna Tabella Selezionata",
					"Per cortesia, seleziona una tabella dalla lista ",
					main.getStagePrincipale());
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
			showAlert(AlertType.WARNING, "Nessuna Selezione", "Nessuna Tabella Selezionata",
					"Per cortesia, seleziona una tabella dalla lista ",
					main.getStagePrincipale());
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
			showAlert(AlertType.WARNING, "Nessuna Selezione", "Nessuna Voce Selezionata",
					"Per cortesia, seleziona una Voce dalla lista ",
					main.getStagePrincipale());
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
			showAlert(AlertType.WARNING, "Nessuna Selezione", "Nessuna Voce Selezionata",
					"Per cortesia, seleziona una Voce dalla lista ",
					main.getStagePrincipale());
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

	/**
	 * chiamata quando l'utente fa click sul bottone Apri Dettaglio (Storico > Distribuzioni)
	 */
	@FXML
	private void handleApriDettaglioDistribuzione(StoricoDistribuzione storicoDistribuzione) {

		main.showDettaglioDistribuzioneDialog(storicoDistribuzione);

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
				if (Constants.TABELLE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf("#vboxTabelle");
				}
				if (Constants.VOCI.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf("#vboxVoci");
				}
				if (Constants.SCHEMI_SUI_QUALI_DISTRIBUIRE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf("#vboxSchemi");
				}
				if (Constants.ANTEPRIMA_E_DISTRIBUZIONE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleAnteprimaDistribuzione();
				}
				if (Constants.SCHEMI_DI_PARTENZA.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf("#vboxSchemiPartenza");
				}
				if (Constants.ANTEPRIMA_E_RIPRISTINO.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleAnteprimaRipristino();
				}
				if (Constants.DISTRIBUZIONI.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleStoricoDistribuzioni();
				}

				System.out.println(newValue.getValue());
			}
		});
	}

	private void hideAllOutsideOf(String paneName) {
		
		VboxNonVisibile("#vboxTabelle");
		VboxNonVisibile("#vboxVoci");
		VboxNonVisibile("#vboxSchemi");
		VboxNonVisibile("#vboxStorico");
		VboxNonVisibile("#vboxSchemiPartenza");
		VboxNonVisibile("#vboxPreView");
		VboxNonVisibile("#vboxRipristino");
		
		if (paneName != null && paneName.length() > 0) VboxVisibile(paneName);
		
	}

	private void VboxVisibile(String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(true);
	}

	private void VboxNonVisibile(String nomeBox) {
		VBox vboxTabelle = (VBox) main.getRootLayout().lookup(nomeBox);
		vboxTabelle.setVisible(false);
	}

	private void handleAnteprimaDistribuzione() {

		if (isInputValidForAnteprimaDistribuzione()) {
			try {
				VboxVisibile("#vboxPreView");
		    	bar.setVisible(true);
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

	private void fillAnteprimaDistribuzione() {

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
	
	private void handleStoricoDistribuzioni() {
		
		// TODO  verificare se è presente almeno una distribuzione sullo storico  
		if (main.getStoricoDistribuzione() != null && main.getStoricoDistribuzione().size() > 0) {
			VboxVisibile("#vboxStorico");
		} else {
			showAlert(AlertType.WARNING, "Errore", "Nessuna Distribuzione Presente", "Non ci sono Distribuzioni", null); 
		}
	}
	
	private void handleAnteprimaRipristino() {
		
		// TODO  verificare se è presente almeno una distribuzione sullo storico  
		if (distribuzioneRipristinabile != null) {
			try {
				VboxVisibile("#vboxRipristino");
				barRipristino.setVisible(false);
				newlistaDeleteForRipristino.clear();
				textAreaRipristinoInsert.setText("");
				anteprimaRipristino();
			} catch (Exception e) {
				VboxNonVisibile("#vboxRipristino");
				showAlert(AlertType.ERROR, "Error", "", e.toString(), null);
			};			
		} else {
			showAlert(AlertType.WARNING, "Errore", "Nessuna Distribuzione Ripristinabile", "Non ci sono Distribuzioni Ripristinabili", null); 
		}
	}
	
	private void anteprimaRipristino() {
		
		main.getSchemiRipristino().clear();
		
		labelRipristino.setText("Ripristino Distribuzione del " + distribuzioneRipristinabile.getDataOraDistribuzione() );
		
		for (Schema schema: distribuzioneRipristinabile.getElencoSchemi()) {
			main.addSchemiRipristinoData(schema);
		}
		
		schemiRipristinoTable.setItems(main.getSchemiRipristino());
		
		String contenuto = "Anteprima di Ripristino Distribuzione\ndel " + distribuzioneRipristinabile.getDataOraDistribuzione() + "\n" + "NOTE: " + distribuzioneRipristinabile.getNote();
		
		showAlert(AlertType.INFORMATION, "Information", contenuto,
				"Selezionando l'elenco degli Schemi e poi la lista delle Delete verranno esposte le relative Insert predisposte per il ripristino",
				null);
		
		bntRipristinoDistribuzioneVoci.setDisable(false);
			
	}
	
	private ArrayList<DeleteStatement> manageDeleteStatements(ArrayList<DeleteStatement> listaDeleteStatement) {
		
		System.out.println("classe OverviewDistriVociController metodo manageDeleteStatements");

		StoricoDistribuzione storicoDistribuzione = new StoricoDistribuzione();
		
		storicoDistribuzione.setListaDeleteStatement(listaDeleteStatement);
		
		ArrayList<DeleteStatement> listaDeleteStatementOutput = new ArrayList<DeleteStatement>();
		
		ObservableList<Schema> listaSchemiSuiQualiDistribuire = schemiTable.getSelectionModel().getSelectedItems();
		
		// DETERMINAZIONE DELL'ELENCO DEGLI SCHEMI SUI QUALI DISTRIBUIRE
		for (int y = 0; y < listaSchemiSuiQualiDistribuire.size(); y++) {
			
			Schema schema = listaSchemiSuiQualiDistribuire.get(y);
			
			// TODO aggiungere eventualmente (se non già presente) lo schema alla lista
			//      degli schemi sui quali si esegue la distribuzione >> per storico distribuzione
			if (listaSchemiPerStoricoDistribuzione.size() == 0  || !listaSchemiPerStoricoDistribuzione.contains(schema)) {
				listaSchemiPerStoricoDistribuzione.add(schema);
			}
		}
		
		// DETERMINAZIONE DELL'ELENCO DEGLI AGGIORNAMENTI DEL D.B.
		ArrayList<QueryDB> listaUpdateDB = new ArrayList<QueryDB>();
		
		for (DeleteStatement deleteStatement : listaDeleteStatement) {
			// STATEMENT DI DELETE
			String selectStatement = Constants.PREFIX_SELECT + deleteStatement.getCodice() + deleteStatement.getWhereCondition();
			listaUpdateDB.add(
					new QueryDB(deleteStatement.getCodice(), deleteStatement.getDeleteStatement(), Constants.DELETE, selectStatement));
			for (int i = 0; i < deleteStatement.getInsertsListFromSchemaOrigine().size(); i++) {
				// STATEMENT DI INSERT
				listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(),
						deleteStatement.getInsertsListFromSchemaOrigine().get(i), Constants.INSERT, null));
			}
		}

		// ESECUZIONE DEGLI STATEMENT SQL
		for (int i = 0 ; i < listaSchemiPerStoricoDistribuzione.size(); i++) {
			
			SchemaDTO schemaDTO = searchSchemaDTO(listaSchemiPerStoricoDistribuzione.get(i).getCodice());

			if (schemaDTO != null) {
				
				Distribuzione distribuzione = new Distribuzione();
				distribuzione.setCodiceSchema(listaSchemiPerStoricoDistribuzione.get(i).getCodice());
				distribuzione.setContatoreInsertGeneratePerBackup(Constants.ZERO);
				distribuzione.setContatoreRigheCancellate(Constants.ZERO);
				distribuzione.setContatoreRigheDistribuite(Constants.ZERO);
				
				// ESECUZIONE DELLE DELETE E DELLE INSERT
				risultatiDTO = model.runMultipleUpdateForDistribution(schemaDTO, listaUpdateDB);
				
				distribuzione.setContatoreInsertGeneratePerBackup(risultatiDTO.getListString().size());
				distribuzione.setListaInsertGeneratePerBackup(risultatiDTO.getListString());

				distribuzione.setContatoreRigheCancellate(risultatiDTO.getRowsDeleted());
				distribuzione.setContatoreRigheDistribuite(risultatiDTO.getRowsInserted());
			}
			
		}
		
		DeleteStatement deleteStatementOutput;
		
		return listaDeleteStatementOutput;
		
	}
	
	private DeleteStatement manageDeleteStatement(DeleteStatement deleteStatement) {
		
		System.out.println("classe OverviewDistriVociController metodo manageDeleteStatement");

		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();

		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;

		System.out.println("selectStatement = " + selectStatement);

		DeleteStatement deleteStatementOutput = deleteStatement;
		
		deleteStatementOutput.setCodiceSchemaOrigine(main.getSchemaDtoOrigine().getSchemaUserName());

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
		
		for (int y = 0; y < listaSchemiSuiQualiDistribuire.size(); y++) {

			int contatoreRigheDistribuite = 0;
			
			Schema schema = listaSchemiSuiQualiDistribuire.get(y);
			
			// TODO aggiungere eventualmente (se non già presente) lo schema alla lista
			//      degli schemi sui quali si esegue la distribuzione >> per storico distribuzione
			if (listaSchemiPerStoricoDistribuzione.size() == 0  || !listaSchemiPerStoricoDistribuzione.contains(schema)) {
				listaSchemiPerStoricoDistribuzione.add(schema);
			}

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

	private boolean manageDeleteStatementForRipristino(DeleteStatement deleteStatement) {
		
		boolean esitoOK = true;
		
		System.out.println("classe OverviewDistriVociController metodo manageDeleteStatementForRipristino");
		
		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();

		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;

		System.out.println("selectStatement = " + selectStatement);
		
		QueryDB deleteDB = new QueryDB();
		deleteDB.setQuery(deleteStatement.getDeleteStatement());
		
		List<Distribuzione> listOfDistribuzioni = deleteStatement.getListaDistribuzione();
		
		for (Distribuzione distribuzione : listOfDistribuzioni) {
			
			System.out.println("Codice Schema = " + distribuzione.getCodiceSchema());
			
			SchemaDTO schemaDTO = searchSchemaDTO(distribuzione.getCodiceSchema());
			
			if (schemaDTO != null) {
				// ESECUZIONE DELLA DELETE (cancello le righe distribuite)
				risultatiDTO = model.runUpdate(schemaDTO, deleteDB);
				if (risultatiDTO.getRowsUpdated() != distribuzione.getContatoreRigheDistribuite()) {
					// TODO msg di errore -->> sembra che il nr. delle righe cancellate non coincida con quanto atteso
					esitoOK = false;
				}
				
				for (int i = 0; i < distribuzione.getListaInsertGeneratePerBackup().size(); i++) {
					QueryDB insertDB = new QueryDB();
					insertDB.setQuery(distribuzione.getListaInsertGeneratePerBackup().get(i));
					// ESECUZIONE DELLE INSERT (inserisco i valori precedenti alla distribuzione)
					risultatiDTO = model.runUpdate(schemaDTO, insertDB);
					if (risultatiDTO.getRowsUpdated() != 1) {
						// TODO msg di errore -->> sembra che la insert non si è conclusa correttamente
						esitoOK = false;
					}
				}
			}
		}

		return esitoOK;
	}
	
	private SchemaDTO searchSchemaDTO(String codiceSchema) {

		for (SchemaDTO schemaDTO : main.getListSchemi()) {
			if (schemaDTO.getSchemaUserName().equalsIgnoreCase(codiceSchema)) {
				return schemaDTO;
			}
		}
		return null;
	}


	private void showAlerAnteprimaDistribuzioneOK() {
		
		showAlert(AlertType.INFORMATION, "Information", "Anteprima di Distribuzione",
				"Selezionando l'elenco degli Statement di Delete verranno esposte le relative Insert predisposte per la distribuzione",
				null);
		
		bntDistribuzioneVoci.setDisable(false);
		
	}
	
	private void showAlertRipristinoOK() {
		
		String contenuto = "";
		
		showAlert(AlertType.INFORMATION, "Information", "Ripristino Distribuzione concluso regolarmente", contenuto, null);
		
		bntRipristinoDistribuzioneVoci.setDisable(true);
		
	}
	
    private void showAlertDistribuzioneOK(StoricoDistribuzione storicoDistribuzione) {
		
		String contenuto = "";
		
		contenuto += "SCHEMA DI PARTENZA: " + storicoDistribuzione.getSchemaPartenza()  + "\n";
		
		//ciclo per elencare le Voci distribuite
		for (int i = 0; i < storicoDistribuzione.getElencoVoci().size(); i++) {
			String voce = storicoDistribuzione.getElencoVoci().get(i);
			if (i == 0) {
				if (storicoDistribuzione.getElencoVoci().size() == 1) {
					contenuto += "VOCE: ";
				} else {
					contenuto += "VOCI: ";		
				}
			}

			contenuto += voce;
			if (i == storicoDistribuzione.getElencoVoci().size() - 1) {
				contenuto += "\n";
			} else {
				contenuto += ", ";
			}
		}
		int contatoreTotInsertGeneratePerBackup = 0;
		int contatoreTotRigheCancellate = 0;
		int contatoreTotRigheDistribuite = 0;
		//ciclo per elencare le Tabelle distribuite
		for (int i = 0; i < storicoDistribuzione.getListaDeleteStatement().size(); i++) {
			String tabella = storicoDistribuzione.getListaDeleteStatement().get(i).getCodice();
			for (Distribuzione distribuzione: storicoDistribuzione.getListaDeleteStatement().get(i).getListaDistribuzione()) {
				contatoreTotInsertGeneratePerBackup +=  distribuzione.getContatoreInsertGeneratePerBackup();
				contatoreTotRigheCancellate += distribuzione.getContatoreRigheCancellate();
				contatoreTotRigheDistribuite += distribuzione.getContatoreRigheDistribuite();
			}
			if (i == 0) {
				if (storicoDistribuzione.getListaDeleteStatement().size() == 1) {
					contenuto += "TABELLA: ";
				} else {
					contenuto += "TABELLE: ";		
				}
			}
			contenuto += tabella;
			if (i == storicoDistribuzione.getListaDeleteStatement().size() - 1) {
				contenuto += "\n";
			} else {
				contenuto += ", ";
			}
		}
		contenuto += "NR. DI SCHEMI DI ARRIVO :  " + storicoDistribuzione.getElencoSchemi().size()  + "\n";
		contenuto += "TOTALE INSERT GENERATE PER RIPRISTINO :  " + contatoreTotInsertGeneratePerBackup  + "\n";
		contenuto += "TOTALE RIGHE CANCELLATE :  " + contatoreTotRigheCancellate  + "\n";
		contenuto += "TOTALE RIGHE DISTRIBUITE :  " + contatoreTotRigheDistribuite  + "\n";
				
		showAlert(AlertType.INFORMATION, "Information", "Distribuzione conclusa regolarmente", contenuto, null);
		
		bntDistribuzioneVoci.setDisable(true);
	}
	
	private boolean isInputValidForAnteprimaDistribuzione() {
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
			showAlert(AlertType.ERROR, "Campi non validi",
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
		
		if (!checkNoteAndConfirmForDistribuzione()) {
			return;
		}
		
		disabledView(true);
		bar.setVisible(true);
		
		bar.setProgress(0);
		
		copyWorkerForExecuteDistribution = createWorkerForExecuteDistribution();
		
        labelInfoEsecuzione.textProperty().unbind();
        labelInfoEsecuzione.textProperty().bind(copyWorkerForExecuteDistribution.messageProperty());

        bar.progressProperty().unbind();
        bar.progressProperty().bind(copyWorkerForExecuteDistribution.progressProperty());

        copyWorkerForExecuteDistribution.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("newValue " + newValue);
            }
        });
        
		Thread backgroundThreadUpdateDB = new Thread(copyWorkerForExecuteDistribution, "updateDataBase-thread");
		backgroundThreadUpdateDB.setDaemon(true);
		backgroundThreadUpdateDB.start();
		
		copyWorkerForExecuteDistribution.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteDistribution - setOnFailed - no eccezione");
				copyWorkerForExecuteDistribution.cancel(true);
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(false);
				bar.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
			} else {
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteDistribution - setOnFailed - eccezione = " + exception.toString());
			}
		});
		
	}

	private boolean checkNoteAndConfirmForDistribuzione() {
		
		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Conferma Distribuzione");
		dialog.setHeaderText("Per cortesia, imposta una nota informativa relativa alla distribuzione");
		dialog.setContentText("Nota informativa:");
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()){
			this.nota = result.get();
			return true;
		} else {
			this.nota = "";
			return false;
		}
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
				
				fillAnteprimaDistribuzione();
				
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
				showAlerAnteprimaDistribuzioneOK();
			}
		};
	}

	
	public Task createWorkerForExecuteDistribution() {

		System.out.println("OverviewDistriVociController metodo createWorkerForExecuteDistribution");
		
		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					System.out.println("Canceling...");
				}
				
				listaSchemiPerStoricoDistribuzione.clear();
				
				ObservableList<DeleteStatement> elencoDeleteStatement = FXCollections.observableArrayList();
				for (int i = 0; i < main.getDeleteStatement().size(); i++) {
					DeleteStatement deleteStatement  = main.getDeleteStatement().get(i);
					elencoDeleteStatement.add(manageDeleteStatement(deleteStatement));
					System.out.println("SQL Statement " + deleteStatement.getDeleteStatement() + " Elaborato!");
				}
				newlistaDelete.clear();
				main.getDeleteStatement().clear();
				for (DeleteStatement ds: elencoDeleteStatement) {
					newlistaDelete.add(ds);
					main.addDeleteStatement(ds);
				}
				deleteStatementTable.setItems(main.getDeleteStatement());
				
				return true;
			}

			@Override
			protected void succeeded() {
				
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteDistribution - succeeded");
				super.succeeded();
				updateMessage("Done!");
				bar.progressProperty().unbind();
				bar.setProgress(0);
				disabledView(false);
				bar.setVisible(false);
				showAlertDistribuzioneOK(aggiornaStoricoDistribuzione());
				clearDistributionInfo();
			}
		};
	}
	
	public void setModel(Model model) {
		this.model = model;
	}

	private StoricoDistribuzione aggiornaStoricoDistribuzione() {
		
		StoricoDistribuzione storicoDistribuzione = new StoricoDistribuzione();
		ArrayList<DeleteStatement> listaDeleteStatement = new ArrayList<DeleteStatement>();
		String schemaPartenza = "";
		for (DeleteStatement deletestatement : newlistaDelete) {
			listaDeleteStatement.add(deletestatement);
			schemaPartenza = deletestatement.getCodiceSchemaOrigine();
		}
		storicoDistribuzione.setElencoSchemi(listaSchemiPerStoricoDistribuzione);
		
		ObservableList<Voce> listaVociSelezionate = vociTable.getSelectionModel().getSelectedItems();
		ArrayList<String> elencoVoci = new ArrayList<String>();
		for (Voce voce: listaVociSelezionate) {
			elencoVoci.add(voce.getCodice());
		}
		storicoDistribuzione.setElencoVoci(elencoVoci);
		
		storicoDistribuzione.setListaDeleteStatement(listaDeleteStatement);
		storicoDistribuzione.setNote(this.nota);
		storicoDistribuzione.setSchemaPartenza(main.getSchemaDtoOrigine().getSchemaUserName());
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");

		storicoDistribuzione.setDataOraDistribuzione(FOMATTER.format(ldt));
		
		main.addStoricoDistribuzione(storicoDistribuzione);
		storicoDistribuzioneTable.setItems(main.getStoricoDistribuzione());
		
		dataTabStoricoDistribuzioneColumn.setSortType(TableColumn.SortType.DESCENDING);
		storicoDistribuzioneTable.getSortOrder().add(dataTabStoricoDistribuzioneColumn);
		storicoDistribuzioneTable.sort();
		
		impostaDistribuzioneRipristinabile();
		
		return storicoDistribuzione;
	}
	
	private void clearDistributionInfo() {
		
		// pulizia delle selezioni sui parametri (schemi di partenza e arrivo, tabelle, voci)		
		clearParameterSelection();

		// pulizia della lista degli statement di delete
		main.getDeleteStatement().clear();
		deleteStatementTable.setItems(main.getDeleteStatement());
		textAreaPreviewInsert.setText("");
	}
	
	private void clearRipristinoInfo() {
		
		// pulizia delle selezioni sui parametri (schemi di partenza e arrivo, tabelle, voci)
		clearParameterSelection();
		
		// pulizia della lista degli statement di delete
		main.getDeleteStatement().clear();
		deleteStatementTable.setItems(main.getDeleteStatement());
		textAreaPreviewInsert.setText("");
		
	}
	
	private void clearParameterSelection() {
		schemiPartenzaTable.getSelectionModel().clearSelection();
		tabelledbTable.getSelectionModel().clearSelection();
		vociTable.getSelectionModel().clearSelection();
		schemiTable.getSelectionModel().clearSelection();
	}
	
	/**
	 * chiamata quando l'utente fa click sul bottone RIPRISTINO DELLA Distribuzione Voci
	 */
	@FXML
	private void ripristino() {
		
		System.out.println("OverviewDistriVociController metodo ripristino");
		
		if (!checkConfirmOfRipristino()) {
			return;
		}
		
		disabledView(true);
		barRipristino.setVisible(true);
		
		barRipristino.setProgress(0);
		
		main.getDeleteStatementForRipristino().clear();
		
		for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
			main.addDeleteStatementForRipristino(deleteStatement);	
		}
		
		copyWorkerForExecuteRipristino = createWorkerForExecuteRipristino();

        barRipristino.progressProperty().unbind();
        barRipristino.progressProperty().bind(copyWorkerForExecuteRipristino.progressProperty());

        copyWorkerForExecuteRipristino.messageProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                System.out.println("newValue " + newValue);
            }
        });
        
		Thread backgroundThreadUpdateDBripristino = new Thread(copyWorkerForExecuteRipristino, "updateDBripristino-thread");
		backgroundThreadUpdateDBripristino.setDaemon(true);
		backgroundThreadUpdateDBripristino.start();
		
		copyWorkerForExecuteRipristino.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				System.out.println("OverviewDistriVociController metodo copyWorkerForExecuteRipristino - setOnFailed - no eccezione");
				copyWorkerForExecuteRipristino.cancel(true);
				barRipristino.progressProperty().unbind();
				barRipristino.setProgress(0);
				disabledView(false);
				barRipristino.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), null);
			} else {
				System.out.println("OverviewDistriVociController metodo copyWorkerForExecuteRipristino - setOnFailed - eccezione = " + exception.toString());
			}
		});
		
	}

	private boolean checkConfirmOfRipristino() {
		
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Conferma Ripristino");
		alert.setHeaderText("Conferma Ripristino");
		alert.setContentText("Confermi il Ripristino?");
		
		Optional<ButtonType> result = alert.showAndWait();
		
		if (result.get() == ButtonType.OK){
			return true;
		} else {
			return false;
		}
	}
	
	public Task createWorkerForExecuteRipristino() {

		System.out.println("OverviewDistriVociController metodo createWorkerForExecuteRipristino");
		
		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					System.out.println("Canceling...");
				}
				for (int i = 0; i < main.getDeleteStatementForRipristino().size(); i++) {
					DeleteStatement deleteStatementForRipristino  = main.getDeleteStatementForRipristino().get(i);
					manageDeleteStatementForRipristino(main.getDeleteStatementForRipristino().get(i));
					System.out.println("SQL Statement " + main.getDeleteStatementForRipristino().get(i).getDeleteStatement() + " Elaborato!");
				}
				newlistaDeleteForRipristino.clear();
				main.getDeleteStatementForRipristino().clear();
				return true;
			}

			@Override
			protected void succeeded() {
				
				System.out.println("OverviewDistriVociController metodo createWorkerForExecuteRipristino - succeeded");
				super.succeeded();
				updateMessage("Done!");
				barRipristino.progressProperty().unbind();
				barRipristino.setProgress(0);
				disabledView(false);
				barRipristino.setVisible(false);
				textAreaRipristinoInsert.setText("");
				main.getSchemiRipristino().clear();
				aggiornaStoricoDistribuzioneRipristinata();
				showAlertRipristinoOK();
				clearRipristinoInfo();
			}
		};
	}
	
	private void aggiornaStoricoDistribuzioneRipristinata() {
		
		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
		this.main.getStoricoDistribuzione().get(indiceDistribuzioneRipristinabile).setDataOraRipristino(FOMATTER.format(ldt));
		
		impostaDistribuzioneRipristinabile();
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
