package es.icarto.gvsig.extgex.cad;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileWriteException;
import com.iver.cit.gvsig.exceptions.validate.ValidateRowException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.edition.IRowEdited;
import com.iver.cit.gvsig.fmap.edition.VectorialEditableAdapter;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.gui.cad.tools.EIELPolylineCADTool;

public class AddFincaCADTool extends EIELPolylineCADTool {

    public final static String KEY = "add_finca_cad_tool";

    private int featIndex;
    private IRowEdited row;

    private static final Logger logger = Logger
	    .getLogger(AddFincaCADTool.class);

    @Override
    public void init() {
	super.init();
	FLyrVect layer = (FLyrVect) getVLE().getLayer();
	featIndex = layer.getSelectionSupport().getSelection().nextSetBit(0);
	try {
	    row = getVLE().getVEA().getRow(featIndex);
	} catch (ExpansionFileReadException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	}

    }

    @Override
    public void addGeometry(IGeometry geometry) {

	IFeature feat = (IFeature) row.getLinkedRow();
	feat.setGeometry(geometry);

	VectorialEditableAdapter vea = getVLE().getVEA();
	try {
	    vea.modifyRow(row.getIndex(), feat, KEY, EditionEvent.ALPHANUMERIC);
	} catch (ExpansionFileWriteException e) {
	    logger.error(e.getStackTrace(), e);
	    NotificationManager.addError(e.getMessage(), e);
	} catch (ExpansionFileReadException e) {
	    logger.error(e.getStackTrace(), e);
	    NotificationManager.addError(e.getMessage(), e);
	} catch (ValidateRowException e) {
	    logger.error(e.getStackTrace(), e);
	    NotificationManager.addError(e.getMessage(), e);
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	    NotificationManager.addError(e.getMessage(), e);
	}

    }

    @Override
    public String getName() {
	return KEY;
    }
}
