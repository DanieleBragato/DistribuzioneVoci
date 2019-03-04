package it.infocamere.sipert.distrivoci.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeleteStatement {

    private final StringProperty codiceTabella;
    private final StringProperty deleteStatement;
    
    // where condition
    private String whereCondition;
    
    // codice identificativo dello schema di partenza/origine (da sviluppo)
    private String codiceSchemaOrigine;
    
    // elenco delle Insert SQL generate tramite lo schema di partenza/origine (da sviluppo), il codice tabella e la where condition dello statement di Delete 
    private ArrayList<String> insertsListFromSchemaOrigine;
    
    // informazioni relative alla distribuzione in produzione: elenco per schema di distribuzione
    
    private ArrayList<Distribuzione> listaDistribuzione;
    

	public DeleteStatement() {
		this(null, null, null, null);
	}

	public DeleteStatement(String codice, String deleteStatement, String codiceSchemaOrigine,  ArrayList<String> insertsList) {
		this.codiceTabella = new SimpleStringProperty(codice);
        this.deleteStatement = new SimpleStringProperty(deleteStatement);
        this.codiceSchemaOrigine = codiceSchemaOrigine;
        this.insertsListFromSchemaOrigine = insertsList;
	}

	public String getCodice() {
		return codiceTabella.get();
	}

	public void setCodice(String codice) {
		this.codiceTabella.set(codice);
	}

	public String getDeleteStatement() {
		return deleteStatement.get();
	}

	public void setDeleteStatement(String deleteStatement) {
		this.deleteStatement.set(deleteStatement);
	}
	
    public StringProperty codiceProperty() {
        return codiceTabella;
    }
	
    public StringProperty deleteStatementProperty() {
        return deleteStatement;
    }

	public ArrayList<String> getInsertsListFromSchemaOrigine() {
		return insertsListFromSchemaOrigine;
	}

	public void setInsertsListFromSchemaOrigine(ArrayList<String> insertsList) {
		this.insertsListFromSchemaOrigine = insertsList;
	}

	public String getCodiceSchemaOrigine() {
		return codiceSchemaOrigine;
	}

	public void setCodiceSchemaOrigine(String codiceSchemaOrigine) {
		this.codiceSchemaOrigine = codiceSchemaOrigine;
	}

	public ArrayList<Distribuzione> getListaDistribuzione() {
		return listaDistribuzione;
	}

	public void setListaDistribuzione(ArrayList<Distribuzione> listaDistribuzione) {
		this.listaDistribuzione = listaDistribuzione;
	}

	public String getWhereCondition() {
		return whereCondition;
	}

	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}	
	
    
}
