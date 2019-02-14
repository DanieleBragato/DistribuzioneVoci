package it.infocamere.sipert.distrivoci.view;

import it.infocamere.sipert.distrivoci.Main;
import javafx.fxml.FXML;

public class DistriVociOverviewController {

    // Referimento al main 
    private Main main;
    
	
    /**
     * Initialializza la classe controller, chiamato automaticamente dopo il caricamento del file fxml
     */
    @FXML
    private void initialize() {
    	
    }
    
    public void setMain(Main main) {
        this.main = main;

        // aggiunta di una observable list alla table
        //queryTable.setItems(mainApp.getQueryData());
    }
    
}
