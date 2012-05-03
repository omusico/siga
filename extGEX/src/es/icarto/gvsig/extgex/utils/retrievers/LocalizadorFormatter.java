package es.icarto.gvsig.extgex.utils.retrievers;


public class LocalizadorFormatter {

    public static final String FORMAT_IDTRAMO = "%1$02d";
    public static final String FORMAT_IDUC = "%1$03d";
    public static final String FORMAT_IDAYUNTAMIENTO = "%1$01d";
    public static final String FORMAT_IDSUBTRAMO = "%1$01d";
    public static final String FORMAT_NROFINCA = "%1$04d";

    public static final String SUBTRAMO_DEFAULT_VALUE = "0";
    public static final String SECCION_DEFAULT_VALUE = "00";
    public static final String FINCA_DEFAULT_VALUE = "0000";

    public static String getUC(String ucValue) {
	return String.format(FORMAT_IDUC, Integer.parseInt(ucValue));
    }

    public static String getTramo(String tramoValue) {
	return String.format(FORMAT_IDTRAMO, Integer.parseInt(tramoValue));
    }

    public static String getAyuntamiento(String ayuntamientoValue) {
	return String.format(FORMAT_IDAYUNTAMIENTO, Integer.parseInt(ayuntamientoValue));
    }

    public static String getSubtramo(String subtramoValue) {
	try {
	    return String.format(FORMAT_IDSUBTRAMO, Integer.parseInt(subtramoValue));
	} catch (NumberFormatException nfe) {
	    return SUBTRAMO_DEFAULT_VALUE;
	}
    }

    public static String getNroFinca(String nroFincaValue) {
	    return String.format(LocalizadorFormatter.FORMAT_NROFINCA,
		    Integer.parseInt(nroFincaValue));
    }

}
