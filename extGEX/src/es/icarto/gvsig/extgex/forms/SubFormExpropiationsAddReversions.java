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
    private JButton addReversionButton;

    protected WindowInfo viewInfo = null;
    private final String title = "Añadir Reversiones";
    private final int width = 275;
    private final int height = 75;

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

	addReversionButton = (JButton) form.getComponentByName("add_reversion_button");
	addReversionButton.addActionListener(this);

	idReversion = (JComboBox) form.getComponentByName("id_reversion");
	for (String id_reversion : getReversionsFromFinca()) {
	    idReversion.addItem(id_reversion);
	}
    }

    private ArrayList<String> getReversionsFromFinca() {
	ArrayList<String> reversions = null;
	try {
	    reversions = new ArrayList<String>();

	    TOCLayerManager tm = new TOCLayerManager();
	    FLyrVect reversionsLayer = tm.getLayerByName("Reversiones");
	    SelectableDataSource reversionsRecordset = reversionsLayer.getRecordset();
	    IGeometry fincaGeometry = layer.getSource().getFeature(currentRow).getGeometry();
	    Geometry jtsFincaGeometry = fincaGeometry.toJTSGeometry();

	    int numReversionIndex = reversionsRecordset.getFieldIndexByName("id_reversion");
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
	String query = "SELECT gid " +
		"FROM audasa_expropiaciones.exp_finca " +
		"WHERE id_finca = " +
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

    private Value[] getReversionData(String numReversion) {
	Value[] reversionData = new Value[3];
	PreparedStatement statement;
	try {
	    String query = "SELECT superficie, importe_reversion " +
		    "FROM audasa_expropiaciones.exp_reversion " +
		    "WHERE id_reversion = '" + numReversion + "';" ;
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(numReversion);
		reversionData[1] = ValueFactory.createValue(rs.getDouble(1));
		reversionData[2] = ValueFactory.createValue(rs.getInt(2));
	    }
	    rs.close();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return reversionData;
    }
}
