package it.infocamere.sipert.distrivoci.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Voce {

    private final StringProperty codiceVoce;
    private final StringProperty descrizioneVoce;

	public Voce() {
		this(null, null);
	}

	public Voce(String codice, String descrizione) {
		this.codiceVoce = new SimpleStringProperty(codice);
        this.descrizioneVoce = new SimpleStringProperty(descrizione);
	}

	public String getCodice() {
		return codiceVoce.get();
	}

	public void setCodice(String codice) {
		this.codiceVoce.set(codice);
	}

	public String getDescrizione() {
		return descrizioneVoce.get();
	}

	public void setDescrizione(String descrizione) {
		this.descrizioneVoce.set(descrizione);
	}
	
    public StringProperty codiceProperty() {
        return codiceVoce;
    }
	
    public StringProperty descrizioneProperty() {
        return descrizioneVoce;
    }

	
	
}
