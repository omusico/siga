package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;
import com.vividsolutions.jts.geom.Geometry;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class SubFormExpropiationsAddReversions extends JPanel implements IWindow, ActionListener {

    private final double INTERSECTION_BUFFER = 100.0;

    private final FormPanel form;
    private final FLyrVect layer;
    private int currentRow;
    private final JTable reversionsTable;
    private final String idFinca;

    private JComboBox idReversion;
    private JTextField superficie;
    private JTextField importe;
    private JButton addReversionButton;

    protected WindowInfo viewInfo = null;
    private final String title = "Añadir Reversiones";
    private final int width = 275;
    private final int height = 125;

    public SubFormExpropiationsAddReversions(FLyrVect layer, JTable reversionsTable, String idFinca) {
	InputStream stream = getClass().getClassLoader().getResourceAsStream("expropiaciones_add_reversiones.xml");
	FormPanel result = null;
	try {
	    result = new FormPanel(stream);
	    this.add(result);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	this.form = result;
	this.layer = layer;
	this.reversionsTable = reversionsTable;
	this.idFinca = idFinca;
	initWidgets();
    }

    private void initWidgets() {
	updateCurrentRowFromFincaID(idFinca);

	addReversionButton = (JButton) form.getComponentByName(DBNames.SUBFORMEXPROPIATIONS_ADD_REVERSIONS_BUTTON);
	addReversionButton.addActionListener(this);

	superficie =  (JTextField) form.getComponentByName(DBNames.SUBFORMEXPROPIATIONS_SUPERFICIE);
	importe = (JTextField) form.getComponentByName(DBNames.SUBFORMEXPROPIATIONS_IMPORTE);

	idReversion = (JComboBox) form.getComponentByName(DBNames.SUBFORMSEXPROPIATIONS_IDREVERSIONS);
	for (String id_reversion : getReversionsFromFinca()) {
	    idReversion.addItem(id_reversion);
	}
    }

    private ArrayList<String> getReversionsFromFinca() {
	ArrayList<String> reversions = null;
	try {
	    reversions = new ArrayList<String>();

	    TOCLayerManager tm = new TOCLayerManager();
	    FLyrVect reversionsLayer = tm.getLayerByName(DBNames.LAYER_REVERSIONES);
	    SelectableDataSource reversionsRecordset = reversionsLayer.getRecordset();
	    IGeometry fincaGeometry = layer.getSource().getFeature(currentRow).getGeometry();
	    Geometry jtsFincaGeometry = fincaGeometry.toJTSGeometry();

	    int numReversionIndex = reversionsRecordset.getFieldIndexByName(DBNames.FIELD_IDREVERSION_REVERSIONES);
	    ReadableVectorial layerSourceFeats = reversionsLayer.getSource();
	    for (int i = 0; i < reversionsRecordset.getRowCount(); i++) {
		IGeometry gvGeom = layerSourceFeats.getShape(i);
		if (gvGeom != null) {
		    if (!gvGeom.equals(jtsFincaGeometry)) {
			Geometry auxJTSGeom = gvGeom.toJTSGeometry();
			if ((jtsFincaGeometry.buffer(INTERSECTION_BUFFER)).intersects(auxJTSGeom)) {
			    String reversion = reversionsRecordset.getFieldValue(i, numReversionIndex).toString();
			    reversions.add(reversion);
			}
		    }
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return reversions;
    }

    public void updateCurrentRowFromFincaID(String idFinca) {
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_GID_FINCAS + " " +
		"FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCAS + " " +
		"WHERE " + DBNames.FIELD_IDFINCA_FINCAS + " = " +
		"'" + idFinca + "';";
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
	if (e.getSource() == addReversionButton) {
	    String numReversion = idReversion.getSelectedItem().toString();
	    Value[] reversionData = getReversionData(numReversion);
	    DefaultTableModel tableModel = (DefaultTableModel) reversionsTable.getModel();
	    tableModel.addRow(reversionData);
	}
    }

    private Value[] getReversionData(String idReversion) {
	Value[] reversionData = new Value[3];

	reversionData[0] = ValueFactory.createValue(idReversion);
	if (!superficie.getText().isEmpty()) {
	    reversionData[1] = ValueFactory.createValue(Double.parseDouble(superficie.getText()));
	}
	if (!importe.getText().isEmpty()) {
	    reversionData[2] = ValueFactory.createValue(Integer.parseInt(importe.getText()));
	}

	return reversionData;
    }
}
