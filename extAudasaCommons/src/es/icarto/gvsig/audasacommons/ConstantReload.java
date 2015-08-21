package es.icarto.gvsig.audasacommons;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.MDIManager;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.exceptions.layers.ReloadLayerException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.drivers.DBLayerDefinition;
import com.iver.cit.gvsig.fmap.drivers.VectorialDriver;
import com.iver.cit.gvsig.fmap.drivers.jdbc.postgis.PostGisDriver;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayersIterator;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.elle.gui.wizard.load.ConstantUtils;

public class ConstantReload {

    private static final Logger logger = Logger.getLogger(ConstantReload.class);

    public ConstantReload() {
	// TODO
	View view = (View) PluginServices.getMDIManager().getActiveWindow();

	TOCLayerManager tocManager = new TOCLayerManager();
	List<FLyrVect> joinedLayers = tocManager.getJoinedLayers();
	List<FLyrVect> editingLayers = tocManager.getEditingLayers();

	String errorMsg = "";

	if (!joinedLayers.isEmpty()) {
	    errorMsg += "Deshaga las uniones o enlaces de las siguientes capas para poder continuar:\nn";
	    for (FLyrVect l : joinedLayers) {
		errorMsg += " - " + l.getName() + "\n";
	    }
	    errorMsg += "\n\n";

	}

	if (!editingLayers.isEmpty()) {
	    errorMsg += "Cierre la edición de las siguientes capas para poder continuar:\nn";
	    for (FLyrVect l : editingLayers) {
		errorMsg += " - " + l.getName() + "\n";
	    }

	}

	if (!errorMsg.isEmpty()) {
	    throw new RuntimeException(errorMsg);
	}

	closeNotViewWindowsAndDocuments();

	reloadLayers(view.getMapOverview());
	reloadLayers(view.getMapControl());
    }

    private void closeNotViewWindowsAndDocuments() {
	closeWindows();
	closeDocuments();
    }

    private void closeWindows() {
	final MDIManager mdiManager = PluginServices.getMDIManager();
	for (IWindow window : mdiManager.getAllWindows()) {
	    if (!(window instanceof BaseView)) {
		mdiManager.closeWindow(window);
	    }
	}
    }

    private void closeDocuments() {
	Project project = ((ProjectExtension) PluginServices
		.getExtension(ProjectExtension.class)).getProject();

	List<ProjectDocument> documents = project.getDocuments();

	String viewType = ProjectViewFactory.registerName;

	for (ProjectDocument doc : documents) {
	    String docType = doc.getProjectDocumentFactory().getRegisterName();
	    if (!docType.equals(viewType)) {
		project.delDocument(doc);
	    }
	}
    }

    private void reloadLayers(MapControl mapControl) {
	List<FLyrVect> layersToBeReloaded = getLayersToBeReloaded(mapControl);
	for (FLyrVect l : layersToBeReloaded) {
	    sanitize(l);
	    updateLayerConstants(l);
	    reload(l);
	}
	mapControl.drawMap(true);
    }

    private List<FLyrVect> getLayersToBeReloaded(MapControl mapControl) {
	List<FLyrVect> layersToBeReloaded = new ArrayList<FLyrVect>();

	String[] foo = ConstantUtils.getTablesAffectedByConstant("Municipio");

	List<String> tablesWithConstants = Arrays.asList(foo);

	FLayers layers = mapControl.getMapContext().getLayers();

	LayersIterator it = new LayersIterator(layers);
	while (it.hasNext()) {
	    FLayer layer = it.nextLayer();
	    DBLayerDefinition lyrDef = getDBLayerDefinition(layer);
	    if (lyrDef == null) {
		continue;
	    }
	    String tableName = lyrDef.getTableName();
	    if (tablesWithConstants.contains(tableName)) {
		layersToBeReloaded.add((FLyrVect) layer);
	    }
	}
	return layersToBeReloaded;
    }

    // Not sure if this clean actions are needed. Just in case
    private void sanitize(FLyrVect vectLayer) {
	vectLayer.clearSpatialCache();
	vectLayer.deleteSpatialIndex();
    }

    // TODO
    // coge el antiguo where de la capa
    // calcula el nuevo where de la capa
    // lo setea en la definición de la capa
    private void updateLayerConstants(FLyrVect l) {
	DBLayerDefinition lyrDef = getDBLayerDefinition(l);
	lyrDef.setWhereClause("WHERE municipio IN ('36045', '36057')");
    }

    private void reload(FLyrVect l) {
	try {
	    SaveSelection saveSelection = new SaveSelection(l);
	    l.reload();
	    saveSelection.restoreSelection();
	} catch (ReloadLayerException e) {
	    logger.error(e.getStackTrace(), e);
	}

    }

    /**
     * @return the DBLayerDefinition of the layer if is a layer that uses
     *         PostGisDriver (and is a FLyrVect). And null if not
     */
    private DBLayerDefinition getDBLayerDefinition(FLayer layer) {
	if (layer instanceof FLyrVect) {
	    FLyrVect vectLayer = (FLyrVect) layer;
	    VectorialDriver driver = vectLayer.getSource().getDriver();
	    if (driver instanceof PostGisDriver) {
		PostGisDriver postgis = (PostGisDriver) driver;
		return postgis.getLyrDef();
	    }
	}
	return null;
    }

}
