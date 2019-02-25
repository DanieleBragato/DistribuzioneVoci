package it.infocamere.sipert.distrivoci.view;

import java.io.File;

import it.infocamere.sipert.distrivoci.Main;
import javafx.fxml.FXML;
import javafx.stage.FileChooser;

public class RootLayoutController {
	
    private Main main;
    
    public void setMain(Main main) {
        this.main = main;
    }
	 
    @FXML
    private void handleExit() {

		System.exit(0);
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
