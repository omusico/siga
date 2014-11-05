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

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.ReconocimientosHandler;
import es.icarto.gvsig.extgia.forms.utils.TrabajosHandler;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class SenhalizacionVariableForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "senhalizacion_variable";

    JTextField senhalizacionVariableIDWidget;
    CalculateComponentValue senhalizacionVariableid;

    public SenhalizacionVariableForm(FLyrVect layer) {
	super(layer);

	// int[] trabajoColumnsSize = { 1, 30, 90, 70, 200 };
	addTableHandler(new TrabajosHandler(getTrabajosDBTableName(),
		getWidgetComponents(), getElementID(),
		DBFieldNames.trabajosColNames, DBFieldNames.trabajosColAlias,
		this));

	addTableHandler(new ReconocimientosHandler(
		getReconocimientosDBTableName(), getWidgetComponents(),
		getElementID(),
		DBFieldNames.reconocimientosWhitoutIndexFieldsNames,
		DBFieldNames.reconocimientosWhitoutIndexFieldsAlias, this));
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Senhalizacion_Variable);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (senhalizacionVariableIDWidget.getText().isEmpty()) {
	    senhalizacionVariableid = new SenhalizacionVariableCalculateIDValue(
		    this, getWidgetComponents(), getElementID(), getElementID());
	    senhalizacionVariableid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	repaint();
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
				"El ID está en uso, por favor, escoja otro.",
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
    public String getElement() {
	return DBFieldNames.Elements.Senhalizacion_Variable.name();
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
    public boolean isSpecialCase() {
	// TODO Auto-generated method stub
	return false;
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
