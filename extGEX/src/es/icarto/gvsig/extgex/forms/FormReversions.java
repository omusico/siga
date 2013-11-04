package es.icarto.gvsig.extgex.forms;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.GEXPreferences;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;


@SuppressWarnings("serial")
public class FormReversions extends AbstractForm implements TableModelListener {

    private FLyrVect layer = null;

    private FormPanel form;
    private JTable fincasAfectadas;
    private JTextField expId;
    private FormExpropiationsLauncher expropiationsLauncher;

    public FormReversions(FLyrVect layer, IGeometry insertedGeom) {
	super(layer);
	this.layer = layer;
	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer,
		this);
	if (filesLinkB != null) {
	    actionsToolBar.add(filesLinkB);
	}

	this.getActionsToolBar().remove(saveB);
	this.getActionsToolBar().remove(removeB);
	this.getActionsToolBar().remove(undoB);
	this.getActionsToolBar().remove(copyPreviousB);
	this.getActionsToolBar().remove(copySelectedB);
    }

    @Override
    protected void enableSaveButton(boolean bool) {
	if (!isChangedValues()) {
	    saveB.setEnabled(false);
	} else {
	    saveB.setEnabled(bool);
	}
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	ImageComponent image = (ImageComponent) form.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	expropiationsLauncher = new FormExpropiationsLauncher(this);
	fincasAfectadas = (JTable) widgets.get("tabla_fincas_afectadas");
	fincasAfectadas.addMouseListener(expropiationsLauncher);

	expId = (JTextField) widgets.get("exp_id");
    }

    private String getExpId() {
	return expId.getText();
    }



    @Override
    protected void removeListeners() {
	super.removeListeners();
	fincasAfectadas.removeMouseListener(expropiationsLauncher);
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger("ReversionsFileForm");
    }

    @Override
    public FormPanel getFormBody() {
	if (form == null) {
	    InputStream stream = getClass().getClassLoader().getResourceAsStream("reversiones.xml");
	    FormPanel result = null;
	    try {
		result = new FormPanel(stream);
	    } catch (FormException e) {
		e.printStackTrace();
	    }
	    form = result;
	}
	return form;
    }

    @Override
    protected void fillSpecificValues() {
	updateJTableFincasAfectadas();
    }

    private void updateJTableFincasAfectadas() {

	ArrayList<String> columnasFincas = new ArrayList<String>();
	columnasFincas.add("Id_Finca");
	columnasFincas.add("Expedientes PM");
	columnasFincas.add("Superficie");
	columnasFincas.add("Importe");
	columnasFincas.add("Fecha");

	double totalSuperficie = 0.0;
	double totalImporte = 0.0;

	try {
	    DefaultTableModel tableModel = new DefaultTableModel() {
		@Override
		public boolean isCellEditable(int row, int column) {
		    return false;
		}
	    };
	    for (String columnName : columnasFincas) {
		tableModel.addColumn(columnName);
	    }
	    fincasAfectadas.setModel(tableModel);
	    fincasAfectadas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    fincasAfectadas.getColumnModel().getColumn(0).setPreferredWidth(165);
	    fincasAfectadas.getColumnModel().getColumn(1).setPreferredWidth(207);
	    fincasAfectadas.getColumnModel().getColumn(4).setPreferredWidth(90);


	    Value[] reversionData = new Value[5];
	    ResultSet rs = getFincasByExpReversion();

	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		reversionData[1] = ValueFactory.createValue(getExpedientesPMByFinca(rs.getString(1)));
		if (rs.getObject(2) != null) {
		    totalSuperficie = totalSuperficie + rs.getDouble(2);
		    reversionData[2] = ValueFactory.createValue(getDoubleFormatted(rs.getDouble(2)));
		}else {
		    reversionData[2] = ValueFactory.createNullValue();
		}
		if (rs.getObject(3) != null) {
		    totalImporte = totalImporte + rs.getDouble(3);
		    reversionData[3] = ValueFactory.createValue(getDoubleFormatted(rs.getDouble(3)));
		}else {
		    reversionData[3] = ValueFactory.createNullValue();
		}
		if (rs.getObject(4) != null) {
		    reversionData[4] = ValueFactory.createValue(getDateFormatted(rs.getDate(4)));
		}else {
		    reversionData[4] = ValueFactory.createNullValue();
		}
		tableModel.addRow(reversionData);
	    }
	    reversionData[0] = ValueFactory.createValue("<html><b>" + "TOTAL" + "</b></html>");
	    reversionData[1] = ValueFactory.createNullValue();
	    reversionData[2] = ValueFactory.createValue("<html><b>" + totalSuperficie + "</b></html>");
	    reversionData[3] = ValueFactory.createValue("<html><b>" + totalImporte + "</b></html>");
	    reversionData[4] = ValueFactory.createNullValue();
	    tableModel.addRow(reversionData);
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private String getDateFormatted(Date date) {
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
	return dateFormat.format(date);
    }

    private String getDoubleFormatted(Double doubleValue) {
	NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
	return doubleFormat.format(doubleValue);
    }

    private ResultSet getFincasByExpReversion() throws SQLException {
	PreparedStatement statement;
	String query = "SELECT " +
		DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + ", " +
		DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", " +
		DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES + ", " +
		DBNames.FIELD_FECHA_FINCAS_REVERSIONES + " " +
		"FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
		"WHERE " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + " = '" + getExpId() + "';";
	statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	statement.execute();
	ResultSet rs = statement.getResultSet();
	return rs;
    }

    private String getExpedientesPMByFinca(String idFinca) {
	String expedientesPM = "";
	PreparedStatement statement;
	String query = "SELECT " +
		DBNames.FIELD_NUMPM_FINCAS_PM + " " +
		"FROM " + DBNames.PM_SCHEMA + "." + DBNames.TABLE_FINCAS_PM + " " +
		"WHERE " + DBNames.FIELD_IDFINCA_FINCAS_PM + " = '" + idFinca +
		"';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		expedientesPM = expedientesPM + rs.getString(1) + "/";
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return expedientesPM;
    }

    @Override
    public String getXMLPath() {
	return GEXPreferences.getPreferences().getXMLFilePath();
    }

    @Override
    public void tableChanged(TableModelEvent arg0) {
	super.setChangedValues(true);
	super.saveB.setEnabled(true);
    }

}
