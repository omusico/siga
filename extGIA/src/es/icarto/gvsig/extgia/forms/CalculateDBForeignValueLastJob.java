package es.icarto.gvsig.extgia.forms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class CalculateDBForeignValueLastJob extends CalculateDBForeignValue {

    String unidad;

    public CalculateDBForeignValueLastJob(Map<String, String> foreingKey, String unidad) {
	super(foreingKey);
	this.unidad = unidad;
    }

    public CalculateDBForeignValueLastJob(Map<String, String> foreingKey) {
	super(foreingKey);
    }

    @Override
    protected String getForeignField() {
	return DBFieldNames.MEDICION;
    }

    @Override
    protected abstract String getTableName();

    @Override
    protected abstract String getIDField();

    @Override
    protected String getSQLSentence() {
	if (unidad.equalsIgnoreCase(" ") || !getLastJobByUnit()) {
	    return "SELECT " + getForeignField() + " FROM " + DBFieldNames.GIA_SCHEMA + "."
		    + getTableName() + " WHERE " + getIDField() + " = " + "'" + foreingKey.get(getIDField())
		    + "'" +" AND " + DBFieldNames.FECHA + " IN (SELECT max(" + DBFieldNames.FECHA + ") FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + getTableName() + " WHERE " + getIDField() + " = "
		    + "'" + foreingKey.get(getIDField()) + "')";
	} else {
	    return "SELECT " + getForeignField() + " FROM " + DBFieldNames.GIA_SCHEMA + "."
		    + getTableName() + " WHERE " + getIDField() + " = " + "'" + foreingKey.get(getIDField())
		    + "'" +" AND " + DBFieldNames.FECHA + " IN (SELECT max(" + DBFieldNames.FECHA + ") FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + getTableName() + " WHERE " + getIDField() + " = "
		    + "'" + foreingKey.get(getIDField()) + "' AND unidad = '" + unidad + "')";
	}
    }

    protected Boolean getLastJobByUnit() {
	if (!unidad.equalsIgnoreCase(" ")) {
	    try {
		Connection connection = DBSession.getCurrentSession().getJavaConnection();
		PreparedStatement statement;
		String query = "SELECT " + getForeignField() + " FROM " + DBFieldNames.GIA_SCHEMA + "."
			+ getTableName() + " WHERE " + getIDField() + " = " + "'" + foreingKey.get(getIDField())
			+ "'" +" AND " + DBFieldNames.UNIDAD + " = '" + unidad + "'";
		statement = connection.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		return rs.next();
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	}
	return null;
    }
}
