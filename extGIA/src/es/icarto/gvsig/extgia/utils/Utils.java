package es.icarto.gvsig.extgia.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class Utils {

    private final static SimpleDateFormat DATE_FORMAT = DateFormatNT
	    .getDateFormat();
    private final static NumberFormat NUMBER_FORMAT = NumberFormat
	    .getInstance(Locale.getDefault());

    private Utils() {
	throw new AssertionError("Non instantiable class");
    }

    @Deprecated
    public static String writeValue(String value) {
	if (value == null) {
	    return "";
	} else if (value.equalsIgnoreCase("t")) {
	    return "Sí";
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
		valueFormatted = "Sí";
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
	} else if (o instanceof Number) {
	    return NUMBER_FORMAT.format(o);
	} else if (o instanceof Boolean) {
	    return ((Boolean) o) ? "Sí" : "No";
	}
	return o.toString();
    }
}
