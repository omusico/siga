package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.NewStatusBar;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.constants.Constant;
import es.udc.cartolab.gvsig.elle.constants.ConstantReload;
import es.udc.cartolab.gvsig.elle.constants.ConstantUtils;
import es.udc.cartolab.gvsig.elle.constants.ZoomToConstant;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.LoadLegend;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;

@SuppressWarnings("serial")
public class LoadConstantsWizardComponent extends WizardComponent {

    private boolean reload = false;

    private ConstantsPanel constantsPanel;

    public final static String PROPERTY_VIEW = "view";

    public LoadConstantsWizardComponent(Map<String, Object> properties) {
	super(properties);
	setUpUI();
    }

    private void setUpUI() {
	this.setLayout(new BorderLayout());
	constantsPanel = new ConstantsPanel();
	this.add(constantsPanel, BorderLayout.CENTER);
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

	List<String> values = constantsPanel.getCouncils();

	Object aux = properties.get(PROPERTY_VIEW);
	if (!(aux instanceof View)) {
	    throw new WizardException("Couldn't retrieve the view");
	}
	View view = (View) aux;

	ELLEMap map = null;

	if (reload) {
	    List<ELLEMap> loadedMaps = MapDAO.getInstance().getLoadedMaps();
	    for (ELLEMap m : loadedMaps) {
		if (m.getView().equals(view)
			&& m.getName().equals(view.getName())) {
		    map = m;
		}
	    }

	    // TODO. Que hacer si map es null aquí? Por ahora nos contentamos
	    // con algo parecido al Null Object Pattern para evitar chequeos de
	    // null sobre map
	    map = map != null ? map : new ELLEMap(null, null);
	    String where = buildWhereAndSetConstants(map, values);
	    new ConstantReload(view, where);
	} else {
	    Object tmp = properties
		    .get(SigaLoadMapWizardComponent.PROPERTY_MAP_NAME);
	    String mapName = (tmp == null ? "" : tmp.toString());

	    try {
		map = MapDAO.getInstance().getMap(view, mapName);
		buildWhereAndSetConstants(map, values);

		Collection<String> tablesAffectedByConstant = ConstantUtils
			.getTablesAffectedByConstant();
		map.load(view.getProjection(), tablesAffectedByConstant);

		loadLegends(view, mapName);

		if (view.getModel().getName().equals("ELLE View")
			&& (view.getModel() instanceof ProjectView)) {
		    ((ProjectView) view.getModel()).setName(mapName);
		}

		Constant constant = new Constant(values, view.getMapControl());
		ZoomToConstant zoomToConstant = new ZoomToConstant(
			view.getMapControl(), constant);
		zoomToConstant.zoom(values);
	    } catch (Exception e) {
		throw new WizardException(e);
	    }

	}

	writeCouncilsLoadedInStatusBar(values);

    }

    private void loadLegends(View view, String mapName) throws WizardException {
	FLayers layers = view.getMapControl().getMapContext().getLayers();
	try {
	    loadLegends(layers, false, mapName);
	    layers = view.getMapOverview().getMapContext().getLayers();
	    loadLegends(layers, true, mapName);
	} catch (SQLException e) {
	    throw new WizardException(e);
	} catch (IOException e) {
	    throw new WizardException(e);
	}
    }

    private void loadLegends(FLayers layers, boolean overview, String mapName)
	    throws SQLException, IOException {
	for (int i = 0; i < layers.getLayersCount(); i++) {
	    FLayer layer = layers.getLayer(i);
	    if (layer instanceof FLyrVect) {

		LoadLegend.loadLegend((FLyrVect) layer, mapName, overview,
			LoadLegend.DB_LEGEND);
	    } else if (layer instanceof FLayers) {
		loadLegends((FLayers) layer, overview, mapName);
	    }
	}
    }

    private void writeCouncilsLoadedInStatusBar(List<String> values) {
	String areaByConnectedUser = ConstantUtils.getAreaByConnectedUser();
	final NewStatusBar statusBar = PluginServices.getMainFrame()
		.getStatusBar();
	if (values.size() != 1) {
	    if (areaByConnectedUser.equalsIgnoreCase("ambas")) {
		statusBar.setMessage("constants", "Municipio: " + "TODOS");
	    } else if (areaByConnectedUser.equalsIgnoreCase("norte")) {
		statusBar.setMessage("constants", "Municipio: " + "Área Norte");
	    } else {
		statusBar.setMessage("constants", "Municipio: " + "Área Sur");
	    }
	} else {
	    String msg = "Municipio: "
		    + ConstantUtils.getNombreMunicipioById(values.get(0));
	    statusBar.setMessage("constants", msg);
	}
    }

    public void setReload(boolean reload) {
	this.reload = true;
    }

    private String buildWhereAndSetConstants(ELLEMap map, List<String> values) {
	// TODO: An index on selectedConstant field could speed up the queryS
	String where = "WHERE municipio IN (";
	if (!values.isEmpty()) {

	    ELLEMap.setConstantValuesSelected(values);

	    for (String s : values) {
		where += "'" + s + "', ";
	    }
	    where = where.substring(0, where.length() - 2) + ")";

	} else if (!ConstantUtils.getAreaByConnectedUser().equalsIgnoreCase(
		"ambas")) {
	    ELLEMap.setConstantValuesSelected(new ArrayList<String>());
	    Collection<String> councils = ConstantUtils
		    .getCouncilsByConnectedUser();
	    for (String s : councils) {
		where += "'" + s + "', ";
	    }
	    where = where.substring(0, where.length() - 2) + ")";

	} else {
	    ELLEMap.setConstantValuesSelected(new ArrayList<String>());
	    where = "";
	}

	map.setWhereOnAllLayers(where);
	map.setWhereOnAllOverviewLayers(where);
	return where;
    }
}
