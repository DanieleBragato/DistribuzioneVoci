package it.infocamere.sipert.distrivoci;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.infocamere.sipert.distrivoci.model.Model;
import it.infocamere.sipert.distrivoci.util.DistributionStep;
import it.infocamere.sipert.distrivoci.view.OverviewDistriVociController;
import it.infocamere.sipert.distrivoci.view.RootLayoutController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;

public class MainWithTreeView extends Application {
    private static final JMetro.Style STYLE = JMetro.Style.DARK;

	private Stage stagePrincipale;
    private BorderPane rootLayout;
    
    private List<DistributionStep> distributionStep = Arrays.asList(
            new DistributionStep("Tabelle", "Parametri"),
            new DistributionStep("Voci", "Parametri"),
            new DistributionStep("Schemi sui quali distribuire", "Parametri"),
            new DistributionStep("Anteprima", "Anteprima Elaborazione "));
    private TreeItem<String> rootNode;

    public static void main(String[] args) {
        launch(args);
    }

    public MainWithTreeView() {
        this.rootNode = new TreeItem<>("Distribuzione Voci");
    }

    @Override
    public void start(Stage stagePrincipale) {
    	
        this.stagePrincipale = stagePrincipale;
        
        this.stagePrincipale.initStyle(StageStyle.UNDECORATED);
        this.stagePrincipale.setTitle("Distribuzione Voci");
        
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
        
	public void initRootLayout() {

		try {
			// carico della root layout dall' fxml file
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			// esposizione della Scene contenente il root layout.
			Scene scene = new Scene(rootLayout);

			scene.setFill(Color.LIGHTGRAY);
			
			new JMetro(STYLE).applyTheme(scene);
			stagePrincipale.setScene(scene);

			// do al controllore l'accesso alla main app.
			RootLayoutController controller = loader.getController();
			controller.setMain2(this);

			stagePrincipale.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    
    public void showOverview() {
        try {
            // Load overview.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/OverviewDistriVoci2.fxml"));
            AnchorPane overview = (AnchorPane) loader.load();
            
            rootNode.setExpanded(true);
            for (DistributionStep step : distributionStep) {
                TreeItem<String> stepLeaf = new TreeItem<>(step.getName());
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

            stagePrincipale.setTitle("Tree View Sample");
            
            
            TreeView<String> treeView = new TreeView<>(rootNode);
            
            setTreeCellFactory(treeView);
            
            AnchorPane anchorPaneSX = (AnchorPane) overview.lookup("#anchorPaneSX");
            anchorPaneSX.getChildren().add(treeView);
            
            // Set distri voci overview into the center of root layout.
            rootLayout.setCenter(overview);

            // Give the controller access to the main app.
            OverviewDistriVociController controller = loader.getController();
			// set the model
			Model model = new Model() ;
			controller.setModel(model);
            controller.setMain2(this);
            controller.setFilter();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void setTreeCellFactory(TreeView<String> tree) {
        tree.setCellFactory(param -> new TreeCell<String>() {
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                //setDisclosureNode(null);

                if (empty) {
                    setText("");
                    setGraphic(null);
                } else {
                    setText(item);
                }
            }

        });

        tree.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
            	if ("Tabelle".equalsIgnoreCase(newValue.getValue())) {
            		VboxVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxVoci");
            		VboxNonVisibile("#vboxSchemi");
            	}
            	if ("Voci".equalsIgnoreCase(newValue.getValue())) {
            		VboxNonVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxSchemi");
            		VboxVisibile("#vboxVoci");
            	}
            	if ("Schemi sui quali distribuire".equalsIgnoreCase(newValue.getValue())) {
            		VboxNonVisibile("#vboxTabelle");
            		VboxNonVisibile("#vboxVoci");
            		VboxVisibile("#vboxSchemi");
            	}
                System.out.println(newValue.getValue());
            }
        });
    }
    
    private void VboxVisibile (String nomeBox) {
		VBox vboxTabelle = (VBox) rootLayout.lookup(nomeBox);
		vboxTabelle.setVisible(true);
    }
    private void VboxNonVisibile (String nomeBox) {
		VBox vboxTabelle = (VBox) rootLayout.lookup(nomeBox);
		vboxTabelle.setVisible(false);
    }
    
    
}