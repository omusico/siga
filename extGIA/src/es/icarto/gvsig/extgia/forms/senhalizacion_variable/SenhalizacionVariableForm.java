package es.icarto.gvsig.extgia.forms.senhalizacion_variable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.GIAAlphanumericTableHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Elements;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class SenhalizacionVariableForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "senhalizacion_variable";

    JTextField senhalizacionVariableIDWidget;
    CalculateComponentValue senhalizacionVariableid;

    public SenhalizacionVariableForm(FLyrVect layer) {
	super(layer);

	addTableHandler(new GIAAlphanumericTableHandler(
		getTrabajosDBTableName(), getWidgets(), getElementID(),
		DBFieldNames.trabajosColNames, DBFieldNames.trabajosColAlias,
		DBFieldNames.trabajosColWidths, this));

	addTableHandler(new GIAAlphanumericTableHandler(
		getReconocimientosDBTableName(), getWidgets(), getElementID(),
		DBFieldNames.reconocimientosWhitoutIndexColNames,
		DBFieldNames.reconocimientosWhitoutIndexColAlias, null, this));
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (senhalizacionVariableIDWidget.getText().isEmpty()) {
	    senhalizacionVariableid = new SenhalizacionVariableCalculateIDValue(
		    this, getWidgetComponents(), getElementID(), getElementID());
	    senhalizacionVariableid.setValue(true);
	}
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey(getElementID())) {
	    if (getElementIDValue() != "") {
		String query = "SELECT id_senhal_variable FROM audasa_extgia.senhalizacion_variable "
			+ " WHERE id_senhal_variable = '"
			+ getElementIDValue()
			+ "';";
		PreparedStatement statement = null;
		Connection connection = DBSession.getCurrentSession()
			.getJavaConnection();
		try {
		    statement = connection.prepareStatement(query);
		    statement.execute();
		    ResultSet rs = statement.getResultSet();
		    if (rs.next()) {
			JOptionPane.showMessageDialog(null,
				"El ID est� en uso, por favor, escoja otro.",
				"ID en uso", JOptionPane.WARNING_MESSAGE);
			return true;
		    }
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	    }
	}
	return super.validationHasErrors();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	senhalizacionVariableIDWidget = (JTextField) widgets
		.get(getElementID());

    }

    @Override
    public Elements getElement() {
	return Elements.Senhalizacion_Variable;
    }

    @Override
    public String getElementID() {
	return "id_senhal_variable";
    }

    @Override
    public String getElementIDValue() {
	return senhalizacionVariableIDWidget.getText();
    }

    @Override
    public JTable getReconocimientosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public JTable getTrabajosJTable() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getImagesDBTableName() {
	return "senhalizacion_variable_imagenes";
    }

    @Override
    public String getBasicName() {
	return TABLENAME;
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
