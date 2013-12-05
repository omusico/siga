package es.icarto.gvsig.extgia.utils;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.junit.BeforeClass;
import org.junit.Test;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestSqlUtils {

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

    @Test
    public void testInsertTrabajo() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("id_trabajo", ValueFactory.createNullValue());
	mockData.put("fecha", ValueFactory.createValue("01/01/1980"));
	mockData.put("unidad", ValueFactory.createValue("foo"));
	mockData.put("medicion_contratista", ValueFactory.createValue(999));
	mockData.put("medicion_audasa", ValueFactory.createValue(999));
	mockData.put("observaciones", ValueFactory.createValue("bar"));
	mockData.put("fecha_certificado", ValueFactory.createValue("01/01/1980"));

	SqlUtils.insert("audasa_extgia", "areas_descanso_trabajos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND unidad = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertTrabajoWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("id_trabajo", ValueFactory.createNullValue());
	mockData.put("fecha", ValueFactory.createNullValue());
	mockData.put("unidad", ValueFactory.createValue("foo"));
	mockData.put("medicion_contratista", ValueFactory.createValue(999));
	mockData.put("medicion_audasa", ValueFactory.createNullValue());
	mockData.put("observaciones", ValueFactory.createValue("bar"));
	mockData.put("fecha_certificado", ValueFactory.createNullValue());

	SqlUtils.insert("audasa_extgia", "areas_descanso_trabajos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND unidad = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertReconocimiento() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("n_inspeccion", ValueFactory.createNullValue());
	mockData.put("nombre_revisor", ValueFactory.createValue("foo"));
	mockData.put("fecha_inspeccion", ValueFactory.createValue("01/01/1980"));
	mockData.put("sup_pavimentada", ValueFactory.createValue(1));
	mockData.put("aceras", ValueFactory.createValue(1));
	mockData.put("bordillos", ValueFactory.createValue(1));
	mockData.put("zona_ajardinada", ValueFactory.createValue(1));
	mockData.put("servicios", ValueFactory.createValue(1));
	mockData.put("indice_estado", ValueFactory.createValue(1.0));
	mockData.put("observaciones", ValueFactory.createValue("bar"));

	SqlUtils.insert("audasa_extgia", "areas_descanso_reconocimientos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos" +
		" WHERE " + "id_area_descanso = '2311360581' AND nombre_revisor = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertReconocimientoWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("n_inspeccion", ValueFactory.createNullValue());
	mockData.put("nombre_revisor", ValueFactory.createValue("foo"));
	mockData.put("fecha_inspeccion", ValueFactory.createNullValue());
	mockData.put("sup_pavimentada", ValueFactory.createValue(1));
	mockData.put("aceras", ValueFactory.createValue(1));
	mockData.put("bordillos", ValueFactory.createNullValue());
	mockData.put("zona_ajardinada", ValueFactory.createValue(1));
	mockData.put("servicios", ValueFactory.createValue(1));
	mockData.put("indice_estado", ValueFactory.createValue(1.0));
	mockData.put("observaciones", ValueFactory.createValue("bar"));

	SqlUtils.insert("audasa_extgia", "areas_descanso_reconocimientos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos" +
		" WHERE " + "id_area_descanso = '2311360581' AND nombre_revisor = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertRamales() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("id_ramal", ValueFactory.createNullValue());
	mockData.put("ramal", ValueFactory.createValue("foo"));
	mockData.put("sentido_ramal", ValueFactory.createValue("bar"));
	mockData.put("longitud", ValueFactory.createValue(999));

	SqlUtils.insert("audasa_extgia", "areas_descanso_ramales", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales" +
		" WHERE " + "id_area_descanso = '2311360581' AND ramal = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertRamalesWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_area_descanso", ValueFactory.createValue("2311360581"));
	mockData.put("id_ramal", ValueFactory.createNullValue());
	mockData.put("ramal", ValueFactory.createValue("foo"));
	mockData.put("sentido_ramal", ValueFactory.createNullValue());
	mockData.put("longitud", ValueFactory.createNullValue());

	SqlUtils.insert("audasa_extgia", "areas_descanso_ramales", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales" +
		" WHERE " + "id_area_descanso = '2311360581' AND ramal = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertCarreteraEnlazada() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_enlace", ValueFactory.createValue("111415031007810"));
	mockData.put("id_carretera_enlazada", ValueFactory.createNullValue());
	mockData.put("clave_carretera", ValueFactory.createValue("foo"));
	mockData.put("pk", ValueFactory.createValue(9.999));
	mockData.put("titular", ValueFactory.createValue("bar"));
	mockData.put("tipo_cruce", ValueFactory.createValue("foo-bar"));

	SqlUtils.insert("audasa_extgia", "enlaces_carreteras_enlazadas", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas" +
		" WHERE " + "id_enlace = '111415031007810' AND clave_carretera = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertCarreteraEnlazadaWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_enlace", ValueFactory.createValue("111415031007810"));
	mockData.put("id_carretera_enlazada", ValueFactory.createNullValue());
	mockData.put("clave_carretera", ValueFactory.createValue("foo"));
	mockData.put("pk", ValueFactory.createNullValue());
	mockData.put("titular", ValueFactory.createValue("bar"));
	mockData.put("tipo_cruce", ValueFactory.createNullValue());

	SqlUtils.insert("audasa_extgia", "enlaces_carreteras_enlazadas", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas" +
		" WHERE " + "id_enlace = '111415031007810' AND clave_carretera = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateTrabajo() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("fecha", ValueFactory.createValue("31/12/1980"));
	mockData.put("unidad", ValueFactory.createValue("bar"));
	mockData.put("medicion_contratista", ValueFactory.createValue(888));
	mockData.put("medicion_audasa", ValueFactory.createValue(888));
	mockData.put("observaciones", ValueFactory.createValue("foo"));
	mockData.put("fecha_certificado", ValueFactory.createValue("31/12/1980"));

	SqlUtils.update("audasa_extgia",
		"areas_descanso_trabajos",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND unidad = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), "1980-12-31");
	    assertEquals(rs.getString(4), mockData.get("unidad").toString());
	    assertEquals(rs.getString(5), mockData.get("medicion_contratista").toString());
	    assertEquals(rs.getString(6), mockData.get("medicion_audasa").toString());
	    assertEquals(rs.getString(7), mockData.get("observaciones").toString());
	    assertEquals(rs.getString(8), "1980-12-31");
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateTrabajoDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("fecha", ValueFactory.createNullValue());
	mockData.put("unidad", ValueFactory.createValue("bar"));
	mockData.put("medicion_contratista", ValueFactory.createValue(888));
	mockData.put("medicion_audasa", ValueFactory.createNullValue());
	mockData.put("observaciones", ValueFactory.createValue("foo"));
	mockData.put("fecha_certificado", ValueFactory.createValue("31/12/1980"));

	SqlUtils.update("audasa_extgia",
		"areas_descanso_trabajos",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND unidad = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), null);
	    assertEquals(rs.getString(4), mockData.get("unidad").toString());
	    assertEquals(rs.getString(5), mockData.get("medicion_contratista").toString());
	    assertEquals(rs.getString(6), null);
	    assertEquals(rs.getString(7), mockData.get("observaciones").toString());
	    assertEquals(rs.getString(8), "1980-12-31");
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateReconocimiento() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("nombre_revisor", ValueFactory.createValue("bar"));
	mockData.put("fecha_inspeccion", ValueFactory.createValue("31/12/1980"));
	mockData.put("sup_pavimentada", ValueFactory.createValue(2));
	mockData.put("aceras", ValueFactory.createValue(2));
	mockData.put("bordillos", ValueFactory.createValue(2));
	mockData.put("zona_ajardinada", ValueFactory.createValue(2));
	mockData.put("servicios", ValueFactory.createValue(2));
	mockData.put("indice_estado", ValueFactory.createValue(2.0));
	mockData.put("observaciones", ValueFactory.createValue("foo"));

	SqlUtils.update("audasa_extgia",
		"areas_descanso_reconocimientos",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos" +
		" WHERE " + "id_area_descanso = '2311360581' AND nombre_revisor = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("nombre_revisor").toString());
	    assertEquals(rs.getString(4), "1980-12-31");
	    assertEquals(rs.getString(5), mockData.get("sup_pavimentada").toString());
	    assertEquals(rs.getString(6), mockData.get("aceras").toString());
	    assertEquals(rs.getString(7), mockData.get("bordillos").toString());
	    assertEquals(rs.getString(8), mockData.get("zona_ajardinada").toString());
	    assertEquals(rs.getString(9), mockData.get("servicios").toString());
	    // TODO: comparing double value (indice_estado)
	    assertEquals(rs.getString(11), mockData.get("observaciones").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateReconocimientoDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("nombre_revisor", ValueFactory.createValue("bar"));
	mockData.put("fecha_inspeccion", ValueFactory.createNullValue());
	mockData.put("sup_pavimentada", ValueFactory.createValue(2));
	mockData.put("aceras", ValueFactory.createValue(2));
	mockData.put("bordillos", ValueFactory.createNullValue());
	mockData.put("zona_ajardinada", ValueFactory.createValue(2));
	mockData.put("servicios", ValueFactory.createValue(2));
	mockData.put("indice_estado", ValueFactory.createValue(1.888));
	mockData.put("observaciones", ValueFactory.createValue("foo"));

	SqlUtils.update("audasa_extgia",
		"areas_descanso_reconocimientos",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos" +
		" WHERE " + "id_area_descanso = '2311360581' AND nombre_revisor = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("nombre_revisor").toString());
	    assertEquals(rs.getString(4), null);
	    assertEquals(rs.getString(5), mockData.get("sup_pavimentada").toString());
	    assertEquals(rs.getString(6), mockData.get("aceras").toString());
	    assertEquals(rs.getString(7), null);
	    assertEquals(rs.getString(8), mockData.get("zona_ajardinada").toString());
	    assertEquals(rs.getString(9), mockData.get("servicios").toString());
	    assertEquals(rs.getString(10), mockData.get("indice_estado").toString());
	    assertEquals(rs.getString(11), mockData.get("observaciones").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateRamales() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("ramal", ValueFactory.createValue("bar"));
	mockData.put("sentido_ramal", ValueFactory.createValue("foo"));
	mockData.put("longitud", ValueFactory.createValue(888));

	SqlUtils.update("audasa_extgia",
		"areas_descanso_ramales",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales" +
		" WHERE " + "id_area_descanso = '2311360581' AND ramal = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("ramal").toString());
	    assertEquals(rs.getString(4), mockData.get("sentido_ramal").toString());
	    assertEquals(rs.getString(5), mockData.get("longitud").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateRamalesDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("ramal", ValueFactory.createValue("bar"));
	mockData.put("sentido_ramal", ValueFactory.createNullValue());
	mockData.put("longitud", ValueFactory.createNullValue());

	SqlUtils.update("audasa_extgia",
		"areas_descanso_ramales",
		mockData,
		"id_area_descanso",
		"2311360581");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales" +
		" WHERE " + "id_area_descanso = '2311360581' AND ramal = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("ramal").toString());
	    assertEquals(rs.getString(4), null);
	    assertEquals(rs.getString(5), null);
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateCarreteraEnlazada() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("clave_carretera", ValueFactory.createValue("bar"));
	mockData.put("pk", ValueFactory.createValue(8.888));
	mockData.put("titular", ValueFactory.createValue("foo"));
	mockData.put("tipo_cruce", ValueFactory.createValue("bar-foo"));

	SqlUtils.update("audasa_extgia",
		"enlaces_carreteras_enlazadas",
		mockData,
		"id_enlace",
		"111415031007810");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas" +
		" WHERE " + "id_enlace = '111415031007810' AND clave_carretera = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("clave_carretera").toString());
	    assertEquals(rs.getString(4), mockData.get("pk").toString());
	    assertEquals(rs.getString(5), mockData.get("titular").toString());
	    assertEquals(rs.getString(6), mockData.get("tipo_cruce").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateCarreteraEnlazadaDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("clave_carretera", ValueFactory.createValue("bar"));
	mockData.put("pk", ValueFactory.createNullValue());
	mockData.put("titular", ValueFactory.createValue("foo"));
	mockData.put("tipo_cruce", ValueFactory.createValue("bar-foo"));

	SqlUtils.update("audasa_extgia",
		"enlaces_carreteras_enlazadas",
		mockData,
		"id_enlace",
		"111415031007810");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas" +
		" WHERE " + "id_enlace = '111415031007810' AND clave_carretera = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("clave_carretera").toString());
	    assertEquals(rs.getString(4), null);
	    assertEquals(rs.getString(5), mockData.get("titular").toString());
	    assertEquals(rs.getString(6), mockData.get("tipo_cruce").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void deleteTrabajo() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND unidad = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia", "areas_descanso_trabajos", "id_trabajo", rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_trabajos WHERE " +
		"id_area_descanso = '2311360581' AND id_trabajo = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }

    @Test
    public void deleteReconocimiento() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos" +
		" WHERE " + "id_area_descanso = '2311360581' AND nombre_revisor = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia",
		"areas_descanso_reconocimientos",
		"n_inspeccion",
		rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_reconocimientos WHERE " +
		"id_area_descanso = '2311360581' AND n_inspeccion = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }

    @Test
    public void deleteRamal() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales" +
		" WHERE " + "id_area_descanso = '2311360581' AND ramal = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia",
		"areas_descanso_ramales",
		"id_ramal",
		rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.areas_descanso_ramales WHERE " +
		"id_area_descanso = '2311360581' AND id_ramal = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }

    @Test
    public void deleteCarreteraEnlazada() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas" +
		" WHERE " + "id_enlace = '111415031007810' AND clave_carretera = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia",
		"enlaces_carreteras_enlazadas",
		"id_carretera_enlazada",
		rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.enlaces_carreteras_enlazadas WHERE " +
		"id_enlace = '111415031007810' AND id_carretera_enlazada = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }

    @Test
    public void testInsertSenhal() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_elemento_senhalizacion", ValueFactory.createValue(1));
	mockData.put("id_senhal_vertical", ValueFactory.createNullValue());
	mockData.put("tipo_senhal", ValueFactory.createValue("foo"));
	mockData.put("codigo_senhal", ValueFactory.createValue("bar"));
	mockData.put("leyenda", ValueFactory.createValue("foo2"));
	mockData.put("panel_complementario", ValueFactory.createValue(true));
	mockData.put("codigo_panel", ValueFactory.createValue("bar2"));
	mockData.put("texto_panel", ValueFactory.createValue("foo3"));
	mockData.put("reversible", ValueFactory.createValue(true));
	mockData.put("luminosa", ValueFactory.createValue(false));
	mockData.put("tipo_superficie", ValueFactory.createValue("bar3"));
	mockData.put("material_superficie", ValueFactory.createValue("foo4"));
	mockData.put("material_retrorreflectante", ValueFactory.createValue("bar4"));
	mockData.put("nivel_reflectancia", ValueFactory.createValue("foo5"));
	mockData.put("ancho", ValueFactory.createValue(999));
	mockData.put("alto", ValueFactory.createValue(999));
	mockData.put("superficie", ValueFactory.createValue(9.9));
	mockData.put("altura", ValueFactory.createValue(9.9));
	mockData.put("fabricante", ValueFactory.createValue("bar5"));
	mockData.put("fecha_fabricacion", ValueFactory.createValue("01/01/1980"));
	mockData.put("fecha_instalacion", ValueFactory.createValue("01/01/1980"));
	mockData.put("fecha_reposicion", ValueFactory.createValue("01/01/1980"));
	mockData.put("marcado_ce", ValueFactory.createValue(false));
	mockData.put("observaciones", ValueFactory.createValue("foo-bar"));

	SqlUtils.insert("audasa_extgia", "senhalizacion_vertical_senhales", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales" +
		" WHERE " + "tipo_senhal = 'foo' AND codigo_panel = 'bar2'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertSenhalWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_elemento_senhalizacion", ValueFactory.createValue(1));
	mockData.put("id_senhal_vertical", ValueFactory.createNullValue());
	mockData.put("tipo_senhal", ValueFactory.createNullValue());
	mockData.put("codigo_senhal", ValueFactory.createValue("bar"));
	mockData.put("leyenda", ValueFactory.createValue("foo2"));
	mockData.put("panel_complementario", ValueFactory.createNullValue());
	mockData.put("codigo_panel", ValueFactory.createValue("bar2"));
	mockData.put("texto_panel", ValueFactory.createValue("foo3"));
	mockData.put("reversible", ValueFactory.createValue(true));
	mockData.put("luminosa", ValueFactory.createValue(false));
	mockData.put("tipo_superficie", ValueFactory.createValue("bar3"));
	mockData.put("material_superficie", ValueFactory.createValue("foo4"));
	mockData.put("material_retrorreflectante", ValueFactory.createValue("bar4"));
	mockData.put("nivel_reflectancia", ValueFactory.createValue("foo5"));
	mockData.put("ancho", ValueFactory.createValue(999));
	mockData.put("alto", ValueFactory.createNullValue());
	mockData.put("superficie", ValueFactory.createNullValue());
	mockData.put("altura", ValueFactory.createValue(9.9));
	mockData.put("fabricante", ValueFactory.createValue("bar5"));
	mockData.put("fecha_fabricacion", ValueFactory.createNullValue());
	mockData.put("fecha_instalacion", ValueFactory.createValue("01/01/1980"));
	mockData.put("fecha_reposicion", ValueFactory.createValue("01/01/1980"));
	mockData.put("marcado_ce", ValueFactory.createValue(false));
	mockData.put("observaciones", ValueFactory.createValue("foo-bar"));

	SqlUtils.insert("audasa_extgia", "senhalizacion_vertical_senhales", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales" +
		" WHERE " + "tipo_senhal = 'foo' AND codigo_panel = 'bar2'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateSenhal() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("tipo_senhal", ValueFactory.createValue("bar"));
	mockData.put("codigo_senhal", ValueFactory.createValue("foo"));
	mockData.put("leyenda", ValueFactory.createValue("bar2"));
	mockData.put("panel_complementario", ValueFactory.createValue(false));
	mockData.put("codigo_panel", ValueFactory.createValue("foo2"));
	mockData.put("texto_panel", ValueFactory.createValue("bar3"));
	mockData.put("reversible", ValueFactory.createValue(false));
	mockData.put("luminosa", ValueFactory.createValue(true));
	mockData.put("tipo_superficie", ValueFactory.createValue("foo3"));
	mockData.put("material_superficie", ValueFactory.createValue("bar4"));
	mockData.put("material_retrorreflectante", ValueFactory.createValue("foo4"));
	mockData.put("nivel_reflectancia", ValueFactory.createValue("bar5"));
	mockData.put("ancho", ValueFactory.createValue(888));
	mockData.put("alto", ValueFactory.createValue(888));
	mockData.put("superficie", ValueFactory.createValue(8.88));
	mockData.put("altura", ValueFactory.createValue(8.88));
	mockData.put("fabricante", ValueFactory.createValue("foo5"));
	mockData.put("fecha_fabricacion", ValueFactory.createValue("31/12/1980"));
	mockData.put("fecha_instalacion", ValueFactory.createValue("31/12/1980"));
	mockData.put("fecha_reposicion", ValueFactory.createValue("31/12/1980"));
	mockData.put("marcado_ce", ValueFactory.createValue(true));
	mockData.put("observaciones", ValueFactory.createValue("bar-foo"));

	SqlUtils.update("audasa_extgia",
		"senhalizacion_vertical_senhales",
		mockData,
		"id_elemento_senhalizacion",
		"1");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales" +
		" WHERE " + "id_elemento_senhalizacion = '1' AND tipo_senhal = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("tipo_senhal").toString());
	    assertEquals(rs.getString(4), mockData.get("codigo_senhal").toString());
	    assertEquals(rs.getString(5), mockData.get("leyenda").toString());
	    assertEquals(rs.getString(6), String.valueOf(mockData.get("panel_complementario").toString().charAt(0)));
	    assertEquals(rs.getString(7), mockData.get("codigo_panel").toString());
	    assertEquals(rs.getString(8), mockData.get("texto_panel").toString());
	    assertEquals(rs.getString(9), String.valueOf(mockData.get("reversible").toString().charAt(0)));
	    assertEquals(rs.getString(10), String.valueOf(mockData.get("luminosa").toString().charAt(0)));
	    assertEquals(rs.getString(11), mockData.get("tipo_superficie").toString());
	    assertEquals(rs.getString(12), mockData.get("material_superficie").toString());
	    assertEquals(rs.getString(13), mockData.get("material_retrorreflectante").toString());
	    assertEquals(rs.getString(14), mockData.get("nivel_reflectancia").toString());
	    assertEquals(rs.getString(15), mockData.get("ancho").toString());
	    assertEquals(rs.getString(16), mockData.get("alto").toString());
	    assertEquals(rs.getString(17), mockData.get("superficie").toString());
	    assertEquals(rs.getString(18), mockData.get("altura").toString());
	    assertEquals(rs.getString(19), mockData.get("fabricante").toString());
	    assertEquals(rs.getString(20), "1980-12-31");
	    assertEquals(rs.getString(21), "1980-12-31");
	    assertEquals(rs.getString(22), "1980-12-31");
	    assertEquals(rs.getString(23), String.valueOf(mockData.get("marcado_ce").toString().charAt(0)));
	    assertEquals(rs.getString(24), mockData.get("observaciones").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateSenhalDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("tipo_senhal", ValueFactory.createValue("bar"));
	mockData.put("codigo_senhal", ValueFactory.createValue("foo"));
	mockData.put("leyenda", ValueFactory.createValue("bar2"));
	mockData.put("panel_complementario", ValueFactory.createNullValue());
	mockData.put("codigo_panel", ValueFactory.createValue("foo2"));
	mockData.put("texto_panel", ValueFactory.createValue("bar3"));
	mockData.put("reversible", ValueFactory.createValue(false));
	mockData.put("luminosa", ValueFactory.createValue(true));
	mockData.put("tipo_superficie", ValueFactory.createValue("foo3"));
	mockData.put("material_superficie", ValueFactory.createValue("bar4"));
	mockData.put("material_retrorreflectante", ValueFactory.createValue("foo4"));
	mockData.put("nivel_reflectancia", ValueFactory.createValue("bar5"));
	mockData.put("ancho", ValueFactory.createValue(888));
	mockData.put("alto", ValueFactory.createValue(888));
	mockData.put("superficie", ValueFactory.createValue(8.88));
	mockData.put("altura", ValueFactory.createValue(8.88));
	mockData.put("fabricante", ValueFactory.createValue("foo5"));
	mockData.put("fecha_fabricacion", ValueFactory.createValue("31/12/1980"));
	mockData.put("fecha_instalacion", ValueFactory.createValue("31/12/1980"));
	mockData.put("fecha_reposicion", ValueFactory.createNullValue());
	mockData.put("marcado_ce", ValueFactory.createValue(true));
	mockData.put("observaciones", ValueFactory.createNullValue());

	SqlUtils.update("audasa_extgia",
		"senhalizacion_vertical_senhales",
		mockData,
		"id_elemento_senhalizacion",
		"1");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales" +
		" WHERE " + "id_elemento_senhalizacion = '1' AND tipo_senhal = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), mockData.get("tipo_senhal").toString());
	    assertEquals(rs.getString(4), mockData.get("codigo_senhal").toString());
	    assertEquals(rs.getString(5), mockData.get("leyenda").toString());
	    assertEquals(rs.getString(6), null);
	    assertEquals(rs.getString(7), mockData.get("codigo_panel").toString());
	    assertEquals(rs.getString(8), mockData.get("texto_panel").toString());
	    assertEquals(rs.getString(9), String.valueOf(mockData.get("reversible").toString().charAt(0)));
	    assertEquals(rs.getString(10), String.valueOf(mockData.get("luminosa").toString().charAt(0)));
	    assertEquals(rs.getString(11), mockData.get("tipo_superficie").toString());
	    assertEquals(rs.getString(12), mockData.get("material_superficie").toString());
	    assertEquals(rs.getString(13), mockData.get("material_retrorreflectante").toString());
	    assertEquals(rs.getString(14), mockData.get("nivel_reflectancia").toString());
	    assertEquals(rs.getString(15), mockData.get("ancho").toString());
	    assertEquals(rs.getString(16), mockData.get("alto").toString());
	    assertEquals(rs.getString(17), mockData.get("superficie").toString());
	    assertEquals(rs.getString(18), mockData.get("altura").toString());
	    assertEquals(rs.getString(19), mockData.get("fabricante").toString());
	    assertEquals(rs.getString(20), "1980-12-31");
	    assertEquals(rs.getString(21), "1980-12-31");
	    assertEquals(rs.getString(22), null);
	    assertEquals(rs.getString(23), String.valueOf(mockData.get("marcado_ce").toString().charAt(0)));
	    assertEquals(rs.getString(24), null);
	}
	assertEquals(true, inserted);
    }

    @Test
    public void deleteSenhal() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales" +
		" WHERE " + "id_elemento_senhalizacion = '1' AND tipo_senhal = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia",
		"senhalizacion_vertical_senhales",
		"id_senhal_vertical",
		rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.senhalizacion_vertical_senhales WHERE " +
		"id_elemento_senhalizacion = '1' AND id_senhal_vertical = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }

    @Test
    public void testInsertFirme() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_firme", ValueFactory.createValue(1));
	mockData.put("id_trabajo", ValueFactory.createNullValue());
	mockData.put("fecha", ValueFactory.createValue("01/01/1980"));
	mockData.put("pk_inicial", ValueFactory.createValue(9.999));
	mockData.put("pk_final", ValueFactory.createValue(9.999));
	mockData.put("sentido", ValueFactory.createValue("foo"));
	mockData.put("descripcion", ValueFactory.createValue("bar"));
	mockData.put("fecha_certificado", ValueFactory.createValue("01/01/1980"));
	mockData.put("explanada_cm", ValueFactory.createValue("foo2"));
	mockData.put("zahorra_artificial_cm", ValueFactory.createValue("bar2"));
	mockData.put("suelo_cemento_cm", ValueFactory.createValue("foo3"));
	mockData.put("grava_cemento_cm", ValueFactory.createValue("bar3"));
	mockData.put("mbc_base_cm", ValueFactory.createValue("foo4"));
	mockData.put("mbc_intermedia_cm", ValueFactory.createValue("bar4"));
	mockData.put("mbc_rodadura_cm", ValueFactory.createValue("foo5"));
	mockData.put("explanada", ValueFactory.createValue("bar5"));
	mockData.put("zahorra_artificial", ValueFactory.createValue("foo6"));
	mockData.put("suelo_cemento", ValueFactory.createValue("bar6"));
	mockData.put("gc_arido_grueso", ValueFactory.createValue("foo7"));
	mockData.put("gc_arido_fino", ValueFactory.createValue("bar7"));
	mockData.put("gc_cemento", ValueFactory.createValue("foo8"));
	mockData.put("mbc_bas_huso", ValueFactory.createValue("bar8"));
	mockData.put("mbc_bas_arido_grueso", ValueFactory.createValue("foo9"));
	mockData.put("mbc_bas_arido_fino", ValueFactory.createValue("bar9"));
	mockData.put("mbc_bas_filler", ValueFactory.createValue("foo10"));
	mockData.put("mbc_bas_ligante", ValueFactory.createValue("bar10"));
	mockData.put("mbc_rod_huso", ValueFactory.createValue("foo11"));
	mockData.put("mbc_rod_arido_grueso", ValueFactory.createValue("bar11"));
	mockData.put("mbc_rod_arido_fino", ValueFactory.createValue("foo12"));
	mockData.put("mbc_rod_filler", ValueFactory.createValue("bar12"));
	mockData.put("mbc_rod_ligante", ValueFactory.createValue("foo13"));

	SqlUtils.insert("audasa_extgia", "firme_trabajos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos" +
		" WHERE " + "id_firme = 1 AND sentido = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testInsertFirmeWithNullValues() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("id_firme", ValueFactory.createValue(1));
	mockData.put("id_trabajo", ValueFactory.createNullValue());
	mockData.put("fecha", ValueFactory.createNullValue());
	mockData.put("pk_inicial", ValueFactory.createValue(9.999));
	mockData.put("pk_final", ValueFactory.createValue(9.999));
	mockData.put("sentido", ValueFactory.createValue("foo"));
	mockData.put("descripcion", ValueFactory.createValue("bar"));
	mockData.put("fecha_certificado", ValueFactory.createNullValue());
	mockData.put("explanada_cm", ValueFactory.createValue("foo2"));
	mockData.put("zahorra_artificial_cm", ValueFactory.createValue("bar2"));
	mockData.put("suelo_cemento_cm", ValueFactory.createValue("foo3"));
	mockData.put("grava_cemento_cm", ValueFactory.createValue("bar3"));
	mockData.put("mbc_base_cm", ValueFactory.createValue("foo4"));
	mockData.put("mbc_intermedia_cm", ValueFactory.createValue("bar4"));
	mockData.put("mbc_rodadura_cm", ValueFactory.createValue("foo5"));
	mockData.put("explanada", ValueFactory.createValue("bar5"));
	mockData.put("zahorra_artificial", ValueFactory.createValue("foo6"));
	mockData.put("suelo_cemento", ValueFactory.createValue("bar6"));
	mockData.put("gc_arido_grueso", ValueFactory.createValue("foo7"));
	mockData.put("gc_arido_fino", ValueFactory.createValue("bar7"));
	mockData.put("gc_cemento", ValueFactory.createValue("foo8"));
	mockData.put("mbc_bas_huso", ValueFactory.createNullValue());
	mockData.put("mbc_bas_arido_grueso", ValueFactory.createValue("foo9"));
	mockData.put("mbc_bas_arido_fino", ValueFactory.createValue("bar9"));
	mockData.put("mbc_bas_filler", ValueFactory.createValue("foo10"));
	mockData.put("mbc_bas_ligante", ValueFactory.createValue("bar10"));
	mockData.put("mbc_rod_huso", ValueFactory.createValue("foo11"));
	mockData.put("mbc_rod_arido_grueso", ValueFactory.createValue("bar11"));
	mockData.put("mbc_rod_arido_fino", ValueFactory.createValue("foo12"));
	mockData.put("mbc_rod_filler", ValueFactory.createValue("bar12"));
	mockData.put("mbc_rod_ligante", ValueFactory.createValue("foo13"));

	SqlUtils.insert("audasa_extgia", "firme_trabajos", mockData);

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos" +
		" WHERE " + "id_firme = 1 AND sentido = 'foo'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateFirme() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("fecha", ValueFactory.createValue("31/12/1980"));
	mockData.put("pk_inicial", ValueFactory.createValue(8.888));
	mockData.put("pk_final", ValueFactory.createValue(8.888));
	mockData.put("sentido", ValueFactory.createValue("bar"));
	mockData.put("descripcion", ValueFactory.createValue("foo"));
	mockData.put("fecha_certificado", ValueFactory.createValue("31/12/1980"));
	mockData.put("explanada_cm", ValueFactory.createValue("bar2"));
	mockData.put("zahorra_artificial_cm", ValueFactory.createValue("foo2"));
	mockData.put("suelo_cemento_cm", ValueFactory.createValue("bar3"));
	mockData.put("grava_cemento_cm", ValueFactory.createValue("foo3"));
	mockData.put("mbc_base_cm", ValueFactory.createValue("bar4"));
	mockData.put("mbc_intermedia_cm", ValueFactory.createValue("foo4"));
	mockData.put("mbc_rodadura_cm", ValueFactory.createValue("bar5"));
	mockData.put("explanada", ValueFactory.createValue("foo5"));
	mockData.put("zahorra_artificial", ValueFactory.createValue("bar6"));
	mockData.put("suelo_cemento", ValueFactory.createValue("foo6"));
	mockData.put("gc_arido_grueso", ValueFactory.createValue("bar7"));
	mockData.put("gc_arido_fino", ValueFactory.createValue("foo7"));
	mockData.put("gc_cemento", ValueFactory.createValue("bar8"));
	mockData.put("mbc_bas_huso", ValueFactory.createValue("foo8"));
	mockData.put("mbc_bas_arido_grueso", ValueFactory.createValue("bar9"));
	mockData.put("mbc_bas_arido_fino", ValueFactory.createValue("foo9"));
	mockData.put("mbc_bas_filler", ValueFactory.createValue("bar10"));
	mockData.put("mbc_bas_ligante", ValueFactory.createValue("foo10"));
	mockData.put("mbc_rod_huso", ValueFactory.createValue("bar11"));
	mockData.put("mbc_rod_arido_grueso", ValueFactory.createValue("foo11"));
	mockData.put("mbc_rod_arido_fino", ValueFactory.createValue("bar12"));
	mockData.put("mbc_rod_filler", ValueFactory.createValue("foo12"));
	mockData.put("mbc_rod_ligante", ValueFactory.createValue("bar13"));

	SqlUtils.update("audasa_extgia",
		"firme_trabajos",
		mockData,
		"id_firme",
		"1");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos" +
		" WHERE " + "id_firme = '1' AND sentido = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), "1980-12-31");
	    assertEquals(rs.getString(4), mockData.get("pk_inicial").toString());
	    assertEquals(rs.getString(5), mockData.get("pk_final").toString());
	    assertEquals(rs.getString(6), mockData.get("sentido").toString());
	    assertEquals(rs.getString(7), mockData.get("descripcion").toString());
	    assertEquals(rs.getString(8), "1980-12-31");
	    assertEquals(rs.getString(9), mockData.get("explanada_cm").toString());
	    assertEquals(rs.getString(10), mockData.get("zahorra_artificial_cm").toString());
	    assertEquals(rs.getString(11), mockData.get("suelo_cemento_cm").toString());
	    assertEquals(rs.getString(12), mockData.get("grava_cemento_cm").toString());
	    assertEquals(rs.getString(13), mockData.get("mbc_base_cm").toString());
	    assertEquals(rs.getString(14), mockData.get("mbc_intermedia_cm").toString());
	    assertEquals(rs.getString(15), mockData.get("mbc_rodadura_cm").toString());
	    assertEquals(rs.getString(16), mockData.get("explanada").toString());
	    assertEquals(rs.getString(17), mockData.get("zahorra_artificial").toString());
	    assertEquals(rs.getString(18), mockData.get("suelo_cemento").toString());
	    assertEquals(rs.getString(19), mockData.get("gc_arido_grueso").toString());
	    assertEquals(rs.getString(20), mockData.get("gc_arido_fino").toString());
	    assertEquals(rs.getString(21), mockData.get("gc_cemento").toString());
	    assertEquals(rs.getString(22), mockData.get("mbc_bas_huso").toString());
	    assertEquals(rs.getString(23), mockData.get("mbc_bas_arido_grueso").toString());
	    assertEquals(rs.getString(24), mockData.get("mbc_bas_arido_fino").toString());
	    assertEquals(rs.getString(25), mockData.get("mbc_bas_filler").toString());
	    assertEquals(rs.getString(26), mockData.get("mbc_bas_ligante").toString());
	    assertEquals(rs.getString(27), mockData.get("mbc_rod_huso").toString());
	    assertEquals(rs.getString(28), mockData.get("mbc_rod_arido_grueso").toString());
	    assertEquals(rs.getString(29), mockData.get("mbc_rod_arido_fino").toString());
	    assertEquals(rs.getString(30), mockData.get("mbc_rod_filler").toString());
	    assertEquals(rs.getString(31), mockData.get("mbc_rod_ligante").toString());
	}
	assertEquals(true, inserted);
    }

    @Test
    public void testUpdateFirmeDeletingSomeValue() throws SQLException {
	HashMap<String, Value> mockData = new HashMap<String, Value>();
	mockData.put("fecha", ValueFactory.createNullValue());
	mockData.put("pk_inicial", ValueFactory.createValue(8.888));
	mockData.put("pk_final", ValueFactory.createValue(8.888));
	mockData.put("sentido", ValueFactory.createValue("bar"));
	mockData.put("descripcion", ValueFactory.createValue("foo"));
	mockData.put("fecha_certificado", ValueFactory.createValue("31/12/1980"));
	mockData.put("explanada_cm", ValueFactory.createValue("bar2"));
	mockData.put("zahorra_artificial_cm", ValueFactory.createValue("foo2"));
	mockData.put("suelo_cemento_cm", ValueFactory.createValue("bar3"));
	mockData.put("grava_cemento_cm", ValueFactory.createValue("foo3"));
	mockData.put("mbc_base_cm", ValueFactory.createValue("bar4"));
	mockData.put("mbc_intermedia_cm", ValueFactory.createValue("foo4"));
	mockData.put("mbc_rodadura_cm", ValueFactory.createValue("bar5"));
	mockData.put("explanada", ValueFactory.createValue("foo5"));
	mockData.put("zahorra_artificial", ValueFactory.createValue("bar6"));
	mockData.put("suelo_cemento", ValueFactory.createValue("foo6"));
	mockData.put("gc_arido_grueso", ValueFactory.createValue("bar7"));
	mockData.put("gc_arido_fino", ValueFactory.createValue("foo7"));
	mockData.put("gc_cemento", ValueFactory.createValue("bar8"));
	mockData.put("mbc_bas_huso", ValueFactory.createValue("foo8"));
	mockData.put("mbc_bas_arido_grueso", ValueFactory.createValue("bar9"));
	mockData.put("mbc_bas_arido_fino", ValueFactory.createValue("foo9"));
	mockData.put("mbc_bas_filler", ValueFactory.createValue("bar10"));
	mockData.put("mbc_bas_ligante", ValueFactory.createValue("foo10"));
	mockData.put("mbc_rod_huso", ValueFactory.createValue("bar11"));
	mockData.put("mbc_rod_arido_grueso", ValueFactory.createValue("foo11"));
	mockData.put("mbc_rod_arido_fino", ValueFactory.createValue("bar12"));
	mockData.put("mbc_rod_filler", ValueFactory.createValue("foo12"));
	mockData.put("mbc_rod_ligante", ValueFactory.createNullValue());

	SqlUtils.update("audasa_extgia",
		"firme_trabajos",
		mockData,
		"id_firme",
		"1");

	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos" +
		" WHERE " + "id_firme = '1' AND sentido = 'bar'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	    assertEquals(rs.getString(3), null);
	    assertEquals(rs.getString(4), mockData.get("pk_inicial").toString());
	    assertEquals(rs.getString(5), mockData.get("pk_final").toString());
	    assertEquals(rs.getString(6), mockData.get("sentido").toString());
	    assertEquals(rs.getString(7), mockData.get("descripcion").toString());
	    assertEquals(rs.getString(8), "1980-12-31");
	    assertEquals(rs.getString(9), mockData.get("explanada_cm").toString());
	    assertEquals(rs.getString(10), mockData.get("zahorra_artificial_cm").toString());
	    assertEquals(rs.getString(11), mockData.get("suelo_cemento_cm").toString());
	    assertEquals(rs.getString(12), mockData.get("grava_cemento_cm").toString());
	    assertEquals(rs.getString(13), mockData.get("mbc_base_cm").toString());
	    assertEquals(rs.getString(14), mockData.get("mbc_intermedia_cm").toString());
	    assertEquals(rs.getString(15), mockData.get("mbc_rodadura_cm").toString());
	    assertEquals(rs.getString(16), mockData.get("explanada").toString());
	    assertEquals(rs.getString(17), mockData.get("zahorra_artificial").toString());
	    assertEquals(rs.getString(18), mockData.get("suelo_cemento").toString());
	    assertEquals(rs.getString(19), mockData.get("gc_arido_grueso").toString());
	    assertEquals(rs.getString(20), mockData.get("gc_arido_fino").toString());
	    assertEquals(rs.getString(21), mockData.get("gc_cemento").toString());
	    assertEquals(rs.getString(22), mockData.get("mbc_bas_huso").toString());
	    assertEquals(rs.getString(23), mockData.get("mbc_bas_arido_grueso").toString());
	    assertEquals(rs.getString(24), mockData.get("mbc_bas_arido_fino").toString());
	    assertEquals(rs.getString(25), mockData.get("mbc_bas_filler").toString());
	    assertEquals(rs.getString(26), mockData.get("mbc_bas_ligante").toString());
	    assertEquals(rs.getString(27), mockData.get("mbc_rod_huso").toString());
	    assertEquals(rs.getString(28), mockData.get("mbc_rod_arido_grueso").toString());
	    assertEquals(rs.getString(29), mockData.get("mbc_rod_arido_fino").toString());
	    assertEquals(rs.getString(30), mockData.get("mbc_rod_filler").toString());
	    assertEquals(rs.getString(31), null);
	}
	assertEquals(true, inserted);
    }

    @Test
    public void deleteFirme() throws SQLException {
	Statement st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	ResultSet rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos" +
		" WHERE " + "id_firme = '1' AND sentido = 'bar'");
	rs.next();

	SqlUtils.delete("audasa_extgia",
		"firme_trabajos",
		"id_trabajo",
		rs.getString(2));

	st = DBSession.getCurrentSession().getJavaConnection().createStatement();
	rs = st.executeQuery("SELECT * FROM audasa_extgia.firme_trabajos WHERE " +
		"id_firme = '1' AND id_trabajo = '" + rs.getString(2) + "'");
	boolean inserted = false;
	if (rs.next()) {
	    inserted = true;
	}
	assertEquals(false, inserted);
    }
}
