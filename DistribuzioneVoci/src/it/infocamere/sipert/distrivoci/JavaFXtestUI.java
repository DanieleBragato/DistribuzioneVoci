package it.infocamere.sipert.distrivoci;

import javafx.application.Application;

import javafx.scene.Node;

import javafx.scene.Scene;

import javafx.scene.control.TreeItem;

import javafx.scene.control.TreeView;

import javafx.scene.image.Image;

import javafx.scene.image.ImageView;

import javafx.scene.layout.StackPane;

import javafx.stage.Stage;

 

/**

 *

 * @web http://javafxportal.blogspot.com/

 */

public class JavaFXtestUI extends Application {

     

    Image nodeImage = new Image("file:resources/images/SETUP.png");


 

    /**

     * @param args the command line arguments

     */

    public static void main(String[] args) {

        launch(args);

    }

     

    @Override

    public void start(Stage primaryStage) {

         

        TreeItem<String> treeItemRoot = new TreeItem<> ("Root");

         

        TreeItem<String> nodeItemA = new TreeItem<>("Item A");

        TreeItem<String> nodeItemB = new TreeItem<>("Item B");

        TreeItem<String> nodeItemC = new TreeItem<>("Item C");

        treeItemRoot.getChildren().addAll(nodeItemA, nodeItemB, nodeItemC);

         

        TreeItem<String> nodeItemA1 = new TreeItem<>("Item A1", 

                new ImageView(nodeImage));

        TreeItem<String> nodeItemA2 = new TreeItem<>("Item A2", 

                new ImageView(nodeImage));

        TreeItem<String> nodeItemA3 = new TreeItem<>("Item A3", 

                new ImageView(nodeImage));

         

        nodeItemA.getChildren().addAll(nodeItemA1, nodeItemA2, nodeItemA3);

         

        TreeView<String> treeView = new TreeView<>(treeItemRoot);

        StackPane root = new StackPane();

        root.getChildren().add(treeView);

         

        Scene scene = new Scene(root, 300, 250);

         

        primaryStage.setTitle("javafxportal.blogspot.com");

        primaryStage.setScene(scene);

        primaryStage.show();

    }

}
