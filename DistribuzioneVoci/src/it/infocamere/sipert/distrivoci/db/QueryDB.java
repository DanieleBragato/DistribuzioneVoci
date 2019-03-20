package it.infocamere.sipert.distrivoci.db;

public class QueryDB {
	
	private String tableName;
	private String query;
	private String operationType;
	private String selectStatement;
	
	public QueryDB() {
		super();
	}
	
	/**
	 * 
	 * @param tableName
	 * @param query
	 * @param operationType
	 * @param selectStatement
	 */
	
	public QueryDB(String tableName, String query, String operationType, String selectStatement) {
		super();
		this.tableName = tableName;
		this.query = query;
		this.operationType = operationType;
		this.selectStatement = selectStatement;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getQuery() {
		return query;
	}
	public void setQuery(String query) {
		this.query = query;
	}
	public String getOperationType() {
		return operationType;
	}
	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	
	public String getSelectStatement() {
		return selectStatement;
	}

	public void setSelectStatement(String selectStatement) {
		this.selectStatement = selectStatement;
	}

	@Override
	public String toString() {
		return "QueryDB [tableName=" + tableName + ", query=" + query + ", operationType=" + operationType
				+ ", selectStatement=" + selectStatement + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((operationType == null) ? 0 : operationType.hashCode());
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
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
		QueryDB other = (QueryDB) obj;
		if (operationType == null) {
			if (other.operationType != null)
				return false;
		} else if (!operationType.equals(other.operationType))
			return false;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}

	
	
}
