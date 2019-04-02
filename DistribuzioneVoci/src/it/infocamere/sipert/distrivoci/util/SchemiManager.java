package it.infocamere.sipert.distrivoci.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.exception.ErroreColonneFileXlsSchemiKo;
import it.infocamere.sipert.distrivoci.exception.ErroreFileSchemiNonTrovato;
import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class SchemiManager {
	
	private boolean colonneFileXlsSchemiOk = false;
	
	private static final Logger LOGGER = Logger.getLogger(SchemiManager.class.getName());

	public SchemaDTO getSchema(File fileSchemiXLS, String nomeFolder, String codiceSchema) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		SchemaDTO schema = new SchemaDTO();
		
		if (!fileSchemiXLS.exists()) {
			LOGGER.error("Errore File Schemi Non Trovato");
			throw new ErroreFileSchemiNonTrovato();
		}
		
		Workbook workbook; 
		
		try {
			boolean trovatoSchema = false;
			workbook = Workbook.getWorkbook(fileSchemiXLS);
			Sheet sheet = workbook.getSheet(1);
			if (nomeFolder.equalsIgnoreCase(sheet.getName())) {
				
				for (int iRiga = 0; iRiga < sheet.getRows() && !trovatoSchema; iRiga++) {
					
					if (iRiga == 0) colonneFileXlsSchemiOk = checkTestataColonne(sheet.getRow(iRiga));
					
					if (!colonneFileXlsSchemiOk) {
						throw new ErroreColonneFileXlsSchemiKo();
					}
					
					if (colonneFileXlsSchemiOk && iRiga > 0) {
						//SchemaDTO schemaDTO = new SchemaDTO();
						
						for (int iColonna = 0; iColonna < sheet.getColumns(); iColonna++) {

							Cell cell = sheet.getCell(iColonna, iRiga);
							CellType cellType = cell.getType();
							
							if (cellType == CellType.LABEL) {
								if (iColonna == Constants.NUM_COLL_SCHEMA) {
									if (cell.getContents().equalsIgnoreCase(codiceSchema)) {
										schema.setSchemaUserName(cell.getContents());
										trovatoSchema = true;
									} else {
										break;
									}
								}
								if (iColonna == Constants.NUM_COLL_PASSWORD) {
									schema.setPassword(cell.getContents());
								}
								if (iColonna == Constants.NUM_COLL_DBNAME) {
									schema.setDbName(cell.getContents());
								}
								if (iColonna == Constants.NUM_COLL_PORT) {
									schema.setPort(cell.getContents());
								}
								if (iColonna == Constants.NUM_COLL_SERVER) {
									schema.setHostServerURL(cell.getContents());
								}
							}
						}						
					}
				}
			}
		
		} catch (BiffException e) {
			LOGGER.error("Errore nella lettura del file xls delle connessioni " + e.toString());
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		} catch (IOException e) {
			LOGGER.error("Errore nella lettura del file xls delle connessioni " + e.toString());
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		}
		
		return schema;
	}
	
	public List<SchemaDTO> getListSchemi(File fileSchemiXLS, boolean schemiPartenza, boolean schemiArrivo) throws ErroreFileSchemiNonTrovato, ErroreColonneFileXlsSchemiKo {
		
		int qualeFoglio = 0;
		
		if (schemiArrivo) qualeFoglio = 0; 
		if (schemiPartenza) qualeFoglio = 1;
		
		List<SchemaDTO> schemi  = new ArrayList<>() ;
		
		if (!fileSchemiXLS.exists()) {
			LOGGER.error("Errore File Schemi Non Trovato");
			throw new ErroreFileSchemiNonTrovato();
		}
		
		Workbook workbook; 
		
		try {
			
			workbook = Workbook.getWorkbook(fileSchemiXLS);
			Sheet sheet = workbook.getSheet(qualeFoglio);
			
			for (int iRiga = 0; iRiga < sheet.getRows(); iRiga++) {
				
				if (iRiga == 0) colonneFileXlsSchemiOk = checkTestataColonne(sheet.getRow(iRiga));
				
				if (!colonneFileXlsSchemiOk) {
					LOGGER.error("Errore Colonne File Xls Schemi KO");
					throw new ErroreColonneFileXlsSchemiKo();
				}
				
				if (colonneFileXlsSchemiOk && iRiga > 0) {
					
					SchemaDTO schemaDTO = new SchemaDTO();
					
					for (int iColonna = 0; iColonna < sheet.getColumns(); iColonna++) {

						Cell cell = sheet.getCell(iColonna, iRiga);
						CellType cellType = cell.getType();
						
						
						if (cellType == CellType.LABEL) {
							if (iColonna == Constants.NUM_COLL_SCHEMA) {
								schemaDTO.setSchemaUserName(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_PASSWORD) {
								schemaDTO.setPassword(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_DBNAME) {
								schemaDTO.setDbName(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_PORT) {
								schemaDTO.setPort(cell.getContents());
							}
							if (iColonna == Constants.NUM_COLL_SERVER) {
								schemaDTO.setHostServerURL(cell.getContents());
							}
						}
					}					
					schemi.add(schemaDTO) ;
				}
			}
			
		} catch (BiffException e) {
			LOGGER.error("Errore nella lettura del file xls delle connessioni " + e.toString());
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		} catch (IOException e) {
			LOGGER.error("Errore nella lettura del file xls delle connessioni " + e.toString());
			e.printStackTrace();
			throw new RuntimeException("Errore nella lettura del file xls delle connessioni", e);
		}

		return schemi;
	}

	private boolean checkTestataColonne(Cell[] row) {

		int qtaOK = 0;

		for (int i = 0; i < row.length; i++) {
			if (row[i].getType() == CellType.LABEL) {
				if (i == Constants.NUM_COLL_SCHEMA && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SCHEMA)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PASSWORD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PASSWORD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_SCHEMA_AD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SCHEMA_AD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PASSWORD_AD && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PASSWORD_AD)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_DBNAME && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_DBNAME)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_PORT && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_PORT)) {
					qtaOK++;
				}
				if (i == Constants.NUM_COLL_SERVER && row[i].getContents().equalsIgnoreCase(Constants.NOME_COLL_SERVER)) {
					qtaOK++;
				}
			}
		}
		if (qtaOK == row.length) {
			return true;
		}
		return false;
	}

}
