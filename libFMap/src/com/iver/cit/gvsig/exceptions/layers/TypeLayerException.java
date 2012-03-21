package com.iver.cit.gvsig.exceptions.layers;

/**
 * @author Vicente Caballero Navarro
 */
public class TypeLayerException extends LoadLayerException {

	public TypeLayerException(String l,Throwable exception) {
		super(l,exception);
		init();
	}
	/**
	 *
	 */
	private void init() {
		messageKey = "error_type_layer";
		formatString = "Can´t use type of the layer: %(layer) ";
	}

}
