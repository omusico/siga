package es.icarto.gvsig.extgex.locators;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;

import com.hardcode.driverManager.DriverLoadException;
import com.hardcode.gdbms.driver.exceptions.InitializeDriverException;
import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.hardcode.gdbms.engine.data.DataSource;
import com.hardcode.gdbms.engine.data.DataSourceFactory;
import com.hardcode.gdbms.engine.instruction.EvaluationException;
import com.hardcode.gdbms.engine.instruction.SemanticException;
import com.hardcode.gdbms.engine.values.Value;
import com.hardcode.gdbms.engine.values.ValueWriter;
import com.hardcode.gdbms.parser.ParseException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.edition.EditableAdapter;
import com.iver.cit.gvsig.fmap.edition.EditionEvent;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.iver.cit.gvsig.fmap.layers.ReadableVectorial;
import com.iver.cit.gvsig.fmap.layers.SelectableDataSource;
import com.iver.cit.gvsig.project.documents.view.gui.BaseView;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.gvWindow;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.retrievers.KeyValueRetriever;
import es.icarto.gvsig.navtableforms.ormlite.domain.KeyValue;

public class LocatorByPK extends gvWindow implements ActionListener {

	private static final String TRAMO_FIELD = "tramo";
    private static final String PK_FIELD = "pks";
    
    private final FormPanel formBody;

    public final String ID_TRAMO = "tramo";
    private JComboBox tramoCB;
    
    public final String ID_PKNUMBER = "pk_number";
    private JComboBox pkNumberCB;

    public final String ID_GOTOPK = "goToPKButton";
    private JButton goToPKB;

    public LocatorByPK() {
	super(400, 200);
	formBody = new FormPanel("LocatorByPK.xml");
	formBody.setVisible(true);
	this.add(formBody, BorderLayout.CENTER);
	this.setTitle("Localizador por PK");
	initWidgets();
    }

    public void initWidgets() {
    tramoCB = (JComboBox) formBody.getComponentByName(ID_TRAMO);
    pkNumberCB = (JComboBox) formBody.getComponentByName(ID_PKNUMBER);
    tramoCB.addActionListener(this);
    fillTramo();
	fillPK();
	goToPKB = (JButton) formBody.getComponentByName(ID_GOTOPK);
	goToPKB.addActionListener(this);
    }

    public FLyrVect getPKSLayer() {
    TOCLayerManager toc = new TOCLayerManager();
    return toc.getLayerByName(DBNames.LAYER_PKS);
    }
    
    private void fillTramo() {
    KeyValueRetriever kvPks = new KeyValueRetriever((FLyrVect) getPKSLayer(), 
        	TRAMO_FIELD, TRAMO_FIELD);
    kvPks.setOrderBy(TRAMO_FIELD);
    ArrayList<String> distinctValues = new ArrayList<String>();
    for (KeyValue kv : kvPks.getValues()) {
    	if (!distinctValues.contains(kv.getValue())) {
    		distinctValues.add(kv.getValue());
    	}
    }
    for (String dkv : distinctValues) {
    	tramoCB.addItem(dkv);
    }	
    }
    
    private void fillPK2() {
    KeyValueRetriever kvPks = new KeyValueRetriever((FLyrVect) getPKSLayer(), 
    		PK_FIELD, PK_FIELD);
    kvPks.setOrderBy(PK_FIELD);
    for (KeyValue kv : kvPks.getValues()) {
    	pkNumberCB.addItem(kv);
    }	
	}

    private void fillPK() {
    DataSourceFactory dsf;
    try {
		dsf = getPKSLayer().getRecordset().getDataSourceFactory();
		String sqlQuery = "select * from " + getPKSLayer().getRecordset().getName() + 
	    	" where tramo = " + "'" + tramoCB.getSelectedItem().toString() + "'" +
	    	" order by pks;";
	    DataSource ds = dsf.executeSQL(sqlQuery, EditionEvent.ALPHANUMERIC);
	    ds.setDataSourceFactory(dsf);
	    SelectableDataSource sds = new SelectableDataSource(ds);
	    EditableAdapter ea = new EditableAdapter();
	    ea.setOriginalDataSource(sds);
	    pkNumberCB.removeAllItems();
	    int pkIndex = ea.getRecordset().getFieldIndexByName(PK_FIELD);
	    for (int i=0; i<ea.getRecordset().getRowCount(); i++) {
	    	pkNumberCB.addItem(ea.getRecordset().getFieldValue(i, pkIndex));
	    }
	} catch (ReadDriverException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (DriverLoadException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (SemanticException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (EvaluationException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }
    
	@Override
    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == tramoCB) {
		fillPK();
	}
	if (e.getSource() == goToPKB) {
	    String pkToFind = pkNumberCB.getSelectedItem().toString();
	    String tramo = tramoCB.getSelectedItem().toString();
	    FLyrVect pkLayer = getPKSLayer();
	    if (pkToFind != null) {
		try {
		    SelectableDataSource pkRecordset = pkLayer.getRecordset();
		    int tramoIndex = pkRecordset.getFieldIndexByName(TRAMO_FIELD);
		    int pkIndex = pkRecordset.getFieldIndexByName(PK_FIELD);
		    for (int i = 0; i < pkRecordset.getRowCount(); i++) {
			Value pkValue = pkRecordset.getFieldValue(i,
				pkIndex);
			String pkStringValue = pkValue
				.getStringValue(ValueWriter.internalValueWriter);
			Value tramoValue = pkRecordset.getFieldValue(i, tramoIndex);

			if ((pkStringValue.compareToIgnoreCase(pkToFind) == 0) && 
					(tramoValue.toString().compareToIgnoreCase(tramo) == 0)){
			    zoom(pkLayer, i);
			    return;
			}
		    }
		} catch (ReadDriverException e1) {
		    e1.printStackTrace();
		}
	    }
	}
    }

    private FLayer getPKLayer() {
	FLayer pkLayer = null;
	BaseView view = (BaseView) PluginServices.getMDIManager()
		.getActiveWindow();
	MapControl mapControl = view.getMapControl();
	FLayers flayers = mapControl.getMapContext().getLayers();
	pkLayer = flayers.getLayer(DBNames.LAYER_PKS);
	return pkLayer;
    }

    private void zoom(FLyrVect layer, int pos) {
	try {
	    Rectangle2D rectangle = null;
	    IGeometry g;
	    ReadableVectorial source = (layer).getSource();
	    source.start();
	    g = source.getShape(pos);
	    source.stop();
	    /*
	     * fix to avoid zoom problems when layer and view projections aren't
	     * the same.
	     */
	    if (layer.getCoordTrans() != null) {
		g.reProject(layer.getCoordTrans());
	    }
	    rectangle = g.getBounds2D();
	    if (rectangle.getWidth() < 200) {
		rectangle.setFrameFromCenter(rectangle.getCenterX(), rectangle
			.getCenterY(), rectangle.getCenterX() + 100, rectangle
			.getCenterY() + 100);
	    }
	    if (rectangle != null) {
		layer.getMapContext().getViewPort().setExtent(rectangle);
	    }
	} catch (InitializeDriverException e) {
	    e.printStackTrace();
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

}
