package it.infocamere.sipert.distrivoci.view;

import java.util.List;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Distribuzione;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DettaglioStoricoDistribuzioneDialogController {

    @FXML // fx:id="labelTitle"
    private Label labelTitle; // Value injected by FXMLLoader
    
    @FXML // fx:id="labelTitle2"
    private Label labelTitle2; // Value injected by FXMLLoader

    @FXML // fx:id="filterField"
    private TextField filterField; // Value injected by FXMLLoader

    @FXML // fx:id="schemiTable"
    private TableView<Schema> schemiTable; // Value injected by FXMLLoader

    @FXML // fx:id="codiceSchemaColumn"
    private TableColumn<Schema, String> codiceSchemaColumn; // Value injected by FXMLLoader

    @FXML // fx:id="descrizioneSchemaColumn"
    private TableColumn<Schema, String> descrizioneSchemaColumn; // Value injected by FXMLLoader

    @FXML // fx:id="deleteStatementTable"
    private TableView<DeleteStatement> deleteStatementTable; // Value injected by FXMLLoader

	@FXML // fx:id="codiceTabDeleteColumn"
	private TableColumn<DeleteStatement, String> codiceTabDeleteColumn;
    
    @FXML // fx:id="statementDeleteColumn"
    private TableColumn<DeleteStatement, String> statementDeleteColumn; // Value injected by FXMLLoader

    @FXML // fx:id="textAreaPreviewInsert"
    private TextArea textAreaPreviewInsert; // Value injected by FXMLLoader
    
    @FXML // fx:id="textAreaPreviewInsertBckup"
    private TextArea textAreaPreviewInsertBckup; // Value injected by FXMLLoader

    @FXML
    private Label labelInsertRipristino;
    
    @FXML
    private Label labelInsert;
    
    @FXML // fx:id="bntHelp"
    private Button bntHelp; // Value injected by FXMLLoader
    
    @FXML // fx:id="bntStatistiche"
    private Button bntStatistiche; // Value injected by FXMLLoader
    
    @FXML // fx:id="bntExit"
    private Button bntExit; // Value injected by FXMLLoader
    
    /**
     * i dati nel formato di observable list di Schema 
     */
    private ObservableList<Schema> schemi = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list degli statement di Delete 
     */
    private ObservableList<DeleteStatement> elencoDelete = FXCollections.observableArrayList();
	
    private boolean exitClicked = false;
    private Stage dialogStage;
	private Main main;
	
	private StoricoDistribuzione storicoDistribuzione;
	
	private String codiceSchemaSelezionato;
	
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	
        assert labelTitle != null : "fx:id=\"labelTitle\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert labelTitle2 != null : "fx:id=\"labelTitle2\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        
        assert labelInsertRipristino != null : "fx:id=\"labelInsertRipristino\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert labelInsert != null : "fx:id=\"labelInsert\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        
        assert filterField != null : "fx:id=\"filterField\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert schemiTable != null : "fx:id=\"schemiTable\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert codiceSchemaColumn != null : "fx:id=\"codiceSchemaColumn\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert descrizioneSchemaColumn != null : "fx:id=\"descrizioneSchemaColumn\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert deleteStatementTable != null : "fx:id=\"deleteStatementTable\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert statementDeleteColumn != null : "fx:id=\"statementDeleteColumn\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert codiceTabDeleteColumn != null : "fx:id=\"codiceTabDeleteColumn\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert textAreaPreviewInsert != null : "fx:id=\"textAreaPreviewInsert\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert textAreaPreviewInsertBckup != null : "fx:id=\"textAreaPreviewInsertBckup\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        
        assert bntHelp != null : "fx:id=\"bntHelp\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert bntStatistiche != null : "fx:id=\"bntStatistiche\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert bntExit != null : "fx:id=\"bntExit\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";

		// Initializza la lista degli schemi con 2 colonne - codice e descrizione
		codiceSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		descrizioneSchemaColumn.setCellValueFactory(cellData -> cellData.getValue().descrizioneProperty()); 
        
		// Initializza la lista degli statement di delete (2 colonne - codice tabella e
		// relativo statement)
		codiceTabDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().codiceProperty());
		statementDeleteColumn.setCellValueFactory(cellData -> cellData.getValue().deleteStatementProperty());
		
		// Listener per la selezione del dettaglio dello statement di delete e dei
		// relativi statement di insert
		deleteStatementTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showInsertsDetails(newValue));
		
		// Listener per la selezione del dettaglio degli statement di delete 
		schemiTable.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDeletesDetails(newValue));
				
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
    public void setStoricoDistribuzione(StoricoDistribuzione storicoDistribuzione) {    	
        
        if (storicoDistribuzione != null) {
        	this.storicoDistribuzione = storicoDistribuzione;
        	String contenuto = "         DISTRIBUZIONE del " + this.storicoDistribuzione.getDataOraDistribuzione();
			if (this.storicoDistribuzione.getDataOraRipristino() != null) {
				contenuto += " RIPRISTINATA il " + this.storicoDistribuzione.getDataOraRipristino(); 
			}
			labelTitle.setText(contenuto);
			
			String contenuto2 = "         SCHEMA di PARTENZA " + this.storicoDistribuzione.getSchemaPartenza() + " - NOTE: " + this.storicoDistribuzione.getNote();
			labelTitle2.setText(contenuto2);
			
			// aggiunta di una observable list alla table
			if (this.storicoDistribuzione.getElencoSchemi() != null && this.storicoDistribuzione.getElencoSchemi().size() > 0) {
				schemi.addAll(this.storicoDistribuzione.getElencoSchemi());
				schemiTable.setItems(schemi);				
			}
        }
    }
    
    
    @FXML
    void handleStatistiche(ActionEvent event) {
	
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("STATISTICHE DISTRIBUZIONE");
		
		String header = "ESECUZIONE DEL " + this.storicoDistribuzione.dataOraDistribuzioneProperty().getValue() + "\n";
		header += "NOTE: " + this.storicoDistribuzione.getNote();
		
		alert.setHeaderText(header);
		
		String contenuto = "";
//		contenuto += "NOTE: " + this.storicoDistribuzione.getNote()  + "\n";
		contenuto += "SCHEMA DI PARTENZA: " + this.storicoDistribuzione.getSchemaPartenza()  + "\n";
		
		//ciclo per elencare le Voci distribuite
		for (int i = 0; i < this.storicoDistribuzione.getElencoVoci().size(); i++) {
			String voce = this.storicoDistribuzione.getElencoVoci().get(i);
			if (i == 0) {
				if (this.storicoDistribuzione.getElencoVoci().size() == 1) {
					contenuto += "VOCE: ";
				} else {
					contenuto += "VOCI: ";		
				}
			}

			contenuto += voce;
			if (i == this.storicoDistribuzione.getElencoVoci().size() - 1) {
				contenuto += "\n";
			} else {
				contenuto += ", ";
			}
		}
		int contatoreTotInsertGeneratePerBackup = 0;
		int contatoreTotRigheCancellate = 0;
		int contatoreTotRigheDistribuite = 0;
		//ciclo per elencare le Tabelle distribuite
		for (int i = 0; i < this.storicoDistribuzione.getListaDeleteStatement().size(); i++) {
			String tabella = this.storicoDistribuzione.getListaDeleteStatement().get(i).getCodice();
			for (Distribuzione distribuzione: this.storicoDistribuzione.getListaDeleteStatement().get(i).getListaDistribuzione()) {
				contatoreTotInsertGeneratePerBackup +=  distribuzione.getContatoreInsertGeneratePerBackup();
				contatoreTotRigheCancellate += distribuzione.getContatoreRigheCancellate();
				contatoreTotRigheDistribuite += distribuzione.getContatoreRigheDistribuite();
			}
			if (i == 0) {
				if (this.storicoDistribuzione.getListaDeleteStatement().size() == 1) {
					contenuto += "TABELLA: ";
				} else {
					contenuto += "TABELLE: ";		
				}
			}
			contenuto += tabella;
			if (i == this.storicoDistribuzione.getListaDeleteStatement().size() - 1) {
				contenuto += "\n";
			} else {
				contenuto += ", ";
			}
		}
		contenuto += "NR. DI SCHEMI DI ARRIVO :  " + this.storicoDistribuzione.getElencoSchemi().size()  + "\n";
		contenuto += "TOTALE INSERT GENERATE PER RIPRISTINO :  " + contatoreTotInsertGeneratePerBackup  + "\n";
		contenuto += "TOTALE RIGHE CANCELLATE :  " + contatoreTotRigheCancellate  + "\n";
		contenuto += "TOTALE RIGHE DISTRIBUITE :  " + contatoreTotRigheDistribuite  + "\n";
		
		alert.setContentText(contenuto);
		alert.showAndWait();
    }
    
    
    @FXML
    private void handleHelp(ActionEvent event) {
    	
		showAlert(AlertType.INFORMATION, "Informazione", "Seleziona uno Schema", 
				"Per cortesia, seleziona uno Schema e successivamente gli Statement di Delete per visualizzare i dettagli della Distribuzione" , null);
    	
    }
    
    @FXML
    private boolean handleExit(ActionEvent event) {
    	
        exitClicked = true;
        dialogStage.close();
        return exitClicked;
    }
    
    public boolean isexitClicked() {
        return exitClicked;
    }

	private void showInsertsDetails(DeleteStatement newValue) {

		if (newValue != null && newValue.getInsertsListFromSchemaOrigine() != null
				&& newValue.getInsertsListFromSchemaOrigine().size() > 0) {
			
			String textArea = "";
			int countStatement = 0;
			countStatement = newValue.getInsertsListFromSchemaOrigine().size();
			for (String insertStatement : newValue.getInsertsListFromSchemaOrigine()) {
				textArea = textArea + insertStatement + "\n";
			}
			labelInsert.setText("Insert - " + countStatement + " statement" );
			textAreaPreviewInsert.setText(textArea);
			
			String textAreaBckup = "";
			int countStatementBckup = 0;
			for (Distribuzione distribuzione: newValue.getListaDistribuzione()) {
				if (codiceSchemaSelezionato.equalsIgnoreCase(distribuzione.getCodiceSchema())) {
					if (distribuzione.getListaInsertGeneratePerBackup() != null) {
						countStatementBckup = distribuzione.getListaInsertGeneratePerBackup().size();
						for (String insertStatementForBckup : distribuzione.getListaInsertGeneratePerBackup()) {
							textAreaBckup = textAreaBckup + insertStatementForBckup + "\n";
						}
					}
					break;
				}
			}
			labelInsertRipristino.setText("Insert di ripristino - " + countStatementBckup + " statement" );
			textAreaPreviewInsertBckup.setText(textAreaBckup);
		}
	}
    
	private void showDeletesDetails(Schema newValue) {
		
		if (newValue != null && newValue.getCodice() != null) {
			codiceSchemaSelezionato = newValue.getCodice();
			elencoDelete.clear();
			textAreaPreviewInsert.setText("");
			textAreaPreviewInsertBckup.setText("");
			labelInsert.setText("Insert");
			labelInsertRipristino.setText("Insert di ripristino");
			for (int i = 0; i < this.storicoDistribuzione.getListaDeleteStatement().size(); i++) {
				DeleteStatement deleteStatement = this.storicoDistribuzione.getListaDeleteStatement().get(i);
				for (int y = 0; y < deleteStatement.getListaDistribuzione().size(); y++) {
					Distribuzione distr = deleteStatement.getListaDistribuzione().get(y);
					if (distr.getCodiceSchema().equalsIgnoreCase(codiceSchemaSelezionato)) {
						elencoDelete.add(deleteStatement);
					}
				}
			}
			deleteStatementTable.setItems(elencoDelete);
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

