package es.icarto.gvsig.extgia.forms.senhalizacion_vertical;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JComboBox;

import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.commons.utils.ImageUtils;
import es.icarto.gvsig.navtableforms.IValidatableForm;
import es.icarto.gvsig.navtableforms.gui.images.ImageHandler;

public class SenhalesImageHandler implements ImageHandler, KeyListener,
ActionListener {

    private static final Dimension BOUNDARY = new Dimension(115, 115);
    private final String imgComp;
    private final IValidatableForm form;
    private final String folderPath;
    private final ImageComponent image;

    private String extension = ".gif";
    private ImageIcon emptyImage;
    private final JComboBox tipo;
    private final JComboBox codigo;

    public SenhalesImageHandler(String imgComp, String tipoName,
	    String codigoName, String folderPath, IValidatableForm form) {
	this.imgComp = imgComp;
	this.folderPath = folderPath.endsWith(File.separator) ? folderPath
		: folderPath + File.separator;
	this.form = form;
	this.image = (ImageComponent) form.getFormPanel().getComponentByName(
		imgComp);

	tipo = (JComboBox) form.getWidgets().get(tipoName);
	codigo = (JComboBox) form.getWidgets().get(codigoName);

	String emptyImagePath = folderPath + "0_placa.png";
	emptyImage = ImageUtils.getScaled(emptyImagePath, BOUNDARY);
    }

    public void setEmptyImage(String imgPath) {
	emptyImage = ImageUtils.getScaled(imgPath, BOUNDARY);
    }

    public void setExtension(String ext) {
	extension = ext.startsWith(".") ? ext : "." + ext;
    }

    @Override
    public void setListeners() {
	tipo.addActionListener(this);
	codigo.addActionListener(this);
    }

    @Override
    /**
     * The name of the components this handler is associated on, and commonly the name of the handler itself used in maps
     */
    public String getName() {
	return imgComp;
    }

    @Override
    public void removeListeners() {
	tipo.removeActionListener(this);
	codigo.removeActionListener(this);
    }

    private String getComboValue(JComboBox combo) {
	Object tmpValue = combo.getSelectedItem();
	return (tmpValue != null) ? tmpValue.toString().trim() : "";
    }

    @Override
    public void fillValues() {

	String tipoValue = getComboValue(tipo);
	String codigoValue = getComboValue(codigo);

	ImageIcon icon = emptyImage;

	if (tipoValue.equals("Cartel")) {
	    if (codigoValue.isEmpty() || (codigoValue.equals("Otro"))) {
		icon = ImageUtils.getScaled(folderPath + "0_cartel.png",
			BOUNDARY);
	    } else {
		String imgPath = folderPath + codigoValue + extension;
		icon = ImageUtils.getScaled(imgPath, BOUNDARY);
	    }

	} else if (tipoValue.equals("Placa")) {
	    if (codigoValue.isEmpty() || (codigoValue.equals("Otro"))) {
		icon = ImageUtils.getScaled(folderPath + "0_placa.png",
			BOUNDARY);
	    } else {
		String imgPath = folderPath + codigoValue + extension;
		icon = ImageUtils.getScaled(imgPath, BOUNDARY);
	    }
	} else {
	    // This condition is the same as "Placa" but this approach is more
	    // readable
	    if (codigoValue.isEmpty() || (codigoValue.equals("Otro"))) {
		icon = ImageUtils.getScaled(folderPath + "0_placa.png",
			BOUNDARY);
	    } else {
		String imgPath = folderPath + codigoValue + extension;
		icon = ImageUtils.getScaled(imgPath, BOUNDARY);
	    }
	}

	image.setIcon(icon);
	image.repaint();

    }

    @Override
    public void fillEmptyValues() {
	fillValues();
    }

    private void delegate() {
	if (!form.isFillingValues()) {
	    fillValues();
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	delegate();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
	delegate();
    }

}
