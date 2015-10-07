package es.icarto.gvsig.extgia.forms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class CalculateDBForeignValue {

    Map<String, String> foreingKey;

    public CalculateDBForeignValue(Map<String, String> foreingKey) {
	this.foreingKey = foreingKey;
    }

    protected abstract String getComponentName();

    protected abstract String getForeignField();

    protected abstract String getTableName();

    protected abstract String getIDField();

    public ForeignValue getForeignValue() {
	return new ForeignValue(getComponentName(), getValue());
    }

    protected String getSQLSentence() {
	return "SELECT " + getForeignField() + " FROM " + DBFieldNames.GIA_SCHEMA + "."
		+ getTableName() + " WHERE " + getIDField() + " = " + "'" + foreingKey.get(getIDField()) + "'";
    }

    protected String getValue() {
	try {
	    Connection connection = DBSession.getCurrentSession().getJavaConnection();
	    PreparedStatement statement;
	    statement = connection.prepareStatement(getSQLSentence());
	    ResultSet rs = statement.executeQuery();
	    if (rs.next()) {
		if (rs.getString(1) != null) {
		    if (rs.getString(1).contains(".")) {
			return rs.getString(1).replace(".", ",");
		    } else {
			return rs.getString(1);
		    }
		}
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

}
