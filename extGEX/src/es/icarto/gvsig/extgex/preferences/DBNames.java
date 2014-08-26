package es.icarto.gvsig.extgex.preferences;

public class DBNames {

    // schemas
    public static final String EXPROPIATIONS_SCHEMA = "audasa_expropiaciones";
    public static final String PM_SCHEMA = "audasa_pm";

    // IDs along the whole DB. They are the same within several tables
    public static final String FIELD_IDCULTIVO = "id_cultivo";
    public static final String FIELD_IDFINCA = "id_finca";
    public static final String FIELD_IDPARROQUIA = "id_parroquia";
    public static final String FIELD_IDAYUNTAMIENTO = "id_ayuntamiento";
    public static final String FIELD_IDUC = "id_uc";
    public static final String FIELD_IDTRAMO = "id_tramo";

    // table tramos
    public static final String TABLE_TRAMOS = "tramos";
    public static final String FIELD_IDTRAMO_TRAMOS = "id_tramo";
    public static final String FIELD_NOMBRETRAMO_TRAMOS = "nombre_tramo";

    // table uc
    public static final String TABLE_UC = "uc";
    public static final String FIELD_IDUC_UC = "id_uc";
    public static final String FIELD_NOMBREUC_UC = "nombre_uc";
    public static final String FIELD_IDTRAMO_UC = "id_tramo";

    // table ayuntamientos
    public static final String TABLE_AYUNTAMIENTOS = "ayuntamientos";
    public static final String FIELD_IDUC_AYUNTAMIENTO = "id_uc";
    public static final String FIELD_NOMBREAYUNTAMIENTO_AYUNTAMIENTO = "nombre_ayuntamiento";

    // table parroquias_subtramos
    public static final String TABLE_PARROQUIASSUBTRAMOS = "parroquias_subtramos";
    public static final String FIELD_NOMBREPARROQUIA_PARROQUIASUBTRAMOS = "nombre_parroquia";

    // table exp_finca
    public static final String FIELD_IDFINCA_FINCAS = "id_finca";
    public static final String FIELD_NUMEROFINCA_FINCAS = "numero_finca";
    public static final String FIELD_SECCION_FINCAS = "seccion";
    public static final String FIELD_TRAMO_FINCAS = "tramo";
    public static final String FIELD_UC_FINCAS = "unidad_constructiva";
    public static final String FIELD_AYUNTAMIENTO_FINCAS = "ayuntamiento";
    public static final String FIELD_PARROQUIASUBTRAMO_FINCAS = "parroquia_subtramo";
    public static final String FIELD_GID_FINCAS = "gid";

    // table reversiones
    public static final String FIELD_SUPERFICIE_REVERSIONES = "superficie";
    public static final String FIELD_OCUPACION_REVERSIONES = "ocupacion";
    public static final String FIELD_FECHAACTA_REVERSIONES = "fecha_acta_reversion";
    public static final String FIELD_IDREVERSION_REVERSIONES = "exp_id";
    public static final String FIELD_GID_REVERSIONES = "gid";

    // table fincas_reversiones
    public static final String TABLE_REVERSIONES = "exp_reversion";
    public static final String FIELD_IDREVERSION_FINCAS_REVERSIONES = "id_reversion";
    public static final String FIELD_IDEXPROPIACION_FINCAS_REVERSIONES = "id_finca";
    public static final String FIELD_SUPERFICIE_FINCAS_REVERSIONES = "superficie";
    public static final String FIELD_IMPORTE_FINCAS_REVERSIONES = "importe";
    public static final String FIELD_IMPORTE_FINCAS_REVERSIONES_EUROS = "importe_euros";
    public static final String FIELD_IMPORTE_FINCAS_REVERSIONES_PTAS = "importe_ptas";
    public static final String FIELD_FECHA_FINCAS_REVERSIONES = "fecha_acta";
    public static final String FIELD_AYUNTAMIENTO_REVERSIONES = "ayuntamiento_nombre";

    // table fincas_pm
    public static final String TABLE_FINCAS_PM = "fincas_pm";
    public static final String FIELD_IDFINCA_FINCAS_PM = "id_finca";
    public static final String FIELD_NUMPM_FINCAS_PM = "numero_pm";

    // table expropiaciones
    public static final String FIELD_ID_FINCA_EXPROPIACIONES = "id_finca";
    public static final String FIELD_IDCULTIVO_EXPROPIACIONES = "id_cultivo";
    public static final String FIELD_SUPERFICIE_EXPROPIACIONES = "superficie_expropiada";

