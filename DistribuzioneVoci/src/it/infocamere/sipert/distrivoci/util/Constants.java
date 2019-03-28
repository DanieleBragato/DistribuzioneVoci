package it.infocamere.sipert.distrivoci.util;

public class Constants {

	public static final int NUM_COLL_SCHEMA = 0;
	public static final int NUM_COLL_PASSWORD = 1;
	public static final int NUM_COLL_SCHEMA_AD = 2;
	public static final int NUM_COLL_PASSWORD_AD = 3;
	public static final int NUM_COLL_DBNAME = 4;
	public static final int NUM_COLL_PORT = 5;
	public static final int NUM_COLL_SERVER = 6;
	
	public static final String NOME_COLL_SCHEMA = "Schema";
	public static final String NOME_COLL_PASSWORD = "Password";
	public static final String NOME_COLL_SCHEMA_AD = "Schema_AD";
	public static final String NOME_COLL_PASSWORD_AD = "Password_AD";
	public static final String NOME_COLL_DBNAME = "DbName";
	public static final String NOME_COLL_PORT = "Port";
	public static final String NOME_COLL_SERVER = "Server";
	
	public static final String PRODUZIONE = "PRODUZIONE";
	public static final String SVILUPPO = "SVILUPPO";
	
	public static final String PARZIALE = "PARZIALE";
	
	public static final String SCHEMA = "Schema";
	public static final String SETEUR7ES = "SETEUR7ES";
	public static final String NOME_FOLDER_SETEUR7ES = "Connessione_SETEUR7ES";
	public static final String CDVOCEXX = "CDVOCEXX";
	
	public static final String PREFIX_WHERE_CONDITION = " WHERE CDVOCEXX  IN('";
	
	public static final String PREFIX_WHERE_CONDITION_WITH_CDTPFORM = " WHERE CDTPFORM = 'PEVOCI' AND CDVOCEXX  IN('";
	
	public static final String PREFIX_WHERE_ROWCOUNT_EQUAL_ONE = " WHERE ROWNUM = 1";
	
	public static final String INSERT_LABEL_CONTENTS = "Insert - origine valori Schema ";
	
	public static final String ESPADE = "ESPADE";
	public static final String ESPAFM = "ESPAFM";
	
	public static final String PREFIX_SELECT = "SELECT * FROM ";
	public static final String PREFIX_DELETE = "DELETE FROM ";
	
	public static final String SELECT_COUNT_ALL_OBJECTS_WHERE = "select count(*) from all_objects where object_type in ('TABLE','VIEW', 'SYNONYM') and object_name = ";
	
	public static final String TABELLE = "Tabelle";
	public static final String VOCI = "Voci";
	public static final String SCHEMI_SUI_QUALI_DISTRIBUIRE = "Schemi sui quali distribuire";
	public static final String ANTEPRIMA_E_DISTRIBUZIONE = "Anteprima e Distribuzione";
	public static final String SCHEMI_DI_PARTENZA = "Schemi di Partenza";
	public static final String ANTEPRIMA_E_RIPRISTINO = "Anteprima e Ripristino";
	public static final String ANTEPRIMA_E_RIPRISTINO_VOCE = "Anteprima e Ripristino Voce";
	public static final String STORICO_DISTRIBUZIONI = "Distribuzioni";
	
	public static final String  DELETE = "DELETE";
	public static final String  INSERT = "INSERT";
	public static final String  UPDATE = "UPDATE";
	
	public static final String  SELECT = "SELECT";
	
	public static final String  GET_INFO_COLUMNS = "GET_INFO_COLUMNS";
	
	public static final int COLONNA_ZERO = 0;
	public static final int RIGA_ZERO = 0;
	
	public static final int ZERO = 0;
	
	public static final String BOX_TABELLE = "#vboxTabelle";
	public static final String BOX_VOCI = "#vboxVoci";
	public static final String BOX_SCHEMI_SUI_QUALI_DISTRIBUIRE = "#vboxSchemi";
	public static final String BOX_STORICO = "#vboxStorico";
	public static final String BOX_SCHEMI_PARTENZA = "#vboxSchemiPartenza";
	public static final String BOX_ANTEPRIMA_DISTRIBUZIONE = "#vboxPreView";
	public static final String BOX_ANTEPRIMA_RIPRISTINO = "#vboxRipristino";
	public static final String BOX_ANTEPRIMA_RIPRISTINO_VOCE = "#vboxRipristinoVoce";
	
}
