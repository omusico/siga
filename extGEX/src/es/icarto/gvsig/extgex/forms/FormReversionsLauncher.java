package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.event.InternalFrameListener;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.edition.IEditableSource;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.extgex.utils.managers.TableLayerManager;
import es.icarto.gvsig.navtableforms.gui.TableUtils;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;
import es.udc.cartolab.gvsig.navtable.AlphanumericNavTable;

public class FormReversionsLauncher implements MouseListener {

    private static final int BUTTON_RIGHT = 3;

    private final FormExpropiations formExpropiations;
    private FormReversions formReversions;
    private JTable table;
    private final FLyrVect layerReversions;
    private IEditableSource tableFincasReversions;

    public FormReversionsLauncher(FormExpropiations form) {
	this.formExpropiations = form;
	this.layerReversions = getLayer();
	//	this.tableFincasReversions = getSource();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if((e.getClickCount() == 2)
		&& TableUtils.hasRows(table)) {
	    openForm();
	} else if((e.getButton() == BUTTON_RIGHT)
		&& TableUtils.hasRows(table)
		&& (table.getSelectedRow() != -1)) {
	    JPopupMenu popup = new JPopupMenu();

	    JMenuItem menuOpenForm = new JMenuItem("Abrir reversiones");
	    menuOpenForm.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    openForm();
		}
	    });
	    popup.add(menuOpenForm);

	    //	    JMenuItem menuOpenANT = new JMenuItem("Editar reversiones");
	    //	    menuOpenANT.addActionListener(new ActionListener() {
	    //		public void actionPerformed(ActionEvent arg0) {
	    //		    openANT();
	    //		}
	    //	    });
	    //	    popup.add(menuOpenANT);

	    popup.show(e.getComponent(), e.getX(), e.getY());
	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void openForm() {
	try {
	    int index = (int) TableUtils.getFeatureIndexFromJTable(table,
		    layerReversions.getRecordset(),
		    DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES);
	    if (index != AbstractNavTable.EMPTY_REGISTER) {
		formReversions = new FormReversions(layerReversions, null);
		if (formReversions.init()) {
		    formReversions.setPosition(index);
		    selectFeaturesInForm();
		    PluginServices.getMDIManager().addWindow(formReversions);
		    // Listening closing actions of formExpropiations
		    //		    JInternalFrame parent = (JInternalFrame) formReversions
		    //			    .getRootPane().getParent();
		    //		    parent.addInternalFrameListener((InternalFrameListener) formExpropiations);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void openANT() {
	try {
	    int index = (int) TableUtils.getFeatureIndexFromJTable(table,
		    tableFincasReversions.getRecordset());
	    if(index != AbstractNavTable.EMPTY_REGISTER) {
		AlphanumericNavTable ant = new AlphanumericNavTable(
			tableFincasReversions,
			"Enlace fincas-reversiones");
		if(ant.init()) {
		    ant.setPosition(index);
		    PluginServices.getMDIManager().addWindow(ant);
		    // Listening closing actions of formExpropiations
		    JInternalFrame parent = (JInternalFrame) ant
			    .getRootPane().getParent();
		    parent.addInternalFrameListener((InternalFrameListener) formExpropiations);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void selectFeaturesInForm() {
	try {
	    ArrayList<Long> rowIndexes = TableUtils.getFeatureIndexesFromJTable(
		    table,
		    layerReversions.getRecordset(),
		    DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES);
	    formReversions.clearSelectedFeatures();
	    formReversions.setOnlySelected(false);
	    if(rowIndexes.size() > 0) {
		for (long rowIndex : rowIndexes) {
		    formReversions.selectFeature(rowIndex);
		}
		formReversions.setOnlySelected(true);
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    formReversions.clearSelectedFeatures();
	    formReversions.setOnlySelected(false);
	}
    }

    private FLyrVect getLayer() {
	if(layerReversions == null) {
	    TOCLayerManager toc = new TOCLayerManager();
	    return toc.getLayerByName(DBNames.LAYER_REVERSIONES);
	}
	return layerReversions;
    }

    private IEditableSource getSource() {
	if(tableFincasReversions == null) {
	    TableLayerManager tableManager = new TableLayerManager();
	    return tableManager.getTableByName(
		    DBNames.TABLE_FINCASREVERSIONES).getModel().getModelo();
	}
	return tableFincasReversions;
    }

}
