package it.infocamere.sipert.distrivoci.model;

import java.util.Date;
import java.util.List;

import it.infocamere.sipert.distrivoci.util.InsertStatement;

public class Distribuzione {
	
	private String codiceSchema;
	private int contatoreInsertGeneratePerBackup;
	private List<InsertStatement> listaInsertGeneratePerBackup;
	private int contatoreRigheCancellate;
	private int contatoreRigheDistribuite;
	
	private Date dataOraDistribuzione;

	public String getCodiceSchema() {
		return codiceSchema;
	}

	public void setCodiceSchema(String codiceSchema) {
		this.codiceSchema = codiceSchema;
	}

	public int getContatoreInsertGeneratePerBackup() {
		return contatoreInsertGeneratePerBackup;
	}

	public void setContatoreInsertGeneratePerBackup(int contatoreInsertGeneratePerBackup) {
		this.contatoreInsertGeneratePerBackup = contatoreInsertGeneratePerBackup;
	}

	public List<InsertStatement> getListaInsertGeneratePerBackup() {
		return listaInsertGeneratePerBackup;
	}

	public void setListaInsertGeneratePerBackup(List<InsertStatement> listaInsertGeneratePerBackup) {
		this.listaInsertGeneratePerBackup = listaInsertGeneratePerBackup;
	}

	public int getContatoreRigheCancellate() {
		return contatoreRigheCancellate;
	}

	public void setContatoreRigheCancellate(int contatoreRigheCancellate) {
		this.contatoreRigheCancellate = contatoreRigheCancellate;
	}

	public int getContatoreRigheDistribuite() {
		return contatoreRigheDistribuite;
	}

	public void setContatoreRigheDistribuite(int contatoreRigheDistribuite) {
		this.contatoreRigheDistribuite = contatoreRigheDistribuite;
	}

	public Date getDataOraDistribuzione() {
		return dataOraDistribuzione;
	}

	public void setDataOraDistribuzione(Date dataOraDistribuzione) {
		this.dataOraDistribuzione = dataOraDistribuzione;
	}

	@Override
	public String toString() {
		return "Distribuzione [codiceSchema=" + codiceSchema + ", contatoreInsertGeneratePerBackup="
				+ contatoreInsertGeneratePerBackup + ", listaInsertGeneratePerBackup=" + listaInsertGeneratePerBackup
				+ ", contatoreRigheCancellate=" + contatoreRigheCancellate + ", contatoreRigheDistribuite="
				+ contatoreRigheDistribuite + ", dataOraDistribuzione=" + dataOraDistribuzione + "]";
	}
	
}
