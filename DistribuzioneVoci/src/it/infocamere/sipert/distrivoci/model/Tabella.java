package it.infocamere.sipert.distrivoci.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Tabella {

	
    private final StringProperty codiceTabella;
    private final StringProperty descrizioneTabella;

	public Tabella() {
		this(null, null);
	}

	public Tabella(String codice, String descrizione) {
		this.codiceTabella = new SimpleStringProperty(codice);
        this.descrizioneTabella = new SimpleStringProperty(descrizione);
	}

	public String getCodice() {
		return codiceTabella.get();
	}

	public void setCodice(String codice) {
		this.codiceTabella.set(codice);
	}

	public String getDescrizione() {
		return descrizioneTabella.get();
	}

	public void setDescrizione(String descrizione) {
		this.descrizioneTabella.set(descrizione);
	}
	
    public StringProperty codiceProperty() {
        return codiceTabella;
    }
	
    public StringProperty descrizioneProperty() {
        return descrizioneTabella;
    }

	
	
}
