package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.extgia.forms.utils.GIASubForm;

@SuppressWarnings("serial")
public class SenhalizacionVerticalSenhalesSubForm extends GIASubForm {

    public static final String TABLENAME = "senhalizacion_vertical_senhales";

    public SenhalizacionVerticalSenhalesSubForm() {
	super(TABLENAME);
	addChained("nombre_senhal", "codigo_senhal");
    }

    @Override
    protected void fillSpecificValues() {
	super.fillSpecificValues();
	ImageComponent image = (ImageComponent) getFormPanel()
		.getComponentByName("img_senhal");

	// TODO.
	final JComboBox codigoSenhal = (JComboBox) getWidgets().get(
		"codigo_senhal");
	Object codigoSenhalValue = codigoSenhal.getSelectedItem();

	ImageIcon icon = new ImageIcon(SymbologyFactory.SymbolLibraryPath
		+ File.separator + "todas" + File.separator + codigoSenhalValue
		+ ".gif");
	image.setIcon(icon);

    }
}
