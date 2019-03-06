package it.infocamere.sipert.distrivoci.view;

import java.io.File;
import java.util.Optional;

import it.infocamere.sipert.distrivoci.Main;
import it.infocamere.sipert.distrivoci.MainWithTreeView;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.stage.FileChooser;

public class RootLayoutController {
	
    private Main main;
    
    private MainWithTreeView main2;
    
    public void setMain(Main main) {
        this.main = main;
    }
    
    public void setMain2(MainWithTreeView main2) {
        this.main2 = main2;
    }
	 
    @FXML
    private void handleExit() {
    	
//    	if (main.getTabelleFilePath() == null) {
//    		if (main.getTabelleDB().size() > 0) {
//            	// alert per indicare dove salvare la lista delle tabelle
//        		Alert alert = new Alert(AlertType.CONFIRMATION);
//        		alert.setTitle("Indicare il File xml della lista tabelle");
//        		alert.setHeaderText("Indicare il File xml sul quale salvare l'elenco delle tabelle");
//        		alert.setContentText("Scegli la tua opzione");
//
//        		ButtonType buttonSelectFile = new ButtonType("Select File Tabelle..");
//        		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
//
//        		alert.getButtonTypes().setAll(buttonSelectFile, buttonTypeCancel);
//
//        		Optional<ButtonType> result = alert.showAndWait();
//        		if (result.get() == buttonSelectFile){
//        			handleSaveTabelle();
//        		}
//            } 
//    	} else {
//    		handleSaveTabelle();
//		}
//    	
//    	if (main.getVociFilePath() == null) {
//    		if (main.getVociData().size() > 0) {
//            	// alert per indicare dove salvare la lista delle voci
//        		Alert alert = new Alert(AlertType.CONFIRMATION);
//        		alert.setTitle("Indicare il File xml della lista voci");
//        		alert.setHeaderText("Indicare il File xml sul quale salvare l'elenco delle voci");
//        		alert.setContentText("Scegli la tua opzione");
//
//        		ButtonType buttonSelectFile = new ButtonType("Select File Voci..");
//        		ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
//
//        		alert.getButtonTypes().setAll(buttonSelectFile, buttonTypeCancel);
//
//        		Optional<ButtonType> result = alert.showAndWait();
//        		if (result.get() == buttonSelectFile){
//        			handleSaveVoci();
//        		}
//            } 
//    	} else {
//    		handleSaveVoci();
//		}
    	
		System.exit(0);
    }

    /**
     * salva sul file correntemente aperto, se non c'è nessun file aperto viene esposto il dialogo Save As
     */
    @FXML
    private void handleSaveTabelle() {
        File tabelleFile = main.getTabelleFilePath();
        if (tabelleFile != null) {
            main.saveTabelleDataToFile(tabelleFile);
        } else {
            handleSaveAsTabelle();
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente di selezionare un file da salvare
     */
    @FXML
    private void handleSaveAsTabelle() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro per l'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // espone il dialogo per il salvataggio del file
        File file = fileChooser.showSaveDialog(main.getStagePrincipale());

        if (file != null) {
            // verifica la corretta estensione del file
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveTabelleDataToFile(file);
        }
    }
    
    
    /**
     * salva sul file correntemente aperto, se non c'è nessun file aperto viene esposto il dialogo Save As
     */
    @FXML
    private void handleSaveVoci() {
        File vociFile = main.getVociFilePath();
        if (vociFile != null) {
            main.saveVociDataToFile(vociFile);
        } else {
            handleSaveAsVoci();
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente di selezionare un file da salvare
     */
    @FXML
    private void handleSaveAsVoci() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro per l'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);

        // espone il dialogo per il salvataggio del file
        File file = fileChooser.showSaveDialog(main.getStagePrincipale());

        if (file != null) {
            // verifica la corretta estensione del file
            if (!file.getPath().endsWith(".xml")) {
                file = new File(file.getPath() + ".xml");
            }
            main.saveVociDataToFile(file);
        }
    }
    
    /**
     * Apre un FileChooser per permettere all'utente la scelta del file degli schemi dei data base oracle
     */
    @FXML
    private void handleOpenSchemi() {
        FileChooser fileChooser = new FileChooser();

        // impostazione del filtro dell'estensione
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
                "XLS files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showOpenDialog(main.getStagePrincipale());

        if (file != null) {
        	main.loadSchemiDataBaseFromFile(file);
        }
    }
    
}
