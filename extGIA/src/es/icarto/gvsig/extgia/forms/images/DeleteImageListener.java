package es.icarto.gvsig.extgia.forms.images;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.jeta.forms.components.image.ImageComponent;

import es.icarto.gvsig.extgia.preferences.DBFieldNames;


public class DeleteImageListener implements ActionListener {

    private final Connection connection;
    private final JButton addImageButton;
    private final ImagesDAO dao;
    private final ImageComponent imageComponent;
    private final String tablename;
    private final String pkField;
    private String pkValue;

    public DeleteImageListener(ImageComponent imageComponent, JButton addImageButton, String tablename,
	    String pkField) {
	this.imageComponent = imageComponent;
	this.addImageButton = addImageButton;
	this.tablename = tablename;
	this.pkField = pkField;

	DBFacade dbFacade = new DBFacade();
	connection = dbFacade.getConnection();
	dao = new ImagesDAO();
    }

    public String getPkValue() {
	return pkValue;
    }

    public void setPkValue(String pkValue) {
	this.pkValue = pkValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	try {
	    Object[] options = {PluginServices.getText(this, "delete"),
		    PluginServices.getText(this, "cancel")};
	    int response = JOptionPane.showOptionDialog(null,
		    PluginServices.getText(this, "img_delete_warning"),
		    PluginServices.getText(this, "delete"),
		    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE,
		    null, options, options[0]);
	    if (response == JOptionPane.YES_OPTION) {
		dao.deleteImageFromDb(connection, DBFieldNames.GIA_SCHEMA, tablename, pkField, pkValue);
		new ShowImageAction(imageComponent, addImageButton, tablename, pkField, pkValue);
		imageComponent.repaint();
	    }
	} catch (SQLException e1) {
	    e1.printStackTrace();
	}
    }

}
