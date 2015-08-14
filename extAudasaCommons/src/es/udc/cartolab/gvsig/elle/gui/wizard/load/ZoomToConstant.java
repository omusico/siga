package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.geom.Rectangle2D;

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
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;

public class ZoomToConstant {

    // ZoomToConstant
    public final static String CONSTANTS_ZOOM_LAYER_FIELD = "municipio_codigo";
    public final static String CONSTANTS_ZOOM_LAYER_NAME = "Constante";

    public ZoomToConstant() {

    }

    public void zoom(String[] values) {
	FLyrVect layer = getEnvelopeConstantLayer();
	if (layer != null) {
	    int i = getPositionOnEnvelope(layer, values);
	    doZoom(layer, i);
	}
    }

    private FLyrVect getEnvelopeConstantLayer() {
	FLayer layer = null;
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers flayers = mapControl.getMapContext().getLayers();
	layer = flayers.getLayer(CONSTANTS_ZOOM_LAYER_NAME);
	return (FLyrVect) layer;
    }

    private int getPositionOnEnvelope(FLyrVect layer, String[] values) {
	try {
	    SelectableDataSource ds = layer.getRecordset();
	    int index = ds.getFieldIndexByName(CONSTANTS_ZOOM_LAYER_FIELD);
	    for (int i = 0; i < ds.getRowCount(); i++) {
		Value value = ds.getFieldValue(i, index);
		String stringValue = value
			.getStringValue(ValueWriter.internalValueWriter);
		// TODO: Sólo vale para hacer zoom a 1 único municipo
		String selectedValue = values.length > 0 ? values[0] : "";
		if ((stringValue.compareToIgnoreCase("'" + selectedValue + "'") == 0)) {
		    return i;
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
	return -1;
    }

    private void doZoom(FLyrVect layer, int pos) {
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
		rectangle.setFrameFromCenter(rectangle.getCenterX(),
			rectangle.getCenterY(), rectangle.getCenterX() + 100,
			rectangle.getCenterY() + 100);
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

}
