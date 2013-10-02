package es.icarto.gvsig.extgia.forms.images;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;

import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.extgia.utils.ImageUtils;

public class ShowImageAction {

    private final ImageComponent imageComponent;
    private final Connection connection;
    private final String tablename;
    private final String pkField;
    private final String pkValue;

    public ShowImageAction(ImageComponent imageComponent, String tablename,
	    String pkField, String pkValue) {
	this.imageComponent = imageComponent;
	this.tablename = tablename;
	this.pkField = pkField;
	this.pkValue = pkValue;
	DBFacade dbFacade = new DBFacade();
	connection = dbFacade.getConnection();
	showImage();
    }

    private void showImage() {
	ImagesDAO dao = new ImagesDAO();
	try {
	    byte[] elementImageBytes = dao.readImageFromDb(connection, DBFieldNames.GIA_SCHEMA,
		    tablename, pkField, pkValue);
	    if (elementImageBytes == null) {
		imageComponent.setIcon(null);
		return;
	    }
	    Image elementImage = ImageUtils.convertByteaToImage(elementImageBytes);
	    BufferedImage imageResized =
		    ImageUtils.resizeImageWithHint((BufferedImage) elementImage, 350, 250);
	    ImageIcon elementIcon = new ImageIcon(imageResized);
	    imageComponent.setIcon(elementIcon);
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}
    }

}
