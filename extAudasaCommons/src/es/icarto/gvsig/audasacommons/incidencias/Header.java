package es.icarto.gvsig.audasacommons.incidencias;

import java.util.HashMap;
import java.util.Map;

public enum Header {

    PK("P.K."), SENTIDO("Sentido"), FECHA("Fecha"), HORA("Hora"), MOTIVO(
	    "Motivo"), DURACION("Duración"), ASISTENCIAS_MOVILIZADAS(
	    "Asistencias Movilizadas"), CLIMA("Clima"), CAUSAS("Causas"), N_VEHICULOS(
	    "nº Vehículos"), HERIDOS("Heridos"), MUERTOS("Muertos"), ASIST_SANITARIA(
	    "Asistencia Sanitaria"), INFORME_ARENA("Informe Areana");

    private static final Map<String, Header> stringToEnum = new HashMap<String, Header>();

    static {
	for (Header op : values()) {
	    stringToEnum.put(op.toString(), op);
	}
    }

    private final String text;
    private int idx = -1;

    private Header(String text) {
	this.text = text;
    }

    public void setIdx(int idx) {
	this.idx = idx;
    }

    public int getIdx() {
	return idx;
    }

    @Override
    public String toString() {
	return this.text;
    }

    /**
     * Returns Header for string, or null if string is invalid
     * 
     */
    public static Header fromString(String text) {
	return stringToEnum.get(text);
    }

}
