package es.icarto.gvsig.extgia.forms.pasos_mediana;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.utils.AbstractFormWithLocationWidgets;
import es.icarto.gvsig.extgia.forms.utils.CalculateComponentValue;
import es.icarto.gvsig.extgia.forms.utils.LaunchGIAForms.Elements;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.preferences.Preferences;
import es.icarto.gvsig.extgia.utils.SqlUtils;

@SuppressWarnings("serial")
public class PasosMedianaForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/pasos_mediana.xml";
    public static final String ABEILLE_RECONOCIMIENTOS_FILENAME = "forms/pasos_mediana_reconocimiento_estado.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/pasos_mediana_trabajos.xml";

    JTextField pasoMedianaIDWidget;
    CalculateComponentValue pasoMedianaid;

    AddReconocimientoListener addReconocimientoListener;
    EditReconocimientoListener editReconocimientoListener;
    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteReconocimientoListener deleteReconocimientoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public PasosMedianaForm(FLyrVect layer) {
	super(layer);
	initWindow();
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(Elements.Pasos_Mediana);
    }

    @Override
    protected void initWindow() {
	super.initWindow();
	this.viewInfo.setTitle("Pasos Mediana");
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 120, 60, 60};
	SqlUtils.createEmbebedTableFromDB(reconocimientoEstado,
		"audasa_extgia", getReconocimientosDBTableName(),
		DBFieldNames.reconocimientoEstadoFields, null, "id_paso_mediana",
		pasoMedianaIDWidget.getText());
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", getTrabajosDBTableName(),
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_paso_mediana",
		pasoMedianaIDWidget.getText());
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	pasoMedianaIDWidget = (JTextField) widgets.get(DBFieldNames.ID_PASO_MEDIANA);

	pasoMedianaid = new PasosMedianaCalculateIDValue(this, getWidgetComponents(),
		DBFieldNames.ID_PASO_MEDIANA, DBFieldNames.TRAMO, DBFieldNames.PK);
	pasoMedianaid.setListeners();

	addReconocimientoListener = new AddReconocimientoListener();
	addReconocimientoButton.addActionListener(addReconocimientoListener);
	editReconocimientoListener = new EditReconocimientoListener();
	editReconocimientoButton.addActionListener(editReconocimientoListener);
	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
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
	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteReconocimientoButton.removeActionListener(deleteReconocimientoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);
	super.removeListeners();
    }

    public class AddReconocimientoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    PasosMedianaReconocimientosSubForm subForm =
		    new PasosMedianaReconocimientosSubForm(
			    ABEILLE_RECONOCIMIENTOS_FILENAME,
			    getReconocimientosDBTableName(),
			    reconocimientoEstado,
			    "id_paso_mediana",
			    pasoMedianaIDWidget.getText(),
			    null,
			    null,
			    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    PasosMedianaReconocimientosSubForm subForm =
		    new PasosMedianaReconocimientosSubForm(
			    ABEILLE_TRABAJOS_FILENAME,
			    getTrabajosDBTableName(),
			    trabajos,
			    "id_paso_mediana",
			    pasoMedianaIDWidget.getText(),
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
		PasosMedianaReconocimientosSubForm subForm =
			new PasosMedianaReconocimientosSubForm(
				ABEILLE_RECONOCIMIENTOS_FILENAME,
				getReconocimientosDBTableName(),
				reconocimientoEstado,
				"id_paso_mediana",
				pasoMedianaIDWidget.getText(),
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
		PasosMedianaReconocimientosSubForm subForm =
			new PasosMedianaReconocimientosSubForm(
				ABEILLE_TRABAJOS_FILENAME,
				getTrabajosDBTableName(),
				trabajos,
				"id_paso_mediana",
				pasoMedianaIDWidget.getText(),
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
	return "pasos_mediana_reconocimiento_estado";
    }
    @Override
    public String getTrabajosDBTableName() {
	return "pasos_mediana_trabajos";
    }

}