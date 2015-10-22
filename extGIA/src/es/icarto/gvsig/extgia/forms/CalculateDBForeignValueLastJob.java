package es.icarto.gvsig.extgia.forms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CalculateDBForeignValueLastJob extends CalculateDBForeignValue {

    private final String unidad;

    public CalculateDBForeignValueLastJob(String unidad,
	    Map<String, String> foreingKey, String componentName,
	    String tableName, String idField) {
	super(foreingKey, componentName, DBFieldNames.MEDICION, tableName,
		idField);
	this.unidad = unidad;

    }

    @Override
    protected String getSQLSentence() {
	if (unidad.equalsIgnoreCase(" ") || !getLastJobByUnit()) {
	    return "SELECT " + foreignField + " FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + tableName + " WHERE "
		    + idField + " = " + "'" + foreingKey.get(idField) + "'"
		    + " AND " + DBFieldNames.FECHA + " IN (SELECT max("
		    + DBFieldNames.FECHA + ") FROM " + DBFieldNames.GIA_SCHEMA
		    + "." + tableName + " WHERE " + idField + " = " + "'"
		    + foreingKey.get(idField) + "')";
	} else {
	    return "SELECT " + foreignField + " FROM "
		    + DBFieldNames.GIA_SCHEMA + "." + tableName + " WHERE "
		    + idField + " = " + "'" + foreingKey.get(idField) + "'"
		    + " AND " + DBFieldNames.FECHA + " IN (SELECT max("
		    + DBFieldNames.FECHA + ") FROM " + DBFieldNames.GIA_SCHEMA
		    + "." + tableName + " WHERE " + idField + " = " + "'"
		    + foreingKey.get(idField) + "' AND unidad = '" + unidad
		    + "')";
	}
    }

    private Boolean getLastJobByUnit() {
	if (!unidad.equalsIgnoreCase(" ")) {
	    try {
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		PreparedStatement statement;
		String query = "SELECT " + foreignField + " FROM "
			+ DBFieldNames.GIA_SCHEMA + "." + tableName + " WHERE "
			+ idField + " = " + "'" + foreingKey.get(idField) + "'"
			+ " AND " + DBFieldNames.UNIDAD + " = '" + unidad + "'";
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
