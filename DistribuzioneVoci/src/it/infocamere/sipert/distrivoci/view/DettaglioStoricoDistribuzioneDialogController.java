package it.infocamere.sipert.distrivoci.view;

import java.util.ResourceBundle;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

public class DettaglioStoricoDistribuzioneDialogController {

	@FXML
	private Label labelTitle;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // fx:id="tabParametri"
    private TabPane tabParametri; // Value injected by FXMLLoader

    @FXML // fx:id="tabStatementSQL"
    private Tab tabStatementSQL; // Value injected by FXMLLoader

    @FXML // fx:id="tabStatistiche"
    private Tab tabStatistiche; // Value injected by FXMLLoader
	
    @FXML
    private Button bntExit;
	
    private boolean exitClicked = false;
    private Stage dialogStage;
	private Main main;
	
	private StoricoDistribuzione storicoDistribuzione;
	
    public void setMain(Main main) {
        this.main = main;
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert tabParametri != null : "fx:id=\"tabParametri\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert tabStatementSQL != null : "fx:id=\"tabStatementSQL\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert tabStatistiche != null : "fx:id=\"tabStatistiche\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert labelTitle != null : "fx:id=\"labelTitle\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";
        assert bntExit != null : "fx:id=\"bntExit\" was not injected: check your FXML file 'DettaglioStoricoDistribuzioneDialog.fxml'.";

    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
	
    public void setStoricoDistribuzione(StoricoDistribuzione storicoDistribuzione) {    	
        
        if (storicoDistribuzione != null) {
        	this.storicoDistribuzione = storicoDistribuzione;
			labelTitle.setText("Dettaglio Distribuzione del " + this.storicoDistribuzione.getDataOraDistribuzione()
					+ " Schema Origine " + this.storicoDistribuzione.getSchemaPartenza() + " - "
					+ this.storicoDistribuzione.getNote());
        }
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

}

