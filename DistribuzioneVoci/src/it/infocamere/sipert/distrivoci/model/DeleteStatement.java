package it.infocamere.sipert.distrivoci.model;

import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class DeleteStatement {

    private final StringProperty codiceTabella;
    private final StringProperty deleteStatement;
    
    private ArrayList<String> insertsList;

	public DeleteStatement() {
		this(null, null, null);
	}

	public DeleteStatement(String codice, String deleteStatement, ArrayList<String> insertsList) {
		this.codiceTabella = new SimpleStringProperty(codice);
        this.deleteStatement = new SimpleStringProperty(deleteStatement);
        this.insertsList = insertsList;
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

	public ArrayList<String> getInsertsList() {
		return insertsList;
	}

	public void setInsertsList(ArrayList<String> insertsList) {
		this.insertsList = insertsList;
	}	
    
}
