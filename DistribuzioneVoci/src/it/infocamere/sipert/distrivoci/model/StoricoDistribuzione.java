package it.infocamere.sipert.distrivoci.model;

import java.util.ArrayList;
import java.util.Date;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;

public class StoricoDistribuzione {
	
	private StringProperty dataOraDistribuzione;
	
	private StringProperty note;
	
    // informazioni relative alla distribuzione in produzione: elenco delle DeleteStatement
    
    private ArrayList<DeleteStatement> listaDeleteStatement;

    public StoricoDistribuzione() {
    	this(null, null, null);
    }
    

	public StoricoDistribuzione(StringProperty dataOraDistribuzione, StringProperty note,
			ArrayList<DeleteStatement> listaDeleteStatement) {
		
		this.dataOraDistribuzione = dataOraDistribuzione;
		this.note = note;
		this.listaDeleteStatement = listaDeleteStatement;
	}



	public ArrayList<DeleteStatement> getListaDeleteStatement() {
		return listaDeleteStatement;
	}

	public void setListaDeleteStatement(ArrayList<DeleteStatement> listaDeleteStatement) {
		this.listaDeleteStatement = listaDeleteStatement;
	}


	public String getDataOraDistribuzione() {
		return dataOraDistribuzione.get();
	}

	public void setDataOraDistribuzione(String dataOraDistribuzione) {
		this.dataOraDistribuzione.set(dataOraDistribuzione);
	}

    public StringProperty dataOraDistribuzioneProperty() {
        return dataOraDistribuzione;
    }

	public String getNote() {
		return note.get();
	}

	public void setNote(String note) {
		this.note.set(note);
	}

    public StringProperty noteProperty() {
        return note;
    }
	
}
