package es.icarto.gvsig.extgia.forms.ramales;

import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.DBFieldNames.Elements;

@SuppressWarnings("serial")
public class RamalesForm extends AbstractFormWithLocationWidgets {

    public static final String[] colNames = { "gid", "ramal", "direccion",
	    "sentido", "longitud" };
    public static final String[] colAlias = { "ID Ramal", "Nombre Ramal",
	    "Dirección", "Sentido", "Longitud" };

    public RamalesForm(FLyrVect layer) {
	super(layer);
    }

    public static final String TABLENAME = "ramales";

    @Override
    public Elements getElement() {
	return DBFieldNames.Elements.Ramales;
    }

    @Override
    public String getElementID() {
	return "gid";
    }

    @Override
    public String getElementIDValue() {
	JTextField idWidget = (JTextField) getWidgets().get(getElementID());
	return idWidget.getText();
    }

    @Override
    public JTable getReconocimientosJTable() {
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	return null;
    }

    @Override
    public String getImagesDBTableName() {
	return getBasicName() + "_imagenes";
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
