package es.icarto.gvsig.extpm.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.jeta.forms.components.panel.FormPanel;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.extpm.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SubFormPMParcelasAfectadas extends JPanel implements IWindow, ActionListener {

    private final double INTERSECTION_BUFFER = 250.0;

    private final FormPanel form;
    private final FLyrVect layer;
    private final int insertedRow;

    private JComboBox idFinca;
    private JTable parcelasAfectadas;
    private DefaultTableModel model;
    private JButton addFinca;
    private JButton removeFinca;

    protected WindowInfo viewInfo = null;
    private final String title = "Parcelas Afectadas";
    private final int width = 450;
    private final int height = 200;

    public SubFormPMParcelasAfectadas(FLyrVect layer, int insertedRow) {
	form = new FormPanel("pm_parcelas_afectadas.xml");
	this.add(form);
	this.layer = layer;
	this.insertedRow = insertedRow;
	initWidgets();
    }

    private void initWidgets() {
	idFinca = (JComboBox)form.getComponentByName("id_finca");
	for (String id_finca : getFincasFromPM()) {
	    idFinca.addItem(id_finca);
	}

	parcelasAfectadas = (JTable)form.getComponentByName("parcelas_afectadas_table");

	ArrayList<String> columnNames = new ArrayList<String>();
	columnNames.add("Tramo");
	columnNames.add("ID Finca");
	model = new DefaultTableModel();
	for (String columnName : columnNames) {
	    model.addColumn(columnName);
	}
	parcelasAfectadas.setModel(model);

	addFinca = (JButton)form.getComponentByName("addFincaButton");
	addFinca.addActionListener(this);
	removeFinca = (JButton)form.getComponentByName("removeFincaButton");
	removeFinca.addActionListener(this);
    }

    private String[] getFincaValuesFromID(String idFinca) {
	PreparedStatement statement;
	String[] fincaValues = new String[2];
	try {
	    String query = "SELECT " + Preferences.TRAMOS_FIELD_NOMBRE + ", "
	    + Preferences.FINCAS_FIELD_IDFINCA +
	    " FROM " + Preferences.FINCAS_TABLENAME + " a, " +
	    Preferences.TRAMOS_TABLENAME + " b " +
	    " WHERE " + Preferences.FINCAS_FIELD_TRAMO + " = " + Preferences.TRAMOS_FIELD_ID +
	    " AND " + Preferences.FINCAS_FIELD_IDFINCA + "=" + "'" + idFinca + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    while (rs.next()) {
		fincaValues[0] = rs.getString(1);
		fincaValues[1] = rs.getString(2);
	    }
	    rs.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return fincaValues;
    }

    private ArrayList<String> getFincasFromPM() {
	ArrayList<String> parcelas = null;
	try {
	    parcelas = new ArrayList<String>();

	    TOCLayerManager tm = new TOCLayerManager();
	    FLyrVect fincasLayer = tm.getLayerByName("Fincas");
	    SelectableDataSource fincasRecordset = fincasLayer.getRecordset();
	    IGeometry pmGeometry = layer.getSource().getFeature(insertedRow).getGeometry();
	    Geometry jtsPmGeometry = pmGeometry.toJTSGeometry();

	    int tramoIndex = fincasRecordset.getFieldIndexByName("tramo");
	    int ucIndex = fincasRecordset.getFieldIndexByName("unidad_constructiva");
	    int municipioIndex = fincasRecordset.getFieldIndexByName("ayuntamiento");
	    int parroquiaIndex = fincasRecordset.getFieldIndexByName("parroquia_subtramo");
	    int numFincaIndex = fincasRecordset.getFieldIndexByName("numero_finca");
	    int seccionIndex = fincasRecordset.getFieldIndexByName("seccion");
	    ReadableVectorial layerSourceFeats = fincasLayer.getSource();
	    for (int i = 0; i < fincasRecordset.getRowCount(); i++) {
		IGeometry gvGeom = layerSourceFeats.getShape(i);
		if (gvGeom != null) {
		    if (!gvGeom.equals(jtsPmGeometry)) {
			Geometry auxJTSGeom = gvGeom.toJTSGeometry();
			if ((jtsPmGeometry.buffer(INTERSECTION_BUFFER)).intersects(auxJTSGeom)) {
			    String parcela = fincasRecordset.getFieldValue(i, tramoIndex).toString() +
			    fincasRecordset.getFieldValue(i, ucIndex).toString() +
			    fincasRecordset.getFieldValue(i, municipioIndex).toString() +
			    fincasRecordset.getFieldValue(i, parroquiaIndex).toString() + "-" +
			    fincasRecordset.getFieldValue(i, numFincaIndex).toString() + "-" +
			    fincasRecordset.getFieldValue(i, seccionIndex).toString();
			    parcelas.add(parcela);
			}
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return parcelas;
    }

    @Override
    public WindowInfo getWindowInfo() {
	viewInfo = new WindowInfo(WindowInfo.MODALDIALOG);
	viewInfo.setTitle(title);
	viewInfo.setWidth(width);
	viewInfo.setHeight(height);
	return viewInfo;
    }

    @Override
    public Object getWindowProfile() {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == addFinca) {
	    String idFincaValue = idFinca.getSelectedItem().toString().replaceAll("-", "");
	    model.addRow(getFincaValuesFromID(idFincaValue));
	}
	if (e.getSource() == removeFinca) {
	    int[] selectedRows = parcelasAfectadas.getSelectedRows();
	    for (int i=0; i<selectedRows.length; i++) {
		model.removeRow(i);
	    }

	}
    }

}
