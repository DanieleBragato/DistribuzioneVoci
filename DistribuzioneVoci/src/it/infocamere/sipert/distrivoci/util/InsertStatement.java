package it.infocamere.sipert.distrivoci.util;

public class InsertStatement {

	private String Voce = "";
	private String InsertStatement = "";
	
	public String getVoce() {
		return Voce;
	}
	public void setVoce(String voce) {
		Voce = voce;
	}
	public String getInsertStatement() {
		return InsertStatement;
	}
	public void setInsertStatement(String insertStatement) {
		InsertStatement = insertStatement;
	}
	@Override
	public String toString() {
		return "InsertStatemenrt [Voce=" + Voce + ", InsertStatement=" + InsertStatement + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((InsertStatement == null) ? 0 : InsertStatement.hashCode());
		result = prime * result + ((Voce == null) ? 0 : Voce.hashCode());
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
		InsertStatement other = (InsertStatement) obj;
		if (InsertStatement == null) {
			if (other.InsertStatement != null)
				return false;
		} else if (!InsertStatement.equals(other.InsertStatement))
			return false;
		if (Voce == null) {
			if (other.Voce != null)
				return false;
		} else if (!Voce.equals(other.Voce))
			return false;
		return true;
	}
	
}
