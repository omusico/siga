package es.udc.cartolab.gvsig.elle.constants;

import java.util.ArrayList;
import java.util.Collection;
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

public class ConstantReload {

    private static final Logger logger = Logger.getLogger(ConstantReload.class);
    private final String where;

    public ConstantReload(View view, String where) {
	this.where = where;

	String errorMsg = ConstantUtils.constantChecks(view);
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

	Collection<String> tablesWithConstants = ConstantUtils
		.getTablesAffectedByConstant();

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

    private void updateLayerConstants(FLyrVect l) {
	DBLayerDefinition lyrDef = getDBLayerDefinition(l);
	lyrDef.setWhereClause(where);
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
