package com.iver.cit.gvsig.fmap.drivers.jdbc.postgis;

import java.util.HashMap;

public class PostgresReservedWords {
	private static HashMap<String, Boolean> reserverdWords = new HashMap<String, Boolean>();
	
	static{
		Boolean bol = true;
		reserverdWords.put("ADD", bol);
		reserverdWords.put("ALL", bol);
		reserverdWords.put("ALTER", bol);
		reserverdWords.put("AND", bol);
		reserverdWords.put("ANY", bol);
		reserverdWords.put("AS", bol);
		reserverdWords.put("ASC", bol);
		reserverdWords.put("BEGIN", bol);
		reserverdWords.put("BETWEEN", bol);
		reserverdWords.put("BOTH BY", bol);
		reserverdWords.put("CASCADE", bol);
		reserverdWords.put("CAST", bol);
		reserverdWords.put("CHAR", bol);
		reserverdWords.put("CHARACTER", bol);
		reserverdWords.put("CHECK", bol);
		reserverdWords.put("CLOSE", bol);
		reserverdWords.put("COLLATE", bol);
		reserverdWords.put("COLUMN", bol);
		reserverdWords.put("COMMIT", bol);
		reserverdWords.put("CONSTRAINT", bol);
		reserverdWords.put("CREATE", bol);
		reserverdWords.put("CURRENT_DATE", bol);
		reserverdWords.put("CURRENT_TIME", bol);
		reserverdWords.put("CURRENT_TIMESTAMP", bol);
		reserverdWords.put("CURSOR", bol);
		reserverdWords.put("DECLARE", bol);
		reserverdWords.put("DEFAULT", bol);
		reserverdWords.put("DELETE", bol);
		reserverdWords.put("DESC", bol);
		reserverdWords.put("DISTINCT", bol);
		reserverdWords.put("DROP", bol);
		reserverdWords.put("EXECUTE", bol);
		reserverdWords.put("EXISTS", bol);
		reserverdWords.put("EXTRACT", bol);
		reserverdWords.put("FETCH", bol);
		reserverdWords.put("FLOAT", bol);
		reserverdWords.put("FOR", bol);
		reserverdWords.put("FROM", bol);
		reserverdWords.put("FULL", bol);
		reserverdWords.put("GRANT", bol);
		reserverdWords.put("HAVING", bol);
		reserverdWords.put("IN", bol);
		reserverdWords.put("INNER", bol);
		reserverdWords.put("INSERT", bol);
		reserverdWords.put("INTERVAL", bol);
		reserverdWords.put("INTO", bol);
		reserverdWords.put("IS", bol);
		reserverdWords.put("JOIN", bol);
		reserverdWords.put("LEADING", bol);
		reserverdWords.put("LEFT", bol);
		reserverdWords.put("LIKE", bol);
		reserverdWords.put("LOCAL", bol);
		reserverdWords.put("NAMES", bol);
		reserverdWords.put("NATIONAL", bol);
		reserverdWords.put("NATURAL", bol); 
		reserverdWords.put("NCHAR", bol);
		reserverdWords.put("NO", bol);
		reserverdWords.put("NOT", bol);
		reserverdWords.put("NULL", bol);
		reserverdWords.put("ON", bol);
		reserverdWords.put("OR", bol);
		reserverdWords.put("OUTER", bol);
		reserverdWords.put("PARTIAL", bol);
		reserverdWords.put("PRIMARY", bol);
		reserverdWords.put("PRIVILEGES", bol);
		reserverdWords.put("PROCEDURE", bol);
		reserverdWords.put("PUBLIC", bol);
		reserverdWords.put("REFERENCES", bol);
		reserverdWords.put("REVOKE", bol);
		reserverdWords.put("RIGHT", bol);
		reserverdWords.put("ROLLBACK", bol);
		reserverdWords.put("SELECT", bol);
		reserverdWords.put("SET", bol);
		reserverdWords.put("SUBSTRING", bol);
		reserverdWords.put("TO", bol);
		reserverdWords.put("TRAILING", bol);
		reserverdWords.put("TRIM", bol);
		reserverdWords.put("UNION", bol);
		reserverdWords.put("UNIQUE", bol);
		reserverdWords.put("UPDATE", bol);
		reserverdWords.put("USING", bol);
		reserverdWords.put("VALUES", bol);
		reserverdWords.put("VARCHAR", bol);
		reserverdWords.put("VARYING", bol);
		reserverdWords.put("VIEW", bol);
		reserverdWords.put("WHERE", bol);
		reserverdWords.put("WITH", bol);
		reserverdWords.put("WORK", bol);
		reserverdWords.put("CASE", bol);
		reserverdWords.put("COALESCE", bol);
		reserverdWords.put("CROSS", bol);
		reserverdWords.put("CURRENT", bol);
		reserverdWords.put("CURRENT_USER", bol);
		reserverdWords.put("DEC", bol);
		reserverdWords.put("DECIMAL", bol);
		reserverdWords.put("ELSE", bol);
		reserverdWords.put("END", bol);
		reserverdWords.put("FALSE", bol);
		reserverdWords.put("FOREIGN", bol);
		reserverdWords.put("GLOBAL", bol);
		reserverdWords.put("GROUP", bol);
		reserverdWords.put("LOCAL", bol);
		reserverdWords.put("NULLIF", bol);
		reserverdWords.put("NUMERIC", bol);
		reserverdWords.put("ORDER", bol);
		reserverdWords.put("POSITION", bol);
		reserverdWords.put("PRECISION", bol);
		reserverdWords.put("SESSION_USER", bol);
		reserverdWords.put("TABLE", bol);
		reserverdWords.put("THEN", bol);
		reserverdWords.put("TRANSACTION", bol);
		reserverdWords.put("TRUE", bol);
		reserverdWords.put("USER", bol);
		reserverdWords.put("WHEN", bol);
		reserverdWords.put("ABORT", bol);
		reserverdWords.put("ANALYZE", bol);
		reserverdWords.put("BINARY", bol);
		reserverdWords.put("CLUSTER", bol);
		reserverdWords.put("CONSTRAINT", bol);
		reserverdWords.put("COPY", bol);
		reserverdWords.put("DO", bol);
		reserverdWords.put("EXPLAIN", bol);
		reserverdWords.put("EXTEND", bol);
		reserverdWords.put("LISTEN", bol);
		reserverdWords.put("LOAD", bol);
		reserverdWords.put("LOCK", bol);
		reserverdWords.put("MOVE", bol);
		reserverdWords.put("NEW", bol);
		reserverdWords.put("NONE", bol);
		reserverdWords.put("NOTIFY", bol);
		reserverdWords.put("RESET", bol);
		reserverdWords.put("SETOF", bol);
		reserverdWords.put("SHOW", bol);
		reserverdWords.put("UNLISTEN", bol);
		reserverdWords.put("UNTIL", bol);
		reserverdWords.put("VACUUM", bol);
		reserverdWords.put("VERBOSE", bol);
	}
	
	public static boolean isReserved(String word){
		return reserverdWords.containsKey(word.toUpperCase());
	}
}
