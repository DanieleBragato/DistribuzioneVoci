package it.infocamere.sipert.distrivoci.db.dto;

public class DistributionResultsDTO extends GenericResultsDTO {

	private String tableName;

	public DistributionResultsDTO(String tableName) {
		super();
		this.tableName = tableName;
	}

	public DistributionResultsDTO() {
		// TODO Auto-generated constructor stub
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String toString() {
		return "DistributionResultsDTO [tableName=" + tableName + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((tableName == null) ? 0 : tableName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		DistributionResultsDTO other = (DistributionResultsDTO) obj;
		if (tableName == null) {
			if (other.tableName != null)
				return false;
		} else if (!tableName.equals(other.tableName))
			return false;
		return true;
	}
	
	
	
}
