package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.SqlUtils;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

@SuppressWarnings("serial")
public abstract class AbstractFormWithLocationWidgets extends AbstractForm {

    private static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    private static final String BASE_CONTRATISTA = "base_contratista";
    private static final String TRAMO = "tramo";
    private static final String TIPO_VIA = "tipo_via";
    private static final String TIPO_VIA_PF = "tipo_via_pf";
    private static final String NOMBRE_VIA = "nombre_via";
    private static final String NOMBRE_VIA_PF = "nombre_via_pf";

    private FormPanel form;

    private JComboBox areaMantenimientoWidget;
    private JComboBox baseContratistaWidget;
    private JComboBox tramoWidget;
    private JComboBox tipoViaWidget;
    private JComboBox nombreViaWidget;

    private JComboBox tipoViaPFWidget;
    private JComboBox nombreViaPFWidget;

    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;
    private UpdateTipoViaListener updateTipoViaListener;
    private UpdateNombreViaListener updateNombreViaListener;

    private UpdateNombreViaPFListener updateNombreViaPFListener;

    protected JTable reconocimientoEstado;
    protected JTable trabajos;
    protected JButton addReconocimientoButton;
    protected JButton editReconocimientoButton;
    protected JButton deleteReconocimientoButton;
    protected JButton addTrabajoButton;
    protected JButton editTrabajoButton;
    protected JButton deleteTrabajoButton;

    public AbstractFormWithLocationWidgets (FLyrVect layer) {
	super(layer);
	initWidgets();
	setListeners();
    }

    protected void initWindow() {
	this.viewInfo.setHeight(700);
	this.viewInfo.setWidth(690);
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	areaMantenimientoWidget = (JComboBox) widgets.get(AREA_MANTENIMIENTO);
	baseContratistaWidget = (JComboBox) widgets.get(BASE_CONTRATISTA);
	tramoWidget = (JComboBox) widgets.get(TRAMO);
	tipoViaWidget = (JComboBox) widgets.get(TIPO_VIA);
	nombreViaWidget = (JComboBox) widgets.get(NOMBRE_VIA);

	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();
	updateTipoViaListener = new UpdateTipoViaListener();
	updateNombreViaListener = new UpdateNombreViaListener();

	areaMantenimientoWidget.addActionListener(updateBaseContratistaListener);
	baseContratistaWidget.addActionListener(updateTramoListener);
	tramoWidget.addActionListener(updateTipoViaListener);
	tipoViaWidget.addActionListener(updateNombreViaListener);

	if (elementHasIPandFP()) {
	    tipoViaPFWidget = (JComboBox) widgets.get(TIPO_VIA_PF);
	    updateNombreViaPFListener = new UpdateNombreViaPFListener();
	    tipoViaPFWidget.addActionListener(updateNombreViaPFListener);
	    nombreViaPFWidget = (JComboBox) widgets.get(NOMBRE_VIA_PF);
	}

	reconocimientoEstado = (JTable) widgets.get("reconocimiento_estado");
	trabajos = (JTable) widgets.get("trabajos");
	addReconocimientoButton = (JButton) form.getComponentByName("add_reconocimiento_button");
	editReconocimientoButton = (JButton) form.getComponentByName("edit_reconocimiento_button");
	addTrabajoButton = (JButton) form.getComponentByName("add_trabajo_button");
	editTrabajoButton = (JButton) form.getComponentByName("edit_trabajo_button");
	deleteReconocimientoButton = (JButton) form.getComponentByName("delete_reconocimiento_button");
	deleteTrabajoButton = (JButton) form.getComponentByName("delete_trabajo_button");
    }

