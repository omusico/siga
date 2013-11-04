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

public class FormExpropiationsLauncher implements MouseListener {

    private static final int BUTTON_RIGHT = 3;

    private final FormReversions formReversions;
    private FormExpropiations formExpropiations;
    private JTable table;
    private FLyrVect layerExpropiations;
    private IEditableSource tableFincasReversions;

    public FormExpropiationsLauncher(FormReversions form) {
	this.formReversions = form;
	this.layerExpropiations = getLayer();
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
		&& TableUtils.isProperRowSelected(table)) {
	    JPopupMenu popup = new JPopupMenu();

	    JMenuItem menuOpenForm = new JMenuItem("Abrir expropiaciones");
	    menuOpenForm.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    openForm();
		}
	    });
	    popup.add(menuOpenForm);

	    //	    JMenuItem menuOpenANT = new JMenuItem("Editar fincas");
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
	    int rowIndex = (int) TableUtils.getFeatureIndexFromJTable(
		    table,
		    layerExpropiations.getRecordset());
	    if (rowIndex != AbstractNavTable.EMPTY_REGISTER) {
		formExpropiations = new FormExpropiations(layerExpropiations, null);
		if (formExpropiations.init()) {
		    formExpropiations.setPosition(rowIndex);
		    selectFeaturesInForm(rowIndex);
		    PluginServices.getMDIManager().addWindow(formExpropiations);
		    // Listening closing actions of formReversions
		    //		    JInternalFrame parent = (JInternalFrame) formExpropiations
		    //			    .getRootPane().getParent();
		    //		    parent.addInternalFrameListener((InternalFrameListener) formReversions);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void openANT() {
	try {
	    int rowIndex = (int) TableUtils.getFeatureIndexFromJTable(
		    table,
		    tableFincasReversions.getRecordset());
	    if(rowIndex != AbstractNavTable.EMPTY_REGISTER) {
		AlphanumericNavTable ant = new AlphanumericNavTable(
			tableFincasReversions,
			"Enlace fincas-reversiones");
		if(ant.init()) {
		    ant.setPosition(rowIndex);
		    PluginServices.getMDIManager().addWindow(ant);
		    // Listening closing actions of formReversions
		    JInternalFrame parent = (JInternalFrame) ant
			    .getRootPane().getParent();
		    parent.addInternalFrameListener((InternalFrameListener) formReversions);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void selectFeaturesInForm(int rowIndex) {
	try {
	    ArrayList<Long> rowIndexes = TableUtils.getFeatureIndexesFromJTable(
		    table,
		    layerExpropiations.getRecordset(),
		    DBNames.FIELD_IDEXPROPIACION_FINCAS_REVERSIONES);
	    formExpropiations.clearSelectedFeatures();
	    //	    for (long rowIndex : rowIndexes) {
	    formExpropiations.selectFeature(rowIndex);
	    //	    }
	    formExpropiations.setOnlySelected(true);
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    formExpropiations.clearSelectedFeatures();
	    formExpropiations.setOnlySelected(false);
	}
    }

    private FLyrVect getLayer() {
	if(layerExpropiations == null) {
	    TOCLayerManager toc = new TOCLayerManager();
	    layerExpropiations = toc.getLayerByName(DBNames.LAYER_FINCAS);
	}
	return layerExpropiations;
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
