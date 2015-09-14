package es.icarto.gvsig.siga;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;

import es.icarto.gvsig.commons.queries.ConnectionWrapper;
import es.icarto.gvsig.utils.DesktopApi;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class StreetViewListener extends PointSelectionListener {

    private static final Logger logger = Logger
	    .getLogger(StreetViewListener.class);

    public StreetViewListener(MapControl mc) {
	super(mc);
    }

    /**
     * The image to display when the cursor is active.
     */
    private final Image img = PluginServices.getIconTheme()
	    .get(OpenStreetViewExtension.KEY + "-cursor").getImage();

    /**
     * The cursor used to work with this tool listener.
     * 
     * @see #getCursor()
     */
    private final Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(
	    img, new Point(16, 16), "");

    @Override
    public Cursor getCursor() {
	return cur;
    }

    @Override
    public void point(PointEvent event) throws BehaviorException {

	// Tolerancia de 3 pixels
	Point2D p = event.getPoint();
	final Point2D mapPoint = mapCtrl.getViewPort().toMapPoint(
		(int) p.getX(), (int) p.getY());

	try {
	    PluginServices.getMDIManager().setWaitCursor();
	    // final double tol = mapCtrl.getViewPort().toMapDistance(3);
	    // lookForClosestPointFeature(mapPoint, tol);
	    openInPoint(mapPoint);
	} catch (URISyntaxException e) {
	    logger.error(e.getStackTrace(), e);
	} finally {
	    PluginServices.getMDIManager().restoreCursor();
	}

    }

    private void openInPoint(Point2D p) throws URISyntaxException {

	Point2D latLng = getLatLng(p);
	if (latLng == null) {
	    return;
	}
	// http://stackoverflow.com/a/542965/930271
	String baseUri = "http://maps.google.com/maps?q=&layer=c&cbll=%s,%s";
	String uri = String.format(baseUri, latLng.getY(), latLng.getX());

	DesktopApi.browse(new URI(uri));

    }

    /**
     * Take care. This uses a custom row in spatial_ref_sys table to make the
     * reprojection insert into spatial_ref_sys (srid, auth_name, auth_srid,
     * srtext, proj4text) (select 111222, auth_name, auth_srid, srtext,
     * '+proj=utm +zone=29 +ellps=intl +units=m
     * towgs84=-125.098545,-76.000054,-156.198703,0.0,0.0,-1.129,8.30463103
     * +no_defs' from spatial_ref_sys where srid = 23029);
     */
    private Point2D getLatLng(Point2D p) {
	String query = String
		.format("select st_x(a.geom), st_y(a.geom) from (select st_transform(st_geomfromtext('POINT( %s %s)',111222), 4326) as geom) a",
			p.getX(), p.getY());
	ConnectionWrapper cw = new ConnectionWrapper(DBSession
		.getCurrentSession().getJavaConnection());
	DefaultTableModel table = cw.execute(query);
	if (table.getRowCount() != 1) {
	    logger.error(this.getClass().getName() + ":" + query);
	    NotificationManager.addWarning("Error transformando el punto");
	    return null;
	}
	return new Point2D.Double((Double) table.getValueAt(0, 0),
		(Double) table.getValueAt(0, 1));
    }

    // private void lookForClosestPointFeature(Point2D p, double tol)
    // throws ExpansionFileReadException, ReadDriverException,
    // VisitorException, IOException, URISyntaxException {
    // LayersIterator it = new LayersIterator(mapCtrl.getMapContext()
    // .getLayers());
    // while (it.hasNext()) {
    // FLayer layer = it.nextLayer();
    // if (validLayer(layer)) {
    // FLyrVect lyrVect = (FLyrVect) layer;
    //
    // final FBitSet newBitSet = lyrVect.queryByPoint(p, tol);
    // if (!newBitSet.isEmpty()) {
    // final int selectedPos = newBitSet.nextSetBit(0);
    // IGeometry geometry = lyrVect.getSource()
    // .getFeature(selectedPos).getGeometry();
    // Coordinate coord = geometry.toJTSGeometry().getCoordinate();
    // Point2D point = new Point2D.Double(coord.y, coord.x);
    // openInPoint(point);
    // break;
    // }
    // }
    // }
    // }
    //
    // private boolean validLayer(FLayer layer) {
    // if (layer instanceof FLyrVect && layer.isVisible()) {
    // FLyrVect lyrVect = (FLyrVect) layer;
    // try {
    // return lyrVect.getShapeType() == FShape.POINT;
    // } catch (ReadDriverException e) {
    // logger.error(e.getStackTrace(), e);
    // }
    // }
    // return false;
    // }

    @Override
    public void pointDoubleClick(PointEvent event) throws BehaviorException {
    }

}
