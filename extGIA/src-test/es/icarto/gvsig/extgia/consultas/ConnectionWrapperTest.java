package es.icarto.gvsig.extgia.consultas;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Random;

import javax.swing.table.DefaultTableModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ConnectionWrapperTest {

    private ConnectionWrapper conW;
    private Connection con;

    @Before
    public void setUp() throws Exception {
	String url = "jdbc:postgresql://localhost:5432/audasa_test";
	con = DriverManager.getConnection(url, "postgres", "postgres");
	con.setAutoCommit(false);
	conW = new ConnectionWrapper(con);
    }

    @After
    public void tearDown() throws Exception {
	con.rollback();
    }

    @Test
    public void test() {
	String tablename = "foo" + new Random().nextInt(Integer.MAX_VALUE);
	conW.execute("CREATE TABLE " + tablename
		+ " (gid SERIAL PRIMARY KEY, name text);");
	conW.execute("insert into " + tablename
		+ " (name) VALUES ('foo'), ('bar');");

	DefaultTableModel t1 = conW.execute("SELECT * FROM " + tablename
		+ " WHERE gid > 2;");
	assertEquals("Should return an empty resultset", 0, t1.getRowCount());

	DefaultTableModel t2 = conW.execute("SELECT * FROM " + tablename + ";");
	assertEquals("Two rows", 2, t2.getRowCount());
    }
}
