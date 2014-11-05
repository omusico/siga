package es.icarto.gvsig.extgex.forms;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;

@SuppressWarnings("serial")
public class FormExpropiationLine extends BasicAbstractForm {

    public static final String TOCNAME = "Linea_Expropiacion";
    public static final String TABLENAME = "linea_expropiacion";

    public FormExpropiationLine(FLyrVect layer) {
	super(layer);
    }

    @Override
    public String getBasicName() {
	return TABLENAME;
    }

    @Override
    protected String getSchema() {
	return DBNames.SCHEMA_DATA;
    }

}
