package it.infocamere.sipert.distrivoci;
	
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.distrivoci.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.distrivoci.model.DeleteStatement;
import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.model.Schema;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzione;
import it.infocamere.sipert.distrivoci.model.StoricoDistribuzioniListWrapper;
import it.infocamere.sipert.distrivoci.model.Tabella;
import it.infocamere.sipert.distrivoci.model.TabelleListWrapper;
import it.infocamere.sipert.distrivoci.model.Voce;
import it.infocamere.sipert.distrivoci.model.VociListWrapper;
import it.infocamere.sipert.distrivoci.util.Constants;
import it.infocamere.sipert.distrivoci.util.DistributionStep;
import it.infocamere.sipert.distrivoci.view.DettaglioStoricoDistribuzioneDialogController;
import it.infocamere.sipert.distrivoci.view.ListaVociRipristinabiliController;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	
	static Logger logger = Logger.getLogger(Main.class);
	
	private Stage stagePrincipale;
    private BorderPane rootLayout;
    
    private List<DistributionStep> distributionStep = Arrays.asList(
            new DistributionStep("Schemi di Partenza", "Parametri"),
            new DistributionStep("Tabelle", "Parametri"),
            new DistributionStep("Voci", "Parametri"),
            new DistributionStep("Schemi sui quali distribuire", "Parametri"),
            new DistributionStep("Anteprima e Distribuzione", "Anteprima e Distribuzione Voci"),
    		new DistributionStep("Distribuzioni", "Storico "),
    		new DistributionStep("Anteprima e Ripristino", "Ripristino Distribuzione"),
    		new DistributionStep("Anteprima e Ripristino Voce", "Ripristino Voce"));
    private TreeItem<String> rootNode;
	
    /**
     * i dati nel formato di observable list di Tabella.
     */
    private ObservableList<Tabella> tabelleDB = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Voce.
     */
    private ObservableList<Voce> voci = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Schema >> PARTENZA
     */
    private ObservableList<Schema> schemiPartenza = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Schema >> ARRIVO
     */
    private ObservableList<Schema> schemi = FXCollections.observableArrayList();
	
    /**
     * i dati nel formato di observable list di Schema >> per RIPRISTINO
     */
    private ObservableList<Schema> schemiRipristino = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di DeleteStatement.
     */
    private ObservableList<DeleteStatement> deleteStatement = FXCollections.observableArrayList();
	
    /**
     * i dati nel formato di observable list di DeleteStatement.
     */
    private ObservableList<DeleteStatement> deleteStatementForRipristino = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list dello storico delle Distribuzioni
     */
    private ObservableList<StoricoDistribuzione> storicoDistribuzione = FXCollections.observableArrayList();
    
    /**
     * i dati nel formato di observable list di Schema >> per RIPRISTINO VOCE
     */
    private ObservableList<Schema> schemiRipristinoVoce = FXCollections.observableArrayList();
    
    
	/**
     *  gli schemi dei data base oracle da trattare
     */
    private List<SchemaDTO> listSchemi = new ArrayList<SchemaDTO>();
    
	/**
     *  gli schemi dei data base oracle di partenza
     */
    private List<SchemaDTO> listSchemiPartenza = new ArrayList<SchemaDTO>();
    
    /**
     *   schema di partenza (sviluppo) dal quale recuperare i dati da distribuire sugli schemi di arrivo (produzione)
     */
    private SchemaDTO schemaDtoOrigine;
    
    private Map <String, String> mapProvince = new HashMap<String, String>();
    
    private Voce voceDaRipristinare = new Voce();
    
