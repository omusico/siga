package es.icarto.gvsig.audasacommons;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.cit.gvsig.fmap.core.IFeature;
import com.iver.cit.gvsig.fmap.layers.FBitSet;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.VectorialDBAdapter;

/**
 * Features selected on a layer are tracked by its row number. Many operations
 * on the layer, like add new records, cad tools or any start/stop editing
 * process cleans the selection. Store the old selection FBitSet or
 * SelectionSupport is not a valid strategy to restore the selection after an
 * operation because the row number can be changed.
 *
 * This class stores the ID of the selected features (the primary keys in a
 * postgis layer) and then select the features which id has been stored.
 *
 * If the layer has records selected before apply restoreSelection those are not
 * modified, only the new ones are added to the selection.
 *
 * Warning: This class only works with Postgis Layers (or more correctly with
 * layers that have a VectorialDBAdapter as source. Other sources set as id for
 * the feature the row number so can not be used. If the source is not a
 * VectorialDBAdapter the class silently fail
 */
public class SaveSelection {

    private static final Logger logger = Logger.getLogger(SaveSelection.class);

    private final FLyrVect layer;
    private final Collection<String> pksSelected;

    private boolean error = false;

    public SaveSelection(FLyrVect layer) {
	this.layer = layer;
	pksSelected = new ArrayList<String>();

	if (!isDBLayer()) {
	    error = true;
	}

	try {
	    storeOriginalSelection();
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	    error = true;
	}
    }

    private boolean isDBLayer() {
	return layer.getSource() instanceof VectorialDBAdapter;
    }

    private void storeOriginalSelection() throws ReadDriverException {
	FBitSet bs = layer.getSelectionSupport().getSelection();
	ReadableVectorial source = layer.getSource();

	source.start();

	for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
	    IFeature feature = source.getFeature(i);
	    pksSelected.add(feature.getID());
	}

	source.stop();
    }

    public boolean restoreSelection() {

	if (pksSelected.isEmpty() || error) {
	    return error;
	}

	try {
	    doRestoreSelection();
	    error = false;
	} catch (ReadDriverException e) {
	    logger.error(e.getStackTrace(), e);
	    error = true;
	}

	return error;
    }

    private void doRestoreSelection() throws ReadDriverException {
	ReadableVectorial source = layer.getSource();
	Collection<String> pks = new ArrayList<String>(pksSelected);
	source.start();
	FBitSet selection = layer.getSelectionSupport().getSelection();
	for (int i = 0; i < source.getShapeCount(); i++) {
	    IFeature feat = source.getFeature(i);
	    String id = feat.getID();
	    if (pks.contains(id)) {
		pks.remove(id);
		selection.set(i);
		if (pks.isEmpty()) {
		    break;
		}
	    }
	}

	source.stop();
    }

    /**
     * This method could be changed on the future to a better error management
     * pattern, that allows represent if the error happens on the initialization
     * or in the restoration phase
     */
    public boolean getError() {
	return error;
    }

}