package es.icarto.gvsig.navtableforms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import org.cresques.cts.IProjection;
import org.junit.BeforeClass;
import org.junit.Test;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.udc.cartolab.gvsig.navtable.TestProperties;
import es.udc.cartolab.gvsig.testutils.Drivers;
import es.udc.cartolab.gvsig.users.utils.DBSession;
import es.udc.cartolab.gvsig.users.utils.DBSessionPostGIS;

public class TestNavtableOrder {
    private static final IProjection CRS = CRSFactory.getCRS("EPSG:23029");
    private static final String SCHEMA = "public";
    private static final String TABLE = "test_navtable_order";

    @BeforeClass
    public static void doSetupBeforeClass() {
	Drivers.initgvSIGDrivers(TestProperties.driversPath);
	try {
	    DBSessionPostGIS.createConnection("localhost", 5434, "audasa_test",
		    null, "postgres", "postgres");
	} catch (DBException e) {
	    e.printStackTrace();
	}

    }

    @Test
    public void test() throws SQLException, DBException, ReadDriverException {
	DBSession session = DBSession.getCurrentSession();
	Connection con = session.getJavaConnection();

	assertFalse(session.tableExists(SCHEMA, TABLE));
	String createTableQuery = "CREATE TABLE test_navtable_order (gid SERIAL PRIMARY KEY, intorder INTEGER);";
	Statement stat = con.createStatement();
	stat.executeUpdate(createTableQuery);
	stat.executeQuery("select AddGeometryColumn('public', 'test_navtable_order', 'geom', 23029, 'POINT', 2);");
	assertTrue(session.tableExists(SCHEMA, TABLE));

	int[] ids = new int[5500];
	Random r = new Random();
	for (int i = 0; i < ids.length; i++) {
	    int id = r.nextInt();
	    ids[i] = id;
	    String query = String
		    .format("INSERT INTO public.test_navtable_order (intorder, geom) VALUES (%d, ST_SetSRID(ST_MakePoint(0, 0), 23029));",
			    id);
	    stat.executeUpdate(query);
	}

	String[][] table = session.getTable(TABLE, SCHEMA, null);
	assertEquals(5500, table.length);

	FLyrVect layer = (FLyrVect) session.getLayer("test", TABLE, SCHEMA,
		null, CRS);
	layer.getSource().stop();

	stat.execute("SELECT DropGeometryTable('public', 'test_navtable_order')");
	assertFalse(session.tableExists(SCHEMA, TABLE));
    }

}
