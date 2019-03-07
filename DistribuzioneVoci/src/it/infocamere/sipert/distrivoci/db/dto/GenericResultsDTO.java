package it.infocamere.sipert.distrivoci.db.dto;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class GenericResultsDTO {

	private String schema = null;
	private List<LinkedHashMap<String, Object>> listLinkedHashMap = new ArrayList<LinkedHashMap<String,Object>>();
	
	private List<String> listString = new ArrayList<String>();
	
	private int rowsUpdated = 0;
	

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

	public List<String> getListString() {
		return listString;
	}

	public void setListString(List<String> listString) {
		this.listString = listString;
	}
	
	@Override
	public String toString() {
		return "GenericResultsDTO [schema=" + schema + ", listLinkedHashMap=" + listLinkedHashMap + ", listString="
				+ listString + ", rowsUpdated=" + rowsUpdated + "]";
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

}
