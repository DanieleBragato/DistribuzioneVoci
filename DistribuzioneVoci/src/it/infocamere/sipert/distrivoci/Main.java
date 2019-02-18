package it.infocamere.sipert.distrivoci;
	
import java.io.IOException;

import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.view.OverviewController;
import it.infocamere.sipert.distrivoci.view.RootLayoutController;
import it.infocamere.sipert.distrivoci.view.TabellaEditDialogController;
import it.infocamere.sipert.distrivoci.view.VoceEditDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	
	private Stage stagePrincipale;
    private BorderPane rootLayout;
	
    /**
     * i dati nel formato di observable list di Tabella.
     */
    private ObservableList<Tabella> tabelleDB = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Voce.
     */
    private ObservableList<Voce> voci = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Schema.
     */
    private ObservableList<Schema> schemi = FXCollections.observableArrayList();
	
	public Main() {
        // Aggiungo alcuni dati di esempio
    	tabelleDB.add(new Tabella("tabella1", "la prima tabella"));
    	tabelleDB.add(new Tabella("tabella2", "la seconda tabella"));
    	
    	voci.add(new Voce("voce1", "first voice"));
    	voci.add(new Voce("voce2", "second voice"));
    	
    	schemi.add(new Schema("schema1", "first schemi"));
    	schemi.add(new Schema("schema2", "second schemi"));
    }
    
	@Override
	public void start(Stage stagePrincipale) {
		
        this.stagePrincipale = stagePrincipale;
        
        this.stagePrincipale.initStyle(StageStyle.UNDECORATED);
        this.stagePrincipale.setTitle("Distri Voci");
        
        // Set the application icon.
        //this.stagePrincipale.getIcons().add(new Image("file:resources/images/globalquery_32.png"));

        initRootLayout();

        showOverview();
		
	}
	
    /**
     * Ritorna lo stage principale
     * @return
     */
    public Stage getStagePrincipale() {
        return stagePrincipale;
    }
	
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void initRootLayout() {

		try {
			// carico della root layout dall' fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// esposizione della Scene contenente il root layout.
			Scene scene = new Scene(rootLayout);
			stagePrincipale.setScene(scene);

			// do al controllore l'accesso alla main app.
			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			stagePrincipale.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

		// tentativo di caricamento dell'ultimo file delle query aperto 
//		File file = getQueryFilePath();
//		if (file != null) {
//			loadQueryDataFromFile(file);
//		}
		// tentativo di caricamento dell'ultimo file degli schemi dei data base oracle aperto 
//		File fileSchemiDB = getSchemiFilePath();
//		if (fileSchemiDB != null) {
//			loadSchemiDataBaseFromFile(fileSchemiDB);
//		}
		// tentativo di ricerca dell'ultimo file aperto dei risultati 
//		File filePathRisultati = getFilePathRisultati();
//		if (filePathRisultati != null) {
//			setPathResultsFile(filePathRisultati.getAbsolutePath());
//		}
	}
	
    public void showOverview() {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/Overview.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set person overview into the center of root layout.
            rootLayout.setCenter(overview);

            // Give the controller access to the main app.
            OverviewController controller = loader.getController();
            controller.setMain(this);
            controller.setFilter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Ritorna i dati nel formato di observable list of Tabella. 
     * @return
     */
    public ObservableList<Tabella> getTabelleDB() {
        return tabelleDB;
    }
    
    public void setTabelleDB(ObservableList<Tabella> tabelleDataBase ) {
		this.tabelleDB = tabelleDataBase;
	}
    
    public boolean addTabellaToTabelleDbData (Tabella tabellaDataBase) {
    	
    	if (tabellaDataBase != null) {
    		this.tabelleDB.add(tabellaDataBase);
    		return true;
    	}
    	return false;
    }
   
    /**
     * Ritorna i dati nel formato di observable list of Tabella. 
     * @return
     */
    public ObservableList<Voce> getVociData() {
        return voci;
    }
    
    public void setVociData(ObservableList<Voce> voci ) {
		this.voci =voci;
	}
    
    public boolean addVoceToVociData (Voce voce) {
    	if (voce != null) {
    		this.voci.add(voce);
    		return true;
    	}
    	return false;
    }
   
    /**
     * Ritorna i dati nel formato di observable list of Schema. 
     * @return
     */
    public ObservableList<Schema> getSchemi() {
        return schemi;
    }
    
    public void setSchemi(ObservableList<Schema> schemi ) {
		this.schemi = schemi;
	}
    
    public boolean addSchemiData (Schema schema) {
    	
    	if (schema != null) {
    		this.schemi.add(schema);
    		return true;
    	}
    	return false;
    }
   
	/**
     * Apre un dialog per editare i dettagli di una specifica tabella. Se l'utente clicca OK
     * i cambiamenti vengono salvati e ritorna true
     * 
     * @param tabella
     * @return true , se l'utente clicca su OK, altrimenti false
     */
    public boolean showTabellaEditDialog(Tabella tabella) {
        try {
            // carico dell' fxml file e creazione del nuovo stage per il popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TabellaEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            
            dialogStage.initStyle(StageStyle.UNDECORATED);
            
            //.setValue(Boolean.FALSE);
            
            //dialogStage.getIcons().add(new Image("file:resources/images/globalquery_32.png"));
            
            dialogStage.setTitle("Edit Tabella");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            TabellaEditDialogController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setTabella(tabella);
            
			// set the model
//			Model model = new Model() ;
//			controller.setModel(model);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
    
	/**
     * Apre un dialog per editare i dettagli di una specifica voce. Se l'utente clicca OK
     * i cambiamenti vengono salvati e ritorna true
     * 
     * @param voce
     * @return true , se l'utente clicca su OK, altrimenti false
     */
    public boolean showVoceEditDialog(Voce voce) {
        try {
            // carico dell' fxml file e creazione del nuovo stage per il popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/VoceEditDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            
            dialogStage.initStyle(StageStyle.UNDECORATED);
            
            //.setValue(Boolean.FALSE);
            
            //dialogStage.getIcons().add(new Image("file:resources/images/globalquery_32.png"));
            
            dialogStage.setTitle("Edit Voce");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            VoceEditDialogController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setVoce(voce);
            
			// set the model
//			Model model = new Model() ;
//			controller.setModel(model);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
}
