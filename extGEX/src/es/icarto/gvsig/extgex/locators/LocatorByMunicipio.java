package es.icarto.gvsig.extgex.locators;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.PreferencesPage;
import es.icarto.gvsig.commons.gui.gvWindow;
import es.icarto.gvsig.extgex.locators.actions.IPositionRetriever;
import es.icarto.gvsig.extgex.locators.actions.ZoomToHandler;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.retrievers.KeyValueRetriever;
import es.icarto.gvsig.extgex.utils.retrievers.PositionRetriever;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class LocatorByMunicipio extends gvWindow implements IPositionRetriever, ActionListener {

    private final FormPanel formBody;
    private JComboBox ayuntamiento;
    private JComboBox parroquia;
    private JButton zoom;

    public LocatorByMunicipio() {
	super(400, 145);
	InputStream stream = getClass().getClassLoader().getResourceAsStream("LocatorByMunicipio.xml");
	FormPanel result = null;
	try {
	    result = new FormPanel(stream);
	} catch (FormException e) {
	    e.printStackTrace();
	}
	formBody = result;
	formBody.setVisible(true);
	this.add(formBody, BorderLayout.CENTER);
	this.setTitle("Localizador por Municipio o Parroquia");
    }

    public void initWidgets() {

	ImageComponent image = (ImageComponent) formBody.getComponentByName("image");
	ImageIcon icon = new ImageIcon (PreferencesPage.AUDASA_ICON);
	image.setIcon(icon);

	ayuntamiento = (JComboBox) formBody.getComponentByName("ayuntamiento");
	parroquia = (JComboBox) formBody.getComponentByName("parroquia");
	fillAyuntamiento();
	ayuntamiento.addActionListener(this);
	fillParroquia();
	zoom = (JButton) formBody.getComponentByName("zoom");
	ZoomToHandler zoomToHandler = new ZoomToHandler(this);
	zoom.addActionListener(zoomToHandler);
    }

    private void fillParroquia() {
	ArrayList<KeyValue> fks = new ArrayList<KeyValue>();
	KeyValueRetriever kvParroquia;
	if(ayuntamiento.getSelectedItem() instanceof KeyValue) {
	    KeyValue concello = new KeyValue(DBNames.COD_CONCELLO,
		    ((KeyValue) ayuntamiento.getSelectedItem()).getKey());
	    fks.add(concello);
	    kvParroquia = new KeyValueRetriever(
		    getParroquiaLayer(),
		    DBNames.COD_PARROQUIA,
		    DBNames.NOME_PARROQUIA,
		    fks);
	    kvParroquia.setOrderBy(DBNames.NOME_PARROQUIA);
	} else {
	    kvParroquia = new KeyValueRetriever(
		    getParroquiaLayer(),
		    DBNames.COD_PARROQUIA,
		    DBNames.NOME_PARROQUIA);
	    kvParroquia.setOrderBy(DBNames.NOME_PARROQUIA);
	}
	parroquia.removeAllItems();
	parroquia.addItem("");
	for (KeyValue kv : kvParroquia.getValues()) {
	    parroquia.addItem(kv);
	}
    }

    private void fillAyuntamiento() {
	KeyValueRetriever kvMunicipio = new KeyValueRetriever(
		getMunicipioLayer(),
		DBNames.COD_CONCELLO,
		DBNames.NOME_AYUNTAMIENTO);
	kvMunicipio.setOrderBy(DBNames.NOME_AYUNTAMIENTO);
	for (KeyValue kv : kvMunicipio.getValues()) {
	    ayuntamiento.addItem(kv);
	}
    }

    public boolean init() {
	TOCLayerManager toc = new TOCLayerManager();
	if((toc.getLayerByName(DBNames.LAYER_MUNICIPIOS) != null) &&
		(toc.getLayerByName(DBNames.LAYER_PARROQUIAS) != null)) {
	    initWidgets();
	    return true;
	}
	return false;
    }

    @Override
    public int getPosition() {
	String fieldName = null;
	String fieldValue = null;
	int position = AbstractNavTable.EMPTY_REGISTER;
	FLyrVect layer = getLayer();
	if(layer == null) {
	    JOptionPane.showMessageDialog(this,
		    "Es necesario seleccionar un municipio o parroquia.");
	    position = AbstractNavTable.EMPTY_REGISTER;

	} else if(layer.getName().equalsIgnoreCase(DBNames.LAYER_PARROQUIAS)) {
	    fieldValue = ((KeyValue) parroquia.getSelectedItem()).getKey();
	    fieldName = DBNames.COD_PARROQUIA;
	    PositionRetriever positionRetriever = new PositionRetriever(
		    layer, fieldName, fieldValue);
	    position = positionRetriever.getPosition();
	} else if(layer.getName().equalsIgnoreCase(DBNames.LAYER_MUNICIPIOS)){
	    fieldName = DBNames.COD_CONCELLO;
	    fieldValue = ((KeyValue) ayuntamiento.getSelectedItem()).getKey();
	    PositionRetriever positionRetriever = new PositionRetriever(
		    layer, fieldName, fieldValue);
	    position = positionRetriever.getPosition();
	}
	return position;
    }

    public FLyrVect getMunicipioLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(DBNames.LAYER_MUNICIPIOS);
    }

    public FLyrVect getParroquiaLayer() {
	TOCLayerManager toc = new TOCLayerManager();
	return toc.getLayerByName(DBNames.LAYER_PARROQUIAS);
    }

    @Override
    public FLyrVect getLayer() {
	if(parroquia.getSelectedItem() instanceof KeyValue) {
	    return getParroquiaLayer();
	} else if (ayuntamiento.getSelectedItem() instanceof KeyValue ){
	    return getMunicipioLayer();
	} else {
	    return null;
	}
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
	fillParroquia();
    }

}
