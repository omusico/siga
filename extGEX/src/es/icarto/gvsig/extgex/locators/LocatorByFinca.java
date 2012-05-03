package es.icarto.gvsig.extgex.locators;

import java.awt.BorderLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.locators.actions.FormOpener;
import es.icarto.gvsig.extgex.locators.actions.IPositionRetriever;
import es.icarto.gvsig.extgex.locators.actions.ZoomToHandler;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.preferences.PreferencesPage;
import es.icarto.gvsig.extgex.utils.ComboBoxValuesHandler;
import es.icarto.gvsig.extgex.utils.gvWindow;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.retrievers.FincaSeccionRetriever;
import es.icarto.gvsig.extgex.utils.retrievers.IDFincaRetriever;
import es.icarto.gvsig.extgex.utils.retrievers.LocalizadorFormatter;
import es.icarto.gvsig.extgex.utils.retrievers.PositionRetriever;
import es.icarto.gvsig.navtableforms.ormlite.ORMLite;
import es.icarto.gvsig.navtableforms.ormlite.domain.DomainValues;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

@SuppressWarnings("serial")
public class LocatorByFinca extends gvWindow implements IPositionRetriever, ItemListener {

    private final FormPanel formBody;

    private String tramoKey;
    private String ucKey;
    private String ayuntamientoKey;
    private String parroquiaSubtramoKey;

    private JComboBox tramo;
    private JComboBox uc;
    private JComboBox ayuntamiento;
    private JComboBox parroquiaSubtramo;
    private JComboBox fincaSeccion;
    private JButton zoom;
    private JButton openForm;

    private ComboBoxValuesHandler ucValuesUpdater;
    private ComboBoxValuesHandler ayuntamientoValuesHandler;
    private ComboBoxValuesHandler subtramoValuesHandler;
    private ZoomToHandler zoomToHandler;
    private FormOpener formOpener;

    public LocatorByFinca() {
	super(400, 330);
	formBody = new FormPanel("LocatorByFinca.xml");
	formBody.setVisible(true);
	this.add(formBody, BorderLayout.CENTER);
	this.setTitle("Localizador por Finca");
    }

    public boolean init() {
	tramo = (JComboBox) formBody.getComponentByName("tramo");
	uc = (JComboBox) formBody.getComponentByName("unidad_constructiva");
	ayuntamiento = (JComboBox) formBody.getComponentByName("ayuntamiento");
	parroquiaSubtramo = (JComboBox) formBody.getComponentByName("parroquia_subtramo");
	fincaSeccion = (JComboBox) formBody.getComponentByName("finca_seccion");
	openForm = (JButton) formBody.getComponentByName("openform");
	zoom = (JButton) formBody.getComponentByName("zoom");

	ucValuesUpdater = new ComboBoxValuesHandler(
		getXMLFile(), uc, tramo);
	tramo.addItemListener(ucValuesUpdater);

	ayuntamientoValuesHandler = new ComboBoxValuesHandler(
		getXMLFile(), ayuntamiento, uc);
	uc.addItemListener(ayuntamientoValuesHandler);

	ArrayList<JComboBox> subtramoDependences = new ArrayList<JComboBox>();
	subtramoDependences.add(uc);
	subtramoDependences.add(ayuntamiento);
	subtramoValuesHandler = new ComboBoxValuesHandler(
		getXMLFile(), parroquiaSubtramo, subtramoDependences);
	ayuntamiento.addItemListener(subtramoValuesHandler);
	parroquiaSubtramo.addItemListener(this);

	zoomToHandler = new ZoomToHandler(this);
	zoom.addActionListener(zoomToHandler);

	formOpener = new FormOpener(this);
	openForm.addActionListener(formOpener);

	//init filling of cascaded combobox
	fillTramo();

	TOCLayerManager toc = new TOCLayerManager();
	if(toc.getLayerByName(DBNames.LAYER_FINCAS) != null) {
	    return true;
	}
	return false;
    }

