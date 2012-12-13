package es.icarto.gvsig.extgia.forms.enlaces;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;

@SuppressWarnings("serial")
public class EnlacesForm extends AbstractFormWithLocationWidgets {

    public static String ABEILLE_FILENAME = "forms/enlaces.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/enlaces_reconocimiento_estado.xml";
    public static final String ABEILLE_CARRETERAS_FILENAME = "forms/enlaces_carreteras.xml";

    JTextField enlaceIDWidget;
    CalculateComponentValue enlaceid;

    JTable carreteras;
    JTable ramales;

    JButton addCarreteraButton;
    JButton editCarreteraButton;
    JButton deleteCarreteraButton;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;

    AddCarreteraListener addCarreteraListener;
    EditCarreteraListener editCarreteraListener;
    DeleteCarreteraListener deleteCarreteraListener;


    public EnlacesForm(FLyrVect layer) {
	super(layer);
	initWindow();
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar("enlaces");
    }

    @Override
    protected void initWindow() {
	super.initWindow();
	this.viewInfo.setTitle("Enlaces");
    }

    @Override
    protected void fillSpecificValues() {
	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	DBFieldNames.setReconocimientoEstadoFields(DBFieldNames.enlacesReconocimientoEstadoFields);
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_enlace", enlaceIDWidget.getText());

	SqlUtils.createEmbebedTableFromDB(carreteras, DBFieldNames.GIA_SCHEMA,
		"enlaces_carreteras_enlazadas", DBFieldNames.carreteras_enlazadas,
		null, "id_enlace", enlaceIDWidget.getText());

	SqlUtils.createEmbebedTableFromDB(ramales, DBFieldNames.GIA_SCHEMA,
		"enlaces_ramales", DBFieldNames.ramales,
		null, "id_enlace", enlaceIDWidget.getText());

    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	enlaceIDWidget = (JTextField) widgets.get(DBFieldNames.ID_ENLACE);

	enlaceid = new EnlacesCalculateIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_ENLACE, DBFieldNames.AREA_MANTENIMIENTO, DBFieldNames.BASE_CONTRATISTA,
		DBFieldNames.TRAMO, DBFieldNames.TIPO_VIA, DBFieldNames.MUNICIPIO, DBFieldNames.PK);
	enlaceid.setListeners();

	carreteras = (JTable) super.getFormBody().getComponentByName("tabla_carreteras");
	ramales = (JTable) super.getFormBody().getComponentByName("tabla_ramales");

	addCarreteraButton = (JButton) super.getFormBody().getComponentByName("add_carretera_button");
	editCarreteraButton = (JButton) super.getFormBody().getComponentByName("edit_carretera_button");
	deleteCarreteraButton = (JButton) super.getFormBody().getComponentByName("delete_carretera_button");

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	addCarreteraListener = new AddCarreteraListener();
	addCarreteraButton.addActionListener(addCarreteraListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	editCarreteraListener = new EditCarreteraListener();
	editCarreteraButton.addActionListener(editCarreteraListener);
	deleteReconocimientoListener = new DeleteReconocimientoListener();
	deleteReconocimientoButton.addActionListener(deleteReconocimientoListener);
	deleteCarreteraListener = new DeleteCarreteraListener();
	deleteCarreteraButton.addActionListener(deleteCarreteraListener);

    }

    @Override
    protected void removeListeners() {
	addReconocimientoButton.removeActionListener(addReconocimientoListener);
	editReconocimientoButton.removeActionListener(editReconocimientoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);
	super.removeListeners();

	DBFieldNames.setReconocimientoEstadoFields(DBFieldNames.genericReconocimientoEstadoFields);
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    EnlacesReconocimientosSubForm subForm =
		    new EnlacesReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_enlace",
			    enlaceIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    EnlacesCarreterasSubForm subForm =
		    new EnlacesCarreterasSubForm(
			    ABEILLE_CARRETERAS_FILENAME,
			    "enlaces_carreteras_enlazadas",
			    carreteras,
			    "id_enlace",
			    enlaceIDWidget.getText(),
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
		EnlacesReconocimientosSubForm subForm =
			new EnlacesReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_enlace",
				enlaceIDWidget.getText(),
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

    public class EditCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (carreteras.getSelectedRowCount() != 0) {
		int row = carreteras.getSelectedRow();
		EnlacesCarreterasSubForm subForm =
			new EnlacesCarreterasSubForm(
				ABEILLE_CARRETERAS_FILENAME,
				"enlaces_carreteras_enlazadas",
				carreteras,
				"id_enlace",
				enlaceIDWidget.getText(),
				"id_carretera_enlazada",
				carreteras.getValueAt(row, 0).toString(),
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

    public class DeleteCarreteraListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(carreteras, "enlace_carreteras_enlazadas",
		    "id_carretera_enlazada");
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
	return Preferences.getPreferences().getXMLFilePath();
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
	return "enlaces_reconocimiento_estado";
    }

    @Override
    public String getTrabajosDBTableName() {
	// TODO Auto-generated method stub
	return null;
    }


}
