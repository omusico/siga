package com.iver.cit.gvsig.exceptions.layers;

/**
 * @author Vicente Caballero Navarro
 */
public class LegendLayerException extends LoadLayerException {

	public LegendLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_legend_layer";
		formatString = "Can´t load legend of the layer: %(layer) ";
	}

}
