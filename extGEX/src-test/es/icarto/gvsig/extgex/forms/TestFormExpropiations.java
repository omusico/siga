package es.icarto.gvsig.extgex.forms;

import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.junit.Test;

import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.CommonMethodsForTestDBForms;
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
    public void testOpenExpropiationsForm() throws SQLException, DBException {
	DBSession session = DBSession.getCurrentSession();
	FLayer layer = session.getLayer(getTableName(), getTableName(),
		getSchema(), null, CRSFactory.getCRS("EPSG:23029"));

	AbstractForm dialog = new FormExpropiations((FLyrVect) layer, null);
	assertTrue(dialog != null);
	assertTrue(dialog.init());
    }

}
