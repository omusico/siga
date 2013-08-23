package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
import es.icarto.gvsig.navtableforms.ormlite.domainvalidator.listeners.DependentComboboxHandler;

@SuppressWarnings("serial")
public class SenhalizacionVerticalForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/senhalizacion_vertical.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/senhalizacion_vertical_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/senhalizacion_vertical_trabajos.xml";
    public static final String ABEILLE_SENHALES_FILENAME = "forms/senhalizacion_vertical_senhales.xml";

    JTextField elementoSenhalizacionIDWidget;
    CalculateComponentValue elementoSenhalizacionid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    JTable senhales;

    JButton addSenhalButton;
    JButton editSenhalButton;
    JButton deleteSenhalButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    AddSenhalListener addSenhalListener;
    EditSenhalListener editSenhalListener;
    DeleteSenhalListener deleteSenhalListener;

    public SenhalizacionVerticalForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Senhalizacion_Vertical);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (elementoSenhalizacionIDWidget.getText().isEmpty()) {
	    elementoSenhalizacionid = new SenhalizacionVerticalCalculateIDValue(this, getWidgetComponents(),
		    DBFieldNames.ID_ELEMENTO_SENHALIZACION, DBFieldNames.ID_ELEMENTO_SENHALIZACION);
	    elementoSenhalizacionid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 130, 70, 50 };
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_elemento_senhalizacion",
		elementoSenhalizacionIDWidget.getText(),
		"n_inspeccion");
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", getTrabajosDBTableName(),
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_elemento_senhalizacion",
		elementoSenhalizacionIDWidget.getText(),
		"id_trabajo");
	int[] senhalesColumnsSize = {20, 45, 45, 180, 40, 40};
	SqlUtils.createEmbebedTableFromDB(senhales,
		"audasa_extgia", "senhalizacion_vertical_senhales", DBFieldNames.senhales,
		senhalesColumnsSize, "id_elemento_senhalizacion",
		elementoSenhalizacionIDWidget.getText(),
		"id_senhal_vertical");
	repaint();
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	elementoSenhalizacionIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ELEMENTO_SENHALIZACION);

	JComboBox direccion = (JComboBox) getWidgetComponents().get(
		"direccion");
	tipoVia = (JComboBox) getWidgetComponents().get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this,
		tipoVia, direccion);
	tipoVia.addActionListener(direccionDomainHandler);

	senhales = (JTable) super.getFormBody().getComponentByName("tabla_senhales");

	addSenhalButton = (JButton) super.getFormBody().getComponentByName("add_senhal_button");
	editSenhalButton = (JButton) super.getFormBody().getComponentByName("edit_senhal_button");
	deleteSenhalButton = (JButton) super.getFormBody().getComponentByName("delete_senhal_button");

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);

	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);

	addSenhalListener = new AddSenhalListener();
	addSenhalButton.addActionListener(addSenhalListener);
	editSenhalListener = new EditSenhalListener();
	editSenhalButton.addActionListener(editSenhalListener);
	deleteSenhalListener = new DeleteSenhalListener();
	deleteSenhalButton.addActionListener(deleteSenhalListener);

    }

    @Override
    protected void removeListeners() {
	tipoVia.removeActionListener(direccionDomainHandler);

	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);

	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);

	addSenhalButton.removeActionListener(addSenhalListener);
	editSenhalButton.removeActionListener(editSenhalListener);
	deleteSenhalButton.removeActionListener(deleteSenhalListener);

	super.removeListeners();

	DBFieldNames.setTrabajosFields(DBFieldNames.genericTrabajoFields);
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    SenhalizacionVerticalReconocimientosSubForm subForm =
		    new SenhalizacionVerticalReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_elemento_senhalizacion",
			    elementoSenhalizacionIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    SenhalizacionVerticalTrabajosSubForm subForm = new SenhalizacionVerticalTrabajosSubForm(
		    ABEILLE_TRABAJOS_FILENAME,
		    getTrabajosDBTableName(),
		    trabajos,
		    "id_elemento_senhalizacion",
		    elementoSenhalizacionIDWidget.getText(),
		    null,
		    null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddSenhalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    SenhalizacionVerticalSenhalesSubForm subForm = new SenhalizacionVerticalSenhalesSubForm(
		    ABEILLE_SENHALES_FILENAME,
		    "senhalizacion_vertical_senhales",
		    senhales,
		    "id_elemento_senhalizacion",
		    elementoSenhalizacionIDWidget.getText(),
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
		SenhalizacionVerticalReconocimientosSubForm subForm =
			new SenhalizacionVerticalReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_elemento_senhalizacion",
				elementoSenhalizacionIDWidget.getText(),
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
		SenhalizacionVerticalTrabajosSubForm subForm = new SenhalizacionVerticalTrabajosSubForm(
			ABEILLE_TRABAJOS_FILENAME,
			getTrabajosDBTableName(),
			trabajos,
			"id_elemento_senhalizacion",
			elementoSenhalizacionIDWidget.getText(),
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

    public class EditSenhalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (senhales.getSelectedRowCount() != 0) {
		int row = senhales.getSelectedRow();
		SenhalizacionVerticalSenhalesSubForm subForm = new SenhalizacionVerticalSenhalesSubForm(
			ABEILLE_SENHALES_FILENAME,
			"senhalizacion_vertical_senhales",
			senhales,
			"id_elemento_senhalizacion",
			elementoSenhalizacionIDWidget.getText(),
			"id_senhal_vertical",
			senhales.getValueAt(row, 0).toString(),
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
	    deleteElement(trabajos, getTrabajosDBTableName(), getTrabajosIDField());
	}
    }

    public class DeleteSenhalListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(senhales, "senhalizacion_vertical_senhales", "id_senhal_vertical");
	}
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
		.getResource("rules/senhalizacion_vertical_metadata.xml")
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
	return "senhalizacion_vertical_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	return "senhalizacion_vertical_trabajos";
    }

    @Override
    public boolean isSpecialCase() {
	// TODO Auto-generated method stub
	return false;
    }

    @Override
    protected String getBasicName() {
	return "Señalización Vertical";
    }

    @Override
    public String getElement() {
	return DBFieldNames.Elements.Senhalizacion_Vertical.name();
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
    protected boolean hasSentido() {
	return true;
    }

    @Override
    public String getElementID() {
	return "id_elemento_senhalizacion";
    }

}
