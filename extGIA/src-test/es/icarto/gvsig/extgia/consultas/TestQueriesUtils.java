package es.icarto.gvsig.extgia.consultas;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.drivers.DBException;

import es.icarto.gvsig.commons.queries.Field;
import es.icarto.gvsig.commons.queries.Utils;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.testutils.Drivers;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestQueriesUtils {

    @BeforeClass
    public static void setupBeforeClass() {
	try {
	    Drivers.initgvSIGDrivers("/home/development/audasa/siga/_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers");
	    DBSession.createConnection("localhost", 5434, "audasa_test", null,
		    "postgres", "postgres");

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetFields() throws IOException, DBException, SQLException,
	    URISyntaxException {

	URI uri = getClass().getClassLoader().getResource(".").toURI();
	String foo = new File(uri).getParent() + "/";

	ORMLite ormLite = new ORMLite(foo + "rules/consultas_metadata.xml");
	DomainValues dv = ormLite.getAppDomain().getDomainValuesForComponent(
		"elemento");

	assertEquals(20, dv.getValues().size());
	for (KeyValue kv : dv.getValues()) {
	    List<Field> fields = Utils.getFields(foo
		    + "config/columns.properties", "audasa_extgia", kv.getKey()
		    .toLowerCase());
	    assertTrue(String.format("No columns in table: %s", kv.getKey()),
		    fields.size() > 0);

	    // System.out.println("");
	    // System.out.println("");
	    // System.out.println("");
	    // System.out.println(kv.getValue());
	    for (Field c : fields) {
		// if (props.getProperty(c, null) == null) {
		// System.out.println(String.format("%s=", c));
		// }
		assertNotEquals(
			String.format("Table: %s, column: %s", kv.getKey(), c),
			c.getKey(), c.getLongName());
		assertFalse(c.getKey().equals("the_geom"));
		assertFalse(c.getKey().equals("gid"));

	    }
	}
    }
}