    public class UpdateBaseContratistaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues()) {
		String id = ((KeyValue)areaMantenimientoWidget.getSelectedItem()).getKey();
		String getBaseContratistaQuery =
			"SELECT id, item FROM audasa_extgia_dominios.base_contratista" +
				" WHERE id_am = " + id + ";";
		baseContratistaWidget.removeAllItems();
		baseContratistaWidget.addItem(new KeyValue("", ""));
		if (!id.isEmpty()) {
		    for (KeyValue value: SqlUtils.getKeyValueListFromSql(getBaseContratistaQuery)) {
			baseContratistaWidget.addItem(value);
		    }
		}
	    }
	}

    }

    public class UpdateTramoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && baseContratistaWidget.getItemCount()!=0) {
		String id = ((KeyValue) baseContratistaWidget.getSelectedItem()).getKey();
		String getTramoQuery =
			"SELECT id, item FROM audasa_extgia_dominios.tramo" +
				" WHERE id_bc = " + id + ";";
		tramoWidget.removeAllItems();
		tramoWidget.addItem(new KeyValue("", ""));
		if (!id.isEmpty()) {
		    for (KeyValue value: SqlUtils.getKeyValueListFromSql(getTramoQuery)) {
			tramoWidget.addItem(value);
		    }
		}
	    }
	}

    }

    public class UpdateTipoViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tramoWidget.getItemCount()!=0) {
		String id = ((KeyValue) tramoWidget.getSelectedItem()).getKey();
		String getTipoViaQuery =
			"SELECT id, item  FROM audasa_extgia_dominios.tipo_via" +
				" WHERE id_tramo = " + id + ";";
		tipoViaWidget.removeAllItems();
		tipoViaWidget.addItem(new KeyValue("", ""));
		if (!id.isEmpty()) {
		    for (KeyValue value: SqlUtils.getKeyValueListFromSql(getTipoViaQuery)) {
			tipoViaWidget.addItem(value);
		    }
		    if (elementHasIPandFP()) {
			tipoViaPFWidget.removeAllItems();
			tipoViaPFWidget.addItem(new KeyValue("", ""));
			for (KeyValue value: SqlUtils.getKeyValueListFromSql(getTipoViaQuery)) {
			    tipoViaPFWidget.addItem(value);
			}
		    }
		}
	    }
	}

    }

    public class UpdateNombreViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaWidget.getItemCount()!=0) {
		String id = ((KeyValue) tipoViaWidget.getSelectedItem()).getKey();
		String getNombreViaQuery =
			"SELECT id, item FROM audasa_extgia_dominios.nombre_via" +
				" WHERE id_tv = " + id + ";";
		nombreViaWidget.removeAllItems();
		nombreViaWidget.addItem(new KeyValue("", ""));
		if (!id.isEmpty()) {
		    for (KeyValue value: SqlUtils.getKeyValueListFromSql(getNombreViaQuery)) {
			nombreViaWidget.addItem(value);
		    }
		}
	    }
	}

    }

    public class UpdateNombreViaPFListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaPFWidget.getItemCount()!=0) {
		String id = ((KeyValue) tipoViaPFWidget.getSelectedItem()).getKey();
		String getNombreViaQuery =
			"SELECT id, item FROM audasa_extgia_dominios.nombre_via" +
				" WHERE id_tv = " + id + ";";
		nombreViaPFWidget.removeAllItems();
		nombreViaPFWidget.addItem(new KeyValue("", ""));
		for (KeyValue value: SqlUtils.getKeyValueListFromSql(getNombreViaQuery)) {
		    nombreViaPFWidget.addItem(value);
		}
	    }
	}

    }

    @Override
    protected void removeListeners() {
	areaMantenimientoWidget.removeActionListener(updateBaseContratistaListener);
	baseContratistaWidget.removeActionListener(updateTramoListener);
	tramoWidget.removeActionListener(updateTipoViaListener);
	tipoViaWidget.removeActionListener(updateNombreViaListener);

	if (elementHasIPandFP()) {
	    tipoViaPFWidget.removeActionListener(updateNombreViaPFListener);
	}

	super.removeListeners();
    }

    public JComboBox getAreaMantenimientoWidget() {
	return areaMantenimientoWidget;
    }

    public JComboBox getBaseContratistaWidget() {
	return baseContratistaWidget;
    }

    public JComboBox getTramoWidget() {
	return tramoWidget;
    }

    public JComboBox getTipoViaWidget() {
	return tipoViaWidget;
    }

    public JComboBox getNombreViaWidget() {
	return nombreViaWidget;
    }

    private boolean elementHasIPandFP() {
	if (layer.getName().equalsIgnoreCase("taludes")) {
	    return true;
	}else {
	    return false;
	}
    }

    @Override
    public FormPanel getFormBody() {
	if (this.form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream(getFormBodyPath());
	    try {
		this.form = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	}
	return this.form;
    }

    protected void deleteElement(JTable embebedTable, String dbTableName,
	    String pkField) {

	if (embebedTable.getSelectedRowCount() != 0) {
	    Object[] options = {"Eliminar", "Cancelar"};
	    int response = JOptionPane.showOptionDialog(null,
		    "Los datos seleccionados se eliminarán de forma permanente.",
		    "Eliminar",
		    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, // do not use a custom Icon
		    options, // the titles of buttons
		    options[0]); // default button title
	    if (response == JOptionPane.YES_OPTION) {
		int selectedRow = embebedTable.getSelectedRow();
		String pkValue = embebedTable.getValueAt(selectedRow, 0).toString();
		DefaultTableModel model = (DefaultTableModel) embebedTable.getModel();
		model.removeRow(selectedRow);
		SqlUtils.delete(DBFieldNames.GIA_SCHEMA, dbTableName, pkField, pkValue);
		repaint();
	    } else {
		// Nothing to do
	    }
	}else {
	    JOptionPane.showMessageDialog(null,
		    "Debe seleccionar una fila para editar los datos.",
		    "Ninguna fila seleccionada",
		    JOptionPane.INFORMATION_MESSAGE);
	}
    }

    @Override
    protected abstract void fillSpecificValues();

    public abstract String getFormBodyPath();

    @Override
    public abstract Logger getLoggerName();

    @Override
    public abstract String getXMLPath();

    public abstract JTable getReconocimientosJTable();

    public abstract JTable getTrabajosJTable();

    public abstract String getReconocimientosDBTableName();

    public abstract String getTrabajosDBTableName();

    public String getReconocimientosIDField() {
	return "n_inspeccion";
    }

    public String getTrabajosIDField() {
	return "id_trabajo";
    }
}
