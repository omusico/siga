package es.icarto.gvsig.commons.queries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    private final static List<String> reservedColumns = Arrays
	    .asList(new String[] { "gid", "the_geom", "geom" });

    public static List<Field> getFields(String filePath, String schema,
	    String tablename) {
	List<Field> fields = new ArrayList<Field>();
	try {
	    DBSession session = DBSession.getCurrentSession();
	    InputStream input = new FileInputStream(filePath);
	    Properties props = new Properties();
	    props.load(input);
	    String[] columns = session.getColumns(schema, tablename);
	    List<String> asList = Arrays.asList(columns);
	    // List<String> l = new ArrayList<String>(asList);
	    // l.remove("gid");
	    // l.remove("the_geom");

	    for (String c : asList) {
		if (reservedColumns.contains(c)) {
		    continue;
		}
		fields.add(new Field(c, props.getProperty(c, c)));
	    }
	} catch (FileNotFoundException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (IOException e) {
	    logger.error(e.getStackTrace(), e);
	} catch (SQLException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return fields;
    }
}
