package it.infocamere.sipert.distrivoci;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.distrivoci.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.view.OverviewDistriVociController;
import it.infocamere.sipert.distrivoci.view.RootLayoutController;
import it.infocamere.sipert.distrivoci.view.TabellaEditDialogController;
import it.infocamere.sipert.distrivoci.view.VoceEditDialogController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
    /**
     * i dati nel formato di observable list di DeleteStatement.
     */
    private ObservableList<DeleteStatement> deleteStatement = FXCollections.observableArrayList();
	
	/**
     *  gli schemi dei data base oracle da trattare
     */
    private List<SchemaDTO> listSchemi = new ArrayList<SchemaDTO>();
    
    private Map <String, String> mapProvince = new HashMap<String, String>();
    
    
	public Main() {
		
        // Aggiungo alcuni dati di esempio
    	tabelleDB.add(new Tabella("tabella1", "la prima tabella"));
    	tabelleDB.add(new Tabella("tabella2", "la seconda tabella"));
    	
    	voci.add(new Voce("voce1", "first voice"));
    	voci.add(new Voce("voce2", "second voice"));
    	
    	schemi.add(new Schema("schema1", "first schemi"));
    	schemi.add(new Schema("schema2", "second schemi"));
    	schemi.add(new Schema("schema3", "terzo schemi"));
    	schemi.add(new Schema("schema4", "quarto schemi"));
    	schemi.add(new Schema("schema5", "quinto schemi"));
    	schemi.add(new Schema("schema6", "sesto schemi"));
    	schemi.add(new Schema("schema7", "Settimo schemi"));
    	schemi.add(new Schema("schema8", "ottavo schemi"));
    	schemi.add(new Schema("schema9", "nono schemi"));
    	schemi.add(new Schema("schema10", "decimo schemi"));
    	schemi.add(new Schema("schema11", "undicesimo schemi"));
    	schemi.add(new Schema("schema12", "dodicesimo schemi"));
    	schemi.add(new Schema("schema13", "tredicesimo schemi"));
    	schemi.add(new Schema("schema14", "quattordicesimo schemi"));
    	schemi.add(new Schema("schema15", "quindicesimo schemi"));
    	schemi.add(new Schema("schema16", "sedicesimo schemi"));
    	
    	loadProvince();
    	
    }
    
	private void loadProvince() {

        try {
			File file = new File("resources/elencoProvince.txt"); 
			BufferedReader br = new BufferedReader(new FileReader(file)); 			
			String st; 
			while ((st = br.readLine()) != null) {
				String codiceProvincia = st.split(" ")[1];
				String provincia = ""; 
				if (codiceProvincia.length() > 2) {
					codiceProvincia = st.split(" ")[2];
					provincia = st.split(" ")[0] + " " + st.split(" ")[1];
				} else {
					codiceProvincia = st.split(" ")[1];
					provincia = st.split(" ")[0];
				}
				//mapProvince.put(st.split(" ")[1], st.split(" ")[0]);
				mapProvince.put(codiceProvincia, provincia);
			}
			br.close();
		} catch (FileNotFoundException e) {
			System.out.println("eccezione in fase di lettura file delle province (non trovato) - eccezione = " + e.toString());
		} catch (IOException e) {
			System.out.println("eccezione in fase di lettura file delle province - eccezione = " + e.toString());
		} 
        
		System.out.println("trovate " + mapProvince.size() + " province");
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
		
		// tentativo di caricamento dell'ultimo file aperto degli schemi dei data base oracle  
		File fileSchemiDB = getSchemiFilePath();
		if (fileSchemiDB != null) {
			loadSchemiDataBaseFromFile(fileSchemiDB);
		}
	}
	
	public File getSchemiFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePathSchemiDB = prefs.get("filePathSchemiDB", null);
		if (filePathSchemiDB != null) {
			return new File(filePathSchemiDB);
		} else {
			return null;
		}
	}
	
	public void loadSchemiDataBaseFromFile(File fileSchemiXLS) {

		Model model = new Model();

		try {
			listSchemi = model.getSchemi(fileSchemiXLS);
			setSchemiDataBaseFilePath(fileSchemiXLS);
			if (listSchemi.size() > 0) schemi.clear();
			for (int i = 0; i < listSchemi.size(); i++) {
				Schema schema = new Schema();
				String codSchema = listSchemi.get(i).getSchemaUserName();
				String targaProvincia = codSchema.substring(3, 5);
				schema.setCodice(listSchemi.get(i).getSchemaUserName());
				schema.setDescrizione(trovaProvincia(targaProvincia));
				schemi.add(schema);
			}

		} catch (ErroreFileSchemiNonTrovato e) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("File excel degli schemi non trovato!");
			alert.showAndWait();
		} catch (ErroreColonneFileXlsSchemiKo e1) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText("File excel degli schemi con colonne errate!");
			alert.showAndWait();
		} catch (RuntimeException e2) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setContentText(e2.toString());
			alert.showAndWait();
		}
	}
	
    private String trovaProvincia(String substring) {
    	String provincia = mapProvince.get(substring);
    	if (provincia == null || provincia.length() <= 0) {
    		provincia = "PROVINCIA NON TROVATA";
    	}
    	return provincia;
	}

	public void setSchemiDataBaseFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePathSchemiDB", file.getPath());
        } else {
            prefs.remove("filePathSchemiDB");
        }
    }
	
    public void showOverview() {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OverviewDistriVoci.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();

            // Set distri voci overview into the center of root layout.
            rootLayout.setCenter(overview);

            // Give the controller access to the main app.
            OverviewDistriVociController controller = loader.getController();
            controller.setMain(this);
            controller.setFilter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public List<SchemaDTO> getListSchemi() {
		return listSchemi;
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
     * Ritorna i dati nel formato di observable list of DeleteStatement. 
     * @return
     */
    public ObservableList<DeleteStatement> getDeleteStatement() {
        return deleteStatement;
    }
    
    public void setDeleteStatement(ObservableList<DeleteStatement> deleteStatement ) {
		this.deleteStatement = deleteStatement;
	}
    
    public boolean addDeleteStatement (DeleteStatement deleteStatement) {
    	
    	if (deleteStatement != null) {
    		this.deleteStatement.add(deleteStatement);
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
