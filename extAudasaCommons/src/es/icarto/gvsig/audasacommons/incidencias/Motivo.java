package es.icarto.gvsig.audasacommons.incidencias;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public enum Motivo {
    ANIMALES("ANIMALES", new Color(166, 206, 227)), PEATONES("PEATONES",
	    new Color(166, 206, 227)), INCENDIOS("INCENDIOS", new Color(31,
	    120, 180)), V_NO_AUTORIZADO("V. NO AUTORIZADO", new Color(178, 223,
	    138)), V_DIR_CONTRARIA("V. DIR. CONTRARIA", new Color(51, 160, 44)), VERTIDOS(
	    "VERTIDOS", new Color(251, 154, 153)), OBJETOS("OBJETOS",
	    new Color(255, 127, 0)), V_PARADO_AVERIADO("V.PARADO O AVERIADO",
	    new Color(253, 191, 111)), OTROS_EVENTOS("OTROS EVENTOS",
	    new Color(227, 26, 28)), ACCIDENTES("ACCIDENTES", new Color(202,
	    178, 214)), RETENCION("RETENCION", new Color(106, 61, 154)), CLIMA_ADVERSA(
	    "CLIMATOLOGIA ADVERSA", new Color(255, 255, 153)), TTES_ESPECIALES(
	    "TTES. ESPECIALES", new Color(177, 89, 40)), ALARMAS_SEGUR(
	    "ALARMAS SEGUR.", new Color(0, 0, 0)), AVERIA("AVERIA O DETERIORO",
	    new Color(194, 7, 157)), OTRAS_LLAMADAS("OTRAS LLAMADAS",
	    new Color(227, 26, 28)), CONDUCCION_TEM("CONDUCCION TEMERARIA",
	    new Color(51, 160, 44));

    private static final Map<String, Motivo> stringToEnum = new HashMap<String, Motivo>();

    static {
	for (Motivo op : values()) {
	    stringToEnum.put(op.toString(), op);
	}
    }

    private final String text;
    private final Color color;

    private Motivo(String text, Color color) {
	this.text = text;
	this.color = color;
    }

    @Override
    public String toString() {
	return this.text;
    }

    /**
     * Returns Motivo for string, or null if string is invalid
     * 
     */
    public static Motivo fromString(String text) {
	return stringToEnum.get(text);
    }

    public Color color() {
	return this.color;
    }

}
