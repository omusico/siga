package org.gvsig.mapsheets.print.audasa;

import java.io.File;

import org.gvsig.tools.file.PathGenerator;

import com.iver.andami.PluginServices;

public class AudasaPreferences {

    public static final String TITULO_VENTANA = "Crear mapa";

    // Templates
    public static final String A3_CONSULTAS = 
	    "Expediente consultas (A3)";
    public static final String A3_CONSULTAS_LOCALIZADOR = 
	    "Expediente consultas modificado (A3)";
    public static final String A4_CONSULTAS = 
	    "Expediente consultas (A4)";
    public static final String A4_CONSULTAS_LOCALIZADOR = 
	    "Expediente consultas modificado (A4)";
    public static final String A3_DIMENSIONES = 
	    "Estado dimensiones (A3)";
    public static final String A3_DIMENSIONES_LOCALIZADOR = 
	    "Estado dimensiones modificado (A3)";
    public static final String PERSONALIZADA =
        "Otra plantilla:";

    // Variables for templates
    public static final String INGENIERO_DIRECTOR = "ingeniero_director";
    public static final String PROVINCIA = "provincia";
    public static final String MUNICIPIO = "municipio";
    public static final String PROYECTO = "proyecto";
    public static final String DIBUJO = "dibujo";
    public static final String CONSULTA = "consulta";
    public static final String FECHA = "fecha";
    public static final String TITULO_ESTUDIO = "titulo_estudio";
    public static final String TITULO_PLANO = "titulo_plano";
    public static final String TRAMO = "tramo";
    public static final String NUMERO_PLANO = "numero_plano";
    public static final String CONSULTORA = "consultora";
    public static final String INGENIERO_AUTOR = "ingeniero_autor";
    public static final String CLAVE = "clave";
    public static final String SUSTITUYE_A = "sustituye_a";
    public static final String SUSTITUIDO_POR = "sustituido_por";

    //units (cm) for inserting the view in the map
    public static final double VIEW_X_POSITION = 1.5;
    public static final double VIEW_Y_POSITION = 1;
    public static final double VIEW_WIDTH_A4 = 27.2;
    public static final double VIEW_HEIGHT_A4 = 17.8;
    public static final double VIEW_WIDTH_A3 = 39.5;
    public static final double VIEW_HEIGHT_A3 = 26.5;

    public static final double OVERVIEW_X = 35.7;
    public static final double OVERVIEW_Y = 1.3;
    public static final double OVERVIEW_WIDTH = 5;
    public static final double OVERVIEW_HEIGHT = 5;

    public static File getSelectedFile(String sourceButton) {
	PathGenerator.getInstance().setBasePath(PluginServices
		.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
		.getResource("plantillas/").toString());
	if(sourceButton.equals(A4_CONSULTAS_LOCALIZADOR)) {
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A4_Expedientes_Consultas_Modificado.gvt").getFile());
	} else if(sourceButton.equals(A4_CONSULTAS)){
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A4_Expedientes_Consultas.gvt").getFile());
	} else if(sourceButton.equals(A3_CONSULTAS_LOCALIZADOR)) {
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A3_Expedientes_Consultas_Modificado.gvt").getFile());
	} else if(sourceButton.equals(A3_CONSULTAS)) {
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A3_Expedientes_Consultas.gvt").getFile());
	} else if(sourceButton.equals(A3_DIMENSIONES_LOCALIZADOR)) {
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A3_Estado_Dimensiones_Modificado.gvt").getFile());
	} else {
	    return new File(PluginServices
			.getPluginServices("es.icarto.gvsig.extgex").getClassLoader()
			.getResource("plantillas/Informes_A3_Estado_Dimensiones.gvt").getFile());
	}
    }

}
