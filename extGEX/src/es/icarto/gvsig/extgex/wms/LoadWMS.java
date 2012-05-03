package es.icarto.gvsig.extgex.wms;

import java.awt.Dimension;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Hashtable;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriver;
import com.iver.cit.gvsig.fmap.drivers.wms.FMapWMSDriverFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrWMS;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadWMS {

    private final String wmsName;

    public LoadWMS(String wmsName) {
	this.wmsName = wmsName;
    }

    public void Load() {

	DBSession dbs = DBSession.getCurrentSession();
	String whereC = DBNames.FIELD_LAYER_WMS + "=" + "'" + wmsName + "'";
	try {
	    // columns in order from wms table: id_wms[0], layer_name[1],
	    // layer[2],
	    // srs[3], format[4], host[5]
	    String[][] wmsValues = null;
	    wmsValues = dbs.getTable(DBNames.TABLE_WMS, dbs.getSchema(), whereC);

	    String host = wmsValues[0][5];
	    URL url = null;
	    url = new URL(host);
	    String sLayer = wmsValues[0][2];
	    String srs = wmsValues[0][3];
	    String format = wmsValues[0][4];
	    FLyrWMS layer = new FLyrWMS();

	    layer.setHost(url);
	    layer.setFullExtent(new Rectangle2D.Float(430819, 4623297, 286459,
		    232149));

	    layer.setFormat(format);
	    layer.setLayerQuery(sLayer);
	    layer.setInfoLayerQuery(sLayer);
	    layer.setSRS(srs);
	    ((FLayer) layer).setName(wmsValues[0][1]);
	    layer.setWmsTransparency(true);
	    // Vector styles = new Vector();
	    // String[] sLayers = sLayer.split(",");
	    // for (int i = 0; i < sLayers.length; i++) {
	    // styles.add("planos_tif_bn");
	    // }
	    // layer.setStyles(styles);
	    // layer.setDimensions(getDimensions());
	    // "gvSIG Raster Driver";
	    FMapWMSDriver driver;
	    driver = FMapWMSDriverFactory.getFMapDriverForURL(url);
	    layer.setDriver(driver);

	    Hashtable online_resources = new Hashtable();
	    online_resources.put("GetFeatureInfo", wmsValues[0][5]);
	    online_resources.put("GetMap", wmsValues[0][5]);
	    layer.setOnlineResources(online_resources);
	    layer.setFixedSize(new Dimension(-1, -1));
	    layer.setQueryable(false);

	    ((FLayer) layer).setVisible(true);
	    View v = (View) PluginServices.getMDIManager().getActiveWindow();
	    MapContext mapContext = v.getModel().getMapContext();

	    mapContext.getLayers().addLayer(0, layer);

	} catch (SQLException e) {
	    e.printStackTrace();
	} catch (MalformedURLException e) {
	    e.printStackTrace();
	} catch (ConnectException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
}
