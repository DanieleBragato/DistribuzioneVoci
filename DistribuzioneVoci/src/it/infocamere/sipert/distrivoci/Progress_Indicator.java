package it.infocamere.sipert.distrivoci;  
import javafx.application.Application;  
import javafx.scene.Scene;  
import javafx.scene.control.ProgressIndicator;  
import javafx.scene.layout.StackPane;  
import javafx.stage.Stage;  

public class Progress_Indicator extends Application{  
  
    @Override  
    public void start(Stage primaryStage) throws Exception {  
        // TODO Auto-generated method stub  
        ProgressIndicator PI=new ProgressIndicator(400);  
          
        StackPane root = new StackPane();  
        root.getChildren().add(PI);  
        Scene scene = new Scene(root,300,200);  
        primaryStage.setScene(scene);  
        primaryStage.setTitle("Progress Indicator Example");  
        primaryStage.show();  
          
    }  
    public static void main(String[] args) {  
        launch(args);  
    }  
  
}  