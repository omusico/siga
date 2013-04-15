package es.icarto.gvsig.extgia.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;

import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
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
			//If type is date (91) then we use varchar(12) in order to
			//create the Value since date Value causes problems
		    }else if (rsMetaData.getColumnType(i+1) == 91){
			values.put(rsMetaData.getColumnName(i+1),
				ValueFactory.createValueByType(rs.getString(i+1),
					12));
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
		    //If type is date (91) then we use varchar(12) in order to
		    //create the Value since date Value causes problems
		    if (rsMetaData.getColumnType(i+1) == 91) {
			types.put(rsMetaData.getColumnName(i+1), 12);
		    }else {
			types.put(rsMetaData.getColumnName(i+1), rsMetaData.getColumnType(i+1));
		    }
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
	@SuppressWarnings("serial")
	DefaultTableModel tableModel = new DefaultTableModel() {
	    @Override
	    public boolean isCellEditable(int row, int column) {
		return false;
	    }
	};
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
			//If type is date (91) then we use varchar(12) in order to
			//create the Value since date Value causes problems
		    }else if (rsMetaData.getColumnType(i+1) == 91){
			SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
			Date date = rs.getDate(i+1);
			String dateAsString = dateFormat.format(date);
			tableValues[i] =
				ValueFactory.createValueByType(dateAsString,
					12);
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

    public static void reloadEmbebedTable (JTable embebedTable, String[] fields,
	    String schema, String tableName, String idField, String idValue) {
	PreparedStatement statement;
	DefaultTableModel tableModel = (DefaultTableModel) embebedTable.getModel();
	for (int i=tableModel.getRowCount()-1; i>=0; i--) {
	    tableModel.removeRow(i);
	}
	String query = "SELECT ";
	for (String field : fields) {
	    query = query + field + ",";
	}
	query = query +  "FROM " + schema + "." + tableName +
		" WHERE " + idField + " = '" + idValue + "';";
	query = query.replace(",FROM", " FROM");
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    ResultSetMetaData rsMetaData = rs.getMetaData();
	    Value[] tableValues = new Value[rsMetaData.getColumnCount()];
	    while (rs.next()) {
		for (int i=0; i<rsMetaData.getColumnCount(); i++) {
		    if (rs.getString(i+1) == null) {
			tableValues[i] = ValueFactory.createNullValue();
			//If type is date (91) then we use varchar(12) in order to
			//create the Value since date Value causes problems
		    }else if (rsMetaData.getColumnType(i+1) == 91){
			SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
			Date date = rs.getDate(i+1);
			String dateAsString = dateFormat.format(date);
			tableValues[i] =
				ValueFactory.createValueByType(dateAsString,
					12);
		    }else {
			tableValues[i] =
				ValueFactory.createValueByType(rs.getString(i+1),
					rsMetaData.getColumnType(i+1));
		    }
		}
		tableModel.addRow(tableValues);
	    }
	    embebedTable.repaint();
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (ParseException e) {
	    e.printStackTrace();
	}
    }

    public static void delete(String schema, String tablename, String pkField, String pkValue) {
	PreparedStatement statement;
	String query = "DELETE FROM " + schema + "." + tablename +
		" WHERE " + pkField + " = '" + pkValue + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static void insert(String schema, String tablename, HashMap<String, Value> values) {
	PreparedStatement statement;
	String query = "INSERT INTO " + schema + "." + tablename + " (";
	Iterator<Entry<String, Value>> columnsIterator = values.entrySet().iterator();
	while (columnsIterator.hasNext()) {
	    Entry<String, Value> e = columnsIterator.next();
	    query = query + e.getKey() + ",";
	}
	query = query + ")";
	query = query.replace(",)", ")");
	query = query + " VALUES (";
	Iterator<Entry<String, Value>> valuesIterator = values.entrySet().iterator();
	while (valuesIterator.hasNext()) {
	    Entry<String, Value> e = valuesIterator.next();
	    if (e.getValue().getSQLType() == 4) {
		query = query + e.getValue() + ",";
	    }else {
		query = query + "'" + e.getValue() + "',";
	    }
	}
	query = query + ");";
	query = query.replace(",);", ");");
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static void update(String schema, String tablename, HashMap<String, Value> values,
	    String idField, String idValue) {
	PreparedStatement statement;
	String query = "UPDATE " + schema + "." + tablename + " SET ";
	Iterator<Entry<String, Value>> columnsIterator = values.entrySet().iterator();
	while (columnsIterator.hasNext()) {
	    Entry<String, Value> e = columnsIterator.next();
	    if (e.getValue().getSQLType() == 4) {
		query = query + e.getKey() + " = " + e.getValue() + ",";
	    }else {
		query = query + e.getKey() + " = '" + e.getValue() + "',";
	    }
	}
	query = query + "WHERE";
	query = query.replace(",WHERE", " WHERE ");
	query = query + idField + " = '" + idValue + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	} catch (SQLException e) {
	    e.printStackTrace();
	}

    }

    public static int getNextIdOfSequence(String sequence) {
	int newID = -1;
	PreparedStatement statement;
	String query = "SELECT nextval(" + "'" + sequence + "');";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    newID = rs.getInt(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return newID;
    }
}
