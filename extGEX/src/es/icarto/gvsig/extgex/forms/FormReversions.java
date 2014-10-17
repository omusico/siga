package es.icarto.gvsig.extgex.forms;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueFactory;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.extgex.navtable.NavTableComponentsFactory;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.BasicAbstractForm;
import es.icarto.gvsig.navtableforms.gui.CustomTableModel;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class FormReversions extends BasicAbstractForm implements
	TableModelListener {

    public static final String TABLENAME = "exp_rv";
    public static final String TOCNAME = "Reversiones";

    private JTable fincasAfectadas;
    private JTextField expId;
    private FormExpropiationsLauncher expropiationsLauncher;

    public FormReversions(FLyrVect layer, IGeometry insertedGeom) {
	super(layer);
	addNewButtonsToActionsToolBar();
    }

    private void addNewButtonsToActionsToolBar() {
	JPanel actionsToolBar = this.getActionsToolBar();
	NavTableComponentsFactory ntFactory = new NavTableComponentsFactory();
	JButton filesLinkB = ntFactory.getFilesLinkButton(layer, this);
	if (filesLinkB != null) {
	    actionsToolBar.add(filesLinkB);
	}
	actionsToolBar.add(new JButton(new OpenWebForm(this, "rv")));

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

	Map<String, JComponent> widgets = getWidgets();

	ImageComponent image = (ImageComponent) formBody
		.getComponentByName("image");
	ImageIcon icon = new ImageIcon(PreferencesPage.AUDASA_ICON);
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
    protected void fillSpecificValues() {
	for (JComponent c : getWidgets().values()) {
	    if (c != fincasAfectadas) {
		c.setEnabled(false);
	    }
	}
	updateJTableFincasAfectadas();
    }

    private void updateJTableFincasAfectadas() {

	DefaultTableModel tableModel = setTableHeader();

	double totalSuperficie = 0.0;
	double totalImporteEuros = 0.0;
	double totalImportePtas = 0.0;

	try {

	    fincasAfectadas.setModel(tableModel);
	    // fincasAfectadas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    fincasAfectadas.getColumnModel().getColumn(0).setPreferredWidth(80);
	    fincasAfectadas.getColumnModel().getColumn(1)
		    .setPreferredWidth(185);
	    fincasAfectadas.getColumnModel().getColumn(2)
		    .setPreferredWidth(100);
	    fincasAfectadas.getColumnModel().getColumn(5).setPreferredWidth(60);

	    Value[] reversionData = new Value[tableModel.getColumnCount()];
	    ResultSet rs = getFincasByExpReversion();

	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		reversionData[1] = ValueFactory
			.createValue(getExpedientesPMByFinca(rs.getString(1)));
		if (rs.getObject(2) != null) {
		    totalSuperficie = totalSuperficie + rs.getDouble(2);
		    reversionData[2] = ValueFactory
			    .createValue(getDoubleFormatted(rs.getDouble(2)));
		} else {
		    reversionData[2] = ValueFactory.createNullValue();
		}
		if (rs.getObject(3) != null) {
		    totalImporteEuros = totalImporteEuros + rs.getDouble(3);
		    reversionData[3] = ValueFactory
			    .createValue(getDoubleFormatted(rs.getDouble(3)));
		} else {
		    reversionData[3] = ValueFactory.createNullValue();
		}
		if (rs.getObject(4) != null) {
		    totalImportePtas = totalImportePtas + rs.getDouble(4);
		    reversionData[4] = ValueFactory
			    .createValue(getDoubleFormatted(rs.getDouble(4)));
		} else {
		    reversionData[4] = ValueFactory.createNullValue();
		}
		if (rs.getObject(5) != null) {
		    reversionData[5] = ValueFactory
			    .createValue(getDateFormatted(rs.getDate(5)));
		} else {
		    reversionData[5] = ValueFactory.createNullValue();
		}
		tableModel.addRow(reversionData);
	    }
	    reversionData[0] = ValueFactory.createValue("<html><b>" + "TOTAL"
		    + "</b></html>");
	    reversionData[1] = ValueFactory.createNullValue();
	    reversionData[2] = ValueFactory.createValue("<html><b>"
		    + totalSuperficie + "</b></html>");
	    reversionData[3] = ValueFactory.createValue("<html><b>"
		    + totalImporteEuros + "</b></html>");
	    reversionData[4] = ValueFactory.createValue("<html><b>"
		    + totalImportePtas + "</b></html>");
	    reversionData[5] = ValueFactory.createNullValue();
	    tableModel.addRow(reversionData);
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    private DefaultTableModel setTableHeader() {
	CustomTableModel tableModel = new CustomTableModel();
	ArrayList<Field> columnasFincas = new ArrayList<Field>();
	columnasFincas.add(new Field("id_finca", "<html>Finca</html>"));
	columnasFincas.add(new Field("expedientes_pm",
		"<html>Expedientes PM</html>"));
	columnasFincas.add(new Field("superficie",
		"<html>Superficie (m<sup>2</sup>)</html>"));
	columnasFincas.add(new Field("importe_euros",
		"<html>Importe (&euro;)</html>"));
	columnasFincas.add(new Field("importe_ptas",
		"<html>Importe (Pts)</html>"));
	columnasFincas.add(new Field("fecha_acta", "<html>Fecha</html>"));

	for (Field columnName : columnasFincas) {
	    tableModel.addColumn(columnName);
	}
	return tableModel;
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
	String query = "SELECT "
		+ DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + ", "
		+ DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", "
		+ DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES_EUROS + ", "
		+ DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES_PTAS + ", "
		+ DBNames.FIELD_FECHA_FINCAS_REVERSIONES + " " + "FROM "
		+ DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES
		+ " " + "WHERE " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES
		+ " = '" + getExpId() + "';";
	statement = DBSession.getCurrentSession().getJavaConnection()
		.prepareStatement(query);
	statement.execute();
	ResultSet rs = statement.getResultSet();
	return rs;
    }

    private String getExpedientesPMByFinca(String idFinca) {
	String expedientesPM = "";
	PreparedStatement statement;
	String query = "SELECT " + DBNames.FIELD_NUMPM_FINCAS_PM + " "
		+ "FROM " + DBNames.PM_SCHEMA + "." + DBNames.TABLE_FINCAS_PM
		+ " " + "WHERE " + DBNames.FIELD_IDFINCA_FINCAS_PM + " = '"
		+ idFinca + "';";
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
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
    public String getBasicName() {
	return TABLENAME;
    }
    
    @Override
    protected String getSchema() {
	return DBNames.SCHEMA_DATA;
    }

    @Override
    public void tableChanged(TableModelEvent arg0) {
	super.setChangedValues(true);
	super.saveB.setEnabled(true);
    }

    @Override
    // As this is a non editable form we should never show the warning
    protected boolean showWarning() {
	return true;
    }



}
