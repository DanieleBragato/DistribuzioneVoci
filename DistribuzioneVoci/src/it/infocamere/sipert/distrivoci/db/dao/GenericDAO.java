package it.infocamere.sipert.distrivoci.db.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import it.infocamere.sipert.distrivoci.db.DBConnect;
import it.infocamere.sipert.distrivoci.db.QueryDB;
import it.infocamere.sipert.distrivoci.db.dto.DistributionResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;
import it.infocamere.sipert.distrivoci.util.Constants;
import it.infocamere.sipert.distrivoci.util.EsitoTestConnessioniPresenzaTabelle;

public class GenericDAO {
	
    private static final SimpleDateFormat dateFormat = 
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    private static String tableName = "";
    private static boolean createListOfLinkedHashMap = true;
    private static boolean createListOfInsert = false;
    private static boolean executeUpdate = false;
    
    private ArrayList<DistributionResultsDTO> listaRisultatiDistribuzione = new ArrayList<DistributionResultsDTO>();
	
	public boolean testConnessioneOK (SchemaDTO schemaDTO) {
		
		SchemaDTO schemaDB = schemaDTO;
		
		try {
			Connection conn = DBConnect.getConnection(schemaDB);
			conn.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw new RuntimeException("Errore nella connessione", e);
		}
	}
	
	public EsitoTestConnessioniPresenzaTabelle testConnessionePresenzaTabelle (SchemaDTO schemaDTO, ArrayList<QueryDB> listaQueryDB) {
		
		EsitoTestConnessioniPresenzaTabelle esitoTestConnessioniPresenzaTabelle = new EsitoTestConnessioniPresenzaTabelle();
		
		boolean esito = false;
		String causa = "";
		SchemaDTO schemaDB = schemaDTO;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnect.getConnection(schemaDB);
			System.out.println("classe GenericDAO metodo testConnessionePresenzaTabelle - post DBConnect.getConnection...");
			for (int i = 0; i < listaQueryDB.size(); i++) {
				QueryDB queryDB = listaQueryDB.get(i);
				tableName = queryDB.getTableName();
				preparedStatement = conn.prepareStatement(queryDB.getQuery());
				rs = preparedStatement.executeQuery();
				rs.next();
				java.math.BigDecimal countBiDec = (java.math.BigDecimal) rs.getObject(1);
				if (countBiDec.compareTo(BigDecimal.ZERO) == 0) {
					causa = "Non trovata Tabella " + tableName + " su Schema " + schemaDB.getSchemaUserName(); 
					break;
				}
				if (i == listaQueryDB.size() - 1) esito = true;
			}
			
			
		} catch (SQLSyntaxErrorException e) {
			esito = false;
			throw new RuntimeException(e.toString() + "Schema " + schemaDB.getSchemaUserName() + " tabella " + this.tableName + " SQL " + preparedStatement , e);
		} catch (SQLException e) {
			esito = false;
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (rs != null) {
				try {
					rs.close();
					System.out.println("classe GenericDAO metodo testConnessionePresenzaTabelle - post rs.close()");
				} catch (SQLException e) {	
					esito = false;
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
					System.out.println("classe GenericDAO metodo testConnessionePresenzaTabelle - post preparedStatement.close()");
				} catch (SQLException e) {	
					esito = false;
				}
			}
			if (conn != null) {
				try {
					conn.close();
					System.out.println("classe GenericDAO metodo testConnessionePresenzaTabelle - post conn.close()");
				} catch (SQLException e) {	
					esito = false;
				}
			}
		}	
		
		esitoTestConnessioniPresenzaTabelle.setEsitoGlobale(esito);
		if (!esito) esitoTestConnessioniPresenzaTabelle.setCausaEsitoKO(causa); 
		
