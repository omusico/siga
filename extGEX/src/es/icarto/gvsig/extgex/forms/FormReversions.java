package es.icarto.gvsig.extgex.forms;

import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.ArrayList;
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
	columnasFincas.add(DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES);
	columnasFincas.add(DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES);
	columnasFincas.add(DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES);
	columnasFincas.add(DBNames.FIELD_FECHA_FINCAS_REVERSIONES);

	try {
	    DefaultTableModel tableModel;
	    tableModel = new DefaultTableModel();
	    for (String columnName : columnasFincas) {
		tableModel.addColumn(columnName);
	    }
	    fincasAfectadas.setModel(tableModel);
	    Value[] reversionData = new Value[4];
	    PreparedStatement statement;
	    String query = "SELECT " +
		    DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_SUPERFICIE_FINCAS_REVERSIONES + ", " +
		    DBNames.FIELD_IMPORTE_FINCAS_REVERSIONES + " " +
		    DBNames.FIELD_FECHA_FINCAS_REVERSIONES + " " +
		    "FROM " + DBNames.SCHEMA_DATA + "." + DBNames.TABLE_FINCASREVERSIONES + " " +
		    "WHERE " + DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES + " = '" + getExpId() + "';";
	    statement = DBSession.getCurrentSession().getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		reversionData[0] = ValueFactory.createValue(rs.getString(1));
		if (rs.getObject(2) != null) {
		    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
		    Double doubleValue = rs.getDouble(2);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    reversionData[1] = ValueFactory.createValue(doubleAsString);
		}else {
		    reversionData[1] = null;
		}
		if (rs.getObject(3) != null) {
		    NumberFormat doubleFormat = DoubleFormatNT.getDisplayingFormat();
		    Double doubleValue = rs.getDouble(3);
		    String doubleAsString = doubleFormat.format(doubleValue);
		    reversionData[2] = ValueFactory.createValue(doubleAsString);
		}else {
		    reversionData[2] = null;
		}
		tableModel.addRow(reversionData);
	    }
	    repaint();
	    tableModel.addTableModelListener(this);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
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
