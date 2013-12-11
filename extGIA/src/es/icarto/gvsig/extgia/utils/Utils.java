package es.icarto.gvsig.extgia.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import es.udc.cartolab.gvsig.navtable.format.DateFormatNT;

public class Utils {

    public static String writeValue(String value) {
	if (value == null) {
	    return "";
	}else if (value.equalsIgnoreCase("t")) {
	    return "Sí";
	}else if (value.equalsIgnoreCase("f")) {
	    return "No";
	}else if (value.contains(".")) {
	    return value.replace(".", ",");
	}else {
	    return value;
	}
    }

    public static String writeDBValueFormatted(ResultSet rs, int column) {
	String valueFormatted = null;
	SimpleDateFormat dateFormat = DateFormatNT.getDateFormat();
	NumberFormat nf = NumberFormat.getInstance(Locale.getDefault());
	try {
	    valueFormatted = rs.getString(column);
	    if (rs.getMetaData().getColumnType(column) == 91) {
		Date date = rs.getDate(column);
		valueFormatted = dateFormat.format(date);
		//This is a little 'hack' because of fecha_puesta_servicio
		//is Integer on database instead of Date
	    }else if (rs.getMetaData().getColumnName(column).
		    equalsIgnoreCase("fecha_puesta_servicio")) {
		valueFormatted = rs.getString(column);
	    }else if(rs.getMetaData().getColumnType(column) == 4 ||
		    rs.getMetaData().getColumnType(column) == 8 ||
		    rs.getMetaData().getColumnType(column) == 2) {
		valueFormatted = nf.format(rs.getDouble(column));
	    }else if (rs.getString(column).equals("t")){
		valueFormatted = "Sí";
	    }else if (rs.getString(column).equals("t")){
		valueFormatted = "No";
	    } else {
		valueFormatted = rs.getString(column);
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return valueFormatted;
    }
}
