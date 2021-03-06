package it.infocamere.sipert.distrivoci.db.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import it.infocamere.sipert.distrivoci.util.InsertStatement;

public class GenericResultsDTO {

	private String schema = null;
	private List<LinkedHashMap<String, Object>> listLinkedHashMap = new ArrayList<LinkedHashMap<String,Object>>();
	
	private List<InsertStatement> insertsForBackup = new ArrayList<InsertStatement>();
	
	private int rowsUpdated = 0;
	
	private int rowsDeleted = 0;
	
	private int rowsInserted = 0;
	

	public List<LinkedHashMap<String, Object>> getListLinkedHashMap() {
		return listLinkedHashMap;
	}

	public void setListLinkedHashMap(List<LinkedHashMap<String, Object>> listLinkedHashMap) {
		this.listLinkedHashMap = listLinkedHashMap;
	}

	public GenericResultsDTO() {
		super();
	}

	public GenericResultsDTO(String schema, List<LinkedHashMap<String, Object>> listLinkedHashMap) {
		super();
		this.schema = schema;
		this.listLinkedHashMap = listLinkedHashMap;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}
	

	
	public List<InsertStatement> getInsertsForBackup() {
		return insertsForBackup;
	}

	public void setInsertsForBackup(List<InsertStatement> insertsForBackup) {
		this.insertsForBackup = insertsForBackup;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schema == null) ? 0 : schema.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericResultsDTO other = (GenericResultsDTO) obj;
		if (schema == null) {
			if (other.schema != null)
				return false;
		} else if (!schema.equals(other.schema))
			return false;
		return true;
	}

	public int getRowsUpdated() {
		return rowsUpdated;
	}

	public void setRowsUpdated(int rowsUpdated) {
		this.rowsUpdated = rowsUpdated;
	}

	public int getRowsDeleted() {
		return rowsDeleted;
	}

	public void setRowsDeleted(int rowsDeleted) {
		this.rowsDeleted = rowsDeleted;
	}

	public int getRowsInserted() {
		return rowsInserted;
	}

	public void setRowsInserted(int rowsInserted) {
		this.rowsInserted = rowsInserted;
	}

	@Override
	public String toString() {
		return "GenericResultsDTO [schema=" + schema + ", listLinkedHashMap=" + listLinkedHashMap
				+ ", insertsForBackup=" + insertsForBackup + ", rowsUpdated=" + rowsUpdated + ", rowsDeleted="
				+ rowsDeleted + ", rowsInserted=" + rowsInserted + "]";
	}
	
}
