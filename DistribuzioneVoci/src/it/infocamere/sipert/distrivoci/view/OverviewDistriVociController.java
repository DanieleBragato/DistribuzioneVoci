package it.infocamere.sipert.distrivoci.view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.ToIntFunction;

import org.apache.log4j.Logger;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.db.QueryDB;
import it.infocamere.sipert.distrivoci.db.dto.DistributionResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Distribuzione;
import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.util.ColumnsType;
import it.infocamere.sipert.distrivoci.util.Constants;
import it.infocamere.sipert.distrivoci.util.EsitoTestConnessioniPresenzaTabelle;
import it.infocamere.sipert.distrivoci.util.InsertStatement;
import it.infocamere.sipert.distrivoci.util.LoadingDialog;
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

	protected static final Integer Integer = 69;

	private ToIntFunction<Integer> func = null;

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
	private TableColumn<StoricoDistribuzione, String> sequenceTabStoricoDistribuzioneColumn;

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

	@FXML
	private VBox vboxRipristinoVoce;

	@FXML
	private Label labelRipristinoVoce;

	@FXML
	private Label labelRipristinoVoce1;

	@FXML
	private TableView<Schema> schemiRipristinoTableVoce;

	@FXML
	private TableColumn<Schema, String> codiceSchemaRipristinoVoceColumn;

	@FXML
	private TableColumn<Schema, String> descrizioneSchemaRipristinoVoceColumn;

	@FXML
	private TableView<DeleteStatement> deleteStatementRipristinoVoceTable;

	@FXML
	private TableColumn<DeleteStatement, String> codiceTabRipristinoVoceDeleteColumn;

	@FXML
	private TableColumn<DeleteStatement, String> statementRipristinoVoceDeleteColumn;

	@FXML
	private Label labelRipristinoVoceInsert;

	@FXML
	private TextArea textAreaRipristinoVoceInsert;

	@FXML
	private ProgressBar barRipristinoVoce;

	@FXML
	private Button bntRipristinoDistribuzioneVoce;

	@FXML
	private Button bntDeleteOldest;

