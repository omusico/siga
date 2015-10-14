package es.icarto.gvsig.extgex.locators.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;

import javax.swing.JOptionPane;

import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;

import es.udc.cartolab.gvsig.elle.constants.IPositionRetriever;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class ZoomToHandler implements ActionListener {

    private final IPositionRetriever zoomHandlerData;
    private final boolean zoomAndSelect;

    public ZoomToHandler(IPositionRetriever zoomHandlerData, boolean zoomAndSelect) {
	this.zoomHandlerData = zoomHandlerData;
	this.zoomAndSelect = zoomAndSelect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	int position = zoomHandlerData.getPosition();
	FLyrVect layer = zoomHandlerData.getLayer();
	if(position != AbstractNavTable.EMPTY_REGISTER) {
	    zoom(layer, position);
	    if (zoomAndSelect) {
		select(layer, position);
	    }
	}
    }

    private void select(FLyrVect layer, int position) {
	FBitSet bitset = null;
	int pos = Long.valueOf(position).intValue();
	try {
	    bitset = layer.getRecordset().getSelection();
	    bitset.clear();
	    bitset.set(pos);
	    layer.getRecordset().setSelection(bitset);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void zoom(FLyrVect layer, int pos) {
	try {
	    Rectangle2D rectangle = null;
	    IGeometry g;
	    ReadableVectorial source = (layer).getSource();
	    source.start();
	    g = source.getShape(pos);
	    source.stop();

	    if (g != null) {
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
			    rectangle.getCenterY(),
			    rectangle.getCenterX() + 50,
			    rectangle.getCenterY() + 50);
		}
		if (rectangle != null) {
		    layer.getMapContext().getViewPort().setExtent(rectangle);
		}
	    }else {
		JOptionPane.showMessageDialog(null, PluginServices.getText(
			this, "feature_has_no_geometry_to_zoom"));
	    }
	} catch (InitializeDriverException e) {
	    e.printStackTrace();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

}
