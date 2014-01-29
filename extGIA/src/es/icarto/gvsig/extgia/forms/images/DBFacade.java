package es.icarto.gvsig.extgia.forms.images;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class DBFacade {

    private static Connection connection;

    private DBFacade() {
	String server = DBSession.getCurrentSession().getServer();
	int port = DBSession.getCurrentSession().getPort();
	String database = DBSession.getCurrentSession().getDatabase();

	String url = "jdbc:postgresql://" + server + ":" + port + "/"
		+ database;
	String user = DBSession.getCurrentSession().getUserName();
	String passwd = DBSession.getCurrentSession().getPassword();

	try {
	    Class.forName("org.postgresql.Driver");
	    connection = DriverManager.getConnection(url, user, passwd);
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    public static Connection getConnection() {
	if (connection == null) {
	    new DBFacade();
	}
	return connection;
    }

}
