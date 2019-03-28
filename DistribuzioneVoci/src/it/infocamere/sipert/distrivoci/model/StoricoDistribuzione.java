package it.infocamere.sipert.distrivoci.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class StoricoDistribuzione {
	
	private StringProperty dataOraDistribuzione;
	
	private StringProperty dataOraRipristino;
	
	private StringProperty note;
	
	private StringProperty schemaPartenza;
	
	// informazioni relative alla distribuzione in produzione: elenco delle Voci distribuite
    
    private ArrayList<Voce> elencoVoci;
    
	// informazioni relative alla distribuzione in produzione: elenco degli schemi
	// sui quali è stata effettuata la distribuzione
    
    private ArrayList<Schema> elencoSchemi;
	
    // informazioni relative alla distribuzione in produzione: elenco delle DeleteStatement
    
    private ArrayList<DeleteStatement> listaDeleteStatement;

    public StoricoDistribuzione() {
    	this(null, null, null, null, null, null, null);
    }
    
	public StoricoDistribuzione(String dataOraDistribuzione, String dataOraRipristino, String note, String schemaPartenza,
			ArrayList<Voce> elencoVoci,   ArrayList<Schema> elencoSchemi, ArrayList<DeleteStatement> listaDeleteStatement) {
		
		this.dataOraDistribuzione = new SimpleStringProperty(dataOraDistribuzione);
		this.dataOraRipristino = new SimpleStringProperty(dataOraRipristino);
        this.note = new SimpleStringProperty(note);
        this.schemaPartenza = new SimpleStringProperty(schemaPartenza);
        this.elencoVoci = elencoVoci;
        this.elencoSchemi = elencoSchemi;
        this.listaDeleteStatement = listaDeleteStatement;
	}

	
	public ArrayList<Voce> getElencoVoci() {
		return elencoVoci;
	}

	public void setElencoVoci(ArrayList<Voce> elencoVoci) {
		this.elencoVoci = elencoVoci;
	}
	
	public ArrayList<Schema> getElencoSchemi() {
		return elencoSchemi;
	}

	public void setElencoSchemi(ArrayList<Schema> elencoSchemi) {
		this.elencoSchemi = elencoSchemi;
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

	public String getDataOraRipristino() {
		return dataOraRipristino.get();
	}

	public void setDataOraRipristino(String dataOraRipristino) {
		this.dataOraRipristino.set(dataOraRipristino);
	}

    public StringProperty dataOraRipristinoProperty() {
        return dataOraRipristino;
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
	
	public String getSchemaPartenza() {
		return schemaPartenza.get();
	}

	public void setSchemaPartenza(String schemaPartenza) {
		this.schemaPartenza.set(schemaPartenza);
	}

    public StringProperty schemaPartenzaProperty() {
        return schemaPartenza;
    }
    
}
