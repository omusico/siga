package es.icarto.gvsig.extgia.forms.utils;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.AbstractForm;
import es.icarto.gvsig.navtableforms.gui.tables.AbstractSubForm;
import es.icarto.gvsig.navtableforms.utils.DBConnectionBaseFormFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class GIAFormFactory extends DBConnectionBaseFormFactory {

    private final static GIAFormFactory instance;

    static {
	instance = new GIAFormFactory();
    }

    private GIAFormFactory() {
    }

    public static void registerFormFactory() {
	FormFactory.registerFormFactory(instance);
    }

    @Override
    public AbstractForm createForm(FLyrVect layer) {
	return null;
    }

    @Override
    public AbstractForm createSingletonForm(FLyrVect layer) {
	throw new AssertionError("Not implemented jet");
    }

    @Override
    public AbstractForm createForm(String layerName) {
	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	return createForm(layer);
    }

    @Override
    public AbstractForm createSingletonForm(String layerName) {
	FLyrVect layer = new TOCLayerManager().getLayerByName(layerName);
	return createSingletonForm(layer);
    }

    @Override
    public AbstractSubForm createSubForm(String tableName) {
	return null;
    }

    @Override
    public boolean hasMainForm(String layerName) {
	throw new AssertionError("Not implemented jet");
    }

    @Override
    public boolean allLayersLoaded() {
	throw new AssertionError("Not implemented jet");
    }

    @Override
    public void loadLayer(String layerName) {
	loadLayer(layerName, DBFieldNames.GIA_SCHEMA);
    }

    @Override
    public void loadTable(String tableName) {
	loadTable(tableName, DBFieldNames.GIA_SCHEMA);
    }
}
