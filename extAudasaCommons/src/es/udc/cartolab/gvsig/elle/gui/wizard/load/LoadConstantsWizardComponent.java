package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.elle.db.DBStructure;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class LoadConstantsWizardComponent extends WizardComponent {

    private static final Logger logger = Logger
	    .getLogger(LoadConstantsWizardComponent.class);

    private JPanel listPanel;
    private JList valuesList;
    private DBSession dbs;

    private String selectedConstant;
    private String selectedValue;
    private Object[] selectedValuesList;

    public final static String PROPERTY_VIEW = "view";

    public final static String CONSTANTS_TABLE_NAME = "_constants";
    public final static String CONSTANTS_CONSTANT_FIELD_NAME = "constante";
    public final static String CONSTANTS_AFFECTED_TABLE_NAME = "nombre_tabla";
    public final static String CONSTANTS_FILTER_FIELD_NAME = "campo_filtro";
    public final static String CONSTANTS_QUERY_FIELD_NAME = "campo_query";

    private static final String MUNICIPIO_CONSTANTS_TABLENAME = "audasa_extgia_dominios.municipio_constantes";
    public static final String USUARIOS_TABLENAME = "audasa_expedientes.usuarios";

    // ZoomToConstant
    public final static String CONSTANTS_ZOOM_LAYER_FIELD = "municipio_codigo";
    public final static String CONSTANTS_ZOOM_LAYER_NAME = "Constante";

    public LoadConstantsWizardComponent(Map<String, Object> properties) {
	super(properties);
	setLayout(new BorderLayout());
	if (getListPanel() != null) {
	    add(getListPanel());
	}
    }

    private JPanel getListPanel() {
	dbs = DBSession.getCurrentSession();

	if (listPanel == null) {
	    listPanel = new JPanel();

	    FormPanel form = null;
	    try {
		InputStream stream = getClass().getClassLoader()
			.getResourceAsStream("forms/loadConstants.jfrm");
		form = new FormPanel(stream);
	    } catch (FormException e) {
		logger.error(e.getStackTrace(), e);
		return listPanel;
	    }

	    listPanel.add(form);

	    JLabel constantsLabel = form.getLabel("constantsLabel");
	    constantsLabel.setText(PluginServices.getText(this,
		    "constants_load"));

	    selectedConstant = "Municipio";

	    valuesList = form.getList("valuesList");
	    if (getValuesFromConstantByQuery(selectedConstant) == null) {
		return null;
	    }
	    valuesList
		    .setListData(getValuesFromConstantByQuery(selectedConstant));
	}
	return listPanel;
    }

    private String[] getValuesFromConstantByQuery(String constant) {
	String query;
	final String userArea = getAreaByConnectedUser();
	if (userArea == null) {
	    return null;
	}
	if (userArea.equalsIgnoreCase("ambas")) {
	    query = "SELECT tag FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " ORDER BY orden;";
	} else if (userArea.equalsIgnoreCase("sur")) {
	    // Include Padron in councils list if area = Sur
	    query = "SELECT tag, orden FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " WHERE area = " + "'" + userArea
		    + "' UNION SELECT tag, orden FROM "
		    + MUNICIPIO_CONSTANTS_TABLENAME + " WHERE id = '15065'"
		    + " ORDER BY orden;";
	} else {
	    query = "SELECT tag FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		    + " WHERE area = " + "'" + userArea + "' ORDER BY orden;";
	}
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List<String> resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(1);
		resultArray.add(val);
	    }
	    rs.close();

	    String[] result = new String[resultArray.size()];
	    for (int i = 0; i < resultArray.size(); i++) {
		result[i] = resultArray.get(i);
	    }

	    return result;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public boolean canFinish() {
	return true;
    }

    @Override
    public boolean canNext() {
	return true;
    }

    @Override
    public String getWizardComponentName() {
	return "constants_wizard_component";
    }

    @Override
    public void setProperties() throws WizardException {
    }

    @Override
    public void showComponent() throws WizardException {
    }

    @Override
    public void finish() throws WizardException {

	int[] selected = valuesList.getSelectedIndices();
	callStateChanged();
	String[] valuesSelected = null;

	if (selected.length == 1) {
	    selectedValue = getIdByConstantTag((String) valuesList
		    .getSelectedValue());
	    valuesSelected = new String[1];
	    valuesSelected[0] = selectedValue;
	    ELLEMap.setConstantValuesSelected(valuesSelected);
	} else {
	    // TODO: several constants selected at the same time
	    selectedValuesList = valuesList.getSelectedValues();
	    String[] values = new String[selectedValuesList.length];
	    for (int i = 0; i < selectedValuesList.length; i++) {
		values[i] = getIdByConstantTag(selectedValuesList[i].toString());
	    }
	    ELLEMap.setConstantValuesSelected(values);
	}

	Object tmp = properties
		.get(SigaLoadMapWizardComponent.PROPERTY_MAP_NAME);
	String mapName = (tmp == null ? "" : tmp.toString());

	Object aux = properties.get(PROPERTY_VIEW);
	if (aux != null && aux instanceof View) {
	    View view = (View) aux;
	    try {
		ELLEMap map = MapDAO.getInstance().getMap(view, mapName);
		// String where = "WHERE " +
		// getValueOfFieldByConstant(selectedConstant,
		// CONSTANTS_FILTER_FIELD_NAME) + " IN (";
		// where += ")";

		// TODO. Esto se puede convertir en una �nica consulta con un IN
		// ( ) . El explain analyze demuestra que el rendimiento de usar
		// un = y un IN con un s�lo valor es el mismo.
		// Plantearse meter un �ndice sobre municipio, eso deber�a
		// mejorar la b�squeda, aunque el cuello debe estar en la tx de
		// info no en la bd.
		String where = "WHERE (";
		if (selectedValuesList != null && selectedValuesList.length > 1) {
		    for (int i = 0; i < selectedValuesList.length; i++) {
			if (i != selectedValuesList.length - 1) {
			    where = where
				    + getValueOfFieldByConstant(
					    selectedConstant,
					    CONSTANTS_FILTER_FIELD_NAME)
				    + " = "
				    + "'"
				    + getIdByConstantTag(selectedValuesList[i]
					    .toString()) + "'" + " OR ";
			} else {
			    where = where
				    + getValueOfFieldByConstant(
					    selectedConstant,
					    CONSTANTS_FILTER_FIELD_NAME)
				    + " = "
				    + "'"
				    + getIdByConstantTag(selectedValuesList[i]
					    .toString()) + "')";
			}
		    }
		    map.setWhereOnAllLayers(where);
		    map.setWhereOnAllOverviewLayers(where);
		    ELLEMap.setFiltered(true);
		} else if (selectedValue != null) {
		    where = where
			    + getValueOfFieldByConstant(selectedConstant,
				    CONSTANTS_FILTER_FIELD_NAME) + " = " + "'"
			    + selectedValue + "')";
		    map.setWhereOnAllLayers(where);
		    map.setWhereOnAllOverviewLayers(where);
		    ELLEMap.setFiltered(true);
		} else if (selectedValue == null && selectedValuesList == null
			&& !getAreaByConnectedUser().equalsIgnoreCase("ambas")) {
		    where = where + getWhereWithAllCouncilsOfArea();
		    map.setWhereOnAllLayers(where);
		    map.setWhereOnAllOverviewLayers(where);
		} else {
		    ELLEMap.setFiltered(false);
		}
		map.load(view.getProjection(),
			getTablesAffectedByConstant(selectedConstant));
		if (view.getModel().getName().equals("ELLE View")
			&& (view.getModel() instanceof ProjectView)) {
		    ((ProjectView) view.getModel()).setName(mapName);
		}
		writeCouncilsLoadedInStatusBar();
		zoomToConstant();
	    } catch (Exception e) {
		throw new WizardException(e);
	    }
	} else {
	    throw new WizardException("Couldn't retrieve the view");
	}
    }

    private String getValueOfFieldByConstant(String constant, String field) {
	String query = "SELECT " + field + " FROM " + DBStructure.getSchema()
		+ "." + CONSTANTS_TABLE_NAME + " WHERE "
		+ CONSTANTS_CONSTANT_FIELD_NAME + " = " + "'" + constant + "'"
		+ ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String[] getTablesAffectedByConstant(String constant) {
	String query = "SELECT nombre_tabla FROM " + DBStructure.getSchema()
		+ "." + CONSTANTS_TABLE_NAME + " WHERE "
		+ CONSTANTS_CONSTANT_FIELD_NAME + " = " + "'" + constant + "'"
		+ ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List<String> resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(CONSTANTS_AFFECTED_TABLE_NAME);
		resultArray.add(val);
	    }
	    rs.close();

	    String[] result = new String[resultArray.size()];
	    for (int i = 0; i < resultArray.size(); i++) {
		result[i] = resultArray.get(i);
	    }

	    return result;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private void writeCouncilsLoadedInStatusBar() {
	if (selectedValue == null) {
	    if (getAreaByConnectedUser().equalsIgnoreCase("ambas")) {
		PluginServices
			.getMainFrame()
			.getStatusBar()
			.setMessage("constants",
				selectedConstant + ": " + "TODOS");
	    } else if (getAreaByConnectedUser().equalsIgnoreCase("norte")) {
		PluginServices
			.getMainFrame()
			.getStatusBar()
			.setMessage("constants",
				selectedConstant + ": " + "�rea Norte");
	    } else {
		PluginServices
			.getMainFrame()
			.getStatusBar()
			.setMessage("constants",
				selectedConstant + ": " + "�rea Sur");
	    }
	} else {
	    PluginServices
		    .getMainFrame()
		    .getStatusBar()
		    .setMessage(
			    "constants",
			    selectedConstant + ": "
				    + getNombreMunicipioById(selectedValue));
	}
    }

    private void zoomToConstant() {
	FLyrVect layer = (FLyrVect) getEnvelopeConstantLayer();
	if (layer != null) {
	    int i = getPositionOnEnvelope(layer);
	    zoom(layer, i);
	}
    }

    private int getPositionOnEnvelope(FLyrVect layer) {
	try {
	    SelectableDataSource ds = layer.getRecordset();
	    int index = ds.getFieldIndexByName(CONSTANTS_ZOOM_LAYER_FIELD);
	    for (int i = 0; i < ds.getRowCount(); i++) {
		Value value = ds.getFieldValue(i, index);
		String stringValue = value
			.getStringValue(ValueWriter.internalValueWriter);
		if ((stringValue.compareToIgnoreCase("'" + selectedValue + "'") == 0)) {
		    return i;
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return -1;
    }

    private FLayer getEnvelopeConstantLayer() {
	FLayer layer = null;
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers flayers = mapControl.getMapContext().getLayers();
	layer = flayers.getLayer(CONSTANTS_ZOOM_LAYER_NAME);
	return layer;
    }

    private void zoom(FLyrVect layer, int pos) {
	try {
	    Rectangle2D rectangle = null;
	    IGeometry g;
	    ReadableVectorial source = (layer).getSource();
	    source.start();
	    g = source.getShape(pos);
	    source.stop();
	    /*
	     * fix to avoid zoom problems when layer and view projections aren't
	     * the same.
	     */
	    if (layer.getCoordTrans() != null) {
		g.reProject(layer.getCoordTrans());
	    }
	    rectangle = g.getBounds2D();
	    if (rectangle.getWidth() < 200) {
		rectangle.setFrameFromCenter(rectangle.getCenterX(),
			rectangle.getCenterY(), rectangle.getCenterX() + 100,
			rectangle.getCenterY() + 100);
	    }
	    if (rectangle != null) {
		layer.getMapContext().getViewPort().setExtent(rectangle);
	    }
	} catch (InitializeDriverException e) {
	    e.printStackTrace();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private String getIdByConstantTag(String constantTag) {
	String query = "SELECT id FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE tag =" + "'" + constantTag + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String getNombreMunicipioById(String id) {
	String query = "SELECT item FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE id =" + "'" + id + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private ArrayList<String> getCouncilsByConnectedUser() {
	ArrayList<String> councils = new ArrayList<String>();
	if (getAreaByConnectedUser() == null) {
	    return null;
	}
	String query = "SELECT id FROM " + MUNICIPIO_CONSTANTS_TABLENAME
		+ " WHERE area = '" + getAreaByConnectedUser() + "';";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    while (rs.next()) {
		councils.add(rs.getString(1));
	    }
	    // If area=Sur included also Padron into councils list
	    if (getAreaByConnectedUser().equalsIgnoreCase("sur")) {
		councils.add("15065");
	    }
	    return councils;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String getWhereWithAllCouncilsOfArea() {
	String where = "";
	for (int i = 0; i < getCouncilsByConnectedUser().size(); i++) {
	    if (i != getCouncilsByConnectedUser().size() - 1) {
		where = where
			+ getValueOfFieldByConstant(selectedConstant,
				CONSTANTS_FILTER_FIELD_NAME) + " = " + "'"
			+ getCouncilsByConnectedUser().get(i) + "'" + " OR ";
	    } else {
		where = where
			+ getValueOfFieldByConstant(selectedConstant,
				CONSTANTS_FILTER_FIELD_NAME) + " = " + "'"
			+ getCouncilsByConnectedUser().get(i) + "')";
	    }
	}
	return where;
    }

    public static String getAreaByConnectedUser() {
	String query = "SELECT area FROM "
		+ LoadConstantsWizardComponent.USUARIOS_TABLENAME
		+ " WHERE name =" + "'"
		+ DBSession.getCurrentSession().getUserName() + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = DBSession.getCurrentSession().getJavaConnection()
		    .prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    if (!rs.next()) {
		return null;
	    }
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }
}
