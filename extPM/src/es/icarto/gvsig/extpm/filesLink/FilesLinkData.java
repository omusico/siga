package es.icarto.gvsig.extpm.filesLink;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;

import es.icarto.gvsig.extgex.preferences.PreferencesPage;
import es.icarto.gvsig.extpm.preferences.Preferences;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;

public class FilesLinkData {

    private FLyrVect layer = null;

    public FilesLinkData(FLyrVect layer) {
	this.layer = layer;
    }

    public SelectableDataSource getRecordset() {
	try {
	    return layer.getRecordset();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    return null;
	}
    }

    public String getBaseDirectory() {
	return PreferencesPage.getBaseDirectory();
    }

    public String getDirectoryLayerName() {
	return layer.getName();
    }

    public String getDirectoryFieldName() {
	String xmlFilePath = PluginServices
	.getPluginServices("es.icarto.gvsig.extpm").getClassLoader()
	.getResource(Preferences.XML_ORMLITE_RELATIVE_PATH).getPath();
	return ORMLite.getDataBaseObject(xmlFilePath).getTable(layer.getName())
	.getPrimaryKey()[0];

    }
}
