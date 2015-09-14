package es.udc.cartolab.gvsig.elle.constants;

import java.awt.geom.Rectangle2D;

import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.ViewPort;

public class ZoomToConstant {

    private final MapControl mapControl;

    private final Constant constant;

    public ZoomToConstant(MapControl mapControl, Constant constant) {
	this.mapControl = mapControl;
	this.constant = constant;
    }

    public void zoom(String[] values) {
	Rectangle2D bbox = getGeometry();
	if (bbox != null) {
	    zoomTo(bbox);
	}
    }

    private Rectangle2D getGeometry() {
	Rectangle2D rectangle = constant.getGeometry();

	// constant handles the reprojection
	// if (layer.getCoordTrans() != null) {
	// geometry.reProject(layer.getCoordTrans());
	// }

	if (rectangle.getWidth() < 200) {
	    rectangle.setFrameFromCenter(rectangle.getCenterX(),
		    rectangle.getCenterY(), rectangle.getCenterX() + 100,
		    rectangle.getCenterY() + 100);
	}
	return rectangle;
    }

    private Rectangle2D zoomTo(Rectangle2D rectangle) {

	if (rectangle != null) {
	    final ViewPort viewPort = mapControl.getMapContext().getViewPort();
	    viewPort.setExtent(rectangle);
	    viewPort.refreshExtent();

	}
	return rectangle;

    }
}
