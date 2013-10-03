package es.icarto.gvsig.extgia.forms.images;

import java.awt.image.BufferedImage;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.ImageIcon;

import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.audasacommons.PreferencesPage;
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
		imageComponent.setIcon(getUnavailableImageIcon());
		return;
	    }
	    BufferedImage elementImage = ImageUtils.convertByteaToImage(elementImageBytes);
	    ImageIcon elementIcon = new ImageIcon(elementImage);
	    imageComponent.setIcon(elementIcon);
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}
    }

    private ImageIcon getUnavailableImageIcon() {
	return new ImageIcon (PreferencesPage.IMG_UNAVAILABLE);
    }

}
