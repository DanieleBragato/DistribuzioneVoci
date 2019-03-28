package it.infocamere.sipert.distrivoci.util;

import java.util.LinkedHashMap;

public class ColumnsType {
	
	private LinkedHashMap<String , Integer> columnsType;

	public LinkedHashMap<String, Integer> getColumnsType() {
		return columnsType;
	}

	public void setColumnsType(LinkedHashMap<String, Integer> columnsType) {
		this.columnsType = columnsType;
	}

	@Override
	public String toString() {
		return "ColumnsType [columnsType=" + columnsType + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((columnsType == null) ? 0 : columnsType.hashCode());
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
		ColumnsType other = (ColumnsType) obj;
		if (columnsType == null) {
			if (other.columnsType != null)
				return false;
		} else if (!columnsType.equals(other.columnsType))
			return false;
		return true;
	} 
	
	

}
