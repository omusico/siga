package com.iver.cit.gvsig.exceptions.visitors;


/**
 * @author Vicente Caballero Navarro
 */
public class StartWriterVisitorException extends StartVisitorException {
	public StartWriterVisitorException(String layer,Throwable exception) {
		super(layer,exception);
		init();
		// initCause(exception);
	}

	private void init() {
		messageKey = "error_start_writer_visitor";
		formatString = "Can´t start writer visitor the layer: %(layer) ";
	}
}

// [eiel-gestion-excepciones]