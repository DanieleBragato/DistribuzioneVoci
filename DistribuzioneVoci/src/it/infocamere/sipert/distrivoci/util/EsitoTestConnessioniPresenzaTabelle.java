package it.infocamere.sipert.distrivoci.util;

public class EsitoTestConnessioniPresenzaTabelle {

	private boolean esitoGlobale = false;
	private String CausaEsitoKO = "";
	
	public EsitoTestConnessioniPresenzaTabelle(boolean esitoGlobale, String causaEsitoKO) {
		super();
		this.esitoGlobale = esitoGlobale;
		CausaEsitoKO = causaEsitoKO;
	}
	
	public EsitoTestConnessioniPresenzaTabelle() {
		// TODO Auto-generated constructor stub
	}
	public boolean isEsitoGlobale() {
		return esitoGlobale;
	}
	public void setEsitoGlobale(boolean esitoGlobale) {
		this.esitoGlobale = esitoGlobale;
	}
	public String getCausaEsitoKO() {
		return CausaEsitoKO;
	}
	public void setCausaEsitoKO(String causaEsitoKO) {
		CausaEsitoKO = causaEsitoKO;
	}
	
	
	
}
