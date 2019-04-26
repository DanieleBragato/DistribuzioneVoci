package it.infocamere.sipert.distrivoci.util;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LoadingDialog {

    private final Stage dialogStage;
    private final ProgressIndicator progressIndicator = new ProgressIndicator();

    public LoadingDialog() {
        dialogStage = new Stage();
        
        dialogStage.setMinWidth(1090);
        dialogStage.setMinHeight(870);
        
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("LOADING");
        final Label label = new Label();
        label.setText("Please wait...");
        progressIndicator.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
        
        // changing color with css
        progressIndicator.setStyle(" -fx-progress-color: blue;");
        // changing size without css
        progressIndicator.setMinWidth(250);
        progressIndicator.setMinHeight(250);
        
        final VBox vb = new VBox();
        
        vb.setStyle("-fx-background-color: rgba(255, 255, 255, 0);");
        
        vb.setMinWidth(1000);
        vb.setMinHeight(800);
        
        vb.setSpacing(5);
        vb.setAlignment(Pos.CENTER);
        vb.getChildren().addAll(progressIndicator);
        Scene scene = new Scene(vb);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
    }

    public void activateProgressBar(final Task task) throws InterruptedException {
        progressIndicator.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
