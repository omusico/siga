package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.io.File;

import com.iver.cit.gvsig.fmap.core.SymbologyFactory;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class SenhalizacionVerticalSenhalesSubForm extends GIASubForm {

    public static final String TABLENAME = "senhalizacion_vertical_senhales";

    public SenhalizacionVerticalSenhalesSubForm() {
	super(TABLENAME);
	addChained("nombre_senhal", "codigo_senhal");
	
	String folderPath = SymbologyFactory.SymbolLibraryPath + File.separator
		+ "senhales" + File.separator;
	SenhalesImageHandler imageHandler = new SenhalesImageHandler(
		"img_senhal", "tipo_senhal", "codigo_senhal", folderPath, this);
	imageHandler.setEmptyImage(PreferencesPage.IMG_UNAVAILABLE);
	imageHandler.setExtension(".gif");
	addImageHandler(imageHandler);
    }
}
