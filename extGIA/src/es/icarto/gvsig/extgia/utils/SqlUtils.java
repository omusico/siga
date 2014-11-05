package es.icarto.gvsig.extgia.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SqlUtils {

    public static int getNextIdOfSequence(String sequence) {
	int newID = -1;
	PreparedStatement statement;
	String query = "SELECT nextval(" + "'" + sequence + "');";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    newID = rs.getInt(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return newID;
    }

    public static boolean elementHasType(String element, String tipoConsulta) {
	if (tipoConsulta.equalsIgnoreCase("Características")
		|| ((tipoConsulta.equalsIgnoreCase("Trabajos (Agregados)")))) {
	    return true;
	}
	boolean type = false;
	PreparedStatement statement;
	String query = "SELECT " + tipoConsulta
		+ " FROM audasa_extgia_dominios.elemento "
		+ "WHERE LOWER(id) = LOWER('" + element + "');";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		type = rs.getBoolean(1);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return type;
    }

}
