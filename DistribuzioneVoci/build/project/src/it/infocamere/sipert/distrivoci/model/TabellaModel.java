package it.infocamere.sipert.distrivoci.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TabellaModel {

    private final StringProperty nome;
    private final StringProperty descrizione;
    
    public TabellaModel() {
    	this(null, null);
    }
    
    /**
     * 
     * @param nome
     * @param descrizione
     */
    
	public TabellaModel(String nome, String descrizione) {
		
        this.nome = new SimpleStringProperty(nome);
        this.descrizione = new SimpleStringProperty(descrizione);
	}
	
	public String getNome() {
		return nome.get();
	}
	public String getDescrizione() {
		return descrizione.get();
	}
	
    public void setNome(String nome) {
        this.nome.set(nome);
    }
    public void setDescrizione(String descrizione) {
        this.descrizione.set(descrizione);
    }
    
    public StringProperty descrizioneProperty() {
        return descrizione;
    }
    
}
