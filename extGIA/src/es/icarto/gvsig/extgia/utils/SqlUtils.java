package es.icarto.gvsig.extgia.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

}