//    @FXML
//    private AnchorPane mainAnchorPane;

	// Referimento al main
	private Main main;

	private Model model;

	private Task copyWorkerForGenInserts;

	private Task copyWorkerForCheckConnessioniTabelle;

	private Task copyWorkerForExecuteDistribution;

	private Task copyWorkerForExecuteRipristino;

	private Task copyWorkerForExecuteRipristinoVoce;

	private String nota;

	private ObservableList<DeleteStatement> newlistaDeleteForRipristino = FXCollections.observableArrayList();

	private ObservableList<DeleteStatement> newlistaDeleteForRipristinoVoce = FXCollections.observableArrayList();

	private ArrayList<Schema> listaSchemiPerStoricoDistribuzione = new ArrayList<Schema>();

	private String codiceSchemaRipristinoSelezionato;

	private StoricoDistribuzione distribuzioneRipristinabile;

	private int indiceDistribuzioneRipristinabile;

	protected GenericResultsDTO risultatiDTO;

	private LinkedHashMap<String, ColumnsType> listTablesColumnsType = new LinkedHashMap<String, ColumnsType>();

	private TreeView<String> mainTree;

	private EsitoTestConnessioniPresenzaTabelle esitoTestConnessioniPresenzaTabellePerTask = new EsitoTestConnessioniPresenzaTabelle();

	/**
	 * Placing a listener on this list allows to get notified BY the result when the
	 * task has finished.
	 */
	public ObservableList<Integer> resultNotificationList = FXCollections.observableArrayList();

	public Integer resultValue;

	LoadingDialog loadingDialog = new LoadingDialog();

	boolean manageAnteprimaDistribuzione = false;
	boolean manageAnteprimaRipristino = false;
	boolean manageAnteprimaRipristinoVoce = false;

	static Logger logger = Logger.getLogger(OverviewDistriVociController.class);

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

		// Initializza la lista degli schemi per il ripristino con 2 colonne - codice e
		// descrizione
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

		// Initializza la lista degli schemi per il ripristino Voce con 2 colonne -
		// codice e descrizione
		codiceSchemaRipristinoVoceColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaRipristinoVoceColumn
				.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty());

		// Initializza la lista degli statement di delete per il RIPRISTINO VOCE (2
		// colonne - codice tabella e
		// relativo statement)
		codiceTabRipristinoVoceDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementRipristinoVoceDeleteColumn
				.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());

		// Initializza la lista degli statement di delete per il RIPRISTINO (2 colonne -
		// codice tabella e
		// relativo statement)
		codiceTabRipristinoDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementRipristinoDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());

		// Initializza la lista dello storico delle distribuzioni (2 colonne - data e
		// note della distribuzione
		dataTabStoricoDistribuzioneColumn
				.setCellValueFactory(cellData -> cellData.getValue().dataOraDistribuzioneProperty());
		// dataTabStoricoDistribuzioneColumn.setSortType(TableColumn.SortType.DESCENDING);

		sequenceTabStoricoDistribuzioneColumn
				.setCellValueFactory(cellData -> cellData.getValue().SequenceDistribuzioneProperty());
		sequenceTabStoricoDistribuzioneColumn.setSortType(TableColumn.SortType.DESCENDING);

		ripristinoTabStoricoDistribuzioneColumn
				.setCellValueFactory(cellData -> cellData.getValue().dataOraRipristinoProperty());

		noteTabStoricoDistribuzioneColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
		schemaPartenzaTabStoricoDistribuzioneColumn
				.setCellValueFactory(cellData -> cellData.getValue().schemaPartenzaProperty());

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
//										System.out.println(storicoDistribuzione.getDataOraDistribuzione() + "   "
//												+ storicoDistribuzione.getNote());
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

		// storicoDistribuzioneTable.getSortOrder().add(dataTabStoricoDistribuzioneColumn);
		storicoDistribuzioneTable.getSortOrder().add(sequenceTabStoricoDistribuzioneColumn);

		storicoDistribuzioneTable.sort();

		// Listener per la selezione del dettaglio degli statement di delete Ripristino
		schemiRipristinoTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDeletesRipristinoDetails(newValue));

		// Listener per la selezione del dettaglio dello statement di delete e dei
		// relativi statement di insert per RIPRISTINO
		deleteStatementRipristinoTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInsertsDetailsForRipristino(newValue));

		// Listener per la selezione del dettaglio degli statement di delete Ripristino
		// Voce
		schemiRipristinoTableVoce.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDeletesRipristinoVoceDetails(newValue));

		// Listener per la selezione del dettaglio dello statement di delete e dei
		// relativi statement di insert per RIPRISTINO VOCE
		deleteStatementRipristinoVoceTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInsertsDetailsForRipristinoVoce(newValue));

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
				for (InsertStatement insertStatement : newValue.getInsertsListFromSchemaOrigine()) {

					textArea = textArea + insertStatement.getInsertStatement() + "\n";
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
				if (distribuzione.getCodiceSchema().equalsIgnoreCase(schemaSelezionato.get(Constants.ZERO).getCodice())
						&& distribuzione.getListaInsertGeneratePerBackup() != null) {
					for (InsertStatement insertStatement : distribuzione.getListaInsertGeneratePerBackup()) {
						textArea = textArea + insertStatement.getInsertStatement() + "\n";
					}
				}
			}
			textAreaRipristinoInsert.setText(textArea);
		}
	}

	private void showDeletesRipristinoVoceDetails(Schema newValue) {

		if (newValue != null && newValue.getCodice() != null) {
			codiceSchemaRipristinoSelezionato = newValue.getCodice();
			newlistaDeleteForRipristinoVoce.clear();
			textAreaRipristinoVoceInsert.setText("");
			for (int i = 0; i < distribuzioneRipristinabile.getListaDeleteStatement().size(); i++) {
				DeleteStatement deleteStatement = distribuzioneRipristinabile.getListaDeleteStatement().get(i);
				for (int y = 0; y < deleteStatement.getListaDistribuzione().size(); y++) {
					Distribuzione distr = deleteStatement.getListaDistribuzione().get(y);
					if (distr.getCodiceSchema().equalsIgnoreCase(codiceSchemaRipristinoSelezionato)) {
						newlistaDeleteForRipristinoVoce.add(generaDeleteStatementPerRipristinoVoce(deleteStatement));
					}
				}
			}
			deleteStatementRipristinoVoceTable.setItems(newlistaDeleteForRipristinoVoce);
		}
	}

	private DeleteStatement generaDeleteStatementPerRipristinoVoce(DeleteStatement deleteStatement) {

		DeleteStatement ds = new DeleteStatement();
		ds.setCodice(deleteStatement.getCodice());
		ds.setCodiceSchemaOrigine(deleteStatement.getCodiceSchemaOrigine());
		ds.setColumnsType(deleteStatement.getColumnsType());
		ds.setDeleteStatement(creaDeleteTableSuSingolaVoce(ds.getCodice(), main.getVoceDaRipristinare()));
		ds.setInsertsListFromSchemaOrigine(deleteStatement.getInsertsListFromSchemaOrigine());
		ds.setWhereCondition(""); // ??????????????

		ArrayList<Distribuzione> nuovaListaDistrib = new ArrayList<Distribuzione>();

		for (Distribuzione distrib : deleteStatement.getListaDistribuzione()) {
			Distribuzione d = new Distribuzione();
			d.setCodiceSchema(distrib.getCodiceSchema());
			d.setListaInsertGeneratePerBackup(distrib.getListaInsertGeneratePerBackup());
			nuovaListaDistrib.add(d);
		}

		ds.setListaDistribuzione(nuovaListaDistrib);
		return ds;

	}

	private void showInsertsDetailsForRipristinoVoce(DeleteStatement newValue) {

		// NB >> un solo schema selezionabile
		ObservableList<Schema> schemaSelezionato = schemiRipristinoTableVoce.getSelectionModel().getSelectedItems();

		String textArea = "";

		if (newValue != null) {
			// cerco la distribuzione corrispondente allo schema selezionato
			for (Distribuzione distribuzione : newValue.getListaDistribuzione()) {
				if (distribuzione.getCodiceSchema().equalsIgnoreCase(schemaSelezionato.get(Constants.ZERO).getCodice())
						&& distribuzione.getListaInsertGeneratePerBackup() != null) {
					for (InsertStatement insertStatement : distribuzione.getListaInsertGeneratePerBackup()) {
						if (main.getVoceDaRipristinare().getCodice().equalsIgnoreCase(insertStatement.getVoce())) {
							textArea = textArea + insertStatement.getInsertStatement() + "\n";
						}

					}
				}
			}
			textAreaRipristinoVoceInsert.setText(textArea);
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
			for (int i = 0; i < this.main.getStoricoDistribuzione().size(); i++) {
				if (this.main.getStoricoDistribuzione().get(i).getDataOraRipristino() == null || Constants.PARZIALE
						.equalsIgnoreCase(this.main.getStoricoDistribuzione().get(i).getDataOraRipristino())) {
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
				// If filter text is empty, display all schemas.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare name of every schema with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (schema.getCodice().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches schema name.
				} else if (schema.getDescrizione().toLowerCase().indexOf(lowerCaseFilter) != -1) {
					return true; // Filter matches schema descrizione
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
					"Per cortesia, seleziona una tabella dalla lista ", main.getStagePrincipale());
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
					"Per cortesia, seleziona una tabella dalla lista ", main.getStagePrincipale());
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
					"Per cortesia, seleziona una Voce dalla lista ", main.getStagePrincipale());
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
					"Per cortesia, seleziona una Voce dalla lista ", main.getStagePrincipale());
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
	 * chiamata quando l'utente fa click sul bottone Apri Dettaglio (Storico >
	 * Distribuzioni)
	 */
	@FXML
	private void handleApriDettaglioDistribuzione(StoricoDistribuzione storicoDistribuzione) {

		main.showDettaglioDistribuzioneDialog(storicoDistribuzione);

	}

	public void setTreeCellFactory(TreeView<String> tree) {

		mainTree = tree;

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
				if (Constants.SCHEMI_DI_PARTENZA.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(Constants.BOX_SCHEMI_PARTENZA);
				}
				if (Constants.TABELLE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(Constants.BOX_TABELLE);
				}
				if (Constants.VOCI.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(Constants.BOX_VOCI);
				}
				if (Constants.SCHEMI_SUI_QUALI_DISTRIBUIRE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(Constants.BOX_SCHEMI_SUI_QUALI_DISTRIBUIRE);
				}
				if (Constants.ANTEPRIMA_E_DISTRIBUZIONE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleAnteprimaDistribuzione();
				}
				if (Constants.ANTEPRIMA_E_RIPRISTINO.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleAnteprimaRipristino();
				}
				if (Constants.ANTEPRIMA_E_RIPRISTINO_VOCE.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleAnteprimaRipristinoVoce();
				}
				if (Constants.STORICO_DISTRIBUZIONI.equalsIgnoreCase(newValue.getValue())) {
					hideAllOutsideOf(null);
					handleStoricoDistribuzioni();
				}

				LocalDateTime ldt = LocalDateTime.now();
				DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss");
				String contenuto = newValue.getValue() + " " + FOMATTER.format(ldt);
				// System.out.println(contenuto);
				logger.info(contenuto);
			}
		});
	}

	private void handleAnteprimaRipristinoVoce() {

		// verifica se è presente almeno una distribuzione sullo storico

		manageAnteprimaRipristinoVoce = true;
		manageAnteprimaDistribuzione = false;
		manageAnteprimaRipristino = false;

		if (distribuzioneRipristinabile != null) {
			if (isInputValidForAnteprimaRipristinoVoce()) {
			} else {
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO_VOCE);
			}

		} else {
			showAlert(AlertType.WARNING, "Errore", "Nessuna Distribuzione Ripristinabile",
					"Non ci sono Distribuzioni Ripristinabili", main.getStagePrincipale());
		}

	}

	private void anteprimaRipristinoVoce() {

		labelRipristinoVoce.setText("Distribuzione del " + distribuzioneRipristinabile.getDataOraDistribuzione());
		labelRipristinoVoce1.setText("             Ripristino Voce " + main.getVoceDaRipristinare().getCodice());

		schemiRipristinoTableVoce.setItems(main.getSchemiRipristinoVoce());

		String contenuto = "Anteprima di Ripristino \nDistribuzione del "
				+ distribuzioneRipristinabile.getDataOraDistribuzione() + "\n" + "NOTE: "
				+ distribuzioneRipristinabile.getNote() + "\n" + "Ripristino della VOCE "
				+ main.getVoceDaRipristinare().getCodice();

		showAlert(AlertType.INFORMATION, "Informazione", contenuto,
				"Selezionando l'elenco degli Schemi e poi la lista delle Delete verranno esposte le relative Insert predisposte per il ripristino della VOCE "
						+ main.getVoceDaRipristinare().getCodice(),
				main.getStagePrincipale());

		bntRipristinoDistribuzioneVoce.setDisable(false);

		newlistaDeleteForRipristinoVoce.clear();

	}

	private boolean isInputValidForAnteprimaRipristinoVoce() {

		main.getSchemiRipristinoVoce().clear();

		for (Schema schema : distribuzioneRipristinabile.getElencoSchemi()) {
			main.addSchemiRipristinoVoceData(schema);
		}

		ObservableList<Tabella> listaTabelleDaRipristinare = FXCollections.observableArrayList();

		listTablesColumnsType.clear();
		for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
			listaTabelleDaRipristinare.add(new Tabella(deleteStatement.getCodice(), null));
			listTablesColumnsType.put(deleteStatement.getCodice().toUpperCase(), deleteStatement.getColumnsType());
		}
		;

		if (main.getSchemiRipristinoVoce() != null && main.getSchemiRipristinoVoce().size() > 0) {
			if (isSchemiTabelleOK(main.getSchemiRipristinoVoce(), listaTabelleDaRipristinare)) {
				if (main.getVoceDaRipristinare() == null) {
					return false;
				} else {
					return true;
				}
			} else {
				return false;
			}
		} else {
			showAlert(AlertType.ERROR, "Error", "",
					"Non trovati gli schemi relativi alla Distribuzione da Ripristinare ", main.getStagePrincipale());
			return false;
		}

	}

	private void hideAllOutsideOf(String paneName) {

		VboxNonVisibile(Constants.BOX_TABELLE);
		VboxNonVisibile(Constants.BOX_VOCI);
		VboxNonVisibile(Constants.BOX_SCHEMI_SUI_QUALI_DISTRIBUIRE);
		VboxNonVisibile(Constants.BOX_STORICO);
		VboxNonVisibile(Constants.BOX_SCHEMI_PARTENZA);
		VboxNonVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
		VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO);
		VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO_VOCE);

		if (paneName != null && paneName.length() > 0)
			VboxVisibile(paneName);

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

		manageAnteprimaDistribuzione = true;
		manageAnteprimaRipristino = false;
		manageAnteprimaRipristinoVoce = false;

		disabledView(true);
		if (!isInputValidForAnteprimaDistribuzione()) {
			disabledView(false);
			VboxNonVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
		}

	}

	private void fillAnteprimaDistribuzione() {

		ObservableList<Tabella> listaTabelleSelezionate = tabelledbTable.getSelectionModel().getSelectedItems();
		ObservableList<Voce> listaVociSelezionate = vociTable.getSelectionModel().getSelectedItems();

		ArrayList<String> listDelete = new ArrayList<String>();

		main.clearDeleteStatement();

		for (int i = 0; i < listaTabelleSelezionate.size(); i++) {
			String deleteString = "";
			String whereCondition = "";
			deleteString = Constants.PREFIX_DELETE;
			Tabella tab = listaTabelleSelezionate.get(i);
			DeleteStatement deleteStatement = new DeleteStatement();
			deleteStatement.setCodice(tab.getCodice());

			if (tab.getCodice().toUpperCase().contains(Constants.ESPADE)
					|| tab.getCodice().contains(Constants.ESPAFM)) {
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
					// System.out.println(deleteString + whereCondition);
					logger.info(deleteString + whereCondition);

				}
			}
			deleteStatement.setWhereCondition(whereCondition);
			deleteStatement.setDeleteStatement(deleteString + whereCondition);
			deleteStatement.setColumnsType(listTablesColumnsType.get(deleteStatement.getCodice().toUpperCase()));
			main.addDeleteStatement(manageDeleteStatementGenInserts(deleteStatement));
		}
		deleteStatementTable.setItems(main.getDeleteStatement());
	}

	private DeleteStatement manageDeleteStatementGenInserts(DeleteStatement deleteStatement) {

		String tableName = deleteStatement.getCodice();
		String whereCondition = deleteStatement.getWhereCondition();

		String selectStatement = Constants.PREFIX_SELECT + tableName + whereCondition;

		// System.out.println("selectStatement = " + selectStatement);
		logger.info("selectStatement = " + selectStatement);

		DeleteStatement deleteStatementOutput = deleteStatement;

		QueryDB queryDB = new QueryDB();
		queryDB.setQuery(selectStatement);

		if (main.getSchemaDtoOrigine() != null) {
			risultatiDTO = model.runQueryForGenerateInserts(main.getSchemaDtoOrigine(), queryDB, tableName);
			deleteStatementOutput
					.setInsertsListFromSchemaOrigine((List<InsertStatement>) risultatiDTO.getInsertsForBackup());
			deleteStatementOutput.setCodiceSchemaOrigine(main.getSchemaDtoOrigine().getSchemaUserName());
		} else {
			showAlert(AlertType.ERROR, "Error", "",
					"Non trovati dati di connessione relativi allo schema " + Constants.SETEUR7ES,
					main.getStagePrincipale());
		}
		return deleteStatementOutput;
	}

	private void handleStoricoDistribuzioni() {

		// verifica se è presente almeno una distribuzione sullo storico

		if (main.getStoricoDistribuzione() != null && main.getStoricoDistribuzione().size() > 0) {
			bntDeleteOldest.setDisable(false);
			VboxVisibile(Constants.BOX_STORICO);
		} else {
			showAlert(AlertType.WARNING, "Errore", "Nessuna Distribuzione Presente", "Non ci sono Distribuzioni",
					main.getStagePrincipale());
		}
	}

	@FXML
	private void handleDeleteOldest(ActionEvent event) {

		String dataDistribuzione = "";
		if (main.getStoricoDistribuzione().size() > 0) {
			int indiceDistribuzionePiùVecchia = main.getStoricoDistribuzione().size() - 1;
			dataDistribuzione = main.getStoricoDistribuzione().get(indiceDistribuzionePiùVecchia)
					.getDataOraDistribuzione();
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Cancellazione Distribuzione del " + dataDistribuzione);
			alert.setHeaderText("Conferma la cancellazione della Distribuzione del " + dataDistribuzione);
			alert.setContentText("Confermi la cancellazione?");
			alert.initOwner(main.getStagePrincipale());

			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				main.getStoricoDistribuzione().remove(indiceDistribuzionePiùVecchia);
				storicoDistribuzioneTable.setItems(main.getStoricoDistribuzione());
				logger.info("Cancellata la Distribuzione del " + dataDistribuzione);
				if (main.getStoricoDistribuzione().size() == 0) {
					// SVUOTATO STORICO DISTRIBUZIONI >> PULIZIA DATI RELATIVI ALLA DISTRIBUZIONE
					// RIPRISTINABILE
					distribuzioneRipristinabile = null;
					indiceDistribuzioneRipristinabile = Constants.ZERO;
					bntDeleteOldest.setDisable(true);
				}
			}
		}

	}

	private void handleAnteprimaRipristino() {

		manageAnteprimaRipristino = true;
		manageAnteprimaDistribuzione = false;
		manageAnteprimaRipristinoVoce = false;

		// verifica se è presente almeno una distribuzione sullo storico

		if (distribuzioneRipristinabile != null) {
			if (!isInputValidForAnteprimaRipristino()) {
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO);
			}

		} else {
			showAlert(AlertType.WARNING, "Errore", "Nessuna Distribuzione Ripristinabile",
					"Non ci sono Distribuzioni Ripristinabili", main.getStagePrincipale());
		}
	}

	private void anteprimaRipristino() {

		labelRipristino
				.setText("Ripristino Distribuzione del " + distribuzioneRipristinabile.getDataOraDistribuzione());

		schemiRipristinoTable.setItems(main.getSchemiRipristino());

		String contenuto = "Anteprima di Ripristino \nDistribuzione del "
				+ distribuzioneRipristinabile.getDataOraDistribuzione() + "\n" + "NOTE: "
				+ distribuzioneRipristinabile.getNote();

		showAlert(AlertType.INFORMATION, "Information", contenuto,
				"Selezionando l'elenco degli Schemi e poi la lista delle Delete verranno esposte le relative Insert predisposte per il ripristino",
				main.getStagePrincipale());

		bntRipristinoDistribuzioneVoci.setDisable(false);

	}

	private void manageDeleteStatementsForDistribution(ObservableList<DeleteStatement> listaDeleteStatement) {

		ObservableList<Schema> listaSchemiSuiQualiDistribuire = schemiTable.getSelectionModel().getSelectedItems();

		// DETERMINAZIONE DELL'ELENCO DEGLI SCHEMI SUI QUALI DISTRIBUIRE
		for (int y = 0; y < listaSchemiSuiQualiDistribuire.size(); y++) {

			Schema schema = listaSchemiSuiQualiDistribuire.get(y);

			// TODO aggiungere eventualmente (se non già presente) lo schema alla lista
			// degli schemi sui quali si esegue la distribuzione >> per storico
			// distribuzione
			if (listaSchemiPerStoricoDistribuzione.size() == 0
					|| !listaSchemiPerStoricoDistribuzione.contains(schema)) {
				listaSchemiPerStoricoDistribuzione.add(schema);
			}
		}

		// DETERMINAZIONE DELL'ELENCO DEGLI AGGIORNAMENTI DEL D.B.
		ArrayList<QueryDB> listaUpdateDB = new ArrayList<QueryDB>();

		for (DeleteStatement deleteStatement : listaDeleteStatement) {
			// STATEMENT DI DELETE
			String selectStatement = Constants.PREFIX_SELECT + deleteStatement.getCodice()
					+ deleteStatement.getWhereCondition();
			listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(), deleteStatement.getDeleteStatement(),
					Constants.DELETE, selectStatement, null));
			for (int i = 0; i < deleteStatement.getInsertsListFromSchemaOrigine().size(); i++) {
				// STATEMENT DI INSERT
				listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(),
						deleteStatement.getInsertsListFromSchemaOrigine().get(i).getInsertStatement(), Constants.INSERT,
						null, null));
			}
		}

		// ESECUZIONE DEGLI STATEMENT SQL
		for (int i = 0; i < listaSchemiPerStoricoDistribuzione.size(); i++) {
			SchemaDTO schemaDTO = searchSchemaDTO(listaSchemiPerStoricoDistribuzione.get(i).getCodice());
			if (schemaDTO != null && listaUpdateDB.size() > 0) {
				// GENERAZIONE DELLE INSERT DI BACKUP ED ESECUZIONE DELLE DELETE E DELLE INSERT
				ArrayList<DistributionResultsDTO> tempListaRisultatiPerSingoloSchema = model
						.runMultipleUpdateForDistribution(schemaDTO, listaUpdateDB);
				aggiornaElencoDeleteStatement(tempListaRisultatiPerSingoloSchema);
			}
		}
	}

	private void aggiornaElencoDeleteStatement(ArrayList<DistributionResultsDTO> listaRisultatiPerSingoloSchema) {

		boolean distribuzionePresente = false;

		for (DeleteStatement ds : main.getDeleteStatement()) {
			for (DistributionResultsDTO distributionResultsDTO : listaRisultatiPerSingoloSchema) {
				if (ds.getCodice().equalsIgnoreCase(distributionResultsDTO.getTableName())) {
					distribuzionePresente = false;
					for (int i = 0; i < ds.getListaDistribuzione().size(); i++) {
						Distribuzione distribuzione = ds.getListaDistribuzione().get(i);
						if (distribuzione.getCodiceSchema().equalsIgnoreCase(distributionResultsDTO.getSchema())) {
							// distribuzione già presente nella lista
							distribuzionePresente = true;
							distribuzione.setContatoreRigheCancellate(distributionResultsDTO.getRowsDeleted());
							distribuzione.setContatoreRigheDistribuite(distributionResultsDTO.getRowsInserted());
							if (distributionResultsDTO.getInsertsForBackup() != null
									&& distributionResultsDTO.getInsertsForBackup().size() > 0) {
								distribuzione
										.setListaInsertGeneratePerBackup(distributionResultsDTO.getInsertsForBackup());
								distribuzione.setContatoreInsertGeneratePerBackup(
										distributionResultsDTO.getInsertsForBackup().size());
							}
						}
					}
					if (!distribuzionePresente) {

						Distribuzione distribuzione = new Distribuzione();
						distribuzione.setCodiceSchema(distributionResultsDTO.getSchema());
						distribuzione.setDataOraDistribuzione(new Date());
						distribuzione.setContatoreRigheDistribuite(distributionResultsDTO.getRowsInserted());
						distribuzione.setContatoreRigheCancellate(distributionResultsDTO.getRowsDeleted());

						if (distributionResultsDTO.getInsertsForBackup() != null
								&& distributionResultsDTO.getInsertsForBackup().size() > 0) {
							distribuzione.setListaInsertGeneratePerBackup(distributionResultsDTO.getInsertsForBackup());
							distribuzione.setContatoreInsertGeneratePerBackup(
									distributionResultsDTO.getInsertsForBackup().size());
						}
						ds.getListaDistribuzione().add(distribuzione);
					}
				}
			}
		}
	}

	private void manageDeleteStatementsForRipristino() {

		ArrayList<QueryDB> listaUpdateDB = new ArrayList<QueryDB>();

		for (int i = 0; i < distribuzioneRipristinabile.getElencoSchemi().size(); i++) {
			SchemaDTO schemaDTO = searchSchemaDTO(distribuzioneRipristinabile.getElencoSchemi().get(i).getCodice());
			if (schemaDTO != null) {
				// PREDISPOSIZIONE DELLA LISTA DELLE QUERY DA ESEGUIRE SULLO STESSO SCHEMA
				listaUpdateDB.clear();
				for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
					// STATEMENT DI DELETE
					listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(), deleteStatement.getDeleteStatement(),
							Constants.DELETE, null, null));
					for (Distribuzione distribuzione : deleteStatement.getListaDistribuzione()) {
						if (distribuzione.getCodiceSchema().equalsIgnoreCase(schemaDTO.getSchemaUserName())
								&& distribuzione.getListaInsertGeneratePerBackup() != null) {
							// STATEMENT DI INSERT per il Ripristino
							for (InsertStatement insertDiBackup : distribuzione.getListaInsertGeneratePerBackup()) {
								listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(),
										insertDiBackup.getInsertStatement(), Constants.INSERT, null, null));
							}
						}
					}
				}
				if (listaUpdateDB.size() > 0) {
					// ESECUZIONE DELLE DELETE E DELLE INSERT
					model.runMultipleUpdateForRipristino(schemaDTO, listaUpdateDB);
				}
			}
		}
	}

	private void manageDeleteStatementsForRipristinoVoce(Voce voce) {

		ArrayList<QueryDB> listaUpdateDB = new ArrayList<QueryDB>();

		for (int i = 0; i < distribuzioneRipristinabile.getElencoSchemi().size(); i++) {
			SchemaDTO schemaDTO = searchSchemaDTO(distribuzioneRipristinabile.getElencoSchemi().get(i).getCodice());
			if (schemaDTO != null) {
				// PREDISPOSIZIONE DELLA LISTA DELLE QUERY DA ESEGUIRE SULLO STESSO SCHEMA
				listaUpdateDB.clear();
				for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
					// STATEMENT DI DELETE DELLA SINGOLA VOCE
					listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(),
							creaDeleteTableSuSingolaVoce(deleteStatement.getCodice(), voce), Constants.DELETE, null,
							null));
					for (Distribuzione distribuzione : deleteStatement.getListaDistribuzione()) {
						if (distribuzione.getCodiceSchema().equalsIgnoreCase(schemaDTO.getSchemaUserName())
								&& distribuzione.getListaInsertGeneratePerBackup() != null) {
							// STATEMENT DI INSERT per il Ripristino DELLA SINGOLA VOCE
							for (InsertStatement insertDiBackup : distribuzione.getListaInsertGeneratePerBackup()) {
								if (voce.getCodice().equalsIgnoreCase(insertDiBackup.getVoce())) {
									listaUpdateDB.add(new QueryDB(deleteStatement.getCodice(),
											insertDiBackup.getInsertStatement(), Constants.INSERT, null, null));
								}
							}
						}
					}
				}
				if (listaUpdateDB.size() > 0) {
					// ESECUZIONE DELLE DELETE E DELLE INSERT
					model.runMultipleUpdateForRipristino(schemaDTO, listaUpdateDB);
				}
			}
		}
	}

	private String creaDeleteTableSuSingolaVoce(String tableName, Voce voce) {

		String deleteString = "";
		String whereCondition = "";
		deleteString = Constants.PREFIX_DELETE;

		deleteString += tableName;

		if (tableName.toUpperCase().contains(Constants.ESPADE) || tableName.contains(Constants.ESPAFM)) {
			whereCondition = Constants.PREFIX_WHERE_CONDITION_WITH_CDTPFORM;
		} else {
			whereCondition = Constants.PREFIX_WHERE_CONDITION;
		}

		whereCondition += voce.getCodice() + "')";

		return deleteString + whereCondition;
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
				main.getStagePrincipale());

		bntDistribuzioneVoci.setDisable(false);

	}

	private void showAlertRipristinoOK() {

		String contenuto = "";

		showAlert(AlertType.INFORMATION, "Information", "Ripristino Distribuzione concluso regolarmente", contenuto,
				main.getStagePrincipale());

		bntRipristinoDistribuzioneVoci.setDisable(true);

	}

	private void showAlertRipristinoVoceOK(String voce) {

		String contenuto = "";

		showAlert(AlertType.INFORMATION, "Information", "Voce " + voce + " ripristinata", contenuto,
				main.getStagePrincipale());

	}

	private void showAlertDistribuzioneOK(StoricoDistribuzione storicoDistribuzione) {

		String contenuto = "";

		contenuto += "SCHEMA DI PARTENZA: " + storicoDistribuzione.getSchemaPartenza() + "\n";

		// ciclo per elencare le Voci distribuite
		for (int i = 0; i < storicoDistribuzione.getElencoVoci().size(); i++) {
			Voce voce = storicoDistribuzione.getElencoVoci().get(i);
			if (i == 0) {
				if (storicoDistribuzione.getElencoVoci().size() == 1) {
					contenuto += "VOCE: ";
				} else {
					contenuto += "VOCI: ";
				}
			}

			contenuto += voce.getCodice();
			if (i == storicoDistribuzione.getElencoVoci().size() - 1) {
				contenuto += "\n";
			} else {
				contenuto += ", ";
			}
		}
		int contatoreTotInsertGeneratePerBackup = 0;
		int contatoreTotRigheCancellate = 0;
		int contatoreTotRigheDistribuite = 0;
		// ciclo per elencare le Tabelle distribuite
		for (int i = 0; i < storicoDistribuzione.getListaDeleteStatement().size(); i++) {
			String tabella = storicoDistribuzione.getListaDeleteStatement().get(i).getCodice();
			for (Distribuzione distribuzione : storicoDistribuzione.getListaDeleteStatement().get(i)
					.getListaDistribuzione()) {
				contatoreTotInsertGeneratePerBackup += distribuzione.getContatoreInsertGeneratePerBackup();
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
		contenuto += "NR. DI SCHEMI DI ARRIVO :  " + storicoDistribuzione.getElencoSchemi().size() + "\n";
		contenuto += "TOTALE INSERT GENERATE PER RIPRISTINO :  " + contatoreTotInsertGeneratePerBackup + "\n";
		contenuto += "TOTALE RIGHE CANCELLATE :  " + contatoreTotRigheCancellate + "\n";
		contenuto += "TOTALE RIGHE DISTRIBUITE :  " + contatoreTotRigheDistribuite + "\n";

		showAlert(AlertType.INFORMATION, "Information", "Distribuzione conclusa regolarmente", contenuto,
				main.getStagePrincipale());

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
			ObservableList<Schema> listaSchemiSelezionati = schemiTable.getSelectionModel().getSelectedItems();
			ObservableList<Tabella> listaTabelleSelezionate = tabelledbTable.getSelectionModel().getSelectedItems();
			if (estrazioneColonneTabelleOrigine(listaTabelleSelezionate)) {
				return isSchemiTabelleOK(listaSchemiSelezionati, listaTabelleSelezionate);
			} else {
				return false;
			}
		} else {
			showAlert(AlertType.ERROR, "Campi non validi",
					"Per cortesia, imposta i Parametri necessari per l'Anteprima di Distribuzione", errorMessage,
					main.getStagePrincipale());
			return false;
		}
	}

	private boolean estrazioneColonneTabelleOrigine(ObservableList<Tabella> listaTabelleSelezionate) {

		ArrayList<QueryDB> listaQueryDB = new ArrayList<QueryDB>();

		for (int i = 0; i < listaTabelleSelezionate.size(); i++) {

			String tableName = listaTabelleSelezionate.get(i).getCodice().toUpperCase();

			String selectStatement = Constants.SELECT_COUNT_ALL_OBJECTS_WHERE + "'" + tableName + "'";

			QueryDB queryDB = new QueryDB();
			queryDB.setTableName(tableName);
			queryDB.setQuery(selectStatement);
			queryDB.setOperationType(Constants.GET_INFO_COLUMNS);

			listaQueryDB.add(queryDB);

		}

		EsitoTestConnessioniPresenzaTabelle esitoTestConnessioniPresenzaTabelle = model
				.runQueryForGetInfoColumnsOfTables(main.getSchemaDtoOrigine(), listaQueryDB);

		if (!esitoTestConnessioniPresenzaTabelle.isEsitoGlobale()
				&& "".equalsIgnoreCase(esitoTestConnessioniPresenzaTabelle.getCausaEsitoKO())) {

			showAlert(AlertType.ERROR, "Error", "",
					"Non riuscita connessione allo schema " + main.getSchemaDtoOrigine().getSchemaUserName(),
					main.getStagePrincipale());
			return false;
		}
		if (!esitoTestConnessioniPresenzaTabelle.isEsitoGlobale()) {

			showAlert(AlertType.ERROR, "Error", "", esitoTestConnessioniPresenzaTabelle.getCausaEsitoKO(),
					main.getStagePrincipale());
			return false;
		}

		listTablesColumnsType = esitoTestConnessioniPresenzaTabelle.getListTablesColumnsType();

		return true;

	}

	private boolean isInputValidForAnteprimaRipristino() {

		if (Constants.PARZIALE.equalsIgnoreCase(distribuzioneRipristinabile.getDataOraRipristino())) {
			showAlert(AlertType.WARNING, "Attenzione Distribuzione Parzialmente Ripristinata",
					"Distribuzione del " + distribuzioneRipristinabile.getDataOraDistribuzione()
							+ " Parzialmente Ripristinata",

					"Utilizzare il Ripristino Voce", main.getStagePrincipale());
			return false;
		}

		main.getSchemiRipristino().clear();

		for (Schema schema : distribuzioneRipristinabile.getElencoSchemi()) {
			main.addSchemiRipristinoData(schema);
		}

		ObservableList<Tabella> listaTabelleDaRipristinare = FXCollections.observableArrayList();

		listTablesColumnsType.clear();
		for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
			listaTabelleDaRipristinare.add(new Tabella(deleteStatement.getCodice(), null));
			listTablesColumnsType.put(deleteStatement.getCodice().toUpperCase(), deleteStatement.getColumnsType());
		}

		if (main.getSchemiRipristino() != null && main.getSchemiRipristino().size() > 0) {
			return isSchemiTabelleOK(main.getSchemiRipristino(), listaTabelleDaRipristinare);
		} else {
			showAlert(AlertType.ERROR, "Error", "",
					"Non trovati gli schemi relativi alla Distribuzione da Ripristinare ", main.getStagePrincipale());
			return false;
		}
	}

	private boolean isSchemiTabelleOK(ObservableList<Schema> listaSchemi, ObservableList<Tabella> listaTabelle) {

		ArrayList<QueryDB> listaQueryDB = new ArrayList<QueryDB>();

		for (int i = 0; i < listaTabelle.size(); i++) {

			String tableName = listaTabelle.get(i).getCodice().toUpperCase();
			String selectStatement = Constants.SELECT_COUNT_ALL_OBJECTS_WHERE + "'" + tableName + "'";

			QueryDB queryDB = new QueryDB();
			queryDB.setTableName(tableName);
			queryDB.setQuery(selectStatement);
			queryDB.setOperationType(Constants.SELECT);

			queryDB.setColumnsType(listTablesColumnsType.get(tableName));
			listaQueryDB.add(queryDB);
		}

		// lancio su apposito Task Thread del check sulle connessioni e le tabelle
		try {
			copyWorkerForCheckConnessioniTabelle = createcopyWorkerForCheckConnessioniTabelle(listaSchemi,
					listaQueryDB);
			loadingDialog.activateProgressBar(copyWorkerForCheckConnessioniTabelle);
			copyWorkerForCheckConnessioniTabelle.setOnSucceeded(event -> {
				loadingDialog.getDialogStage().close();
			});
			copyWorkerForCheckConnessioniTabelle.setOnCancelled(event -> {
				loadingDialog.getDialogStage().close();
			});
			loadingDialog.getDialogStage().show();
		} catch (InterruptedException e) {
			System.out.println("eccezione su createcopyWorkerForCheckConnessioniTabelle: " + e.toString());
		}

		copyWorkerForCheckConnessioniTabelle.setOnFailed(e -> {
			esitoTestConnessioniPresenzaTabellePerTask.setEsitoGlobale(false);
			loadingDialog.getDialogStage().close();
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				logger.error(
						"metodo isSchemiTabelleOK Task copyWorkerForCheckConnessioniTabelle - setOnFailed - no eccezione");
				copyWorkerForCheckConnessioniTabelle.cancel(true);
				disabledView(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), main.getStagePrincipale());
			} else {
				logger.error(
						"metodo isSchemiTabelleOK Task copyWorkerForCheckConnessioniTabelle - setOnFailed - eccezione = "
								+ exception.toString());

			}
		});

		copyWorkerForCheckConnessioniTabelle.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// System.out.println("newValue " + newValue);
				logger.info("newValue " + newValue);
			}
		});

		Thread backgroundThreadCheckConnessioniTabelle = new Thread(copyWorkerForCheckConnessioniTabelle,
				"checkConnessioniTabelle-thread");
		backgroundThreadCheckConnessioniTabelle.setDaemon(true);
		backgroundThreadCheckConnessioniTabelle.start();

		return esitoTestConnessioniPresenzaTabellePerTask.isEsitoGlobale();

	}

	private Task createcopyWorkerForCheckConnessioniTabelle(ObservableList<Schema> listaSchemi,
			ArrayList<QueryDB> listaQueryDB) {

		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					loadingDialog.getDialogStage().close();
					logger.error("Canceling of createcopyWorkerForCheckConnessioniTabelle...");
				}
				esitoTestConnessioniPresenzaTabellePerTask.setEsitoGlobale(true);
				for (Schema s : listaSchemi) {
					SchemaDTO schemaDTO = searchSchemaDTO(s.getCodice());
					if (schemaDTO != null) {

						EsitoTestConnessioniPresenzaTabelle esitoTestConnessioniPresenzaTabelle = model
								.testConnessionePresenzaTabelle(schemaDTO, listaQueryDB);

						if (!esitoTestConnessioniPresenzaTabelle.isEsitoGlobale()
								&& "".equalsIgnoreCase(esitoTestConnessioniPresenzaTabelle.getCausaEsitoKO())) {

							esitoTestConnessioniPresenzaTabellePerTask.setEsitoGlobale(false);
							esitoTestConnessioniPresenzaTabellePerTask
									.setCausaEsitoKO("Non riuscita connessione allo schema " + s.getCodice());
						}
						if (!esitoTestConnessioniPresenzaTabelle.isEsitoGlobale()) {

							esitoTestConnessioniPresenzaTabellePerTask.setEsitoGlobale(false);
							esitoTestConnessioniPresenzaTabellePerTask
									.setCausaEsitoKO(esitoTestConnessioniPresenzaTabelle.getCausaEsitoKO());
						}
					} else {
						esitoTestConnessioniPresenzaTabellePerTask.setEsitoGlobale(false);
						esitoTestConnessioniPresenzaTabellePerTask.setCausaEsitoKO(
								"Non trovati dati di connessione relativi allo schema " + s.getCodice());
					}
				}

				return true;

			}

			@Override
			protected void succeeded() {

				logger.info("metodo createcopyWorkerForCheckConnessioniTabelle - succeeded");
				super.succeeded();
				updateMessage("Done!");
				loadingDialog.getDialogStage().close();
				disabledView(false);
				if (!esitoTestConnessioniPresenzaTabellePerTask.isEsitoGlobale()) {
					showAlert(AlertType.ERROR, "Error", "",
							esitoTestConnessioniPresenzaTabellePerTask.getCausaEsitoKO(), main.getStagePrincipale());
				} else {
					if (manageAnteprimaDistribuzione) {
						try {
							VboxVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
							bar.setVisible(true);
							anteprimaDistribuzione();
							disabledView(false);
							// mainAnchorPane.setVisible(true);
						} catch (Exception e) {
							VboxNonVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
							showAlert(AlertType.ERROR, "Error", "", e.toString(), main.getStagePrincipale());
							disabledView(false);
							// mainAnchorPane.setVisible(true);
						}
					}
					if (manageAnteprimaRipristino) {
						try {
							VboxVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO);
							barRipristino.setVisible(false);
							newlistaDeleteForRipristino.clear();
							textAreaRipristinoInsert.setText("");
							anteprimaRipristino();
						} catch (Exception e) {
							VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO);
							showAlert(AlertType.ERROR, "Error", "", e.toString(), main.getStagePrincipale());
						}
						;
					}
					if (manageAnteprimaRipristinoVoce) {
						main.showListaVociRipristinabiliDialog(distribuzioneRipristinabile,
								Constants.VOCI_PER_RIPRISTINO);
						if (main.getVoceDaRipristinare() != null) {
							try {
								VboxVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO_VOCE);
								barRipristinoVoce.setVisible(false);
								newlistaDeleteForRipristino.clear();
								textAreaRipristinoVoceInsert.setText("");
								anteprimaRipristinoVoce();
							} catch (Exception e) {
								VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO_VOCE);
								showAlert(AlertType.ERROR, "Error", "", e.toString(), main.getStagePrincipale());
							}
							;
						}
					}
				}
			}
		};
	}

	private void anteprimaDistribuzione() {

		// System.out.println("OverviewDistriVociController metodo
		// anteprimaDistribuzione");

		textAreaPreviewInsert.setText("");

		labelAnteprimaInsert.setText(Constants.INSERT_LABEL_CONTENTS + main.getSchemaDtoOrigine().getSchemaUserName());

		disabledView(true);

		bar.setProgress(0);

		copyWorkerForGenInserts = createWorkerForGenInserts();

		try {
			loadingDialog.activateProgressBar(copyWorkerForGenInserts);
			copyWorkerForGenInserts.setOnSucceeded(event -> {
				loadingDialog.getDialogStage().close();
			});
			copyWorkerForGenInserts.setOnCancelled(event -> {
				loadingDialog.getDialogStage().close();
			});
			loadingDialog.getDialogStage().show();
		} catch (InterruptedException e) {
			System.out.println("eccezione sul try di BackgroundTask: " + e.toString());
		}

		bar.progressProperty().unbind();
		bar.progressProperty().bind(copyWorkerForGenInserts.progressProperty());

		copyWorkerForGenInserts.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// System.out.println("newValue " + newValue);
				logger.info("newValue " + newValue);
			}
		});

		Thread backgroundThread = new Thread(copyWorkerForGenInserts, "queryDB-anteprima-thread");
		backgroundThread.setDaemon(true);
		backgroundThread.start();

		copyWorkerForGenInserts.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				// System.out.println("OverviewDistriVociController task copyWorkerForGenInserts
				// - setOnFailed - no eccezione" );
				logger.error("task copyWorkerForGenInserts - setOnFailed - no eccezione");
				copyWorkerForGenInserts.cancel(true);
				bar.progressProperty().unbind();
				bar.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), main.getStagePrincipale());
			} else {
				loadingDialog.getDialogStage().close();
				// System.out.println("OverviewDistriVociController task copyWorkerForGenInserts
				// - setOnFailed - eccezione = " + exception.toString());
				logger.error("task copyWorkerForGenInserts - setOnFailed - eccezione = " + exception.toString());
			}
		});

	}

	/**
	 * chiamata quando l'utente fa click sul bottone Distribuzione Voci
	 */
	@FXML
	private void distribuzione() {

		// System.out.println("OverviewDistriVociController metodo distribuzione");

		if (!checkNoteAndConfirmForDistribuzione()) {
			return;
		}

		disabledView(true);
		bar.setVisible(true);

		bar.setProgress(0);

		try {
			copyWorkerForExecuteDistribution = createWorkerForExecuteDistribution();
			loadingDialog.activateProgressBar(copyWorkerForExecuteDistribution);
			copyWorkerForExecuteDistribution.setOnSucceeded(event -> {
				loadingDialog.getDialogStage().close();
			});
			copyWorkerForExecuteDistribution.setOnCancelled(event -> {
				loadingDialog.getDialogStage().close();
			});
			loadingDialog.getDialogStage().show();
		} catch (InterruptedException e) {
			System.out.println("eccezione sul try di BackgroundTask: " + e.toString());
		}

		labelInfoEsecuzione.textProperty().unbind();
		labelInfoEsecuzione.textProperty().bind(copyWorkerForExecuteDistribution.messageProperty());

		bar.progressProperty().unbind();
		bar.progressProperty().bind(copyWorkerForExecuteDistribution.progressProperty());

		copyWorkerForExecuteDistribution.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// System.out.println("newValue " + newValue);
				logger.info("newValue " + newValue);
			}
		});

		Thread backgroundThreadUpdateDB = new Thread(copyWorkerForExecuteDistribution, "updateDataBase-thread");
		backgroundThreadUpdateDB.setDaemon(true);
		backgroundThreadUpdateDB.start();

		copyWorkerForExecuteDistribution.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				// System.out.println("OverviewDistriVociController metodo
				// createWorkerForExecuteDistribution - setOnFailed - no eccezione");
				logger.error("metodo createWorkerForExecuteDistribution - setOnFailed - no eccezione");
				copyWorkerForExecuteDistribution.cancel(true);
				bar.progressProperty().unbind();
				bar.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				bar.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), main.getStagePrincipale());
			} else {
				loadingDialog.getDialogStage().close();
				// System.out.println("OverviewDistriVociController metodo
				// createWorkerForExecuteDistribution - setOnFailed - eccezione = " +
				// exception.toString());
				logger.error("metodo createWorkerForExecuteDistribution - setOnFailed - eccezione = "
						+ exception.toString());

			}
		});

	}

	private boolean checkNoteAndConfirmForDistribuzione() {

		TextInputDialog dialog = new TextInputDialog();
		dialog.setTitle("Conferma Distribuzione");
		dialog.setHeaderText("Per cortesia, imposta una nota informativa relativa alla distribuzione");
		dialog.setContentText("Nota informativa:");
		dialog.initOwner(main.getStagePrincipale());
		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
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

		// System.out.println("OverviewDistriVociController metodo
		// createWorkerForGenInserts");

		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					// System.out.println("Canceling...");
					logger.error("Canceling of createWorkerForGenInserts...");

				}

				fillAnteprimaDistribuzione();

				return true;
			}

			@Override
			protected void succeeded() {
				// System.out.println("OverviewDistriVociController metodo
				// createWorkerForGenInserts - succeeded");
				logger.info("metodo createWorkerForGenInserts - succeeded");
				super.succeeded();
				updateMessage("Done!");
				bar.progressProperty().unbind();
				bar.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				bar.setVisible(false);
				showAlerAnteprimaDistribuzioneOK();
			}
		};
	}

	public Task createWorkerForExecuteDistribution() {

		// System.out.println("OverviewDistriVociController metodo
		// createWorkerForExecuteDistribution");

		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					// System.out.println("Canceling...");
					logger.error("Canceling of createWorkerForExecuteDistribution...");
				}

				listaSchemiPerStoricoDistribuzione.clear();

				manageDeleteStatementsForDistribution(main.getDeleteStatement());

				deleteStatementTable.setItems(main.getDeleteStatement());

				return true;
			}

			@Override
			protected void succeeded() {

				logger.info("metodo createWorkerForExecuteDistribution - succeeded");
				super.succeeded();
				updateMessage("Done!");
				bar.progressProperty().unbind();
				bar.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_DISTRIBUZIONE);
				showAlertDistribuzioneOK(aggiornaStoricoDistribuzione());
				clearDistributionInfo();
				mainTree.getSelectionModel().select(1);
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
		for (DeleteStatement deletestatement : main.getDeleteStatement()) {
			listaDeleteStatement.add(deletestatement);
			schemaPartenza = deletestatement.getCodiceSchemaOrigine();
		}

		ArrayList<Schema> elencoSchemi = new ArrayList<Schema>();
		for (int i = 0; i < listaSchemiPerStoricoDistribuzione.size(); i++) {
			elencoSchemi.add(new Schema(listaSchemiPerStoricoDistribuzione.get(i).getCodice(),
					listaSchemiPerStoricoDistribuzione.get(i).getDescrizione()));
		}

		storicoDistribuzione.setElencoSchemi(elencoSchemi);

		ObservableList<Voce> listaVociSelezionate = vociTable.getSelectionModel().getSelectedItems();
		ArrayList<Voce> elencoVoci = new ArrayList<Voce>();
		for (Voce voce : listaVociSelezionate) {
			Voce vc = new Voce();
			vc.setCodice(voce.getCodice());
			vc.setDataOraRipristino(voce.getDataOraRipristino());
			vc.setDescrizione(voce.getDescrizione());
			elencoVoci.add(vc);
		}
		storicoDistribuzione.setElencoVoci(elencoVoci);

		storicoDistribuzione.setListaDeleteStatement(listaDeleteStatement);
		storicoDistribuzione.setNote(this.nota);
		storicoDistribuzione.setSchemaPartenza(main.getSchemaDtoOrigine().getSchemaUserName());

		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss");

		storicoDistribuzione.setDataOraDistribuzione(FOMATTER.format(ldt));

		main.addStoricoDistribuzione(storicoDistribuzione);
		storicoDistribuzioneTable.setItems(main.getStoricoDistribuzione());

		sequenceTabStoricoDistribuzioneColumn.setSortType(TableColumn.SortType.DESCENDING);

		storicoDistribuzioneTable.getSortOrder().add(sequenceTabStoricoDistribuzioneColumn);

		storicoDistribuzioneTable.sort();

		impostaDistribuzioneRipristinabile();

		return storicoDistribuzione;
	}

	private void clearDistributionInfo() {

		// pulizia delle selezioni sui parametri (schemi di partenza e arrivo, tabelle,
		// voci)
		clearParameterSelection();

		// pulizia della lista degli statement di delete
		main.getDeleteStatement().clear();
		deleteStatementTable.setItems(main.getDeleteStatement());
		textAreaPreviewInsert.setText("");
	}

	private void clearRipristinoInfo() {

		// pulizia delle selezioni sui parametri (schemi di partenza e arrivo, tabelle,
		// voci)
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
	 * chiamata quando l'utente fa click sul bottone RIPRISTINO DELLA Distribuzione
	 * Voci
	 */
	@FXML
	private void ripristino() {

		// System.out.println("OverviewDistriVociController metodo ripristino");

		if (!checkConfirmOfRipristino(null)) {
			return;
		}

		disabledView(true);
		barRipristino.setVisible(true);

		barRipristino.setProgress(0);

		main.getDeleteStatementForRipristino().clear();

		for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
			main.addDeleteStatementForRipristino(deleteStatement);
		}

		try {
			copyWorkerForExecuteRipristino = createWorkerForExecuteRipristino();
			loadingDialog.activateProgressBar(copyWorkerForExecuteRipristino);
			copyWorkerForExecuteRipristino.setOnSucceeded(event -> {
				loadingDialog.getDialogStage().close();
			});
			copyWorkerForExecuteRipristino.setOnCancelled(event -> {
				loadingDialog.getDialogStage().close();
			});
			loadingDialog.getDialogStage().show();
		} catch (InterruptedException e) {
			System.out.println("eccezione sul try di BackgroundTask: " + e.toString());
		}

		barRipristino.progressProperty().unbind();
		barRipristino.progressProperty().bind(copyWorkerForExecuteRipristino.progressProperty());

		copyWorkerForExecuteRipristino.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// System.out.println("newValue " + newValue);
				logger.info("newValue " + newValue);
			}
		});

		Thread backgroundThreadUpdateDBripristino = new Thread(copyWorkerForExecuteRipristino,
				"updateDBripristino-thread");
		backgroundThreadUpdateDBripristino.setDaemon(true);
		backgroundThreadUpdateDBripristino.start();

		copyWorkerForExecuteRipristino.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				// System.out.println("OverviewDistriVociController metodo
				// copyWorkerForExecuteRipristino - setOnFailed - no eccezione");
				logger.error("metodo copyWorkerForExecuteRipristino - setOnFailed - no eccezione");
				copyWorkerForExecuteRipristino.cancel(true);
				barRipristino.progressProperty().unbind();
				barRipristino.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				barRipristino.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), main.getStagePrincipale());
			} else {
				loadingDialog.getDialogStage().close();
				logger.error(
						"metodo copyWorkerForExecuteRipristino - setOnFailed - eccezione = " + exception.toString());
				// System.out.println("OverviewDistriVociController metodo
				// copyWorkerForExecuteRipristino - setOnFailed - eccezione = " +
				// exception.toString());
			}
		});

	}

	private boolean checkConfirmOfRipristino(Voce voce) {

		String voceDaRipristinare = "";

		if (voce != null)
			voceDaRipristinare = " della Voce " + voce.getCodice();

		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Conferma Ripristino" + voceDaRipristinare);
		alert.setHeaderText("Conferma Ripristino" + voceDaRipristinare);
		alert.setContentText("Confermi il Ripristino" + voceDaRipristinare + "?");
		alert.initOwner(main.getStagePrincipale());

		Optional<ButtonType> result = alert.showAndWait();

		if (result.get() == ButtonType.OK) {
			return true;
		} else {
			return false;
		}
	}

	public Task createWorkerForExecuteRipristino() {

		// System.out.println("OverviewDistriVociController metodo
		// createWorkerForExecuteRipristino");

		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					// System.out.println("Canceling...");
					logger.error("Canceling for createWorkerForExecuteRipristino...");
				}

				manageDeleteStatementsForRipristino();

				newlistaDeleteForRipristino.clear();
				main.getDeleteStatementForRipristino().clear();
				return true;
			}

			@Override
			protected void succeeded() {

				// System.out.println("OverviewDistriVociController metodo
				// createWorkerForExecuteRipristino - succeeded");
				logger.info("metodo createWorkerForExecuteRipristino - succeeded");
				super.succeeded();
				updateMessage("Done!");
				barRipristino.progressProperty().unbind();
				barRipristino.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				barRipristino.setVisible(false);
				textAreaRipristinoInsert.setText("");
				main.getSchemiRipristino().clear();
				aggiornaStoricoDistribuzioneRipristinata();
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO);
				showAlertRipristinoOK();
				clearRipristinoInfo();
				mainTree.getSelectionModel().select(1);
			}
		};
	}

	private void aggiornaStoricoDistribuzioneRipristinata() {

		LocalDateTime ldt = LocalDateTime.now();
		DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss");
		this.main.getStoricoDistribuzione().get(indiceDistribuzioneRipristinabile)
				.setDataOraRipristino(FOMATTER.format(ldt));

		impostaDistribuzioneRipristinabile();
	}

	private void aggiornaStoricoDistribuzioneRipristinataParzialmente(Voce voceInput) {

		boolean parziale = false;

		for (Voce voce : this.main.getStoricoDistribuzione().get(indiceDistribuzioneRipristinabile).getElencoVoci()) {
			if (voceInput.getCodice().equalsIgnoreCase(voce.getCodice())) {
				LocalDateTime ldt = LocalDateTime.now();
				DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy kk:mm:ss");
				voce.setDataOraRipristino(FOMATTER.format(ldt));
			} else {
				if (voce.getDataOraRipristino() == null) {
					parziale = true;
				}
			}
		}

		if (parziale) {
			this.main.getStoricoDistribuzione().get(indiceDistribuzioneRipristinabile)
					.setDataOraRipristino(Constants.PARZIALE);
		} else {
			aggiornaStoricoDistribuzioneRipristinata();
		}
	}

	/**
	 * chiamata quando l'utente fa click sul bottone RIPRISTINO DELLA Distribuzione
	 * Voce
	 */
	@FXML
	private void ripristinoVoce() {

		// System.out.println("OverviewDistriVociController metodo ripristinoVoce");

		if (!checkConfirmOfRipristino(main.getVoceDaRipristinare())) {
			return;
		}

		disabledView(true);
		barRipristinoVoce.setVisible(true);

		barRipristinoVoce.setProgress(0);

		main.getDeleteStatementForRipristino().clear();

		for (DeleteStatement deleteStatement : distribuzioneRipristinabile.getListaDeleteStatement()) {
			main.addDeleteStatementForRipristino(deleteStatement);
		}

		try {
			copyWorkerForExecuteRipristinoVoce = createWorkerForExecuteRipristinoVoce(main.getVoceDaRipristinare());
			loadingDialog.activateProgressBar(copyWorkerForExecuteRipristinoVoce);
			copyWorkerForExecuteRipristinoVoce.setOnSucceeded(event -> {
				loadingDialog.getDialogStage().close();
			});
			copyWorkerForExecuteRipristinoVoce.setOnCancelled(event -> {
				loadingDialog.getDialogStage().close();
			});
			loadingDialog.getDialogStage().show();
		} catch (InterruptedException e) {
			System.out.println("eccezione sul try di BackgroundTask: " + e.toString());
		}

		barRipristinoVoce.progressProperty().unbind();
		barRipristinoVoce.progressProperty().bind(copyWorkerForExecuteRipristinoVoce.progressProperty());

		copyWorkerForExecuteRipristinoVoce.messageProperty().addListener(new ChangeListener<String>() {
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				// System.out.println("newValue " + newValue);
				logger.info("newValue " + newValue);
			}
		});

		Thread backgroundThreadUpdateDBripristinoVoce = new Thread(copyWorkerForExecuteRipristinoVoce,
				"updateDBripristinoVoce-thread");
		backgroundThreadUpdateDBripristinoVoce.setDaemon(true);
		backgroundThreadUpdateDBripristinoVoce.start();

		copyWorkerForExecuteRipristinoVoce.setOnFailed(e -> {
			Throwable exception = ((Task) e.getSource()).getException();
			if (exception != null) {
				// System.out.println("OverviewDistriVociController metodo
				// copyWorkerForExecuteRipristinoVoce - setOnFailed - no eccezione");
				logger.error("metodo copyWorkerForExecuteRipristinoVoce - setOnFailed - no eccezione");
				copyWorkerForExecuteRipristinoVoce.cancel(true);
				barRipristinoVoce.progressProperty().unbind();
				barRipristinoVoce.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				barRipristinoVoce.setVisible(false);
				showAlert(AlertType.ERROR, "Error", "", "Errore " + exception.toString(), main.getStagePrincipale());
			} else {
				loadingDialog.getDialogStage().close();
				logger.error("metodo copyWorkerForExecuteRipristinoVoce - setOnFailed - eccezione = "
						+ exception.toString());
				// System.out.println("OverviewDistriVociController metodo
				// copyWorkerForExecuteRipristinoVoce - setOnFailed - eccezione = " +
				// exception.toString());
			}
		});

	}

	public Task createWorkerForExecuteRipristinoVoce(Voce voce) {

		// System.out.println("OverviewDistriVociController metodo
		// createWorkerForExecuteRipristinoVoce");

		return new Task() {
			@Override
			protected Object call() throws Exception {

				if (this.isCancelled()) {
					// System.out.println("Canceling...");
					loadingDialog.getDialogStage().close();
					logger.error("Canceling for createWorkerForExecuteRipristinoVoce...");
				}

				manageDeleteStatementsForRipristinoVoce(voce);

				newlistaDeleteForRipristino.clear();
				main.getDeleteStatementForRipristino().clear();
				return true;
			}

			@Override
			protected void succeeded() {

				// System.out.println("OverviewDistriVociController metodo
				// createWorkerForExecuteRipristinoVoce - succeeded");
				logger.info("metodo createWorkerForExecuteRipristinoVoce - succeeded");
				super.succeeded();
				updateMessage("Done!");
				barRipristinoVoce.progressProperty().unbind();
				barRipristinoVoce.setProgress(0);
				loadingDialog.getDialogStage().close();
				disabledView(false);
				VboxNonVisibile(Constants.BOX_ANTEPRIMA_RIPRISTINO_VOCE);
				main.getSchemiRipristinoVoce().clear();
				aggiornaStoricoDistribuzioneRipristinataParzialmente(voce);
				showAlertRipristinoVoceOK(voce.getCodice());
				clearRipristinoInfo();
				mainTree.getSelectionModel().select(1);
			}
		};
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
