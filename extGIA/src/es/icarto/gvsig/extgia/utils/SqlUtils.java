package es.icarto.gvsig.extgia.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SqlUtils {

    public static ArrayList<KeyValue> getKeyValueListFromSql(String query) {
	ArrayList<KeyValue> values = new ArrayList<KeyValue>();
	PreparedStatement statement = null;
	Connection connection = DBSession.getCurrentSession().getJavaConnection();
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    statement = connection.prepareStatement(query);
	    while (rs.next()) {
		values.add(new KeyValue(rs.getString(1), rs.getString(2)));
	    }
	    return values;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new ArrayList<KeyValue>();
	}
    }

    public static HashMap<String, Value> getValuesFilteredByPk(String schema,
	    String tablename, String idField, String idValue) {
	HashMap<String, Value> values = new HashMap<String, Value>();
	PreparedStatement statement = null;
	Connection connection = DBSession.getCurrentSession().getJavaConnection();
	String query = "SELECT * FROM " + schema + "." + tablename +
		" WHERE " + idField + " = '" + idValue + "';";
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    ResultSetMetaData rsMetaData = rs.getMetaData();
	    statement = connection.prepareStatement(query);
	    while (rs.next()) {
		for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		    if (rs.getString(i+1) == null) {
			values.put(rsMetaData.getColumnName(i+1), ValueFactory.createNullValue());
		    }else {
			values.put(rsMetaData.getColumnName(i+1),
				ValueFactory.createValueByType(rs.getString(i+1),
					rsMetaData.getColumnType(i+1)));
		    }
		}
	    }
	    rs.close();
	    return values;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new HashMap<String, Value>();
	} catch (ParseException e) {
	    e.printStackTrace();
	    return new HashMap<String, Value>();
	}
    }

    public static HashMap<String, Integer> getDataTypesFromDbTable(String schema, String tablename) {
	HashMap<String, Integer> types = new HashMap<String, Integer>();
	PreparedStatement statement = null;
	Connection connection = DBSession.getCurrentSession().getJavaConnection();
	String query = "SELECT * FROM " + schema + "." + tablename + ";";
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    ResultSetMetaData rsMetaData = rs.getMetaData();
	    statement = connection.prepareStatement(query);
	    while (rs.next()) {
		for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		    types.put(rsMetaData.getColumnName(i+1), rsMetaData.getColumnType(i+1));
		}
	    }
	    rs.close();
	    return types;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new HashMap<String, Integer>();
	}
    }

    public static void createEmbebedTableFromDB (JTable embebedTableWidget, String schema,
	    String tablename, String[] fields, int[] columnsSize, String idField, String idValue) {
	ArrayList<String> columnsName = new ArrayList<String>();
	DefaultTableModel tableModel = new DefaultTableModel();
	PreparedStatement statement;
	String query = "SELECT ";
	for (int i=0; i<fields.length-1; i++) {
	    query = query + fields[i] + ", ";
	}
	query = query + fields[fields.length-1]+ " FROM " + schema + "." + tablename +
		" WHERE " + idField + " = '" + idValue + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    ResultSetMetaData rsMetaData = rs.getMetaData();
	    // Columns Name
	    for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		columnsName.add(rsMetaData.getColumnName(i+1));
	    }
	    for (String columnName : columnsName) {
		tableModel.addColumn(columnName);
	    }

	    embebedTableWidget.setModel(tableModel);
	    if (columnsSize != null) {
		for (int i=0; i<columnsSize.length; i++) {
		    embebedTableWidget.getColumnModel().getColumn(i).setPreferredWidth(columnsSize[i]);
		}
	    }
	    // Values
	    Value[] tableValues = new Value[rsMetaData.getColumnCount()];
	    while (rs.next()) {
		for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		    if (rs.getString(i+1) == null) {
			tableValues[i] = ValueFactory.createNullValue();
		    }else {
			tableValues[i] =
				ValueFactory.createValueByType(rs.getString(i+1),
					rsMetaData.getColumnType(i+1));
		    }
		}
		tableModel.addRow(tableValues);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

}
