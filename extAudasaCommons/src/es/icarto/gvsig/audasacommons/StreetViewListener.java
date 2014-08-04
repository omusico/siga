package es.icarto.gvsig.audasacommons;

import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.cresques.cts.ICoordTrans;
import org.cresques.cts.IProjection;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.exceptions.expansionfile.ExpansionFileReadException;
import com.iver.cit.gvsig.exceptions.visitors.VisitorException;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.FShape;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.LayersIterator;
import com.iver.cit.gvsig.fmap.tools.BehaviorException;
import com.iver.cit.gvsig.fmap.tools.PointSelectionListener;
import com.iver.cit.gvsig.fmap.tools.Events.PointEvent;
import com.vividsolutions.jts.geom.Geometry;

public class StreetViewListener extends PointSelectionListener {

    private static final Logger logger = Logger
	    .getLogger(StreetViewListener.class);
    private static final IProjection crs4326 = CRSFactory.getCRS("EPSG:4326");

    public StreetViewListener(MapControl mc) {
	super(mc);
    }

    /**
     * The image to display when the cursor is active.
     */
    private final Image img = PluginServices.getIconTheme()
	    .get("cursor-query-information").getImage();

    /**
     * The cursor used to work with this tool listener.
     * 
     * @see #getCursor()
     */
    private final Cursor cur = Toolkit.getDefaultToolkit().createCustomCursor(
	    img, new Point(16, 16), "");

    private IWindow lastOpenDialog;

    @Override
    public Cursor getCursor() {
	return cur;
    }

    @Override
    public void point(PointEvent event) throws BehaviorException {

	// Tolerancia de 3 pixels
	Point2D p = event.getPoint();
	final double tol = mapCtrl.getViewPort().toMapDistance(3);
	final Point2D mapPoint = mapCtrl.getViewPort().toMapPoint(
		(int) p.getX(), (int) p.getY());

	try {
	    PluginServices.getMDIManager().setWaitCursor();
	    lookForClosestPointFeature(mapPoint, tol);

	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (VisitorException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (IOException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (URISyntaxException e) {
	    logger.error(e.getStackTrace(), e);
	} finally {
	    PluginServices.getMDIManager().restoreCursor();
	}

    }

    private void lookForClosestPointFeature(Point2D p, double tol)
	    throws ExpansionFileReadException, ReadDriverException,
	    VisitorException, IOException, URISyntaxException {
	LayersIterator it = new LayersIterator(mapCtrl.getMapContext()
		.getLayers());
	while (it.hasNext()) {
	    FLayer layer = it.nextLayer();
	    if (layer instanceof FLyrVect) {
		FLyrVect lyrVect = (FLyrVect) layer;
		if (lyrVect.getShapeType() == FShape.POINT) {
		    final FBitSet newBitSet = lyrVect.queryByPoint(p, tol);
		    if (!newBitSet.isEmpty()) {
			lyrVect.getRecordset().setSelection(newBitSet);
			final int selectedPos = lyrVect.getRecordset()
				.getSelection().nextSetBit(0);
			IGeometry geometry = lyrVect.getSource()
				.getFeature(selectedPos).getGeometry()
				.cloneGeometry();

			IProjection lyrProj = lyrVect.getProjection();
			ICoordTrans ct = lyrProj.getCT(crs4326);
			if (ct != null) {
			    geometry.reProject(ct);
			}
			Geometry jts = geometry.toJTSGeometry();
			double lng = jts.getCoordinate().x;
			double lat = jts.getCoordinate().y;
			// http://stackoverflow.com/a/542965/930271
			String baseUri = "http://maps.google.com/maps?q=&layer=c&cbll=%s,%s&layer=c";
			String uri = String.format(baseUri, lat, lng);
			Desktop.getDesktop().browse(new URI(uri));
		    }

		}
	    }

	}

    }

    @Override
    public void pointDoubleClick(PointEvent event) throws BehaviorException {
    }

}
