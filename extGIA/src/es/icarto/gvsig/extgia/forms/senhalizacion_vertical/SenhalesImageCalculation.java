package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_A;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_B;
import static es.icarto.gvsig.extgia.preferences.DBFieldNames.AREA_DESCANSO_C;

import java.awt.Dimension;
import java.io.File;
import java.math.BigDecimal;

import javax.swing.ImageIcon;

import com.iver.cit.gvsig.fmap.core.SymbologyFactory;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.utils.ImageUtils;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.calculation.Calculation;

public class SenhalesImageCalculation extends Calculation {

    private static final String CODIGO_SENHAL = "codigo_senhal";
    private static final String TIPO_SENHAL = "tipo_senhal";
    
    private final String folderPath = SymbologyFactory.SymbolLibraryPath + File.separator
		+ "senhales" + File.separator;
    
    private final String extension = ".gif";
    
    private final String imgPath = PreferencesPage.IMG_UNAVAILABLE;
    private final ImageIcon emptyImage = ImageUtils.getScaled(imgPath, new Dimension(105, 60));

    public SenhalesImageCalculation(IValidatableForm form) {
	super(form);
    }

    @Override
    protected String resultName() {
	return "img_senhal";
    }

    @Override
    protected String[] operandNames() {
	return new String[] {TIPO_SENHAL, CODIGO_SENHAL};
    }
    
    @Override
    protected void setValue(boolean valid) {
        if (!valid) {
            return;
        }
        String tipoValue = stringValue(TIPO_SENHAL);
	String codigoValue = stringValue(CODIGO_SENHAL);

	ImageIcon icon = null;
	if (tipoValue.equals("Cartel")) {
	    icon = ImageUtils.getScaled(folderPath + "0_cartel.png",
		    new Dimension(105, 60));
	} else if (tipoValue.equals("Placa") && (codigoValue.isEmpty() || (codigoValue.equals("Otro")))) {
	    icon = ImageUtils.getScaled(folderPath + "0_placa.png",
		    new Dimension(105, 60));
	} else if (! codigoValue.isEmpty()){
	    String imgPath = folderPath + codigoValue + extension;
	    // System.out.println(image.getBounds());
	    // ImageIcon icon = ImageUtils.getScaled(imgPath,
	    // image.getBounds().getSize());
	    icon = ImageUtils.getScaled(imgPath, new Dimension(105, 60));
	}

	if (icon == null) {
	    icon = emptyImage;
	}
	((ImageComponent) resultWidget).setIcon(icon);
	resultWidget.repaint();
    }

    @Override
    protected String calculate() {
	return null;
    }

}