    private void fillTramo() {
	DomainValues dv = ORMLite.getAplicationDomainObject(getXMLFile())
		.getDomainValuesForComponent("tramo");
	tramo.removeAllItems();
	Collections.sort((List<KeyValue>) dv.getValues(),
		new Comparator<KeyValue>() {
		    public int compare(KeyValue a, KeyValue b) {
			int aInt = Integer.parseInt(a.getKey());
			int bInt = Integer.parseInt(b.getKey());
			return (aInt >= bInt) ? 1 : -1;
		    }
		});
	for (KeyValue kv : dv.getValues()) {
	    tramo.addItem(kv);
	}
    }

    private String getXMLFile() {
	return PluginServices.getPluginServices("es.icarto.gvsig.extgex")
		.getClassLoader()
		.getResource(PreferencesPage.XML_ORMLITE_RELATIVE_PATH).getPath();
    }

    @Override
    public int getPosition() {
	IDFincaRetriever idFincaRetriever = new IDFincaRetriever(
		tramo,
		uc,
		ayuntamiento,
		parroquiaSubtramo,
		fincaSeccion);
	PositionRetriever positionRetriever = new PositionRetriever(
		getLayer(),
		DBNames.FIELD_IDFINCA,
		idFincaRetriever.getIDFinca());
	return positionRetriever.getPosition();
    }

    @Override
    public FLyrVect getLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(DBNames.LAYER_FINCAS);
    }

    private void updateFincaSeccionComboBox() {
	ArrayList<String> values = getValuesForFincaSeccion();
	if (values != null) {
	    fincaSeccion.removeAllItems();
	    fincaSeccion.setEnabled(true);
	    zoom.setEnabled(true);
	    openForm.setEnabled(true);
	    for (String fincaPlusSeccion : values) {
		fincaSeccion.addItem(fincaPlusSeccion);
	    }
	} else {
	    fincaSeccion.setEnabled(false);
	    zoom.setEnabled(false);
	    openForm.setEnabled(false);
	}
    }

    private ArrayList<String> getValuesForFincaSeccion() {
	FincaSeccionRetriever fincaSeccion = new FincaSeccionRetriever(
		getXMLFile(),
		tramoKey,
		ucKey,
		ayuntamientoKey,
		parroquiaSubtramoKey);
	return fincaSeccion.getValues();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
	if((e.getStateChange() == ItemEvent.SELECTED) &&
		parentsHaveProperValues()) {
	    updateFincaSeccionComboBox();
	}
    }

    private boolean parentsHaveProperValues() {
	parroquiaSubtramoKey = "0";
	if(parroquiaSubtramo.getSelectedItem() instanceof KeyValue) {
	    parroquiaSubtramoKey = LocalizadorFormatter.getSubtramo(
		    ((KeyValue) parroquiaSubtramo.getSelectedItem()).getKey());
	}
	ayuntamientoKey = "";
	if(ayuntamiento.getSelectedItem() instanceof KeyValue){
	    ayuntamientoKey = LocalizadorFormatter.getAyuntamiento(
		    ((KeyValue) ayuntamiento.getSelectedItem()).getKey());
	}
	ucKey = "";
	if(uc.getSelectedItem() instanceof KeyValue){
	    ucKey = LocalizadorFormatter.getUC(
		    ((KeyValue) uc.getSelectedItem()).getKey());
	}
	tramoKey = "";
	if(tramo.getSelectedItem() instanceof KeyValue){
	    tramoKey = LocalizadorFormatter.getTramo(
		    ((KeyValue) tramo.getSelectedItem()).getKey());
	}
	if((ayuntamientoKey != "")
		&& (ucKey != "")
		&& (tramoKey != "")) {
	    return true;
	}
	return true;
    }

    @Override
    public void close() {
	tramo.removeItemListener(ucValuesUpdater);
	uc.removeItemListener(ayuntamientoValuesHandler);
	ayuntamiento.removeItemListener(subtramoValuesHandler);
	parroquiaSubtramo.removeItemListener(this);
	zoom.removeActionListener(zoomToHandler);
	openForm.removeActionListener(formOpener);
	this.close();
    }

}