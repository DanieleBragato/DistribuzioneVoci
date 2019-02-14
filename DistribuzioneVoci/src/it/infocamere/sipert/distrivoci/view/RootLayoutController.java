package it.infocamere.sipert.distrivoci.view;

import it.infocamere.sipert.distrivoci.Main;
import javafx.fxml.FXML;

public class RootLayoutController {
	
    private Main main;
    
    public void setMain(Main main) {
        this.main = main;
    }
	 
    @FXML
    private void handleExit() {

		System.exit(0);
    }

}
