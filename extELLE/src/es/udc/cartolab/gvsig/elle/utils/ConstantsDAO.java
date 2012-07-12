package es.udc.cartolab.gvsig.elle.utils;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConstantsDAO {
    
    public final static String CONSTANTS_TABLE_NAME = "_constants";
    public final static String CONSTANTS_CONSTANT_FIELD_NAME = "constante";
    public final static String CONSTANTS_AFFECTED_TABLE_NAME = "nombre_tabla";
    public final static String CONSTANTS_FILTER_FIELD_NAME = "campo_filtro";
    
    public static String[] getTablesAffectedbyConstant(String constant) {
	String query = "SELECT nombre_tabla FROM " + DBSession.getCurrentSession().getSchema() + "." + CONSTANTS_TABLE_NAME + " WHERE " + CONSTANTS_CONSTANT_FIELD_NAME +  " = " + "'" + constant + "'" + ";";
	PreparedStatement statement;
	    try {
		statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
		statement.execute();
		ResultSet rs = statement.getResultSet();
		
		List <String>resultArray = new ArrayList<String>();
		while (rs.next()) {
			String val = rs.getString(CONSTANTS_AFFECTED_TABLE_NAME);
			resultArray.add(val);
		}
		rs.close();

		String[] result = new String[resultArray.size()];
		for (int i=0; i<resultArray.size(); i++) {
			result[i] = resultArray.get(i);
		}

		return result;
	    } catch (SQLException e) {
		e.printStackTrace();
	    }
	    return null;
    }
}