		return esitoTestConnessioniPresenzaTabelle;
	}
	
	public GenericResultsDTO executeQueryForGenerateInserts(SchemaDTO schemaDTO, QueryDB queryDB, String tableName) {
		
		System.out.println("classe GenericDAO metodo executeQueryForGenerateInserts");
		
		this.tableName = tableName;
		executeUpdate = false;
		createListOfLinkedHashMap = false;
		createListOfInsert = true;
		
		return execute(schemaDTO, queryDB);
		
	}
	
	public GenericResultsDTO executeUpdate(SchemaDTO schemaDTO, QueryDB queryDB) {
		
		System.out.println("classe GenericDAO metodo executeUpdate");
		
		executeUpdate = true;
		
		GenericResultsDTO results;
		return results = execute(schemaDTO, queryDB);
		
	}
	
	public ArrayList<DistributionResultsDTO> executeMultipleUpdateForDistribution(SchemaDTO schemaDTO, ArrayList<QueryDB> listaUpdate) {
		
		System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution");
		
		GenericResultsDTO results = new GenericResultsDTO();
		
		ArrayList<DistributionResultsDTO> listaRisultati = null;
		
		results.setSchema(schemaDTO.getSchemaUserName());
		
		SchemaDTO schemaDB = schemaDTO;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			
			conn = DBConnect.getConnection(schemaDB);
			
			System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution - post DBConnect.getConnection...");
			
			for (int i = 0; i < listaUpdate.size(); i++) {
				
				QueryDB updateDB = listaUpdate.get(i);
				tableName = updateDB.getTableName();
				System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution - SQL = " + updateDB.getQuery());
				
				if (updateDB.getOperationType().toUpperCase().contains(Constants.DELETE)) {
					// IMPOSTAZIONE ED ESECUZIONE DELLA SELECT (per le insert di backup)
					this.tableName = updateDB.getTableName();
					preparedStatement = conn.prepareStatement(updateDB.getSelectStatement());
					rs = preparedStatement.executeQuery();
					// impostare la lista delle insert di backup nel corretto oggetto appartenente alla lista di risultati da ritornare in output					
					aggiornaListaRisultati(schemaDB.getSchemaUserName(), updateDB.getTableName(),
							Constants.SELECT, Constants.ZERO, convertResultSetToListOfString(rs));
					// IMPOSTAZIONE ED ESECUZIONE DELLA DELETE con aggiornamento dei relativi risultati da ritornare in output
					preparedStatement = conn.prepareStatement(updateDB.getQuery());
					aggiornaListaRisultati(schemaDB.getSchemaUserName(), updateDB.getTableName(),
							Constants.DELETE, preparedStatement.executeUpdate(), null);
					
				}
				if (updateDB.getOperationType().toUpperCase().contains(Constants.INSERT)) {
					this.tableName = updateDB.getTableName();
					// IMPOSTAZIONE ED ESECUZIONE DELLA INSERT con aggiornamento dei relativi risultati da ritornare in output
					preparedStatement = conn.prepareStatement(updateDB.getQuery());
					aggiornaListaRisultati(schemaDB.getSchemaUserName(), updateDB.getTableName(),
							Constants.INSERT, preparedStatement.executeUpdate(), null);
				}
			}
			
			
		} catch (SQLSyntaxErrorException e) {
			throw new RuntimeException(e.toString() + "Schema " + schemaDTO.getSchemaUserName() + " tabella " + this.tableName + " SQL " + preparedStatement , e);
		} catch (SQLException e) {
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (rs != null) {
				try {
					rs.close();
					System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution - post rs.close()");
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
					System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution - post preparedStatement.close()");
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
					System.out.println("classe GenericDAO metodo executeMultipleUpdateForDistribution - post conn.close()");
				} catch (SQLException e) {
				}
			}
		}
		
		listaRisultati = listaRisultatiDistribuzione;
		
		return listaRisultati;
		
	}
	
	private void aggiornaListaRisultati(String schema, String tableName, String tipoOperazione, int contatoreRigheInteressate,
			List<String> elencoInsertPerBackup) {
		
		boolean aggiungiAllaLista = false;
		
		DistributionResultsDTO distributionResultsDTO = null;
		
		// ricerca se già presente l'elemento corrispondente alla tabella trattata
		
		for (DistributionResultsDTO drDTO : listaRisultatiDistribuzione) {
			if (drDTO.getTableName().equalsIgnoreCase(tableName)) {
				// è già presente sulla lista
				distributionResultsDTO = drDTO;
				break;
			}
		}
		
		if (distributionResultsDTO == null) {
			// NON è già presente sulla lista >> viene creato
			distributionResultsDTO = new DistributionResultsDTO();
			distributionResultsDTO.setTableName(tableName);
			aggiungiAllaLista = true;
		}
		
		distributionResultsDTO.setSchema(schema);
		
		if (tipoOperazione.equalsIgnoreCase(Constants.SELECT)) {
			distributionResultsDTO.setListString(elencoInsertPerBackup);
		}
		if (tipoOperazione.equalsIgnoreCase(Constants.INSERT)) {
			distributionResultsDTO.setRowsInserted(distributionResultsDTO.getRowsInserted() + contatoreRigheInteressate);
		}
		if (tipoOperazione.equalsIgnoreCase(Constants.DELETE)) {
			distributionResultsDTO.setRowsDeleted(contatoreRigheInteressate);
		}
		if (aggiungiAllaLista) {
			listaRisultatiDistribuzione.add(distributionResultsDTO);
		}

	}
	
	public ArrayList<DistributionResultsDTO> executeMultipleUpdateForRipristino(SchemaDTO schemaDTO, ArrayList<QueryDB> listaUpdate) {
		
		System.out.println("classe GenericDAO metodo executeMultipleUpdateForRipristino");
		
		GenericResultsDTO results = new GenericResultsDTO();
		
		ArrayList<DistributionResultsDTO> listaRisultati = null;
		
		results.setSchema(schemaDTO.getSchemaUserName());
		
		SchemaDTO schemaDB = schemaDTO;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		
		try {
			
			conn = DBConnect.getConnection(schemaDB);
			
			System.out.println("classe GenericDAO metodo executeMultipleUpdateForRipristino - post DBConnect.getConnection... SCHEMA " + schemaDB.getSchemaUserName());
			
			for (int i = 0; i < listaUpdate.size(); i++) {
				
				QueryDB updateDB = listaUpdate.get(i);
				System.out.println("classe GenericDAO metodo executeMultipleUpdateForRipristino - SQL = " + updateDB.getQuery());
				
				if (updateDB.getOperationType().toUpperCase().contains(Constants.DELETE)) {
					this.tableName = updateDB.getTableName();					
					// IMPOSTAZIONE ED ESECUZIONE DELLA DELETE con aggiornamento dei relativi risultati da ritornare in output
					preparedStatement = conn.prepareStatement(updateDB.getQuery());
					aggiornaListaRisultati(schemaDB.getSchemaUserName(), updateDB.getTableName(),
							Constants.DELETE, preparedStatement.executeUpdate(), null);
					
				}
				if (updateDB.getOperationType().toUpperCase().contains(Constants.INSERT)) {
					this.tableName = updateDB.getTableName();
					// IMPOSTAZIONE ED ESECUZIONE DELLA INSERT con aggiornamento dei relativi risultati da ritornare in output
					preparedStatement = conn.prepareStatement(updateDB.getQuery());
					aggiornaListaRisultati(schemaDB.getSchemaUserName(), updateDB.getTableName(),
							Constants.INSERT, preparedStatement.executeUpdate(), null);
				}
			}
			
			
		} catch (SQLSyntaxErrorException e) {
			throw new RuntimeException(e.toString() + "Schema " + schemaDTO.getSchemaUserName() + " tabella " + this.tableName + " SQL " + preparedStatement , e);
		} catch (SQLException e) {
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
					System.out.println("classe GenericDAO metodo executeMultipleUpdateForRipristino - post preparedStatement.close()");
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
					System.out.println("classe GenericDAO metodo executeMultipleUpdateForRipristino - post conn.close()");
				} catch (SQLException e) {
				}
			}
		}
		
		listaRisultati = listaRisultatiDistribuzione;
		
		return listaRisultati;
		
	}
	
	
	public GenericResultsDTO execute(SchemaDTO schemaDTO, QueryDB queryDB) {
		
		System.out.println("classe GenericDAO metodo execute");
		
		GenericResultsDTO results = new GenericResultsDTO();
		
		if (executeUpdate) {
			createListOfLinkedHashMap = false;
			createListOfInsert = false;
		} else {
			if ((createListOfLinkedHashMap && createListOfInsert) || (!createListOfLinkedHashMap && !createListOfInsert)) {
				System.out.println("parametri non coerenti");
				return results;
			}
		}
				
		results.setSchema(schemaDTO.getSchemaUserName());
		
		SchemaDTO schemaDB = schemaDTO;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			connection = DBConnect.getConnection(schemaDB);
			
			System.out.println("classe GenericDAO metodo execute - post DBConnect.getConnection...");
			
			System.out.println("classe GenericDAO metodo execute - SQL = " + queryDB.getQuery());
			
			preparedStatement = connection.prepareStatement(queryDB.getQuery());
			
			if (executeUpdate) {
				System.out.println("classe GenericDAO metodo execute - ante preparedStatement.executeUpdate...");
				results.setRowsUpdated(preparedStatement.executeUpdate());
				System.out.println("classe GenericDAO metodo execute - post preparedStatement.executeUpdate...");
			} else {
				System.out.println("classe GenericDAO metodo execute - ante preparedStatement.executeQuery...");
				rs = preparedStatement.executeQuery();
				System.out.println("classe GenericDAO metodo execute - post preparedStatement.executeQuery...");
				if (createListOfLinkedHashMap) {
					List<LinkedHashMap<String, Object>> listLinkedHashMap = convertResultSetToListOfLinkedHashMap(rs);
					results.setListLinkedHashMap(listLinkedHashMap);
				}
				if (createListOfInsert) {
					results.setListString(convertResultSetToListOfString(rs));
				}
			}

		} catch (SQLSyntaxErrorException e) {
			throw new RuntimeException(e.toString() + "Schema " + schemaDTO.getSchemaUserName() + " tabella " + this.tableName + " SQL " + preparedStatement , e);
		} catch (SQLException e) {
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (rs != null) {
				try {
					rs.close();
					System.out.println("classe GenericDAO metodo execute - post rs.close()");
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
					System.out.println("classe GenericDAO metodo execute - post preparedStatement.close()");
				} catch (SQLException e) {
				}
			}
			if (connection != null) {
				try {
					connection.close();
					System.out.println("classe GenericDAO metodo execute - post conn.close()");
				} catch (SQLException e) {
				}
			}
		}

		return results;
	}

	private List<LinkedHashMap<String,Object>> convertResultSetToListOfLinkedHashMap(ResultSet rs) throws SQLException {
		
	    ResultSetMetaData md = rs.getMetaData();
	    int columns = md.getColumnCount();
	    List<LinkedHashMap<String,Object>> list = new ArrayList<LinkedHashMap<String,Object>>();
	  
	    while (rs.next()) {
	    	LinkedHashMap<String,Object> row = new LinkedHashMap<String, Object>(columns);
	        
	        for(int i=1; i<=columns; ++i) {
	            row.put(md.getColumnName(i),rs.getObject(i));
	        }
	        list.add(row);
	    }

	    return list;
	}
	
	private List<String> convertResultSetToListOfString(ResultSet rs) throws SQLException {
		
		List<String> listOfString = new ArrayList<String>();
		
		ResultSetMetaData rsmd = rs.getMetaData();
		int numColumns = rsmd.getColumnCount();
		
        int[] columnTypes = new int[numColumns];
        String columnNames = "";
        for (int i = 0; i < numColumns; i++) {
            columnTypes[i] = rsmd.getColumnType(i + 1);
            if (i != 0) {
                columnNames += ",";
            }
            columnNames += rsmd.getColumnName(i + 1);
        }
        java.util.Date d = null; 
		
        while (rs.next()) {
            String columnValues = "";
            for (int i = 0; i < numColumns; i++) {
                if (i != 0) {
                    columnValues += ",";
                }

                switch (columnTypes[i]) {
                    case Types.BIGINT:
                    case Types.BIT:
                    case Types.BOOLEAN:
                    case Types.DECIMAL:
                    case Types.DOUBLE:
                    case Types.FLOAT:
                    case Types.INTEGER:
                    case Types.SMALLINT:
                    case Types.TINYINT:
                        String v = rs.getString(i + 1);
                        columnValues += v;
                        break;

                    case Types.DATE:
                        d = rs.getDate(i + 1); 
                    case Types.TIME:
                        if (d == null) d = rs.getTime(i + 1);
                    case Types.TIMESTAMP:
                        if (d == null) d = rs.getTimestamp(i + 1);

                        if (d == null) {
                            columnValues += "null";
                        }
                        else {
                            columnValues += "TO_DATE('"
                                      + dateFormat.format(d)
                                      + "', 'YYYY/MM/DD HH24:MI:SS')";
                        }
                        break;

                    default:
                        v = rs.getString(i + 1);
                        if (v != null) {
                            columnValues += "'" + v.replaceAll("'", "''") + "'";
                        }
                        else {
                            columnValues += "null";
                        }
                        break;
                }
            }
            listOfString.add(String.format("INSERT INTO %s (%s) values (%s)\n", 
                                    tableName,
                                    columnNames,
                                    columnValues));
        }
		return listOfString;
	}
	
}
