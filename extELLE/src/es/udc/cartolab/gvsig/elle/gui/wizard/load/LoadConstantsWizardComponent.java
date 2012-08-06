package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

import es.icarto.gvsig.elle.db.DBStructure;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadConstantsWizardComponent extends WizardComponent {

    private JPanel listPanel;
    private JList constantsList;
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

    //ZoomToConstant
    public final static String CONSTANTS_ZOOM_LAYER_FIELD = "municipio_nombre";
    public final static String CONSTANTS_ZOOM_LAYER_NAME = "linea_exp_envelope";

    public LoadConstantsWizardComponent(Map<String, Object> properties) {
	super(properties);
	setLayout(new BorderLayout());
	add(getListPanel());
    }

    private JPanel getListPanel() {
	dbs = DBSession.getCurrentSession();

	if (listPanel == null) {
	    listPanel = new JPanel();
	    FormPanel form = new FormPanel("forms/loadConstants.jfrm");
	    listPanel.add(form);

	    constantsList = form.getList("constantsList");
	    JLabel constantsLabel = form.getLabel("constantsLabel");
	    constantsLabel.setText(PluginServices.getText(this, "constants_load"));

	    String[] constants = getConstants();
	    constantsList.setListData(constants);
	    constantsList.setSelectedIndex(0);
	    selectedConstant = (String) constantsList.getSelectedValues()[0];

	    valuesList = form.getList("valuesList");
	    JLabel valuesLabel = form.getLabel("valuesLabel");
	    valuesLabel.setText(PluginServices.getText(this, "values_load"));
	    valuesList.setListData(getValuesFromConstantByQuery(selectedConstant));

	    constantsList.addListSelectionListener(new ListSelectionListener() {

		public void valueChanged(ListSelectionEvent arg0) {
		    int[] selected = constantsList.getSelectedIndices();
		    callStateChanged();

		    if (selected.length == 1) {
			selectedConstant = (String) constantsList.getSelectedValues()[0];
			String[] values = getValuesFromConstantByQuery(selectedConstant);
			valuesList.setListData(values);
		    } else {
			//TODO: several constants selected at the same time
		    }
		}
	    });

	    valuesList.addListSelectionListener(new ListSelectionListener() {

		public void valueChanged(ListSelectionEvent arg0) {
		    int[] selected = valuesList.getSelectedIndices();
		    callStateChanged();

		    if (selected.length == 1) {
			selectedValue = (String) valuesList.getSelectedValue();
		    } else {
			//TODO: several constants selected at the same time
			selectedValuesList = valuesList.getSelectedValues();
		    }
		}
	    });
	}
	return listPanel;
    }

    private String[] getConstants() {
	try {
	    String[] constants = dbs.getDistinctValues(CONSTANTS_TABLE_NAME, DBStructure.getSchema(), CONSTANTS_CONSTANT_FIELD_NAME);
	    return constants;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String[] getValuesFromConstant(String constant) {
	try {
	    String[] tables = getTablesAffectedByConstant(constant);
	    String[] values = dbs.getDistinctValues(tables[0], DBStructure.getSchema(), getValueOfFieldByConstant(constant, CONSTANTS_FILTER_FIELD_NAME), true, false);
	    return values;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String[] getValuesFromConstantByQuery(String constant) {
	String query = getValueOfFieldByConstant(constant, CONSTANTS_QUERY_FIELD_NAME);
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List <String>resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(1);
		resultArray.add(val);
	    }
	    rs.close();

	    String[] result = new String[resultArray.size()];
	    for (int i=0; i<resultArray.size(); i++) {
		result[i] = resultArray.get(i);
	    }

	    return result;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    private String getValueOfFieldByConstant(String constant, String field) {
	String query = "SELECT " + field + " FROM " + DBStructure.getSchema() + "." + CONSTANTS_TABLE_NAME + " WHERE " + CONSTANTS_CONSTANT_FIELD_NAME +  " = " + "'" + constant + "'" + ";";
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
	String query = "SELECT nombre_tabla FROM " + DBStructure.getSchema() + "." + CONSTANTS_TABLE_NAME + " WHERE " + CONSTANTS_CONSTANT_FIELD_NAME +  " = " + "'" + constant + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();

	    List <String>resultArray = new ArrayList<String>();
	    while (rs.next()) {
		String val = rs.getString(CONSTANTS_AFFECTED_TABLE_NAME);
		resultArray.add(val);
	    }
	    rs.close();

	    String[] result = new String[resultArray.size()];
	    for (int i=0; i<resultArray.size(); i++) {
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
    public void finish() throws WizardException {
	Object tmp = properties.get(LoadMapWizardComponent.PROPERTY_MAP_NAME);
	String mapName = (tmp == null ? "" : tmp.toString());

	Object aux = properties.get(PROPERTY_VIEW);
	if (aux!=null && aux instanceof View) {
	    View view = (View) aux;
	    try {
		ELLEMap map = MapDAO.getInstance().getMap(view, mapName);
		String where = "WHERE ";
		if (selectedValuesList != null && selectedValuesList.length > 1) {
		    for (int i=0; i<selectedValuesList.length; i++) {
			if (i != selectedValuesList.length-1) {
			    where = where + getValueOfFieldByConstant(selectedConstant, CONSTANTS_FILTER_FIELD_NAME) + " = " + "'" + selectedValuesList[i].toString() + "'" + " OR ";
			}else {
			    where = where + getValueOfFieldByConstant(selectedConstant, CONSTANTS_FILTER_FIELD_NAME) + " = " + "'" + selectedValuesList[i].toString() + "'";
			}
		    }
		    map.setWhereOnAllLayers(where);
		    ELLEMap.setFiltered(true);
		}else if (selectedValue != null) {
		    where = where + getValueOfFieldByConstant(selectedConstant, CONSTANTS_FILTER_FIELD_NAME) + " = " + "'" + selectedValue + "'";
		    map.setWhereOnAllLayers(where);
		    ELLEMap.setFiltered(true);
		} else {
		    ELLEMap.setFiltered(false);
		}
		map.load(view.getProjection(), getTablesAffectedByConstant(selectedConstant));
		if (view.getModel().getName().equals("ELLE View") && (view.getModel() instanceof ProjectView)) {
		    ((ProjectView) view.getModel()).setName(mapName);
		}
		PluginServices.getMainFrame().getStatusBar().setMessage("constants",
			selectedConstant + ": " + selectedValue);
		zoomToConstant();
	    } catch (Exception e) {
		throw new WizardException(e);
	    }
	} else {
	    throw new WizardException("Couldn't retrieve the view");
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
		String stringValue = value.getStringValue(ValueWriter.internalValueWriter);
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
		rectangle.setFrameFromCenter(rectangle.getCenterX(), rectangle
			.getCenterY(), rectangle.getCenterX() + 100, rectangle
			.getCenterY() + 100);
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

    @Override
    public String getWizardComponentName() {
	return "constants_wizard_component";
    }

    @Override
    public void setProperties() throws WizardException {
	// TODO Auto-generated method stub

    }

    @Override
    public void showComponent() throws WizardException {
	// TODO Auto-generated method stub

    }

}
