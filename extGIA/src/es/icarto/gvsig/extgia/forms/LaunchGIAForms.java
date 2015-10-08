package es.icarto.gvsig.extgia.forms;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.batch.BatchAbstractSubForm;
import es.icarto.gvsig.extgia.batch.BatchVegetationTrabajosAbstractSubForm;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasDescansoReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasDescansoTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasPeajeReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasPeajeTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasServicioReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasServicioTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchBarreraRigidaReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchBarreraRigidaTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchEnlacesReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchFirmeReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchFirmeTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchIsletasReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchIsletasTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchJuntasReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchJuntasTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchLechoFrenadoReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchLechoFrenadoTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchLineasSuministroReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchLineasSuministroTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchMurosReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchMurosTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchObrasDesagueTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchObrasPasoTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchPasosMedianaReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchPasosMedianaTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVariableReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVariableTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVerticalReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVerticalTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchTaludesReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchTaludesTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchTransformadoresReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchTransformadoresTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchVallaCierreReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchVallaCierreTrabajos;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.forms.areas_mantenimiento.AreasMantenimientoForm;
import es.icarto.gvsig.extgia.forms.areas_peaje.AreasPeajeForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.forms.firme.FirmeForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.forms.lecho_frenado.LechoFrenadoForm;
import es.icarto.gvsig.extgia.forms.lineas_suministro.LineasSuministroForm;
import es.icarto.gvsig.extgia.forms.muros.MurosForm;
import es.icarto.gvsig.extgia.forms.obras_desague.ObrasDesagueForm;
import es.icarto.gvsig.extgia.forms.obras_paso.ObrasPasoForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_variable.SenhalizacionVariableForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.forms.transformadores.TransformadoresForm;
import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.gui.tables.handler.BaseTableHandler;
import es.icarto.gvsig.navtableforms.gui.tables.model.AlphanumericTableModel;
import es.icarto.gvsig.navtableforms.gui.tables.model.TableModelFactory;
import es.icarto.gvsig.navtableforms.utils.FormFactory;

public class LaunchGIAForms {

    public static boolean callFormDependingOfLayer(FLyrVect layer,
	    boolean editing) {

	final String layerName = layer.getName();
	if (!isGIALayerName(layerName)) {
	    return false;
	}
	AbstractFormWithLocationWidgets form = null;

	switch (DBFieldNames.Elements.valueOf(layerName)) {
	case Areas_Descanso:
	    form = new AreasDescansoForm(layer);
	    break;
	case Areas_Mantenimiento:
	    form = new AreasMantenimientoForm(layer);
	    break;
	case Areas_Peaje:
	    form = new AreasPeajeForm(layer);
	    break;
	case Areas_Servicio:
	    form = new AreasServicioForm(layer);
	    break;
	case Barrera_Rigida:
	    form = new BarreraRigidaForm(layer);
	    break;
	case Enlaces:
	    form = new EnlacesForm(layer);
	    break;
	case Firme:
	    form = new FirmeForm(layer);
	    break;
	case Isletas:
	    form = new IsletasForm(layer);
	    break;
	case Juntas:
	    form = new JuntasForm(layer);
	    break;
	case Lecho_Frenado:
	    form = new LechoFrenadoForm(layer);
	    break;
	case Lineas_Suministro:
	    form = new LineasSuministroForm(layer);
	    break;
	case Muros:
	    form = new MurosForm(layer);
	    break;
	case Obras_Desague:
	    form = new ObrasDesagueForm(layer);
	    break;
	case Obras_Paso:
	    form = new ObrasPasoForm(layer);
	    break;
	case Pasos_Mediana:
	    form = new PasosMedianaForm(layer);
	    break;
	case Senhalizacion_Variable:
	    form = new SenhalizacionVariableForm(layer);
	    break;
	case Senhalizacion_Vertical:
	    form = new SenhalizacionVerticalForm(layer);
	    break;
	case Taludes:
	    form = new TaludesForm(layer);
	    break;
	case Transformadores:
	    form = new TransformadoresForm(layer);
	    break;
	case Valla_Cierre:
	    form = new VallaCierreForm(layer);
	    break;
	default:
	    return false;
	}

	if (form != null) {
	    if (form.init()) {
		PluginServices.getMDIManager().addWindow(form);
	    }
	    if (editing) {
		form.last();
	    }
	}
	return true;
    }

    private static boolean isGIALayerName(String layerName) {
	boolean isGIALayerName = false;
	for (int i = 0; i < DBFieldNames.Elements.values().length; i++) {
	    if (DBFieldNames.Elements.values()[i].toString().equals(layerName)) {
		isGIALayerName = true;
	    }
	}
	return isGIALayerName;
    }

