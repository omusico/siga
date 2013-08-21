package es.icarto.gvsig.extgia.forms.obras_desague;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

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
public class ObrasDesagueForm extends AbstractFormWithLocationWidgets {

    public static final String ABEILLE_FILENAME = "forms/obras_desague_transversal.xml";
    public static final String ABEILLE_TRABAJOS_FILENAME = "forms/obras_desague_transversal_trabajos.xml";

    JTextField obraDesagueIDWidget;
    CalculateComponentValue obraDesagueid;
    private JComboBox tipoVia;
    private DependentComboboxHandler direccionDomainHandler;

    AddTrabajoListener addTrabajoListener;
    EditTrabajoListener editTrabajoListener;
    DeleteTrabajoListener deleteTrabajoListener;

    public ObrasDesagueForm(FLyrVect layer) {
	super(layer);
	initListeners();
    }

    private void addNewButtonsToActionsToolBar() {
	super.addNewButtonsToActionsToolBar(DBFieldNames.Elements.Obras_Desague);
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();

	direccionDomainHandler.updateComboBoxValues();

	if (obraDesagueIDWidget.getText().isEmpty()) {
	    obraDesagueid = new ObrasDesagueCalculateIDValue(this, getWidgetComponents(),
		    DBFieldNames.ID_OBRA_DESAGUE, DBFieldNames.ID_OBRA_DESAGUE);
	    obraDesagueid.setValue(true);
	}

	if (filesLinkButton == null) {
	    addNewButtonsToActionsToolBar();
	}

	// Embebed Tables
	int[] trabajoColumnsSize = {1, 1, 110, 70, 60};
	SqlUtils.createEmbebedTableFromDB(trabajos,
		"audasa_extgia", getTrabajosDBTableName(),
		DBFieldNames.trabajoFields, trabajoColumnsSize, "id_obra_desague",
		obraDesagueIDWidget.getText(),
		"id_trabajo");
	repaint();
    }

    protected void initListeners() {

	HashMap<String, JComponent> widgets = getWidgetComponents();

	obraDesagueIDWidget = (JTextField) widgets.get(DBFieldNames.ID_OBRA_DESAGUE);

	JComboBox direccion = (JComboBox) getWidgetComponents().get(
		"direccion");
	tipoVia = (JComboBox) getWidgetComponents().get("tipo_via");
	direccionDomainHandler = new DependentComboboxHandler(this,
		tipoVia, direccion);
	tipoVia.addActionListener(direccionDomainHandler);

	addTrabajoListener = new AddTrabajoListener();
	addTrabajoButton.addActionListener(addTrabajoListener);
	editTrabajoListener = new EditTrabajoListener();
	editTrabajoButton.addActionListener(editTrabajoListener);
	deleteTrabajoListener = new DeleteTrabajoListener();
	deleteTrabajoButton.addActionListener(deleteTrabajoListener);
    }

    @Override
    protected void removeListeners() {
	addTrabajoButton.removeActionListener(addTrabajoListener);
	editTrabajoButton.removeActionListener(editTrabajoListener);
	deleteTrabajoButton.removeActionListener(deleteTrabajoListener);
	super.removeListeners();
    }

    public class AddTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    ObrasDesagueTrabajosSubForm subForm = new ObrasDesagueTrabajosSubForm(
		    ABEILLE_TRABAJOS_FILENAME,
		    getTrabajosDBTableName(),
		    trabajos,
		    "id_obra_desague",
		    obraDesagueIDWidget.getText(),
		    null,
		    null,
		    false);
	    PluginServices.getMDIManager().addWindow(subForm);
	}
    }

    public class EditTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    if (trabajos.getSelectedRowCount() != 0) {
		int row = trabajos.getSelectedRow();
		ObrasDesagueTrabajosSubForm subForm = new ObrasDesagueTrabajosSubForm(
			ABEILLE_TRABAJOS_FILENAME,
			getTrabajosDBTableName(),
			trabajos,
			"id_obra_desague",
			obraDesagueIDWidget.getText(),
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

    public class DeleteTrabajoListener implements ActionListener {
	@Override
	public void actionPerformed(ActionEvent e) {
	    deleteElement(trabajos, getTrabajosDBTableName(), getTrabajosIDField());
	}
    }


    @Override
    public String getElement() {
	return DBFieldNames.Elements.Obras_Desague.name();
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
		.getResource("rules/obras_desague_metadata.xml")
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
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getTrabajosDBTableName() {
	return "obras_desague_trabajos";
    }

    @Override
    public String getReconocimientosFormFileName() {
	// TODO Auto-generated method stub
	return null;
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
	return "Obras Desague";
    }

    @Override
    protected boolean hasSentido() {
	return true;
    }

}
