package es.icarto.gvsig.extgex.forms;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.extgex.utils.managers.TOCLayerManager;
import es.icarto.gvsig.navtableforms.gui.TableUtils;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class FormExpropiationsLauncher implements MouseListener {

    private static final int BUTTON_RIGHT = 3;

    private FormExpropiations formExpropiations;
    private JTable table;
    private FLyrVect layerExpropiations;

    public FormExpropiationsLauncher(FormReversions form) {
	this.layerExpropiations = getLayer();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if ((e.getClickCount() == 2) && TableUtils.hasRows(table)) {
	    openForm();
	} else if ((e.getButton() == BUTTON_RIGHT) && TableUtils.hasRows(table)
		&& TableUtils.isProperRowSelected(table)) {
	    JPopupMenu popup = new JPopupMenu();

	    JMenuItem menuOpenForm = new JMenuItem("Abrir expropiaciones");
	    menuOpenForm.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
		    openForm();
		}
	    });
	    popup.add(menuOpenForm);
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
	    int rowIndex = (int) TableUtils.getFeatureIndexFromJTable(table,
		    layerExpropiations.getRecordset());
	    if (rowIndex != AbstractNavTable.EMPTY_REGISTER) {
		formExpropiations = new FormExpropiations(layerExpropiations,
			null);
		if (formExpropiations.init()) {
		    formExpropiations.setPosition(rowIndex);
		    selectFeaturesInForm(rowIndex);
		    PluginServices.getMDIManager().addWindow(formExpropiations);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void selectFeaturesInForm(int rowIndex) {
	formExpropiations.clearSelectedFeatures();
	formExpropiations.selectFeature(rowIndex);
	formExpropiations.setOnlySelected(true);
    }

    private FLyrVect getLayer() {
	if (layerExpropiations == null) {
	    TOCLayerManager toc = new TOCLayerManager();
	    layerExpropiations = toc.getLayerByName(DBNames.LAYER_FINCAS);
	}
	return layerExpropiations;
    }

}
