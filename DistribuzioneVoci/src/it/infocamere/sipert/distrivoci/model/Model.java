package it.infocamere.sipert.distrivoci.model;

import java.io.File;
import java.util.List;

import it.infocamere.sipert.distrivoci.db.QueryDB;
import it.infocamere.sipert.distrivoci.db.dao.GenericDAO;
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.distrivoci.exception.ErroreFileSchemiNonTrovato;
import it.infocamere.sipert.distrivoci.util.SchemiManager;

public class Model {

	private List<SchemaDTO> schemi;
	
	boolean schemiPartenza = false;
	boolean schemiArrivo = false;
	
	public List<SchemaDTO> getSchemi(File fileSchemiXLS) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		schemiArrivo = false;
		
		if (!schemiPartenza) schemiArrivo = true;
		
		//if (this.schemi == null) {
			SchemiManager schemiManager = new SchemiManager();
			this.schemi = schemiManager.getListSchemi(fileSchemiXLS , schemiPartenza , schemiArrivo) ;

			//System.out.println("Trovati " +  this.schemi.size() + " schemi");
		//}

		return this.schemi ;

	}
	
	public List<SchemaDTO> getSchemiPartenza(File fileSchemiXLS) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		schemiPartenza = true;
		
		return getSchemi(fileSchemiXLS);
	}
	
	public SchemaDTO getSchema(File fileSchemiXLS, String nomeFolder, String codiceSchema) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		SchemaDTO schema = new SchemaDTO();
		SchemiManager schemiManager = new SchemiManager();
		schema = schemiManager.getSchema(fileSchemiXLS, nomeFolder, codiceSchema);
		
		return schema;
		
	}
	
	public boolean testConnessioneDB(SchemaDTO schemaDB) {
		
		GenericDAO genericDAO = new GenericDAO();
		
		return genericDAO.testConnessioneOK(schemaDB);
		
	}
	
	public GenericResultsDTO runQuery(SchemaDTO schema, QueryDB queryDB, boolean createListOfLinkedHashMap,
			boolean createListOfInsert) {
		
		GenericResultsDTO risultati = null;
		
		GenericDAO genericDAO = new GenericDAO();
		risultati = genericDAO.execute(schema, queryDB);
		
		return risultati;
	}
	
	public GenericResultsDTO runQueryForGenerateInserts(SchemaDTO schema, QueryDB queryDB, String tableName) {
		
		System.out.println("classe Model metodo runQueryForGenerateInserts");
		
		GenericResultsDTO risultati = null;
		
		GenericDAO genericDAO = new GenericDAO();
		risultati = genericDAO.executeQueryForGenerateInserts(schema, queryDB, tableName);
		
		return risultati;
	}

	public GenericResultsDTO runUpdate(SchemaDTO schema, QueryDB queryDB) {
		
		System.out.println("classe Model metodo runUpdate");
		
		GenericResultsDTO risultati = null;
		
		GenericDAO genericDAO = new GenericDAO();
		risultati = genericDAO.executeUpdate(schema, queryDB);
		
		return risultati;
	}
	
}
