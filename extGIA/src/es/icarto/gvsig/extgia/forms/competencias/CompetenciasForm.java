package es.icarto.gvsig.extgia.forms.competencias;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;

@SuppressWarnings("serial")
public class CompetenciasForm extends BasicAbstractForm {

    public static final String TABLENAME = "competencias";

    public CompetenciasForm(FLyrVect layer) {
	super(layer);
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    protected String getSchema() {
	return DBFieldNames.GIA_SCHEMA;
    }

}
