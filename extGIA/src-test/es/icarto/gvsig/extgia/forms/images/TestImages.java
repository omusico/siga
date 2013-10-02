package es.icarto.gvsig.extgia.forms.images;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import es.icarto.gvsig.extgia.utils.ImageUtils;


public class TestImages {

    Connection connection;

    @Before
    public void doSetupCroquis() {
	String url = "jdbc:postgresql://localhost:5432/audasa_test";
	String user = "postgres";
	String passwd = "postgres";
	// postgresql-9.1-903.jdbc3.jar needs to be in the classpasth before th
	// other gvSIG jars related to pgsql.
	// Configure that in your classpath tab if you use eclipse
	try {
	    Class.forName("org.postgresql.Driver");
	    connection = DriverManager.getConnection(url, user, passwd);
	    connection.setAutoCommit(false);
	} catch (ClassNotFoundException e) {
	    e.printStackTrace();
	} catch (SQLException e) {
	    e.printStackTrace();
	}
    }

    @Test
    public void testingInsertAndReadCroquis() throws Exception {

	try {
	    File image = new File("data-test/test.jpg");
	    String query = "DELETE FROM audasa_extgia.taludes_imagenes";
	    PreparedStatement statement = connection.prepareStatement(query);
	    statement.execute();
	    connection.commit();

	    ImagesDAO postgresCroquis = new ImagesDAO();
	    postgresCroquis.insertImageIntoDb(connection, "audasa_extgia", "taludes_imagenes",
		    "id_talud", "C-002N", image, false);
	    byte[] imageDbBytes = postgresCroquis.readImageFromDb(connection,
		    "audasa_extgia", "taludes_imagenes", "id_talud", "C-002N");

	    byte[] imageMockBytes = ImageUtils.convertImageToBytea(image);

	    assertTrue(Arrays.equals(imageDbBytes, imageMockBytes));
	} finally {
	    connection.rollback();
	}
    }

    @Test
    public void testingUpdateAndReadCroquis() throws Exception {

	try {
	    File image = new File("data-test/test.jpg");
	    String query = "DELETE FROM audasa_extgia.taludes_imagenes";
	    PreparedStatement statement = connection.prepareStatement(query);
	    statement.execute();
	    connection.commit();

	    ImagesDAO postgresCroquis = new ImagesDAO();
	    postgresCroquis.insertImageIntoDb(connection, "audasa_extgia", "taludes_imagenes",
		    "id_talud", "C-002N", image, false);
	    File imageToUpdate = new File("data-test/test2.jpg");
	    postgresCroquis.insertImageIntoDb(connection, "audasa_extgia", "taludes_imagenes",
		    "id_talud", "C-002N", imageToUpdate, true);

	    byte[] imageDbBytes = postgresCroquis.readImageFromDb(connection, "audasa_extgia",
		    "taludes_imagenes", "id_talud", "C-002N");

	    byte[] imageMockBytes = ImageUtils
		    .convertImageToBytea(imageToUpdate);

	    assertTrue(Arrays.equals(imageDbBytes, imageMockBytes));
	} finally {
	    connection.rollback();
	}
    }

}
