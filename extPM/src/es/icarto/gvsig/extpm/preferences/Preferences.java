package es.icarto.gvsig.extpm.preferences;


public class Preferences {

    // ORMLITE
    public static final String XML_ORMLITE_RELATIVE_PATH = "data/extpm.xml";

    // GENERAL
    public static final String PM_SCHEMA = "audasa_pm";
    public static final String PM_LAYER_NAME = "Policia_Margenes";

    // FORMS
    public static final String PM_FORM_FILE = "pm.xml";
    public static final String PM_FORM_TITLE = "Políca de Márgenes";

    public static final String PM_FORM_WIDGET_PM_NUMBER = "numero_pm";
    public static final String PM_FORM_WIDGET_PARCELAS_BUTTON = "num_parcela_audasa_button";
    public static final String PM_FORM_WIDGET_AREA = "area";
    public static final String PM_FORM_WIDGET_FECHA = "fecha";
    public static final String PM_FORM_WIDGET_MUNICIPIO = "municipio";
    public static final String PM_FORM_WIDGET_PARROQUIA = "parroquia";

    // DBNAMES
    public static final String PARROQUIAS_TABLENAME = "audasa_cartografia_base.parroquias";
    public static final String PARROQUIAS_FIELD_NAME = "nome_corto";
    public static final String PARROQUIAS_FIELD_CODIGO = "cdconc";

    public static final String MUNICIPIOS_TABLENAME = "audasa_cartografia_base.municipios";
    public static final String MUNICIPIOS_FIELD_CODIGO = "cdconc";
    public static final String MUNICIPIOS_FIELD_NAME = "nome";

}