    public static void callBatchTrabajosSubFormDependingOfElement(
	    String element, String formFileName, String dbTableName,
	    BaseTableHandler trabajosTableHandler) {
	BatchAbstractSubForm subform = null;
	BatchVegetationTrabajosAbstractSubForm vegetationSubForm = null;
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Descanso:
	    subform = new BatchAreasDescansoTrabajos(formFileName, dbTableName);
	    break;
	case Areas_Peaje:
	    subform = new BatchAreasPeajeTrabajos(formFileName, dbTableName);
	    break;
	case Barrera_Rigida:
	    vegetationSubForm = new BatchBarreraRigidaTrabajos(formFileName,
		    dbTableName);
	    break;

	case Areas_Servicio:
	    subform = new BatchAreasServicioTrabajos(formFileName, dbTableName);
	    break;
	case Firme:
	    subform = new BatchFirmeTrabajos(formFileName, dbTableName);
	    break;
	case Isletas:
	    vegetationSubForm = new BatchIsletasTrabajos(formFileName,
		    dbTableName);
	    break;
	case Juntas:
	    subform = new BatchJuntasTrabajos(formFileName, dbTableName);
	    break;
	case Lecho_Frenado:
	    subform = new BatchLechoFrenadoTrabajos(formFileName, dbTableName);
	    break;

	case Lineas_Suministro:
	    subform = new BatchLineasSuministroTrabajos(formFileName,
		    dbTableName);
	    break;
	case Muros:
	    subform = new BatchMurosTrabajos(formFileName, dbTableName);
	    break;
	case Obras_Desague:
	    subform = new BatchObrasDesagueTrabajos(formFileName, dbTableName);
	    break;
	case Obras_Paso:
	    subform = new BatchObrasPasoTrabajos(formFileName, dbTableName);
	    break;
	case Pasos_Mediana:
	    subform = new BatchPasosMedianaTrabajos(formFileName, dbTableName);
	    break;
	case Senhalizacion_Variable:
	    subform = new BatchSenhalizacionVariableTrabajos(formFileName,
		    dbTableName);
	    break;
	case Senhalizacion_Vertical:
	    vegetationSubForm = new BatchSenhalizacionVerticalTrabajos(
		    formFileName, dbTableName);
	    break;
	case Taludes:
	    vegetationSubForm = new BatchTaludesTrabajos(formFileName,
		    dbTableName);
	    break;
	case Transformadores:
	    subform = new BatchTransformadoresTrabajos(formFileName,
		    dbTableName);
	    break;
	case Valla_Cierre:
	    subform = new BatchVallaCierreTrabajos(formFileName, dbTableName);
	    break;

	default:
	    subform = null;
	}

	FormFactory.checkAndLoadTableRegistered(dbTableName);
	AlphanumericTableModel model = TableModelFactory.createFromTable(
		dbTableName, null, null);
	if (subform != null) {
	    subform.setModel(model);
	    subform.actionCreateRecord();
	} else {
	    vegetationSubForm.setModel(model);
	    vegetationSubForm.setTrabajoTableHandler(trabajosTableHandler);
	    vegetationSubForm.actionCreateRecord();
	}
    }

    public static void callBatchReconocimientosSubFormDependingOfElement(
	    String element, String formFileName, String dbTableName) {
	BatchAbstractSubForm subform;
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Areas_Peaje:
	    subform = new BatchAreasPeajeReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Areas_Descanso:
	    subform = new BatchAreasDescansoReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Areas_Servicio:
	    subform = new BatchAreasServicioReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Barrera_Rigida:
	    subform = new BatchBarreraRigidaReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Enlaces:
	    subform = new BatchEnlacesReconocimientos(formFileName, dbTableName);
	    break;
	case Firme:
	    subform = new BatchFirmeReconocimientos(formFileName, dbTableName);
	    break;
	case Isletas:
	    subform = new BatchIsletasReconocimientos(formFileName, dbTableName);
	    break;
	case Juntas:
	    subform = new BatchJuntasReconocimientos(formFileName, dbTableName);
	    break;
	case Lecho_Frenado:
	    subform = new BatchLechoFrenadoReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Lineas_Suministro:
	    subform = new BatchLineasSuministroReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Muros:
	    subform = new BatchMurosReconocimientos(formFileName, dbTableName);
	    break;
	case Pasos_Mediana:
	    subform = new BatchPasosMedianaReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Senhalizacion_Variable:
	    subform = new BatchSenhalizacionVariableReconocimientos(
		    formFileName, dbTableName);
	    break;
	case Senhalizacion_Vertical:
	    subform = new BatchSenhalizacionVerticalReconocimientos(
		    formFileName, dbTableName);
	    break;
	case Taludes:
	    subform = new BatchTaludesReconocimientos(formFileName, dbTableName);
	    break;
	case Transformadores:
	    subform = new BatchTransformadoresReconocimientos(formFileName,
		    dbTableName);
	    break;
	case Valla_Cierre:
	    subform = new BatchVallaCierreReconocimientos(formFileName,
		    dbTableName);
	    break;
	default:
	    subform = null;
	}
	FormFactory.checkAndLoadTableRegistered(dbTableName);
	AlphanumericTableModel model = TableModelFactory.createFromTable(
		dbTableName, null, null);
	subform.setModel(model);
	subform.actionCreateRecord();
    }
}
