package it.infocamere.sipert.distrivoci.util;

import javafx.concurrent.Task;

public class BackgroundTask {
	
    public void execTask(Task task) throws InterruptedException {
        LoadingDialog loadingDialog = new LoadingDialog();
        loadingDialog.activateProgressBar(task);
        task.setOnSucceeded(event -> {
            loadingDialog.getDialogStage().close();
        });
        task.setOnCancelled(event -> {
            loadingDialog.getDialogStage().close();
        });
        loadingDialog.getDialogStage().show();
        Thread thread = new Thread(task);
        thread.start();
        
        synchronized(thread){
            try{        
            	thread.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

}
