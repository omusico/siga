package es.icarto.gvsig.extgia.forms.senhalizacion_variable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class SenhalizacionVariableForm extends AbstractFormWithLocationWidgets {

    public static final String TABLENAME = "senhalizacion_variable";

    JTextField senhalizacionVariableIDWidget;
    CalculateComponentValue senhalizacionVariableid;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public SenhalizacionVariableForm(FLyrVect layer) {
	super(layer);
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Senhalizacion_Variable);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (senhalizacionVariableIDWidget.getText().isEmpty()) {
	    senhalizacionVariableid = new SenhalizacionVariableCalculateIDValue(this, getWidgetComponents(),
		    getElementID(), getElementID());
	    senhalizacionVariableid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		DBFieldNames.GIA_SCHEMA, getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoWhitoutIndexFields, null, getElementID(),
		getElementIDValue(), "n_inspeccion");

	int[] trabajoColumnsSize = {1, 30, 90, 70, 200};
	SqlUtils.createEmbebedTableFromDB(trabajos, DBFieldNames.GIA_SCHEMA,
		getTrabajosDBTableName(), DBFieldNames.trabajoFields,
		trabajoColumnsSize, getElementID(), getElementIDValue(), "id_trabajo");
	repaint();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged().containsKey(getElementID())) {
	    if (getElementIDValue() != "") {
		String query = "SELECT id_senhal_variable FROM audasa_extgia.senhalizacion_variable "
			+ " WHERE id_senhal_variable = '" + getElementIDValue()
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
	reconocimientoEstado = (JTable) widgets.get("reconocimiento_estado_sin_indice");
	senhalizacionVariableIDWidget = (JTextField) widgets.get(getElementID());

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);

	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);

	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);

	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);

	super.removeListeners();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    SenhalizacionVariableReconocimientosSubForm subForm =
		    new SenhalizacionVariableReconocimientosSubForm(
			    getReconocimientosFormFileName(),
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    getElementID(),
			    getElementIDValue(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    SenhalizacionVariableTrabajosSubForm subForm =
		    new SenhalizacionVariableTrabajosSubForm(
			    getTrabajosFormFileName(),
			    getTrabajosDBTableName(),
			    trabajos,
			    getElementID(),
			    getElementIDValue(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }



    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		SenhalizacionVariableReconocimientosSubForm subForm =
			new SenhalizacionVariableReconocimientosSubForm(
				getReconocimientosFormFileName(),
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				getElementID(),
				getElementIDValue(),
				"n_inspeccion",
				reconocimientoEstado.getValueAt(row, 0).toString(),
				true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class EditTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (trabajos.getSelectedRowCount() != 0) {
		int row = trabajos.getSelectedRow();
		SenhalizacionVariableTrabajosSubForm subForm =
			new SenhalizacionVariableTrabajosSubForm(
				getTrabajosFormFileName(),
				getTrabajosDBTableName(),
				trabajos,
				getElementID(),
				getElementIDValue(),
				"id_trabajo",
				trabajos.getValueAt(row, 0).toString(),
				true);
		PluginServices.getMDIManager().addWindow(subForm);
	    }else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
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
