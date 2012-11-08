package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SubFormReversionsAddExpropiations extends JPanel implements IWindow, ActionListener {

    private final double INTERSECTION_BUFFER = 100.0;

    private final FormPanel form;
    private final FLyrVect layer;
    private int currentRow;
    private final JTable fincasTable;
    private final String idReversion;
    private final IGeometry insertedGeom;

    private JComboBox idFinca;
    private JTextField superficie;
    private JTextField importe;
    private JButton addExpropiationButton;

    protected WindowInfo viewInfo = null;
    private final String title = "Añadir Fincas";
    private final int width = 300;
    private final int height = 105;

    public SubFormReversionsAddExpropiations(FLyrVect layer, JTable fincasTable, String idReversion, IGeometry insertedGeom) {
	InputStream stream = getClass().getClassLoader().getResourceAsStream("reversiones_add_expropiaciones.xml");
	FormPanel result = null;
	try {
	    result = new FormPanel(stream);
	    this.add(result);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	this.form = result;
	this.layer = layer;
	this.fincasTable = fincasTable;
	this.idReversion = idReversion;
	this.insertedGeom = insertedGeom;
	initWidgets();
    }

    private void initWidgets() {
	updateCurrentRowFromIDReversion(idReversion);

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	addExpropiationButton = (JButton) form.getComponentByName(DBNames.SUBFORMREVERSIONS_ADD_EXPROPIATIONS_BUTTON);
	addExpropiationButton.addActionListener(this);

	superficie =  (JTextField) form.getComponentByName(DBNames.SUBFORMREVERSIONS_SUPERFICIE);
	importe = (JTextField) form.getComponentByName(DBNames.SUBFORMREVERSIONS_IMPORTE);

	idFinca = (JComboBox) form.getComponentByName(DBNames.SUBFORMSREVERSIONS_IDFINCA);
	for (String id_finca : getFincasFromReversion()) {
	    idFinca.addItem(id_finca);
	}
    }

    private ArrayList<String> getFincasFromReversion() {
	ArrayList<String> fincas = null;
	try {
	    fincas = new ArrayList<String>();

	    TOCLayerManager tm = new TOCLayerManager();
	    FLyrVect fincasLayer = tm.getLayerByName(DBNames.LAYER_FINCAS);
	    SelectableDataSource fincasRecordset = fincasLayer.getRecordset();
	    IGeometry reversionGeometry;
	    if (insertedGeom != null) {
		reversionGeometry = insertedGeom;
	    }else {
		reversionGeometry = layer.getSource().getFeature(currentRow).getGeometry();
	    }
	    Geometry jtsReversionGeometry = reversionGeometry.toJTSGeometry();

	    int tramoIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_TRAMO_FINCAS);
	    int ucIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_UC_FINCAS);
	    int ayuntamientoIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_AYUNTAMIENTO_FINCAS);
	    int parroquiaIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS);
	    int numFincaIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_NUMEROFINCA_FINCAS);
	    int seccionIndex = fincasRecordset.getFieldIndexByName(DBNames.FIELD_SECCION_FINCAS);
	    ReadableVectorial layerSourceFeats = fincasLayer.getSource();
	    for (int i = 0; i < fincasRecordset.getRowCount(); i++) {
		IGeometry gvGeom = layerSourceFeats.getShape(i);
		if (gvGeom != null) {
		    if (!gvGeom.equals(jtsReversionGeometry)) {
			Geometry auxJTSGeom = gvGeom.toJTSGeometry();
			if ((jtsReversionGeometry.buffer(INTERSECTION_BUFFER)).intersects(auxJTSGeom)) {
			    String finca = fincasRecordset.getFieldValue(i, tramoIndex).toString() +
				    fincasRecordset.getFieldValue(i, ucIndex).toString() +
				    fincasRecordset.getFieldValue(i, ayuntamientoIndex).toString() +
				    fincasRecordset.getFieldValue(i, parroquiaIndex).toString() +
				    "--" + fincasRecordset.getFieldValue(i, numFincaIndex).toString() + "--" +
				    fincasRecordset.getFieldValue(i, seccionIndex).toString();
			    fincas.add(finca);
			}
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return fincas;
    }


    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == addExpropiationButton) {
	    String fincaID = idFinca.getSelectedItem().toString();
	    String idFincaValue = idFinca.getSelectedItem().toString().replaceAll("--", "");
	    Value[] fincaData = getFincaData(idFincaValue);
	    DefaultTableModel tableModel = (DefaultTableModel) fincasTable.getModel();
	    tableModel.addRow(fincaData);
	}
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

    public void updateCurrentRowFromIDReversion(String idReversion) {
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_GID_REVERSIONES + " " +
		"FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_REVERSIONES + " " +
		"WHERE " + DBNames.FIELD_IDREVERSION_REVERSIONES + " = " +
		"'" + idReversion + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareCall(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.next();
	    currentRow = Integer.parseInt(rs.getString(1));
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private Value[] getFincaData(String idFinca) {
	Value[] reversionData = new Value[3];

	reversionData[0] = ValueFactory.createValue(idFinca);
	if (!superficie.getText().isEmpty()) {
	    reversionData[1] = ValueFactory.createValue(Double.parseDouble(superficie.getText()));
	}
	if (!importe.getText().isEmpty()) {
	    reversionData[2] = ValueFactory.createValue(Integer.parseInt(importe.getText()));
	}
	return reversionData;
    }

}
