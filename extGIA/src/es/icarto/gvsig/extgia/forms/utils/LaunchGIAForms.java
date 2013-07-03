package es.icarto.gvsig.extgia.forms.utils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.batch.elements.BatchAreasDescansoReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchAreasDescansoTrabajos;
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
import es.icarto.gvsig.extgia.batch.elements.BatchPasosMedianaReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchPasosMedianaTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVerticalReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchSenhalizacionVerticalTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchTaludesReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchTaludesTrabajos;
import es.icarto.gvsig.extgia.batch.elements.BatchVallaCierreReconocimientos;
import es.icarto.gvsig.extgia.batch.elements.BatchVallaCierreTrabajos;
import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.forms.firme.FirmeForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.extgia.forms.valla_cierre.VallaCierreForm;
import es.icarto.gvsig.extgia.preferences.DBFieldNames;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class LaunchGIAForms {



    public static void callFormDependingOfLayer(String layerName, boolean editing) {
	final TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);

	switch (DBFieldNames.Elements.valueOf(layerName)) {
	case Taludes:
	    final TaludesForm taludesForm = new TaludesForm(layer);
	    if (taludesForm.init()) {
		PluginServices.getMDIManager().addWindow(taludesForm);
	    }
	    if (editing) {
		taludesForm.last();
	    }
	    break;
	case Isletas:
	    final IsletasForm isletasForm = new IsletasForm(layer);
	    if (isletasForm.init()) {
		PluginServices.getMDIManager().addWindow(isletasForm);
	    }
	    if (editing) {
		isletasForm.last();
	    }
	    break;
	case Enlaces:
	    final EnlacesForm enlacesForm = new EnlacesForm(layer);
	    if (enlacesForm.init()) {
		PluginServices.getMDIManager().addWindow(enlacesForm);
	    }
	    if (editing) {
		enlacesForm.last();
	    }
	    break;
	case Barrera_Rigida:
	    final BarreraRigidaForm barreraRigidaForm = new BarreraRigidaForm(layer);
	    if (barreraRigidaForm.init()) {
		PluginServices.getMDIManager().addWindow(barreraRigidaForm);
	    }
	    if (editing) {
		barreraRigidaForm.last();
	    }
	    break;
	case Areas_Servicio:
	    final AreasServicioForm areasServicioForm = new AreasServicioForm(layer);
	    if (areasServicioForm.init()) {
		PluginServices.getMDIManager().addWindow(areasServicioForm);
	    }
	    if (editing) {
		areasServicioForm.last();
	    }
	    break;
	case Areas_Descanso:
	    final AreasDescansoForm areasDescansoForm = new AreasDescansoForm(layer);
	    if (areasDescansoForm.init()) {
		PluginServices.getMDIManager().addWindow(areasDescansoForm);
	    }
	    if (editing) {
		areasDescansoForm.last();
	    }
	    break;
	case Juntas:
	    final JuntasForm juntasForm = new JuntasForm(layer);
	    if (juntasForm.init()) {
		PluginServices.getMDIManager().addWindow(juntasForm);
	    }
	    if (editing) {
		juntasForm.last();
	    }
	    break;
	case Pasos_Mediana:
	    final PasosMedianaForm pasosMedianaForm = new PasosMedianaForm(layer);
	    if (pasosMedianaForm.init()) {
		PluginServices.getMDIManager().addWindow(pasosMedianaForm);
	    }
	    if (editing) {
		pasosMedianaForm.last();
	    }
	    break;
	case Senhalizacion_Vertical:
	    final SenhalizacionVerticalForm senhalizacionVerticalForm = new SenhalizacionVerticalForm(layer);
	    if (senhalizacionVerticalForm.init()) {
		PluginServices.getMDIManager().addWindow(senhalizacionVerticalForm);
	    }
	    if (editing) {
		senhalizacionVerticalForm.last();
	    }
	    break;
	case Valla_Cierre:
	    final VallaCierreForm vallaCierreForm = new VallaCierreForm(layer);
	    if (vallaCierreForm.init()) {
		PluginServices.getMDIManager().addWindow(vallaCierreForm);
	    }
	    if (editing) {
		vallaCierreForm.last();
	    }
	    break;
	case Firme:
	    final FirmeForm firmeForm = new FirmeForm(layer);
	    if (firmeForm.init()) {
		PluginServices.getMDIManager().addWindow(firmeForm);
	    }
	    if (editing) {
		firmeForm.last();
	    }
	    break;
	}
    }

    public static void callBatchTrabajosSubFormDependingOfElement(String element,
	    String formFileName, String dbTableName) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    final BatchTaludesTrabajos taludesSubForm = new BatchTaludesTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(taludesSubForm);
	    break;
	case Isletas:
	    final BatchIsletasTrabajos isletasSubForm = new BatchIsletasTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(isletasSubForm);
	    break;
	case Barrera_Rigida:
	    final BatchBarreraRigidaTrabajos barreraRigidaSubForm = new BatchBarreraRigidaTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(barreraRigidaSubForm);
	    break;
	case Areas_Servicio:
	    final BatchAreasServicioTrabajos areasServicioSubForm = new BatchAreasServicioTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(areasServicioSubForm);
	    break;
	case Areas_Descanso:
	    final BatchAreasDescansoTrabajos areasDescansoSubForm = new BatchAreasDescansoTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(areasDescansoSubForm);
	    break;
	case Juntas:
	    final BatchJuntasTrabajos juntasSubForm = new BatchJuntasTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(juntasSubForm);
	    break;
	case Pasos_Mediana:
	    final BatchPasosMedianaTrabajos pasosMedianaSubForm = new BatchPasosMedianaTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(pasosMedianaSubForm);
	    break;
	case Senhalizacion_Vertical:
	    final BatchSenhalizacionVerticalTrabajos senhalizacionVerticalSubForm = new BatchSenhalizacionVerticalTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(senhalizacionVerticalSubForm);
	    break;
	case Valla_Cierre:
	    final BatchVallaCierreTrabajos vallaCierreSubForm = new BatchVallaCierreTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(vallaCierreSubForm);
	    break;
	case Firme:
	    final BatchFirmeTrabajos firmeSubForm = new BatchFirmeTrabajos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(firmeSubForm);
	    break;
	}
    }

    public static void callBatchReconocimientosSubFormDependingOfElement(String element,
	    String formFileName, String dbTableName) {
	switch (DBFieldNames.Elements.valueOf(element)) {
	case Taludes:
	    final BatchTaludesReconocimientos taludesSubForm = new BatchTaludesReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(taludesSubForm);
	    break;
	case Isletas:
	    final BatchIsletasReconocimientos isletasSubForm = new BatchIsletasReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(isletasSubForm);
	    break;
	case Barrera_Rigida:
	    final BatchBarreraRigidaReconocimientos barreraRigidaSubForm = new BatchBarreraRigidaReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(barreraRigidaSubForm);
	    break;
	case Areas_Servicio:
	    final BatchAreasServicioReconocimientos areasServicioSubForm = new BatchAreasServicioReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(areasServicioSubForm);
	    break;
	case Areas_Descanso:
	    final BatchAreasDescansoReconocimientos areasDescansoSubForm = new BatchAreasDescansoReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(areasDescansoSubForm);
	    break;
	case Enlaces:
	    final BatchEnlacesReconocimientos enlacesSubForm = new BatchEnlacesReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(enlacesSubForm);
	    break;
	case Juntas:
	    final BatchJuntasReconocimientos juntasSubForm = new BatchJuntasReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(juntasSubForm);
	    break;
	case Pasos_Mediana:
	    final BatchPasosMedianaReconocimientos pasosMedianaSubForm = new BatchPasosMedianaReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(pasosMedianaSubForm);
	    break;
	case Senhalizacion_Vertical:
	    final BatchSenhalizacionVerticalReconocimientos senhalizacionVerticalSubForm = new BatchSenhalizacionVerticalReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(senhalizacionVerticalSubForm);
	    break;
	case Valla_Cierre:
	    final BatchVallaCierreReconocimientos vallaCierreSubForm = new BatchVallaCierreReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(vallaCierreSubForm);
	    break;
	case Firme:
	    final BatchFirmeReconocimientos firmeSubForm = new BatchFirmeReconocimientos(formFileName,
		    dbTableName);
	    PluginServices.getMDIManager().addWindow(firmeSubForm);
	    break;
	}
    }
}
