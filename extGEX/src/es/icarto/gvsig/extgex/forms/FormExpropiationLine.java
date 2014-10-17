package es.icarto.gvsig.extgex.forms;

import javax.swing.ImageIcon;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
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
    protected void setListeners() {
	super.setListeners();
	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);
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