    // table desafecciones
    public static final String TABLE_DESAFECCIONES = "exp_desafecciones";
    public static final String FIELD_SUPERFICIE_DESAFECCIONES = "superficie";
    public static final String FIELD_OCUPACION_DESAFECCIONES = "ocupacion";
    public static final String FIELD_FECHAACTA_DESAFECCIONES = "fecha_acta_desafeccion";

    // more tables
    public static final String LAYER_FINCAS = "Fincas";
    public static final String TABLE_EXPROPIACIONES = "expropiaciones";
    public static final String TABLE_FINCASREVERSIONES = "finca_reversion";
    public static final String LAYER_MUNICIPIOS = "Municipios";
    public static final String LAYER_PARROQUIAS = "Parroquias";
    public static final String LAYER_PKS = "PKs_AP9";

    // tipo cultivos en DB
    public static final String TABLE_CULTIVOS = "tipo_cultivos";
    public static final String FIELD_ID_CULTIVO_CULTIVOS = "id_cultivo";
    public static final String FIELD_DESCRIPCION_CULTIVOS = "descripcion";
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

    // Other widgets
    public static final String REVERSIONS_ADD_EXPROPIATIONS_BUTTON = "add_expropiations_button";
    public static final String REVERSIONS_DELETE_EXPROPIATIONS_BUTTON = "delete_expropiations_button";
    public static final String SUBFORMREVERSIONS_ADD_EXPROPIATIONS_BUTTON = "add_expropiation_button";
    public static final String SUBFORMSREVERSIONS_IDFINCA = "id_finca";
    public static final String SUBFORMREVERSIONS_SUPERFICIE = "superficie";
    public static final String SUBFORMREVERSIONS_IMPORTE = "importe";

    public static final String EXPROPIATIONS_ADD_REVERSIONS_BUTTON = "add_reversions_button";
    public static final String EXPROPIATIONS_DELETE_REVERSIONS_BUTTON = "delete_reversions_button";
    public static final String SUBFORMEXPROPIATIONS_ADD_REVERSIONS_BUTTON = "add_reversion_button";
    public static final String SUBFORMSEXPROPIATIONS_IDREVERSIONS = "id_reversion";
    public static final String SUBFORMEXPROPIATIONS_SUPERFICIE = "superficie";
    public static final String SUBFORMEXPROPIATIONS_IMPORTE = "importe";
    public static final String SUBFORMEXPROPIATIONS_FECHA = "fecha_acta";

    public static final String EXPROPIATIONS_AFECTADO_PM = "afectado_por_policia_margenes";

    public static final String EXPROPIATIONS_ADD_EXPROPIATION_BUTTON = "add_expropiation_button";
    public static final String EXPROPIATIONS_DELETE_EXPROPIATION_BUTTON = "delete_expropiation_button";

    public static final String SUBFORMEXPROPIATION_ADD_EXPROPIATION_BUTTON = "add_expropiation_subform_button";
    public static final String SUBFORMEXPROPIATION_SUPERFICIE = "expropiacion_superficie";
    public static final String SUBFORMEXPROPIATION_CULTIVO = "expropiacion_cultivo";

    public static final String FINCAS_IMPORTE_PENDIENTE_MEJORAS = "importe_pendiente_mejoras";
    public static final String FINCAS_IMPORTE_PENDIENTE_TERRENOS = "importe_pendiente_terrenos";
    public static final String FINCAS_IMPORTE_PENDIENTE_TOTAL_AUTOCALCULADO = "importe_pendiente_total_autocalculado";

    public static final String FINCAS_MUTUO_ACUERDO = "mutuo_acuerdo_importe";
    public static final String FINCAS_ANTICIPO = "anticipo_importe";
    public static final String FINCAS_DEPOSITO_PREVIO_PAGADO = "deposito_previo_pagado_importe";
    public static final String FINCAS_DEPOSITO_PREVIO_CONSIGNADO = "deposito_previo_consignado_importe";
    public static final String FINCAS_MUTUO_ACUERDO_PARCIAL = "mutuo_acuerdo_parcial_importe";
    public static final String FINCAS_PAGOS_VARIOS = "pagos_varios_importe";
    public static final String FINCAS_DEPOSITO_PREVIO_LEVANTADO = "deposito_previo_levantado_importe";
    public static final String FINCAS_IMPORTE_PAGADO_TOTAL_AUTOCALCULADO = "importe_pagado_total_autocalculado";
    public static final String FINCAS_DEPOSITO_PREVIO_CONSIGNADO_INDEMNIZACION = "deposito_previo_consignado_indemnizacion";
    public static final String FINCAS_LIMITE_ACUERDO_IMORTE = "limite_acuerdo_importe";
    public static final String FINCAS_INDEMNIZACION_IMPORTE = "indemnizacion_importe";
    public static final String FINCAS_JUSTIPRECIO_IMPORTE = "justiprecio_importe";

}
