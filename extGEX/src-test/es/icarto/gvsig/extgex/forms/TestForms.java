package es.icarto.gvsig.extgex.forms;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.SQLException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayerFactory;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestForms {

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
    public void testOpenExpropiationsForm() {
	FLayer layer;
	try {
	    layer = DBSession.getCurrentSession().getLayer("audasa_expropiaciones.exp_finca",
		    CRSFactory.getCRS("EPSG:23029"));
	    FormExpropiations dialog = new FormExpropiations((FLyrVect) layer, null);
	    assertTrue(dialog!=null);
	    assertTrue(dialog.init());
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (DBException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testOpenReversionsForm() {
	FLayer layer;
	try {
	    layer = DBSession.getCurrentSession().getLayer("audasa_expropiaciones.exp_reversion",
		    CRSFactory.getCRS("EPSG:23029"));
	    FormReversions dialog = new FormReversions((FLyrVect) layer, null);
	    assertTrue(dialog!=null);
	    assertTrue(dialog.init());
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (DBException e) {
	    e.printStackTrace();
	}
    }

}
