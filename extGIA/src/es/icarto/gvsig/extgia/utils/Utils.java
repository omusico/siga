package es.icarto.gvsig.extgia.utils;

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

}
