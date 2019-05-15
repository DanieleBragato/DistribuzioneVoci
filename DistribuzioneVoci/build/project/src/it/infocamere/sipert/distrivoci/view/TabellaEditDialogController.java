package it.infocamere.sipert.distrivoci.view;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.model.Tabella;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TabellaEditDialogController {
	
    @FXML
    private TextField nomeField;
    @FXML
    private TextField descrizioneField;

    @FXML
    private Button bntOkSalva;
    @FXML
    private Button bntExit;    
	
    private boolean okClicked = false;
    private Tabella tabella;
    private Stage dialogStage;
	private Main main;
	
    public void setMain(Main main) {
        this.main = main;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
    @FXML
    private void initialize() {

    }
    
    @FXML
    private void handleOkSalva() {
        if (isInputValid()) {
        	tabella.setCodice(nomeField.getText());
        	tabella.setDescrizione(descrizioneField.getText());
        	okClicked = true;
            dialogStage.close();
        }
    }
    
    private boolean isInputValid() {
        String errorMessage = "";

        if (nomeField.getText() == null || nomeField.getText().length() == 0) {
            errorMessage += "Codice tabella non valorizzato!\n"; 
        }
        if (descrizioneField.getText() == null || descrizioneField.getText().length() == 0) {
            errorMessage += "Descrizione non valorizzata!\n"; 
        }

		if (errorMessage.length() == 0) {
			return true;
		} else {
			showAlert(AlertType.ERROR, "campi non validi", "Per cortesia, correggi i campi non validi", errorMessage, dialogStage);
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
	    
    
    @FXML
    private void handleExit(ActionEvent event) {
    	
        //System.out.println("click on Exit button");
        dialogStage.close();
    }
    
    public void setTabella(Tabella tabella) {    	
        this.tabella = tabella;
        nomeField.setText(tabella.getCodice());
        descrizioneField.setText(tabella.getDescrizione());
    }
    
    
    public boolean isOkClicked() {
        return okClicked;
    }
    
}
