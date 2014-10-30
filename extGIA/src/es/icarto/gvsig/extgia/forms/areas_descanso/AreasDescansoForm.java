package es.icarto.gvsig.extgia.forms.areas_descanso;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import javax.swing.JButton;
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
public class AreasDescansoForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_RAMALES_FILENAME = "forms/areas_descanso_ramales.xml";
    public static final String TABLENAME = "areas_descanso";

    JTextField areaDescansoIDWidget;
    CalculateComponentValue areaDescansoid;

    JTable ramales;

    JButton addRamalButton;
    JButton editRamalButton;
    JButton deleteRamalButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    AddRamalListener addRamalListener;
    EditRamalListener editRamalListener;
    DeleteRamalListener deleteRamalListener;

    public AreasDescansoForm(FLyrVect layer) {
	super(layer);
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Areas_Descanso);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		DBFieldNames.GIA_SCHEMA, getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null,
		"id_area_descanso", areaDescansoIDWidget.getText(),
		"n_inspeccion");

	int[] trabajoColumnsSize = { 1, 1, 110, 70, 60 };
	SqlUtils.createEmbebedTableFromDB(trabajos, DBFieldNames.GIA_SCHEMA,
		getTrabajosDBTableName(), DBFieldNames.trabajoFields,
		trabajoColumnsSize, "id_area_descanso",
		areaDescansoIDWidget.getText(), "id_trabajo");

	DBFieldNames.setRamalesFields(DBFieldNames.ramales_area_descanso);
	SqlUtils.createEmbebedTableFromDB(ramales, DBFieldNames.GIA_SCHEMA,
		"areas_descanso_ramales", DBFieldNames.ramales, null,
		"id_area_descanso", areaDescansoIDWidget.getText(), "id_ramal");
	repaint();
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	Map<String, JComponent> widgets = getWidgets();

	areaDescansoIDWidget = (JTextField) widgets
		.get(DBFieldNames.ID_AREA_DESCANSO);

	areaDescansoid = new AreasDescansoCalculateIDValue(this, getWidgets(),
		DBFieldNames.ID_AREA_DESCANSO, DBFieldNames.AREA_MANTENIMIENTO,
		DBFieldNames.BASE_CONTRATISTA, DBFieldNames.TRAMO,
		DBFieldNames.TIPO_VIA, DBFieldNames.MUNICIPIO,
		DBFieldNames.SENTIDO);
	areaDescansoid.setListeners();

	ramales = (JTable) super.getFormBody().getComponentByName(
		"tabla_ramales");

	addRamalButton = (JButton) super.getFormBody().getComponentByName(
		"add_ramal_button");
	editRamalButton = (JButton) super.getFormBody().getComponentByName(
		"edit_ramal_button");
	deleteRamalButton = (JButton) super.getFormBody().getComponentByName(
		"delete_ramal_button");

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	addRamalListener = new AddRamalListener();
	addRamalButton.addActionListener(addRamalListener);

	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	editRamalListener = new EditRamalListener();
	editRamalButton.addActionListener(editRamalListener);

	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton
		.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
	deleteRamalListener = new DeleteRamalListener();
	deleteRamalButton.addActionListener(deleteRamalListener);
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton
		.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton
		.removeActionListener(deleteReconocimientoListener);

	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);

	addRamalButton.removeActionListener(addRamalListener);
	editRamalButton.removeActionListener(editRamalListener);
	deleteRamalButton.removeActionListener(deleteRamalListener);

	super.removeListeners();

	DBFieldNames.setRamalesFields(DBFieldNames.generic_ramales);
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged()
		.containsKey("id_area_descanso")) {
	    if (areaDescansoIDWidget.getText() != "") {
		String query = "SELECT id_area_descanso FROM audasa_extgia.areas_descanso "
			+ " WHERE id_area_descanso = '"
			+ areaDescansoIDWidget.getText() + "';";
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

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasDescansoReconocimientosSubForm subForm = new AreasDescansoReconocimientosSubForm(
		    getReconocimientosFormFileName(),
		    getReconocimientosDBTableName(), reconocimientoEstado,
		    "id_area_descanso", areaDescansoIDWidget.getText(), null,
		    null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasDescansoTrabajosSubForm subForm = new AreasDescansoTrabajosSubForm(
		    getTrabajosFormFileName(), getTrabajosDBTableName(),
		    trabajos, "id_area_descanso",
		    areaDescansoIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasDescansoRamalesSubForm subForm = new AreasDescansoRamalesSubForm(
		    ABEILLE_RAMALES_FILENAME, "areas_descanso_ramales",
		    ramales, "id_area_descanso",
		    areaDescansoIDWidget.getText(), null, null, false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (reconocimientoEstado.getSelectedRowCount() != 0) {
		int row = reconocimientoEstado.getSelectedRow();
		AreasDescansoReconocimientosSubForm subForm = new AreasDescansoReconocimientosSubForm(
			getReconocimientosFormFileName(),
			getReconocimientosDBTableName(), reconocimientoEstado,
			"id_area_descanso", areaDescansoIDWidget.getText(),
			"n_inspeccion", reconocimientoEstado.getValueAt(row, 0)
				.toString(), true);
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
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
		AreasDescansoTrabajosSubForm subForm = new AreasDescansoTrabajosSubForm(
			getTrabajosFormFileName(), getTrabajosDBTableName(),
			trabajos, "id_area_descanso",
			areaDescansoIDWidget.getText(), "id_trabajo", trabajos
				.getValueAt(row, 0).toString(), true);
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class EditRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (ramales.getSelectedRowCount() != 0) {
		int row = ramales.getSelectedRow();
		AreasDescansoRamalesSubForm subForm = new AreasDescansoRamalesSubForm(
			ABEILLE_RAMALES_FILENAME, "areas_descanso_ramales",
			ramales, "id_area_descanso",
			areaDescansoIDWidget.getText(), "id_ramal", ramales
				.getValueAt(row, 0).toString(), true);
		PluginServices.getMDIManager().addWindow(subForm);
	    } else {
		JOptionPane.showMessageDialog(null,
			"Debe seleccionar una fila para editar los datos.",
			"Ninguna fila seleccionada",
			JOptionPane.INFORMATION_MESSAGE);
	    }
	}
    }

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado,
		    getReconocimientosDBTableName(),
		    getReconocimientosIDField());
	}
    }

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, getTrabajosDBTableName(),
		    getTrabajosIDField());
	}
    }

    public class DeleteRamalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(ramales, "areas_descanso_ramales", "id_ramal");
	}
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
    public boolean isSpecialCase() {
	return false;
    }

    @Override
    protected String getBasicName() {
	return TABLENAME;
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Areas_Descanso.name();
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_area_descanso";
    }

    @Override
    public String getElementIDValue() {
	return areaDescansoIDWidget.getText();
    }

    @Override
    public String getImagesDBTableName() {
	return "areas_descanso_imagenes";
    }

}
