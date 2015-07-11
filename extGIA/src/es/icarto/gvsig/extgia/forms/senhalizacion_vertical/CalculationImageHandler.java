package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.awt.Dimension;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.commons.utils.ImageUtils;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandler;

public class CalculationImageHandler implements ImageHandler {
    
    private Calculation calc;
    private String imgComp;
    private IValidatableForm form;
    private ImageComponent image;
    private ImageIcon emptyImage;
    private String extension;

    public CalculationImageHandler(String imgComp, Calculation calc, IValidatableForm form) {
	this.imgComp = imgComp;
	this.calc = calc;
	this.form = form;
	this.image = (ImageComponent) form.getFormPanel().getComponentByName(
		imgComp);

    }

    public void setEmptyImage(String imgPath) {
	emptyImage = ImageUtils.getScaled(imgPath, new Dimension(105, 60));
    }

    public void setExtension(String ext) {
	extension = ext.startsWith(".") ? ext : "." + ext;
    }

    @Override
    public void setListeners() {
	calc.setListeners();
    }

    @Override
    /**
     * The name of the components this handler is associated on, and commonly the name of the handler itself used in maps
     */
    public String getName() {
	return "";
    }

    @Override
    public void removeListeners() {
	calc.removeListeners();
    }

    @Override
    public void fillValues() {
	
    }

    @Override
    public void fillEmptyValues() {
	fillValues();
    }

}
