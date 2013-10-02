package es.icarto.gvsig.extgia.forms.images;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import es.icarto.gvsig.extgia.utils.ImageUtils;

public class ImagesDAO {

    private static final String IMAGE_FIELDNAME = "image";

    public void insertImageIntoDb(Connection connection, String schema, String tablename,
	    String pkField, String pkValue, File image, boolean update) throws SQLException, IOException {

	byte[] imageBytes = ImageUtils.convertImageToBytea(image);
	PreparedStatement statement;
	if (update) {
	    statement = connection.prepareStatement("UPDATE "
		    + schema + "."
		    + tablename + " SET " + IMAGE_FIELDNAME
		    + " = " + "? WHERE " + pkField
		    + " = ?");
	    statement.setBytes(1, imageBytes);
	    statement.setString(2, pkValue);
	} else {
	    statement = connection.prepareStatement("INSERT INTO "
		    + schema + "."
		    + tablename + " VALUES (?, ?)");
	    statement.setString(1, pkValue);
	    statement.setBytes(2, imageBytes);
	}
	statement.executeUpdate();
	if (!connection.getAutoCommit()) {
	    connection.commit();
	}
	statement.close();
    }


    public byte[] readImageFromDb(Connection connection, String schema, String tablename,
	    String pkField, String pkValue) throws SQLException {
	PreparedStatement statement = null;
	try {
	    statement = connection.prepareStatement("SELECT "
		    + IMAGE_FIELDNAME + " FROM "
		    + schema + "." + tablename
		    + " WHERE " + pkField + " = ?");
	    statement.setString(1, pkValue);
	    ResultSet rs = statement.executeQuery();
	    if (rs.next()) {
		return rs.getBytes(1);
	    } else {
		return null;
	    }
	} finally {
	    if (statement != null) {
		statement.close();
	    }
	}
    }

}
