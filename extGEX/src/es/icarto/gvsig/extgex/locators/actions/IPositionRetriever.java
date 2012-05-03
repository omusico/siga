package es.icarto.gvsig.extgex.locators.actions;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;

public interface IPositionRetriever {

    /**
     * @return the position of the feature to zoom in or 
     * AbstractNavTable.EMPTY_REGISTER if none
     */
    public int getPosition();

    /**
     * 
     * @return the layer to work on
     */
    public FLyrVect getLayer();
}
