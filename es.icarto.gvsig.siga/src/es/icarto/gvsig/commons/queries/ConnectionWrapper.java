package es.icarto.gvsig.commons.queries;

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

    /**
     * Creates a new default table model. Executes the query, and insert the
     * resultset in the table. If the query doesn't get any result the table
     * will be empty
     *
     * @param query
     *            to be executed
     * @return a table model with the results
     */
    public DefaultTableModel execute(String query) {
	DefaultTableModel table = new DefaultTableModel();
	execute(query, table);
	return table;
    }

    /**
     * Appends to the given table as match columns and rows as the columns and
     * rows that has the resulset of the executed query
     *
     * @param query
     *            to be executed
     * @param table
     *            to append the data
     */
    public void execute(String query, DefaultTableModel table) {
	PreparedStatement statement = null;
	ResultSet rs = null;
	try {
	    statement = con.prepareStatement(query);
	    if (statement.execute()) {
		rs = statement.getResultSet();
		toTable(rs, table);
	    }
	} catch (SQLException e1) {
	    logger.error(e1.getStackTrace(), e1);
	    logger.error(query);

	    try {
		con.rollback();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }

	} finally {
	    close(rs);
	    close(statement);
	}
    }

    private void toTable(ResultSet rs, DefaultTableModel table)
	    throws SQLException {
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
