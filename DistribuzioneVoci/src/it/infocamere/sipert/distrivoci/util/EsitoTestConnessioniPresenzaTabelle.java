package it.infocamere.sipert.distrivoci.util;

import java.util.LinkedHashMap;

public class EsitoTestConnessioniPresenzaTabelle {

	private boolean esitoGlobale = false;
	private String CausaEsitoKO = "";
	
	LinkedHashMap<String , ColumnsType> listTablesColumnsType = new LinkedHashMap<String , ColumnsType>();
	
	
	public EsitoTestConnessioniPresenzaTabelle(boolean esitoGlobale, String causaEsitoKO, LinkedHashMap<String , ColumnsType> listTablesColumnsType) {
		super();
		this.esitoGlobale = esitoGlobale;
		this.CausaEsitoKO = causaEsitoKO;
		this.listTablesColumnsType = listTablesColumnsType;
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

	public LinkedHashMap<String, ColumnsType> getListTablesColumnsType() {
		return listTablesColumnsType;
	}

	public void setListTablesColumnsType(LinkedHashMap<String, ColumnsType> listTablesColumnsType) {
		this.listTablesColumnsType = listTablesColumnsType;
	}
	
	
	
}
