package es.icarto.gvsig.extgia.forms.utils;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgia.forms.areas_descanso.AreasDescansoForm;
import es.icarto.gvsig.extgia.forms.areas_servicio.AreasServicioForm;
import es.icarto.gvsig.extgia.forms.barrera_rigida.BarreraRigidaForm;
import es.icarto.gvsig.extgia.forms.enlaces.EnlacesForm;
import es.icarto.gvsig.extgia.forms.isletas.IsletasForm;
import es.icarto.gvsig.extgia.forms.juntas.JuntasForm;
import es.icarto.gvsig.extgia.forms.pasos_mediana.PasosMedianaForm;
import es.icarto.gvsig.extgia.forms.senhalizacion_vertical.SenhalizacionVerticalForm;
import es.icarto.gvsig.extgia.forms.taludes.TaludesForm;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;

public class LaunchGIAForms {

    public enum Elements {
	Taludes, Isletas, Enlaces, Barrera_Rigida, Areas_Servicio, Areas_Descanso,
	Juntas, Pasos_Mediana, Senhalizacion_Vertical;
    }

    public static void callFormDependingOfLayer(String layerName, boolean editing) {
	final TOCLayerManager toc = new TOCLayerManager();
	FLyrVect layer = toc.getLayerByName(layerName);

	switch (Elements.valueOf(layerName)) {
	case Taludes:
	    final TaludesForm taludesForm = new TaludesForm(layer);
	    if (taludesForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(taludesForm);
	    }
	    if (editing) {
		taludesForm.last();
	    }
	    break;
	case Isletas:
	    final IsletasForm isletasForm = new IsletasForm(layer);
	    if (isletasForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(isletasForm);
	    }
	    if (editing) {
		isletasForm.last();
	    }
	    break;
	case Enlaces:
	    final EnlacesForm enlacesForm = new EnlacesForm(layer);
	    if (enlacesForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(enlacesForm);
	    }
	    if (editing) {
		enlacesForm.last();
	    }
	    break;
	case Barrera_Rigida:
	    final BarreraRigidaForm barreraRigidaForm = new BarreraRigidaForm(layer);
	    if (barreraRigidaForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(barreraRigidaForm);
	    }
	    if (editing) {
		barreraRigidaForm.last();
	    }
	    break;
	case Areas_Servicio:
	    final AreasServicioForm areasServicioForm = new AreasServicioForm(layer);
	    if (areasServicioForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(areasServicioForm);
	    }
	    if (editing) {
		areasServicioForm.last();
	    }
	    break;
	case Areas_Descanso:
	    final AreasDescansoForm areasDescansoForm = new AreasDescansoForm(layer);
	    if (areasDescansoForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(areasDescansoForm);
	    }
	    if (editing) {
		areasDescansoForm.last();
	    }
	    break;
	case Juntas:
	    final JuntasForm juntasForm = new JuntasForm(layer);
	    if (juntasForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(juntasForm);
	    }
	    if (editing) {
		juntasForm.last();
	    }
	    break;
	case Pasos_Mediana:
	    final PasosMedianaForm pasosMedianaForm = new PasosMedianaForm(layer);
	    if (pasosMedianaForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(pasosMedianaForm);
	    }
	    if (editing) {
		pasosMedianaForm.last();
	    }
	    break;
	case Senhalizacion_Vertical:
	    final SenhalizacionVerticalForm senhalizacionVerticalForm = new SenhalizacionVerticalForm(layer);
	    if (senhalizacionVerticalForm.init()) {
		PluginServices.getMDIManager().addCentredWindow(senhalizacionVerticalForm);
	    }
	    if (editing) {
		senhalizacionVerticalForm.last();
	    }
	    break;
	}
    }
}
