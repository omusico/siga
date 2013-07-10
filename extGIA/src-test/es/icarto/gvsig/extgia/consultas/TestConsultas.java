package es.icarto.gvsig.extgia.consultas;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.junit.BeforeClass;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestConsultas {

    private final String filtersAreaMock = "Norte";
    private final String filtersBaseMock = "Norte";
    private final String filtersTramoMock = "AP-9";

    @BeforeClass
    public static void doSetupBeforeClass() {
	try {
	    initializegvSIGDrivers();
	    DBSession.createConnection("localhost", 5432, "audasa_test", null,
		    "postgres", "postgres");

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static void initializegvSIGDrivers() throws Exception {
	final String fwAndamiDriverPath = "../_fwAndami/gvSIG/extensiones/com.iver.cit.gvsig/drivers";
	final File baseDriversPath = new File(fwAndamiDriverPath);
	if (!baseDriversPath.exists()) {
	    throw new Exception("Can't find drivers path: "
		    + fwAndamiDriverPath);
	}

	LayerFactory.setDriversPath(baseDriversPath.getAbsolutePath());
	if (LayerFactory.getDM().getDriverNames().length < 1) {
	    throw new Exception("Can't find drivers in path: "
		    + fwAndamiDriverPath);
	}
    }

    protected String getSchema() {
	return "audasa_extgia";
    }

    @Test
    public void testCaracteristicasReportsQueries() throws SQLException {

	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    if (ConsultasFieldNames.getCaracteristicasFieldNames(DBFieldNames.Elements.values()[i].toString())!=null) {
		Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
		String query = "SELECT " +
			ConsultasFieldNames.getCaracteristicasFieldNames(DBFieldNames.Elements.values()[i].toString()) +
			" FROM " + getSchema() + "." + DBFieldNames.Elements.values()[i].toString() + ";";
		ResultSet rs = st.executeQuery(query);
		assertTrue(rs!=null);
	    }
	}
    }

    @Test
    public void testTrabajosReportsQueries() throws SQLException {
	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    if (SqlUtils.elementHasType(DBFieldNames.Elements.values()[i].toString(), "Trabajos") &&
		    !DBFieldNames.Elements.values()[i].toString().equals("Firme")) {
		Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
		String query = "SELECT " +
			ConsultasFieldNames.getTrabajosFieldNames(
				ConsultasFieldNames.getElementId(DBFieldNames.Elements.values()[i].toString())) +
				" FROM " + getSchema() + "." + DBFieldNames.Elements.values()[i].toString() +
				"_trabajos;";
		ResultSet rs = st.executeQuery(query);
		assertTrue(rs!=null);
	    }
	}
    }

    @Test
    public void testReconocimientosReportsQueries() throws SQLException {
	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    if (SqlUtils.elementHasType(DBFieldNames.Elements.values()[i].toString(), "Inspecciones") &&
		    !DBFieldNames.Elements.values()[i].toString().equals("Firme")) {
		Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
		String query = "SELECT " +
			ConsultasFieldNames.getReconocimientosFieldNames(
				ConsultasFieldNames.getElementId(DBFieldNames.Elements.values()[i].toString())) +
				" FROM " + getSchema() + "." + DBFieldNames.Elements.values()[i].toString() +
				"_reconocimiento_estado;";
		ResultSet rs = st.executeQuery(query);
		assertTrue(rs!=null);
	    }
	}
    }

    @Test
    public void testFirmeTrabajosReportQuerie() throws SQLException {
	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	    String query = "SELECT " +
		    ConsultasFieldNames.getFirmeTrabajosFieldNames("id_firme") +
		    " FROM " + getSchema() + "." + "firme_trabajos;";
	    ResultSet rs = st.executeQuery(query);
	    assertTrue(rs!=null);
	}
    }

    @Test
    public void testFirmeReconocimientosReportQuerie() throws SQLException {
	for (int i=0; i<DBFieldNames.Elements.values().length; i++) {
	    Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	    String query = "SELECT " +
		    ConsultasFieldNames.getFirmeReconocimientosFieldNames("id_firme") +
		    " FROM " + getSchema() + "." + "firme_reconocimiento_estado;";
	    ResultSet rs = st.executeQuery(query);
	    assertTrue(rs!=null);
	}
    }

}
