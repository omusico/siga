package es.icarto.gvsig.extgia.forms.areas_mantenimiento;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class AreasMantenimientoForm extends AbstractFormWithLocationWidgets {

    public static String ABEILLE_FILENAME = "forms/areas_mantenimiento.xml";
    public static final String ABEILLE_RAMALES_FILENAME = "forms/areas_mantenimiento_ramales.xml";

    JTextField areaMantenimientoIDWidget;
    CalculateComponentValue areaMantenimientoid;

    JButton addRamalButton;
    JButton editRamalButton;
    JButton deleteRamalButton;

    JTable ramales;

    AddRamalListener addRamalListener;
    EditRamalListener editRamalListener;
    DeleteRamalListener deleteRamalListener;

    public AreasMantenimientoForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Areas_Mantenimiento);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (areaMantenimientoIDWidget.getText().isEmpty()) {
	    areaMantenimientoid = new AreasMantenimientoCalculateIDValue(this, getWidgetComponents(),
		    getElementID(), getElementID());
	    areaMantenimientoid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(ramales, DBFieldNames.GIA_SCHEMA,
		"areas_mantenimiento_ramales", DBFieldNames.ramales_area_mantenimiento,
		null, getElementID(), areaMantenimientoIDWidget.getText(), "id_ramal");
	repaint();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged().containsKey(getElementID())) {
	    if (areaMantenimientoIDWidget.getText() != "") {
		String query = "SELECT id_area_mantenimiento FROM audasa_extgia.areas_mantenimiento "
			+ " WHERE id_area_mantenimiento = '" + areaMantenimientoIDWidget.getText()
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

    private void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	areaMantenimientoIDWidget = (JTextField) widgets.get(getElementID());

	ramales = (JTable) super.getFormBody().getComponentByName("tabla_ramales");

	addRamalButton = (JButton) super.getFormBody().getComponentByName("add_ramal_button");
	editRamalButton = (JButton) super.getFormBody().getComponentByName("edit_ramal_button");
	deleteRamalButton = (JButton) super.getFormBody().getComponentByName("delete_ramal_button");

	addRamalListener = new AddRamalListener();
	addRamalButton.addActionListener(addRamalListener);
	editRamalListener = new EditRamalListener();
	editRamalButton.addActionListener(editRamalListener);
	deleteRamalListener = new DeleteRamalListener();
	deleteRamalButton.addActionListener(deleteRamalListener);
    }

    @Override
    protected void removeListeners() {
	addRamalButton.removeActionListener(addRamalListener);
	editRamalButton.removeActionListener(editRamalListener);
	deleteRamalButton.removeActionListener(deleteRamalListener);

	super.removeListeners();
    }

    public class AddRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasMantenimientoRamalesSubForm subForm =
		    new AreasMantenimientoRamalesSubForm(
			    ABEILLE_RAMALES_FILENAME,
			    "areas_mantenimiento_ramales",
			    ramales,
			    getElementID(),
			    areaMantenimientoIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (ramales.getSelectedRowCount() != 0) {
		int row = ramales.getSelectedRow();
		AreasMantenimientoRamalesSubForm subForm =
			new AreasMantenimientoRamalesSubForm(
				ABEILLE_RAMALES_FILENAME,
				"areas_mantenimiento_ramales",
				ramales,
				getElementID(),
				areaMantenimientoIDWidget.getText(),
				"id_ramal",
				ramales.getValueAt(row, 0).toString(),
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

    public class DeleteRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(ramales, "areas_mantenimiento_ramales",
		    "id_ramal");
	}
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Areas_Mantenimiento.name();
    }

    @Override
    public String getElementID() {
	return "id_area_mantenimiento";
    }

    @Override
    public String getElementIDValue() {
	return areaMantenimientoIDWidget.getText();
    }

    @Override
    public String getFormBodyPath() {
	return ABEILLE_FILENAME;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    public String getXMLPath() {
	return this.getClass().getClassLoader()
		.getResource("rules/areas_mantenimiento_metadata.xml")
		.getPath();
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
    public String getReconocimientosDBTableName() {
	return null;
    }

    @Override
    public String getTrabajosDBTableName() {
	return null;
    }

    @Override
    public String getImagesDBTableName() {
	return "areas_mantenimiento_imagenes";
    }

    @Override
    public String getReconocimientosFormFileName() {
	return null;
    }

    @Override
    public String getTrabajosFormFileName() {
	return null;
    }

    @Override
    public boolean isSpecialCase() {
	return false;
    }

    @Override
    protected String getBasicName() {
	return "Áreas Mantenimiento";
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
