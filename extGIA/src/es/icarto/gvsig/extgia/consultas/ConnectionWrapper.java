package es.icarto.gvsig.extgia.consultas;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

public class ConnectionWrapper {

    private static final Logger logger = Logger
	    .getLogger(ConnectionWrapper.class);

    private final Connection con;

    /**
     * Usage:
     * 
     * <pre>
     * {@code
     * ConnectionWrapper conW = new ConnectionWrapper(con);
     * DefaultTableModel table = conW.execute(query);
     * if (table.getRowCount() == 0) {
     * ... take actions for an empty resultset
     * }
     * }
     * </pre>
     * 
     * This class closes correctly the statement and the resultset. The
     * ConnectionWrapper can be reused to execute multiple queries
     */
    public ConnectionWrapper(Connection con) {
	this.con = con;
    }

    public DefaultTableModel execute(String query) {
	PreparedStatement statement = null;
	ResultSet rs = null;
	DefaultTableModel asTable = null;
	try {
	    statement = con.prepareStatement(query);
	    if (statement.execute()) {
		rs = statement.getResultSet();
		asTable = toTable(rs);
	    }
	} catch (SQLException e1) {
	    e1.printStackTrace();
	    return null;
	} finally {
	    close(rs);
	    close(statement);
	}
	return asTable;
    }

    private DefaultTableModel toTable(ResultSet rs) throws SQLException {
	DefaultTableModel table = new DefaultTableModel();
	ResultSetMetaData metaData = rs.getMetaData();
	int numColumns = metaData.getColumnCount();

	for (int i = 0; i < numColumns; i++) {
	    table.addColumn(metaData.getColumnName(i + 1));
	}

	while (rs.next()) {

	    Object rowData[] = new Object[numColumns];
	    for (int i = 0; i < numColumns; i++) {
		rowData[i] = rs.getObject(i + 1);
	    }
	    table.addRow(rowData);
	}
	return table;
    }

    private void close(PreparedStatement statement) {
	if (statement != null) {
	    try {
		statement.close();
	    } catch (SQLException e) {
		logger.error(e.getStackTrace(), e);
	    }
	}
    }

    private void close(ResultSet rs) {
	if (rs != null) {
	    try {
		rs.close();
	    } catch (SQLException e) {
		logger.error(e.getStackTrace(), e);
	    }
	}
    }

}
