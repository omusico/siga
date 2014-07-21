package es.icarto.gvsig.extgex.forms;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class TestFormExpropiations extends CommonMethodsForTestDBForms {

    @Override
    protected String getSchema() {
	return "audasa_expropiaciones";
    }

    @Override
    protected String getTableName() {
	return FormExpropiations.TABLENAME;
    }

    @Test
    public void testOpenExpropiationsForm() {
	FLayer layer;
	try {
	    layer = DBSession.getCurrentSession().getLayer(
		    getSchema() + "." + getTableName(),
		    CRSFactory.getCRS("EPSG:23029"));
	    AbstractForm dialog = new FormExpropiations((FLyrVect) layer, null);
	    assertTrue(dialog != null);
	    assertTrue(dialog.init());
	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (DBException e) {
	    e.printStackTrace();
	}
    }

}
