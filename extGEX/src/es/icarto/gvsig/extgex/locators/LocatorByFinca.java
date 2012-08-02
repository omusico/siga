package es.icarto.gvsig.extgex.locators;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JComboBox;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.locators.actions.FormOpener;
import es.icarto.gvsig.extgex.locators.actions.IPositionRetriever;
import es.icarto.gvsig.extgex.locators.actions.ZoomToHandler;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.queries.QueriesPanel;
import es.icarto.gvsig.extgex.utils.gvWindow;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.retrievers.IDFincaRetriever;
import es.icarto.gvsig.extgex.utils.retrievers.PositionRetriever;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class LocatorByFinca extends gvWindow implements IPositionRetriever {
    private static final Logger logger = Logger.getLogger(QueriesPanel.class);

    private static final String DEFAULT_FILTER = "--TODOS--";

    private final FormPanel formBody;

    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox parroquiaSubtramo;
    private JComboBox fincaSeccion;
    private JButton zoom;
    private JButton openForm;

    private ZoomToHandler zoomToHandler;
    private FormOpener formOpener;

    // Filters
    String tramoSelected = DEFAULT_FILTER;
    String ucSelected = DEFAULT_FILTER;
    String ayuntamientoSelected = DEFAULT_FILTER;
    String parroquiaSelected = null;

    // Listeners
    TramoListener tramoListener;
    UCListener ucListener;
    AyuntamientoListener ayuntamientoListener;
    ParroquiaListener parroquiaListener;
    FincasListener fincasListener;

    DBSession dbs;
    String[][] tramos;

    public LocatorByFinca() {
	super(400, 330);
	formBody = new FormPanel("LocatorByFinca.xml");
	formBody.setVisible(true);
	this.add(formBody, BorderLayout.CENTER);
	this.setTitle("Localizador por Finca");
	dbs = DBSession.getCurrentSession();
    }

    public boolean init() {
	initWidgets();
	initListeners();

	try {
	    String[] order = new String[1];
	    order[0] = DBNames.FIELD_IDTRAMO;
	    String[][] tramos = dbs.getTable(DBNames.TABLE_TRAMOS, DBNames.SCHEMA_DATA,
		    order, false);
	    for (int i = 0; i < tramos.length; i++) {
		tramo.addItem(tramos[i][1]);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}

	zoomToHandler = new ZoomToHandler(this);
	zoom.addActionListener(zoomToHandler);

	formOpener = new FormOpener(this);
	openForm.addActionListener(formOpener);

	TOCLayerManager toc = new TOCLayerManager();
	if(toc.getLayerByName(DBNames.LAYER_FINCAS) != null) {
	    return true;
	}
	return false;
    }

    private void initWidgets() {
	tramo = (JComboBox) formBody.getComponentByName("tramo");
	uc = (JComboBox) formBody.getComponentByName("unidad_constructiva");
	ayuntamiento = (JComboBox) formBody.getComponentByName("ayuntamiento");
	parroquiaSubtramo = (JComboBox) formBody.getComponentByName("parroquia_subtramo");
	fincaSeccion = (JComboBox) formBody.getComponentByName("finca_seccion");
	openForm = (JButton) formBody.getComponentByName("openform");
	zoom = (JButton) formBody.getComponentByName("zoom");
    }

    private void initListeners() {
	tramoListener = new TramoListener();
	tramo.addActionListener(tramoListener);
	ucListener = new UCListener();
	uc.addActionListener(ucListener);
	ayuntamientoListener = new AyuntamientoListener();
	ayuntamiento.addActionListener(ayuntamientoListener);
	parroquiaListener = new ParroquiaListener();
	parroquiaSubtramo.addActionListener(parroquiaListener);
	fincasListener = new FincasListener();
	fincaSeccion.addActionListener(fincasListener);
    }

    public class TramoListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
	    tramoSelected = (String) tramo.getSelectedItem();
	    updateUCDependingOnTramo();

	}
    }

    public class UCListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
	    ucSelected = (String) uc.getSelectedItem();
	    if (ucSelected != null) {
		updateAyuntamientoDependingOnUC();
	    }
	}
    }

    public class AyuntamientoListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
	    ayuntamientoSelected = (String) ayuntamiento.getSelectedItem();
	    if (ayuntamientoSelected != null) {
		updateParroquiaDependingOnAyuntamientoAndUC();
		updateFincaDependingOnAyuntamientoAndUCAndTramo();
	    }
	}
    }

    public class ParroquiaListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    if (!parroquiaSubtramo.isEnabled()) {
		parroquiaSelected = null;
	    } else {
		parroquiaSelected = (String) parroquiaSubtramo
		.getSelectedItem();
		updateFincaDependingOnAyuntamientoAndUCAndTramo();
	    }
	}
    }

    public class FincasListener implements ActionListener {
	public void actionPerformed(ActionEvent e) {
	    updateFincaDependingOnAyuntamientoAndUCAndTramo();
	}
    }

    private void updateUCDependingOnTramo() {

	if (tramoSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    uc.removeAllItems();
	    uc.addItem(new String(DEFAULT_FILTER));
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDUC;
		String tramoSelectedId = getTramoId();
		String whereClause = DBNames.FIELD_IDTRAMO + " = " + "'" + tramoSelectedId
		+ "'";
		String[][] ucs = dbs.getTable(DBNames.TABLE_UC, DBNames.SCHEMA_DATA,
			whereClause, order, false);
		uc.removeAllItems();
		//uc.addItem(new String(DEFAULT_FILTER));
		for (int i = 0; i < ucs.length; i++) {
		    uc.addItem(ucs[i][2]);
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void updateAyuntamientoDependingOnUC() {

	if (ucSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    ayuntamiento.removeAllItems();
	    ayuntamiento.addItem(new String(DEFAULT_FILTER));
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDAYUNTAMIENTO;
		String ucSelectedId = getUcId();
		String whereClause = DBNames.FIELD_IDUC + " = " + "'" + ucSelectedId + "'";
		String[][] ayuntamientos = dbs.getTable(DBNames.TABLE_AYUNTAMIENTOS,
			DBNames.SCHEMA_DATA, whereClause, order, false);
		ayuntamiento.removeAllItems();
		//ayuntamiento.addItem(new String(DEFAULT_FILTER));
		for (int i = 0; i < ayuntamientos.length; i++) {
		    ayuntamiento.addItem(ayuntamientos[i][2]);
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void updateParroquiaDependingOnAyuntamientoAndUC() {

	if (ayuntamientoSelected.compareToIgnoreCase(DEFAULT_FILTER) == 0) {
	    parroquiaSubtramo.removeAllItems();
	    parroquiaSubtramo.setEnabled(false);
	} else {
	    try {
		String[] order = new String[1];
		order[0] = DBNames.FIELD_IDPARROQUIA;
		String ucSelectedId = getUcId();
		String ayuntamientoSelectedId = getAyuntamientoId();
		String whereClause = DBNames.FIELD_IDAYUNTAMIENTO + " = " + "'"
		+ ayuntamientoSelectedId + "'" + " and " + DBNames.FIELD_IDUC + " = "
		+ "'" + ucSelectedId + "'";
		String[][] parroquias = dbs.getTable(DBNames.TABLE_PARROQUIASSUBTRAMOS,
			DBNames.SCHEMA_DATA, whereClause, order, false);
		if (parroquias.length <= 0) {
		    parroquiaSubtramo.setEnabled(false);
		} else if (parroquias[0][0].compareToIgnoreCase("0")==0) {
		    parroquiaSubtramo.setEnabled(false);
		} else {
		    parroquiaSubtramo.setEnabled(true);
		    parroquiaSubtramo.removeAllItems();
		    // parroquiaSubtramo.addItem(new String(DEFAULT_FILTER));
		    for (int i = 0; i < parroquias.length; i++) {
			parroquiaSubtramo.addItem(parroquias[i][3]);
		    }
		}
	    } catch (SQLException e) {
		logger.error(e.getMessage(), e);
	    }
	}
    }

    private void updateFincaDependingOnAyuntamientoAndUCAndTramo() {
	String query = null;
	PreparedStatement statement;
	fincaSeccion.removeAllItems();
	try {
	    if ((parroquiaSelected != null) && (!parroquiaSelected.equalsIgnoreCase(DEFAULT_FILTER))) {
		query = "SELECT " + DBNames.FIELD_NUMEROFINCA_FINCAS + ", " + DBNames.FIELD_SECCION_FINCAS +
		" FROM " + DBNames.EXPROPIATIONS_SCHEMA + "." + DBNames.TABLE_FINCAS +
		" WHERE " + DBNames.FIELD_TRAMO_FINCAS + " = " + "'" + getTramoId() +
		"' AND " + DBNames.FIELD_UC_FINCAS + " = " + "'" + getUcId() +
		"' AND " + DBNames.FIELD_AYUNTAMIENTO_FINCAS + " = " + "'" + getAyuntamientoId() +
		"' AND " + DBNames.FIELD_PARROQUIASUBTRAMO_FINCAS + " = " + "'" + getParroquiaId() + "'";
	    }else {
		query = "SELECT " + DBNames.FIELD_NUMEROFINCA_FINCAS + ", " + DBNames.FIELD_SECCION_FINCAS +
		" FROM " + DBNames.EXPROPIATIONS_SCHEMA + "." + DBNames.TABLE_FINCAS +
		" WHERE " + DBNames.FIELD_TRAMO_FINCAS + " = " + "'" + getTramoId() +
		"' AND " + DBNames.FIELD_UC_FINCAS + " = " + "'" + getUcId() +
		"' AND " + DBNames.FIELD_AYUNTAMIENTO_FINCAS + " = " + "'" + getAyuntamientoId() + "'";
	    }

	    System.out.println("====Query: " + query);

	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		String value = rs.getString(DBNames.FIELD_NUMEROFINCA_FINCAS) + "-" +
		rs.getString(DBNames.FIELD_SECCION_FINCAS);
		fincaSeccion.addItem(value);
	    }
	    rs.close();
	} catch (SQLException e) {
	    logger.error(query, e);
	    e.printStackTrace();
	}
    }

    private String getTramoId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBRETRAMO_TRAMOS + " = "
	+ "'" + tramoSelected + "'";
	String tramoId = getIDFromCB(DBNames.FIELD_IDTRAMO, DBNames.TABLE_TRAMOS, whereSQL);
	return tramoId;
    }

    private String getUcId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBREUC_UC + " = " + "'"
	+ ucSelected + "'";
	String uc = getIDFromCB(DBNames.FIELD_IDUC, DBNames.TABLE_UC, whereSQL);
	return uc;
    }

    private String getAyuntamientoId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBREAYUNTAMIENTO_AYUNTAMIENTO + " = " + "'" + ayuntamientoSelected + "'";
	String ayuntamiento = getIDFromCB(DBNames.FIELD_IDAYUNTAMIENTO, DBNames.TABLE_AYUNTAMIENTOS,
		whereSQL);
	return ayuntamiento;
    }

    private String getParroquiaId() throws SQLException {
	String whereSQL = DBNames.FIELD_NOMBREPARROQUIA_PARROQUIASUBTRAMOS + " = " + "'" + parroquiaSelected + "'";
	String parroquia = getIDFromCB(DBNames.FIELD_IDPARROQUIA, DBNames.TABLE_PARROQUIASSUBTRAMOS,
		whereSQL);
	return parroquia;
    }

    private String getIDFromCB(String fieldID, String tablename,
	    String whereClause) throws SQLException {
	Connection con = dbs.getJavaConnection();
	String query = "SELECT " + fieldID + " FROM " + DBNames.SCHEMA_DATA
	+ "." + tablename + " WHERE " + whereClause;
	Statement st = con.createStatement();
	ResultSet resultSet = st.executeQuery(query);
	resultSet.first();
	return resultSet.getString(1);
    }

    @Override
    public int getPosition() {
	IDFincaRetriever idFincaRetriever = new IDFincaRetriever(
		tramo,
		uc,
		ayuntamiento,
		parroquiaSubtramo,
		fincaSeccion);
	PositionRetriever positionRetriever = new PositionRetriever(
		getLayer(),
		DBNames.FIELD_IDFINCA,
		idFincaRetriever.getIDFinca());
	return positionRetriever.getPosition();
    }

    @Override
    public FLyrVect getLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(DBNames.LAYER_FINCAS);
    }

    @Override
    public void close() {
	tramo.removeActionListener(tramoListener);
	uc.removeActionListener(ucListener);
	ayuntamiento.removeActionListener(ayuntamientoListener);
	parroquiaSubtramo.removeActionListener(parroquiaListener);
	fincaSeccion.removeActionListener(fincasListener);
	zoom.removeActionListener(zoomToHandler);
	openForm.removeActionListener(formOpener);
	this.close();
    }

}