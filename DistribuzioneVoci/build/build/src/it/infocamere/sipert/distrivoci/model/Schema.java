package it.infocamere.sipert.distrivoci.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Schema {

    private final StringProperty codiceSchema;
    private final StringProperty descrizioneSchema;

	public Schema() {
		this(null, null);
	}

	public Schema(String codice, String descrizione) {
		this.codiceSchema = new SimpleStringProperty(codice);
        this.descrizioneSchema = new SimpleStringProperty(descrizione);
	}

	public String getCodice() {
		return codiceSchema.get();
	}

	public void setCodice(String codice) {
		this.codiceSchema.set(codice);
	}

	public String getDescrizione() {
		return descrizioneSchema.get();
	}

	public void setDescrizione(String descrizione) {
		this.descrizioneSchema.set(descrizione);
	}
	
    public StringProperty codiceProperty() {
        return codiceSchema;
    }
	
    public StringProperty descrizioneProperty() {
        return descrizioneSchema;
    }

	
	
}
