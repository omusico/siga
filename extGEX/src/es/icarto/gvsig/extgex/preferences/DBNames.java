package es.icarto.gvsig.extgex.preferences;

public class DBNames {

    //schemas
    public static final String EXPROPIATIONS_SCHEMA = "audasa_expropiaciones";

    // IDs along the whole DB. They are the same within several tables
    public static final String FIELD_IDCULTIVO = "id_cultivo";
    public static final String FIELD_IDFINCA = "id_finca";
    public static final String FIELD_IDPARROQUIA = "id_parroquia";
    public static final String FIELD_IDAYUNTAMIENTO = "id_ayuntamiento";
    public static final String FIELD_IDUC = "id_uc";
    public static final String FIELD_IDTRAMO = "id_tramo";

    // table tramos
    public static final String TABLE_TRAMOS = "tramos";
    public static final String FIELD_NOMBRETRAMO_TRAMOS = "nombre_tramo";

    // table uc
    public static final String TABLE_UC = "uc";
    public static final String FIELD_NOMBREUC_UC = "nombre_uc";

    // table ayuntamientos
    public static final String TABLE_AYUNTAMIENTOS = "ayuntamientos";
    public static final String FIELD_NOMBREAYUNTAMIENTO_AYUNTAMIENTO = "nombre_ayuntamiento";

    // table parroquias_subtramos
    public static final String TABLE_PARROQUIASSUBTRAMOS = "parroquias_subtramos";
    public static final String FIELD_NOMBREPARROQUIA_PARROQUIASUBTRAMOS = "nombre_parroquia";

    // more widgets for expropiationsform
    //    public static final String FIELD_IMPORTEPDTEMEJORAS = "importe_pendiente_mejoras";
    //    public static final String FIELD_IMPORTEPDTETERRENOS = "importe_pendiente_terrenos";
    //    public static final String FIELD_IMPORTEPDTETOTAL = "importe_pendiente_total";

    // table exp_finca
    public static final String TABLE_FINCAS = "exp_finca";
    public static final String FIELD_NUMEROFINCA_FINCAS = "numero_finca";
    public static final String FIELD_SECCION_FINCAS = "seccion";
    public static final String FIELD_TRAMO_FINCAS = "tramo";
    public static final String FIELD_UC_FINCAS = "unidad_constructiva";
    public static final String FIELD_AYUNTAMIENTO_FINCAS = "ayuntamiento";
    public static final String FIELD_PARROQUIASUBTRAMO_FINCAS = "parroquia_subtramo";

    // table reversiones
    public static final String LAYER_REVERSIONES = "Reversiones";
    public static final String FIELD_SUPERFICIE_REVERSIONES = "superficie";
    public static final String FIELD_OCUPACION_REVERSIONES = "ocupacion";
    public static final String FIELD_FECHAACTA_REVERSIONES = "fecha_acta_reversion";
    public static final String FIELD_IDREVERSION_REVERSIONES = "id_reversion";
    public static final String FIELD_NUMEROREVERSION_REVERSIONES = "numero_reversion";

    //table fincas_reversiones
    public static final String FIELD_IDREVERSION_FINCAS_REVERSIONES = "id_reversion";
    public static final String FIELD_IDEXPROPIACION_FINCAS_REVERSIONES = "id_finca";
    public static final String FIELD_SUPERFICIE_FINCAS_REVERSIONES = "superficie";
    public static final String FIELD_IMPORTE_FINCAS_REVERSIONES = "importe";

    //table expropiaciones
    public static final String FIELD_IDCULTIVO_EXPROPIACIONES = "id_cultivo";
    public static final String FIELD_SUPERFICIE_EXPROPIACIONES = "superficie_expropiada";

    //table desafecciones
    public static final String TABLE_DESAFECCIONES = "exp_desafecciones";
    public static final String FIELD_SUPERFICIE_DESAFECCIONES = "superficie";
    public static final String FIELD_OCUPACION_DESAFECCIONES = "ocupacion";
    public static final String FIELD_FECHAACTA_DESAFECCIONES = "fecha_acta_desafeccion";

    // more tables
    public static final String LAYER_FINCAS = "Fincas";
    public static final String TABLE_EXPROPIACIONES = "expropiaciones";
    public static final String TABLE_FINCASREVERSIONES = "fincas_reversiones";
    public static final String LAYER_MUNICIPIOS = "Municipios";
    public static final String LAYER_PARROQUIAS = "Parroquias";
    public static final String LAYER_PKS = "PKs_AP9";

    // tipo cultivos en DB
    public static final String TABLE_CULTIVOS = "tipo_cultivos";
    public static final int VALUE_CULTIVOS_OTROS = 99;
    public static final int VALUE_CULTIVOS_EDIFICACION = 7;
    public static final int VALUE_CULTIVOS_TERRENO = 6;
    public static final int VALUE_CULTIVOS_INCULTO = 5;
    public static final int VALUE_CULTIVOS_VINHA = 4;
    public static final int VALUE_CULTIVOS_PRADO = 3;
    public static final int VALUE_CULTIVOS_LABRADIO = 2;
    public static final int VALUE_CULTIVOS_MONTE = 1;

    // LoadWMS
    public static final String TABLE_WMS = "_wms";
    public static final String FIELD_LAYER_WMS = "layer_name";

    // QueriesPanel
    public static final String SCHEMA_DATA = "audasa_expropiaciones";
    public static final String SCHEMA_QUERIES = "audasa_aplicaciones";
    public static final String TABLE_QUERIES = "consultas";
    public static final String FIELD_CODIGO_QUERIES = "codigo";
    public static final int INDEX_CODIGO_QUERIES = 0;
    public static final int INDEX_DESCRIPCION_QUERIES = 2;

    // Table parroquias
    public static final String COD_CONCELLO = "cdconc";
    public static final String NOME_AYUNTAMIENTO = "nome";
    public static final String COD_PARROQUIA = "codparro";
    public static final String NOME_PARROQUIA = "nome_corto";

    // Table municipio_tramos
    public static final String TABLE_MUNICIPIO_TRAMOS = "municipio_tramos";
    public static final String FIELD_MUNICIPIO_MUNICIPIO_TRAMOS = "municipio";

}
