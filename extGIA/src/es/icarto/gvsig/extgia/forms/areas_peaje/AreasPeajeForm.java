package es.icarto.gvsig.extgia.forms.areas_peaje;

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

import es.icarto.gvsig.extgia.forms.obras_paso.ObrasPasoCalculateIDValue;
import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class AreasPeajeForm extends AbstractFormWithLocationWidgets {

    public static String ABEILLE_FILENAME = "forms/areas_peaje.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/areas_peaje_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/areas_peaje_trabajos.xml";
    public static final String ABEILLE_VIAS_FILENAME = "forms/areas_peaje_vias.xml";

    JTextField areaPeajeIDWidget;
    CalculateComponentValue areaPeajeid;

    JButton addViaButton;
    JButton editViaButton;
    JButton deleteViaButton;

    JTable vias;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    AddViaListener addViaListener;
    EditViaListener editViaListener;
    DeleteViaListener deleteViaListener;

    public AreasPeajeForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Areas_Peaje);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (areaPeajeIDWidget.getText().isEmpty()) {
	    areaPeajeid = new ObrasPasoCalculateIDValue(this, getWidgetComponents(),
		    getElementID(), getElementID());
	    areaPeajeid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		DBFieldNames.GIA_SCHEMA, getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoWhitoutIndexFields, null, getElementID(),
		areaPeajeIDWidget.getText(), "n_inspeccion");

	int[] trabajoColumnsSize = {1, 1, 110, 70, 60};
	SqlUtils.createEmbebedTableFromDB(trabajos, DBFieldNames.GIA_SCHEMA,
		getTrabajosDBTableName(), DBFieldNames.trabajoFields,
		trabajoColumnsSize, getElementID(), areaPeajeIDWidget.getText(), "id_trabajo");

	SqlUtils.createEmbebedTableFromDB(vias, DBFieldNames.GIA_SCHEMA,
		"areas_peaje_vias", DBFieldNames.viasFields,
		null, getElementID(), areaPeajeIDWidget.getText(), "via");
	repaint();
    }

    @Override
    protected boolean validationHasErrors() {
	if (this.getFormController().getValuesChanged().containsKey(getElementID())) {
	    if (areaPeajeIDWidget.getText() != "") {
		String query = "SELECT id_area_peaje FROM audasa_extgia.areas_peaje "
			+ " WHERE id_area_peaje = '" + areaPeajeIDWidget.getText()
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

	areaPeajeIDWidget = (JTextField) widgets.get(getElementID());

	vias = (JTable) super.getFormBody().getComponentByName("tabla_vias");

	addViaButton = (JButton) super.getFormBody().getComponentByName("add_via_button");
	editViaButton = (JButton) super.getFormBody().getComponentByName("edit_via_button");
	deleteViaButton = (JButton) super.getFormBody().getComponentByName("delete_via_button");

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	addViaListener = new AddViaListener();
	addViaButton.addActionListener(addViaListener);

	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	editViaListener = new EditViaListener();
	editViaButton.addActionListener(editViaListener);

	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
	deleteViaListener = new DeleteViaListener();
	deleteViaButton.addActionListener(deleteViaListener);
    }

    @Override
    protected void setListeners() {
	super.setListeners();
	HashMap<String, JComponent> widgets = getWidgetComponents();
	reconocimientoEstado = (JTable) widgets.get("reconocimiento_estado_sin_indice");
    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);

	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);

	addViaButton.removeActionListener(addViaListener);
	editViaButton.removeActionListener(editViaListener);
	deleteViaButton.removeActionListener(deleteViaListener);

	super.removeListeners();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasPeajeReconocimientosSubForm subForm =
		    new AreasPeajeReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    getElementID(),
			    areaPeajeIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasPeajeTrabajosSubForm subForm =
		    new AreasPeajeTrabajosSubForm(
			    ABEILLE_TRABAJOS_FILENAME,
			    getTrabajosDBTableName(),
			    trabajos,
			    getElementID(),
			    areaPeajeIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddViaListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    AreasPeajeViasSubForm subForm =
		    new AreasPeajeViasSubForm(
			    ABEILLE_VIAS_FILENAME,
			    "areas_peaje_vias",
			    vias,
			    getElementID(),
			    areaPeajeIDWidget.getText(),
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
		AreasPeajeReconocimientosSubForm subForm =
			new AreasPeajeReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				getElementID(),
				areaPeajeIDWidget.getText(),
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
		AreasPeajeTrabajosSubForm subForm =
			new AreasPeajeTrabajosSubForm(
				ABEILLE_TRABAJOS_FILENAME,
				getTrabajosDBTableName(),
				trabajos,
				getElementID(),
				areaPeajeIDWidget.getText(),
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

    public class EditViaListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (vias.getSelectedRowCount() != 0) {
		int row = vias.getSelectedRow();
		AreasPeajeViasSubForm subForm =
			new AreasPeajeViasSubForm(
				ABEILLE_VIAS_FILENAME,
				"areas_peaje_vias",
				vias,
				getElementID(),
				areaPeajeIDWidget.getText(),
				"id_via",
				vias.getValueAt(row, 0).toString(),
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

    public class DeleteReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(reconocimientoEstado, getReconocimientosDBTableName(),
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

    public class DeleteViaListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(vias, "areas_peaje_vias",
		    "id_via");
	}
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Areas_Peaje.name();
    }

    @Override
    public String getElementID() {
	return "id_area_peaje";
    }

    @Override
    public String getElementIDValue() {
	return areaPeajeIDWidget.getText();
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
		.getResource("rules/areas_peaje_metadata.xml")
		.getPath();
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
    public String getReconocimientosDBTableName() {
	return "areas_peaje_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	return "areas_peaje_trabajos";
    }

    @Override
    public String getImagesDBTableName() {
	return "areas_peaje_imagenes";
    }

    @Override
    public String getReconocimientosFormFileName() {
	return ABEILLE_RECONOCIMIENTOS_FILENAME;
    }

    @Override
    public String getTrabajosFormFileName() {
	return ABEILLE_TRABAJOS_FILENAME;
    }

    @Override
    public boolean isSpecialCase() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected String getBasicName() {
	return "Áreas Peaje";
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
