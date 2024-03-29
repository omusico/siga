package es.icarto.gvsig.commons.queries;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.icarto.gvsig.commons.utils.Field;
import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;
import es.udc.cartolab.gvsig.navtable.format.DoubleFormatNT;
import es.udc.cartolab.gvsig.navtable.format.IntegerFormatNT;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class);

    private final static List<String> reservedColumns = Arrays
	    .asList(new String[] { "gid", "the_geom", "geom" });

    private final static SimpleDateFormat DATE_FORMAT = DateFormatNT
	    .getDateFormat();
    private final static NumberFormat DOUBLE_FORMAT = DoubleFormatNT
	    .getDisplayingFormat();
    private static final NumberFormat INT_FORMAT = IntegerFormatNT
	    .getDisplayingFormat();

    private Utils() {
	throw new AssertionError("Non instantiable class");
    }

    public static List<Field> getFields(String filePath, String schema,
	    String table, List<String> ignoreColumns) {
	List<Field> fields = new ArrayList<Field>();
	try {
	    DBSession session = DBSession.getCurrentSession();
	    InputStream input = new FileInputStream(filePath);
	    Properties props = new Properties();
	    props.load(input);
	    String[] columns = session.getColumns(schema, table);
	    List<String> asList = Arrays.asList(columns);

	    for (String c : asList) {
		if (ignoreColumns.contains(c)) {
		    continue;
		}
		String longname = props.getProperty(schema + "." + table + "."
			+ c, c);
		fields.add(new Field(c, longname));
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
    
    public static List<Field> getFields(String filePath, String schema,
	    String table) {
	return getFields(filePath, schema, table, reservedColumns);
    }

    @Deprecated
    public static String writeValue(String value) {
	if (value == null) {
	    return "";
	} else if (value.equalsIgnoreCase("t")) {
	    return "S�";
	} else if (value.equalsIgnoreCase("f")) {
	    return "No";
	} else if (value.contains(".")) {
	    return value.replace(".", ",");
	} else {
	    return value;
	}
    }

    @Deprecated
    public static String writeDBValueFormatted(ResultSet rs, int column) {
	String valueFormatted = null;
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
	NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
	try {
	    valueFormatted = rs.getString(column);
	    if (rs.getMetaData().getColumnType(column) == 91) {
		Date date = rs.getDate(column);
		valueFormatted = dateFormat.format(date);
		// This is a little 'hack' because of fecha_puesta_servicio
		// is Integer on database instead of Date
	    } else if (rs.getMetaData().getColumnName(column)
		    .equalsIgnoreCase("fecha_puesta_servicio")) {
		valueFormatted = rs.getString(column);
	    } else if (rs.getMetaData().getColumnType(column) == 4
		    || rs.getMetaData().getColumnType(column) == 8
		    || rs.getMetaData().getColumnType(column) == 2) {
		valueFormatted = nf.format(rs.getDouble(column));
	    } else if (rs.getString(column).equals("t")) {
		valueFormatted = "S�";
	    } else if (rs.getString(column).equals("f")) {
		valueFormatted = "No";
	    } else {
		valueFormatted = rs.getString(column);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return valueFormatted;
    }

    public static String formatValue(Object o) {

	// TODO
	// This is a little 'hack' because of fecha_puesta_servicio
	// is Integer on database instead of Date
	// }else if (rs.getMetaData().getColumnName(column).
	// equalsIgnoreCase("fecha_puesta_servicio")) {
	// valueFormatted = rs.getString(column);
	// }
	if (o == null) {
	    return "";
	} else if (o instanceof Date) {
	    return DATE_FORMAT.format(o);
	} else if (o instanceof Integer) {
	    return INT_FORMAT.format(o);
	} else if (o instanceof Number) {
	    return DOUBLE_FORMAT.format(o);
	} else if (o instanceof Boolean) {
	    return ((Boolean) o) ? "S�" : "No";
	}
	return o.toString();
    }
}
