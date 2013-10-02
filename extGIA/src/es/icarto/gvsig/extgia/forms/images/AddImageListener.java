package es.icarto.gvsig.extgia.forms.images;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;

import es.icarto.gvsig.audasacommons.forms.reports.imagefilechooser.ImageFileChooser;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;

public class AddImageListener implements ActionListener {

    private final Connection connection;
    private final ImagesDAO dao;
    private final String tablename;
    private final String pkField;
    private String pkValue;

    public String getPkValue() {
	return pkValue;
    }

    public void setPkValue(String pkValue) {
	this.pkValue = pkValue;
    }

    public AddImageListener(String tablename, String pkField) {
	this.tablename = tablename;
	this.pkField = pkField;

	DBFacade dbFacade = new DBFacade();
	connection = dbFacade.getConnection();
	dao = new ImagesDAO();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (hasAlreadyImage()) {
	    Object[] overwriteImageOptions = {
		    PluginServices.getText(this, "image_msg_overwrite"),
		    PluginServices.getText(this, "image_msg_cancel") };

	    int m = JOptionPane.showOptionDialog(null,
		    PluginServices.getText(this, "image_msg_already_exists"),
		    null, JOptionPane.YES_NO_CANCEL_OPTION,
		    JOptionPane.INFORMATION_MESSAGE, null,
		    overwriteImageOptions, overwriteImageOptions[1]);

	    if (m == JOptionPane.OK_OPTION) {
		addImage(true);
	    }
	} else {
	    addImage(false);
	}
    }

    private void addImage(boolean update) {
	final ImageFileChooser fileChooser = new ImageFileChooser();
	File image = fileChooser.showDialog();
	if (image != null) {
	    try {
		dao.insertImageIntoDb(connection, DBFieldNames.GIA_SCHEMA, tablename,
			pkField, pkValue, image, update);
	    } catch (SQLException e) {
		e.printStackTrace();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    JOptionPane.showMessageDialog(null,
		    PluginServices.getText(this, "image_msg_added"));
	}
    }

    private boolean hasAlreadyImage() {
	try {
	    byte[] image = dao.readImageFromDb(connection, DBFieldNames.GIA_SCHEMA,
		    tablename, pkField, pkValue);
	    if (image != null) {
		return true;
	    } else {
		return false;
	    }
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}
	return false;
    }
}