//    private Image nodeImageSetup = new Image(
//            getClass().getResourceAsStream("file:resources/images/ICO_SETUP.png"));
    
    
	public Main() {
		
	    //load configuration File
	    PropertyConfigurator.configure("resources/log4j.properties");
		
		this.rootNode = new TreeItem<>("Distribuzione Voci");
    	
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
			logger.error("eccezione in fase di lettura file delle province (non trovato) - eccezione = " + e.toString());
			System.out.println("eccezione in fase di lettura file delle province (non trovato) - eccezione = " + e.toString());
		} catch (IOException e) {
			logger.error("eccezione in fase di lettura file delle province - eccezione = " + e.toString());
			System.out.println("eccezione in fase di lettura file delle province - eccezione = " + e.toString());
		} 
        
        logger.info("trovate " + mapProvince.size() + " province");
		System.out.println("trovate " + mapProvince.size() + " province");
	}

	@Override
	public void start(Stage stagePrincipale) {
		
        this.stagePrincipale = stagePrincipale;
        
        this.stagePrincipale.setOnCloseRequest(evt -> {
            // prevent window from closing
            evt.consume();

            // execute own shutdown procedure
            shutdown(this.stagePrincipale);
        });
        
        //this.stagePrincipale.initStyle(StageStyle.UNDECORATED);
        this.stagePrincipale.setTitle("Distribuzione Voci");
        
        // Set the application icon.
        this.stagePrincipale.getIcons().add(new Image("file:resources/images/ICO_DISTRIB.png"));

        initRootLayout();

        showOverview();
		
	}
	
	private void shutdown(Stage mainWindow) {

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Usa l'opzione Exit");
		alert.setHeaderText("Per cortesia usa l'opzione Exit");
		alert.setContentText("Per cortesia usa l'opzione Exit all'interno del menù File");
		alert.initOwner(mainWindow);
		alert.showAndWait();
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
			
			scene.setFill(Color.LIGHTGRAY);
			//new JMetro(STYLE).applyTheme(scene);
			
			stagePrincipale.setTitle("Distribuzione Voci");
			
			stagePrincipale.setScene(scene);

			// do al controllore l'accesso alla main app.
			RootLayoutController controller = loader.getController();
			controller.setMain(this);

			stagePrincipale.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// tentativo di caricamento dell'ultimo file delle tabelle aperto 
		File fileTabelle = getTabelleFilePath();
		if (fileTabelle != null) {
			loadTabelleDataFromFile(fileTabelle);
		}
		
		// tentativo di caricamento dell'ultimo file delle voci aperto 
		File fileVoci = getVociFilePath();
		if (fileVoci != null) {
			loadVociDataFromFile(fileVoci);
		}

		// tentativo di caricamento dell'ultimo file dello storico delle distribuzioni 
		File fileStoricoDistribuzioni = getStoricoDistribuzioniFilePath();
		if (fileStoricoDistribuzioni != null) {
			loadStoricoDistribuzioniDataFromFile(fileStoricoDistribuzioni);
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
	
    public void loadTabelleDataFromFile(File file) {
    	
        try {
            JAXBContext context = JAXBContext
                    .newInstance(TabelleListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // lettura XML dal file e unmarshalling.
            TabelleListWrapper wrapper = (TabelleListWrapper) um.unmarshal(file);

            tabelleDB.clear();
            tabelleDB.addAll(wrapper.getTabelle());

            // Save del file path sul registro.
            setTabelleFilePath(file);

        } catch (Exception e) { // catches ANY exception
        	
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Elenco Tabelle vuoto");
        	alert.setContentText("Impossibile caricare le tabelle dal file:\n" + file.getPath());
        	
        	alert.showAndWait();
        	tabelleDB.clear();
        }
    }
    
    public void loadVociDataFromFile(File file) {
    	
        try {
            JAXBContext context = JAXBContext
                    .newInstance(VociListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // lettura XML dal file e unmarshalling.
            VociListWrapper wrapper = (VociListWrapper) um.unmarshal(file);

            voci.clear();
            voci.addAll(wrapper.getVoci());

            // Save del file path sul registro.
            setVociFilePath(file);

        } catch (Exception e) { // catches ANY exception
        	
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Elenco Voci vuoto");
        	alert.setContentText("Impossibile caricare le voci dal file:\n" + file.getPath());
        	
        	alert.showAndWait();
        	voci.clear();
        }
    }
	
    public void loadStoricoDistribuzioniDataFromFile(File file) {
    	
        try {
            JAXBContext context = JAXBContext
                    .newInstance(StoricoDistribuzioniListWrapper.class);
            Unmarshaller um = context.createUnmarshaller();

            // lettura XML dal file e unmarshalling.
            StoricoDistribuzioniListWrapper wrapper = (StoricoDistribuzioniListWrapper) um.unmarshal(file);

            storicoDistribuzione.clear();
            storicoDistribuzione.addAll(wrapper.getDistribuzioni());

            // Save del file path sul registro.
            //setVociFilePath(file);
            setStoricoDistribuzioniFilePath(file);

        } catch (Exception e) { // catches ANY exception
        	
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Elenco Distribuzioni vuoto");
        	alert.setContentText("Impossibile caricare lo storico delle distribuzioni dal file:\n" + file.getPath());
        	
        	alert.showAndWait();
        	storicoDistribuzione.clear();
        }
    }
    
    
	public void loadSchemiDataBaseFromFile(File fileSchemiXLS) {

		Model model = new Model();

		try {
			listSchemi = model.getSchemi(fileSchemiXLS);
			//schemaDtoOrigine = model.getSchema(fileSchemiXLS, Constants.NOME_FOLDER_SETEUR7ES, Constants.SETEUR7ES);
			setSchemiDataBaseFilePath(fileSchemiXLS);
			if (listSchemi.size() > 0) schemi.clear();
			for (int i = 0; i < listSchemi.size(); i++) {
				Schema schema = new Schema();
				String codSchema = listSchemi.get(i).getSchemaUserName();
				// test INIZIO
				if (!"HR".equalsIgnoreCase(codSchema)) {
					String targaProvincia = codSchema.substring(3, 5);
					schema.setDescrizione(trovaProvincia(targaProvincia));
				} else {
					schema.setDescrizione("ORACLE 11 XE LOCALE");
				}
				// test FINE
				schema.setCodice(listSchemi.get(i).getSchemaUserName());
				schemi.add(schema);
			}
			
			listSchemiPartenza = model.getSchemiPartenza(fileSchemiXLS);

			if (listSchemiPartenza.size() > 0) schemiPartenza.clear();
			for (int i = 0; i < listSchemiPartenza.size(); i++) {
				Schema schema = new Schema();
				String codSchema = listSchemiPartenza.get(i).getSchemaUserName();
//				// test INIZIO
//				if (!"HR".equalsIgnoreCase(codSchema)) {
//					String targaProvincia = codSchema.substring(3, 5);
//					schema.setDescrizione(trovaProvincia(targaProvincia));
//				} else {
//					schema.setDescrizione("ORACLE 11 XE LOCALE");
//				}
				// test FINE
				schema.setCodice(listSchemiPartenza.get(i).getSchemaUserName());
				schemiPartenza.add(schema);
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

    // gestione salvataggio elenco tabelle
	public File getTabelleFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePathTabelle", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
    public void saveTabelleDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(TabelleListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping dei dati delle tabelle
            TabelleListWrapper wrapper = new TabelleListWrapper();
            wrapper.setTabelle(tabelleDB);

            // Marshalling e salvataggio XML su file
            m.marshal(wrapper, file);

            // Salvataggio del file path sul registro
            setTabelleFilePath(file);
            
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Impossibile salvare i dati");
        	alert.setContentText("Impossibile salvare i dati sul file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }
    
    public void setTabelleFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePathTabelle", file.getPath());
        } else {
            prefs.remove("filePath");
        }
    }
	
    // gestione salvataggio elenco voci
	public File getVociFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePathVoci", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
    public void saveVociDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(VociListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping dei dati delle voci
            VociListWrapper wrapper = new VociListWrapper();
            wrapper.setVoci(voci);

            // Marshalling e salvataggio XML su file
            m.marshal(wrapper, file);

            // Salvataggio del file path sul registro
            setVociFilePath(file);
            
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Impossibile salvare i dati");
        	alert.setContentText("Impossibile salvare i dati sul file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }
    
    public void setVociFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePathVoci", file.getPath());
        } else {
            prefs.remove("filePathVoci");
        }
    }
    
    // gestione salvataggio elenco Storico Distribuzioni
	public File getStoricoDistribuzioniFilePath() {
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String filePath = prefs.get("filePathStoricoDistribuzioni", null);
		if (filePath != null) {
			return new File(filePath);
		} else {
			return null;
		}
	}
	
    public void saveStoricoDistribuzioniDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext
                    .newInstance(StoricoDistribuzioniListWrapper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping dei dati delle Storico Distribuzioni
            StoricoDistribuzioniListWrapper wrapper = new StoricoDistribuzioniListWrapper();
            wrapper.setDistribuzioni(storicoDistribuzione);

            // Marshalling e salvataggio XML su file
            m.marshal(wrapper, file);

            // Salvataggio del file path sul registro
            setStoricoDistribuzioniFilePath(file);
            
        } catch (Exception e) { // catches ANY exception
        	Alert alert = new Alert(AlertType.ERROR);
        	alert.setTitle("Error");
        	alert.setHeaderText("Impossibile salvare i dati");
        	alert.setContentText("Impossibile salvare i dati sul file:\n" + file.getPath());
        	
        	alert.showAndWait();
        }
    }
    
    public void setStoricoDistribuzioniFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePathStoricoDistribuzioni", file.getPath());
        } else {
            prefs.remove("filePathStoricoDistribuzioni");
        }
    }
    
    public void showOverview() {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OverviewDistriVoci.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();
            
            //Image img = new Image("file:resources/images/SETUP.png");
            
            rootNode.setExpanded(true);
            for (DistributionStep step : distributionStep) {
            	TreeItem<String> stepLeaf;
//            	if ("Parametri".equalsIgnoreCase(step.getStep())) {
//            		stepLeaf = new TreeItem<>(step.getName(), new ImageView(img));
//            	} else {
            		stepLeaf = new TreeItem<>(step.getName());	
//            	}
                
                boolean found = false;
                for (TreeItem<String> stepNode : rootNode.getChildren()) {
                	
                    if (stepNode.getValue().contentEquals(step.getStep())){
                        stepNode.getChildren().add(stepLeaf);
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    TreeItem<String> stepNode = new TreeItem<>(
                            step.getStep()
                    );
                    rootNode.getChildren().add(stepNode);
                    stepNode.getChildren().add(stepLeaf);
                }
            }
            
            TreeView<String> treeView = new TreeView<>(rootNode);

            treeView.prefWidth(280);
            treeView.minWidth(280);
            treeView.prefHeight(740);
            treeView.minHeight(740);
            
            AnchorPane anchorPaneSX = (AnchorPane) overview.lookup("#anchorPaneSX");
            
            anchorPaneSX.getChildren().add(treeView);
            
            anchorPaneSX.setTopAnchor(treeView, 0.0);
            anchorPaneSX.setBottomAnchor(treeView, 0.0);
            anchorPaneSX.setRightAnchor(treeView, 0.0);
            anchorPaneSX.setLeftAnchor(treeView, 0.0);
            
            // Set distri voci overview into the center of root layout.
            rootLayout.setCenter(overview);

            // Give the controller access to the main app.
            OverviewDistriVociController controller = loader.getController();
			// set the model
			Model model = new Model() ;
			controller.setModel(model);
            controller.setMain(this);
            controller.setFilter();
            controller.setTreeCellFactory(treeView);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	    
	public BorderPane getRootLayout() {
		return rootLayout;
	}

	public List<SchemaDTO> getListSchemi() {
		return listSchemi;
	}
    
	public List<SchemaDTO> getListSchemiPartenza() {
		return listSchemiPartenza;
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
     * Ritorna i dati nel formato di observable list of Schema. 
     * @return
     */
    public ObservableList<Schema> getSchemiPartenza() {
        return schemiPartenza;
    }
    
    public void setSchemiPartenza(ObservableList<Schema> schemiPartenza ) {
		this.schemiPartenza = schemiPartenza;
	}
    
    public boolean addSchemiPartenzaData (Schema schemaPartenza) {
    	
    	if (schemaPartenza != null) {
    		this.schemiPartenza.add(schemaPartenza);
    		return true;
    	}
    	return false;
    }
    
    
    
    public Voce getVoceDaRipristinare() {
		return voceDaRipristinare;
	}

	public void setVoceDaRipristinare(Voce voceDaRipristinare) {
		this.voceDaRipristinare = voceDaRipristinare;
	}

	/**
     * Ritorna i dati nel formato di observable list of Schema per Ripristino 
     * @return
     */
    public ObservableList<Schema> getSchemiRipristino() {
        return schemiRipristino;
    }
    
    public void setSchemiRipristino(ObservableList<Schema> schemiRipristino ) {
		this.schemiRipristino = schemiRipristino;
	}
    
    public boolean addSchemiRipristinoData (Schema schemaRipristino) {
    	
    	if (schemaRipristino != null) {
    		this.schemiRipristino.add(schemaRipristino);
    		return true;
    	}
    	return false;
    }
    
	/**
     * Ritorna i dati nel formato di observable list of Schema per Ripristino Voce 
     * @return
     */
    public ObservableList<Schema> getSchemiRipristinoVoce() {
        return schemiRipristinoVoce;
    }
    
    public void setSchemiRipristinoVoce(ObservableList<Schema> schemiRipristinoVoce ) {
		this.schemiRipristinoVoce = schemiRipristinoVoce;
	}
    
    public boolean addSchemiRipristinoVoceData (Schema schemaRipristinoVoce) {
    	
    	if (schemaRipristinoVoce != null) {
    		this.schemiRipristinoVoce.add(schemaRipristinoVoce);
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
    
    public boolean clearDeleteStatement () {
    	
    	if (this.deleteStatement != null) {
    		this.deleteStatement.clear();
    		return true;
    	}
    	return false;
    }
   
    /**
     * Ritorna i dati nel formato di observable list of StoricoDistribuzione. 
     * @return
     */
    public ObservableList<StoricoDistribuzione> getStoricoDistribuzione() {
        return storicoDistribuzione;
    }
    
    public void setStoricoDistribuzione(ObservableList<StoricoDistribuzione> storicoDistribuzione ) {
		this.storicoDistribuzione = storicoDistribuzione;
	}
    
    public boolean addStoricoDistribuzione (StoricoDistribuzione storicoDistribuzione) {
    	
    	if (storicoDistribuzione != null) {
    		int sequenza = 0;
    		if (this.storicoDistribuzione == null || this.storicoDistribuzione.size() == Constants.ZERO) {
    			sequenza++;
    		} else {
    			sequenza = this.storicoDistribuzione.size() + 1;
    		}
    		storicoDistribuzione.setSequenceDistribuzione( Integer.toString(sequenza) );	
    		this.storicoDistribuzione.add(storicoDistribuzione);
    		return true;
    	}
    	return false;
    }
    
    public boolean clearStoricoDistribuzione () {
    	
    	if (this.storicoDistribuzione != null) {
    		this.storicoDistribuzione.clear();
    		return true;
    	}
    	return false;
    }
    
    /**
     * Ritorna i dati nel formato di observable list of DeleteStatement per il RIPRISTINO 
     * @return
     */
    public ObservableList<DeleteStatement> getDeleteStatementForRipristino() {
        return deleteStatementForRipristino;
    }
    
    public void setDeleteStatementForRipristino(ObservableList<DeleteStatement> deleteStatemenForRipristino) {
		this.deleteStatementForRipristino = deleteStatemenForRipristino;
	}
    
    public boolean addDeleteStatementForRipristino (DeleteStatement deleteStatementForRipristino) {
    	
    	if (deleteStatementForRipristino != null) {
    		this.deleteStatementForRipristino.add(deleteStatementForRipristino);
    		return true;
    	}
    	return false;
    }
    
    public boolean clearDeleteStatementForRipristino () {
    	
    	if (this.deleteStatementForRipristino != null) {
    		this.deleteStatementForRipristino.clear();
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
            
            dialogStage.getIcons().add(new Image("file:resources/images/ICO_DISTRIB.png"));
            
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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
	/**
     * Apre un dialog per visualizzare i dettagli di una specifica distribuzione.
     * 
     * @param StoricoDistribuzione
     * @return true , se l'utente clicca su OK, altrimenti false
     */
    public boolean showDettaglioDistribuzioneDialog(StoricoDistribuzione storicoDistribuzione) {
        try {
            // carico dell' fxml file e creazione del nuovo stage per il popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/DettaglioStoricoDistribuzioneDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            
            dialogStage.initStyle(StageStyle.UNDECORATED);
            
            dialogStage.getIcons().add(new Image("file:resources/images/ICO_DISTRIB.png"));
            
            dialogStage.setTitle("Dettaglio Distribuzione");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            DettaglioStoricoDistribuzioneDialogController controller = loader.getController();
            controller.setMain(this);
            controller.setFilter();
            controller.setDialogStage(dialogStage);
            controller.setStoricoDistribuzione(storicoDistribuzione);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isexitClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
    
	/**
     * Apre un dialog per visualizzare lista delle Voci Ripristinabili 
     * 
     * @param StoricoDistribuzione
     * @return true , se l'utente clicca su OK, altrimenti false
     */
    public void showListaVociRipristinabiliDialog(StoricoDistribuzione storicoDistribuzione, String scope) {
        try {
            // carico dell' fxml file e creazione del nuovo stage per il popup dialog.
        	
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ListaVociRipristinabili.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Crea lo Stage di dialogo
            Stage dialogStage = new Stage();
            
            dialogStage.initStyle(StageStyle.UNDECORATED);
            
            dialogStage.getIcons().add(new Image("file:resources/images/ICO_DISTRIB.png"));
            
            dialogStage.setTitle("Elenco delle Voci Ripristinabili");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(stagePrincipale);
            Scene scene = new Scene(page);
            
            dialogStage.setScene(scene);

            // Imposta la query nel controller
            ListaVociRipristinabiliController controller = loader.getController();
            controller.setMain(this);
            controller.setDialogStage(dialogStage);
            controller.setScope(scope);
            controller.setStoricoDistribuzione(storicoDistribuzione);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            //return controller.isexitClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            //return false;
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
          
            dialogStage.getIcons().add(new Image("file:resources/images/ICO_DISTRIB.png"));
            
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

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();
            
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    } 
    
	public SchemaDTO getSchemaDtoOrigine() {
		return schemaDtoOrigine;
	}

	public void setSchemaDtoOrigine(SchemaDTO schemaDtoOrigine) {
		this.schemaDtoOrigine = schemaDtoOrigine;
	}
    
}
