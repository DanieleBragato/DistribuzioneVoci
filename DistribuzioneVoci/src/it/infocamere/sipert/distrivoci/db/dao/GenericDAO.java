package it.infocamere.sipert.distrivoci.db.dao;

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
import it.infocamere.sipert.distrivoci.db.dto.GenericResultsDTO;
import it.infocamere.sipert.distrivoci.db.dto.SchemaDTO;

public class GenericDAO {
	
    private static final SimpleDateFormat dateFormat = 
            new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    private static String tableName = "";
    private static boolean createListOfLinkedHashMap = true;
    private static boolean createListOfInsert = false;
	
	public boolean testConnessioneOK (SchemaDTO schemDTO) {
		
		SchemaDTO schemaDB = schemDTO;
		
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
	
	public GenericResultsDTO executeQueryForGenerateInserts(SchemaDTO schemaDTO, QueryDB queryDB, String tableName) {
		
		this.tableName = tableName;
		createListOfLinkedHashMap = false;
		createListOfInsert = true;
		
		return executeQuery(schemaDTO, queryDB);
		
	}
	
	public GenericResultsDTO executeQuery(SchemaDTO schemaDTO, QueryDB queryDB) {
		
		GenericResultsDTO results = new GenericResultsDTO();
		
		if ((createListOfLinkedHashMap && createListOfInsert) || (!createListOfLinkedHashMap && !createListOfInsert)) {
			System.out.println("parametri non coerenti");
			return results;
		}
		
		results.setSchema(schemaDTO.getSchemaUserName());
		
		SchemaDTO schemaDB = schemaDTO;

		Connection conn = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		try {
			conn = DBConnect.getConnection(schemaDB);
			preparedStatement = conn.prepareStatement(queryDB.getQuery());

			rs = preparedStatement.executeQuery();
			
			if (createListOfLinkedHashMap) {
				List<LinkedHashMap<String, Object>> listLinkedHashMap = convertResultSetToListOfLinkedHashMap(rs);
				results.setListLinkedHashMap(listLinkedHashMap);
			}
			if (createListOfInsert) {
				results.setListString(convertResultSetToListOfString(rs));
			}
			
		} catch (SQLSyntaxErrorException e) {
			throw new RuntimeException(e.toString() + "Schema = " + schemaDTO.getSchemaUserName() + " - SQL = " + queryDB.getQuery(), e);
		} catch (SQLException e) {
			throw new RuntimeException("Errore nell'esecuzione della query: " + e.toString() , e);
		} 
		
		finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
				}
			}
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
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
