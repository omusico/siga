package es.icarto.gvsig.extgia.forms.images;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.forms.reports.imagefilechooser.ImageFileChooser;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.ImageUtils;

public class AddImageListener implements ActionListener {

    private final Connection connection;
    private final ImagesDAO dao;
    private final ImageComponent imageComponent;
    private final JButton addImageButton;
    private final String tablename;
    private final String pkField;
    private String pkValue;

    public String getPkValue() {
	return pkValue;
    }

    public void setPkValue(String pkValue) {
	this.pkValue = pkValue;
    }

    public AddImageListener(ImageComponent imageComponent, JButton addImageButton, String tablename,
	    String pkField) {
	this.imageComponent = imageComponent;
	this.addImageButton = addImageButton;
	this.tablename = tablename;
	this.pkField = pkField;

	connection = DBFacade.getConnection();
	dao = new ImagesDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (hasAlreadyImage()) {
	    addImage(true);
	}else {
	    addImage(false);
	}
    }

    private void addImage(boolean update) {
	final ImageFileChooser fileChooser = new ImageFileChooser();
	File fileImage = fileChooser.showDialog();
	if (fileImage != null) {
	    try {
		BufferedImage image = ImageIO.read(fileImage);
		BufferedImage imageResized = resizeImage(image);
		dao.insertImageIntoDb(connection, DBFieldNames.GIA_SCHEMA, tablename,
			pkField, pkValue, imageResized, update);
	    } catch (SQLException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    JOptionPane.showMessageDialog(null,
		    PluginServices.getText(this, "image_msg_added"));
	    new ShowImageAction(imageComponent, addImageButton, tablename, pkField, pkValue);
	    
	}
    }

    private BufferedImage resizeImage(BufferedImage image) {
	BufferedImage imageResized;
	if (image.getWidth() < 615) {
	    return image;
	}
	if (image.getWidth() > image.getHeight()) {
	    imageResized =
		    ImageUtils.resizeImageWithHint(image, 615, 460);
	}else {
	    imageResized =
		    ImageUtils.resizeImageWithHint(image, 345, 460);
	}
	return imageResized;
    }

    private boolean hasAlreadyImage() {
	try {
	    byte[] image = dao.readImageFromDb(connection, DBFieldNames.GIA_SCHEMA, tablename,
		    pkField, pkValue);
	    if (image != null) {
		return true;
	    }else {
		return false;
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return false;
    }
}
