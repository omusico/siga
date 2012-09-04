package es.icarto.gvsig.extpm.preferences;


public class Preferences {

    // -- ORMLITE --
    public static final String XML_ORMLITE_RELATIVE_PATH = "data/extpm.xml";

    // -- GENERAL --
    public static final String PM_SCHEMA = "audasa_pm";
    public static final String PM_LAYER_NAME = "Policia_Margenes";

    // -- FORMS --
    public static final String PM_FORM_FILE = "pm.xml";
    public static final String PM_FORM_TITLE = "Políca de Márgenes";

    public static final String PM_FORM_WIDGET_PM_NUMBER = "numero_pm";
    public static final String PM_FORM_WIDGETS_NUM_PARCELA_CATASTRO = "num_parcela_catastro";
    public static final String PM_FORM_WIDGETS_POLIGONO_CATASTRO = "poligono_catastro";
    public static final String PM_FORM_WIDGET_PARCELAS_BUTTON = "num_parcela_audasa_button";
    public static final String PM_FORM_WIDGET_AREA = "area";
    public static final String PM_FORM_WIDGET_FECHA = "fecha";
    public static final String PM_FORM_WIDGET_MUNICIPIO = "municipio";
    public static final String PM_FORM_WIDGET_PARROQUIA = "parroquia";

    // -- DBNAMES --
    // PM
    public static final String PM_TABLENAME = "audasa_pm.exp_pm";
    public static final String PM_FIELD_NUMEROPM = "numero_pm";
    public static final String PM_FIELD_MUNICIPIO = "municipio";
    public static final String PM_FIELD_PARROQUIA = "parroquia";

    // Parroquias
    public static final String PARROQUIAS_TABLENAME = "audasa_cartografia_base.parroquias";
    public static final String PARROQUIAS_FIELD_NAME = "nome_corto";
    public static final String PARROQUIAS_FIELD_CODIGO = "cdconc";

    // Municipios
    public static final String MUNICIPIOS_TABLENAME = "audasa_cartografia_base.municipios";
    public static final String MUNICIPIOS_FIELD_CODIGO = "cdconc";
    public static final String MUNICIPIOS_FIELD_NAME = "nome";

    // Fincas
    public static final String FINCAS_TABLENAME = "audasa_expropiaciones.exp_finca";
    public static final String FINCAS_FIELD_TRAMO = "tramo";
    public static final String FINCAS_FIELD_IDFINCA = "id_finca";

    // Tramos
    public static final String TRAMOS_TABLENAME = "audasa_expropiaciones.tramos";
    public static final String TRAMOS_FIELD_NOMBRE = "nombre_tramo";
    public static final String TRAMOS_FIELD_ID = "id_tramo";

    // Fincas_PM
    public static final String FINCAS_PM_TABLENAME = "audasa_pm.fincas_pm";
    public static final String FINCAS_PM_FIELD_IDFINCA = "id_finca";
    public static final String FINCAS_PM_FIELD_NUMEROPM = "numero_pm";

    // Municipios Aux
    public static final String MUNICIPIOS_AUX_TABLENAME = "audasa_aplicaciones.orden_ayuntamientos";
    public static final String MUNICIPIOS_AUX_FIELD_NOMBRE = "nombre";
    public static final String MUNICIPIOS_AUX_FIELD_ORDEN = "orden";

}
