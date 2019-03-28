package it.infocamere.sipert.distrivoci.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Voce {

    private final StringProperty codiceVoce;
    private final StringProperty descrizioneVoce;
    private final StringProperty dataOraRipristino;

	public Voce() {
		this(null, null, null);
	}

	public Voce(String codice, String descrizione, String dataOraRipristino) {
		this.codiceVoce = new SimpleStringProperty(codice);
        this.descrizioneVoce = new SimpleStringProperty(descrizione);
        this.dataOraRipristino = new SimpleStringProperty(dataOraRipristino);
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

	public String getDataOraRipristino() {
		return dataOraRipristino.get();
	}
    
    public StringProperty dataOraRipristinoProperty() {
        return dataOraRipristino;
    }

	public void setDataOraRipristino(String dataOraRipristino) {
		this.dataOraRipristino.set(dataOraRipristino);
	}
    
}
