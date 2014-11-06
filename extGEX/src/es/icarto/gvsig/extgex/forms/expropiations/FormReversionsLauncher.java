package es.icarto.gvsig.extgex.forms.expropiations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;

import com.hardcode.gdbms.driver.exceptions.ReadDriverException;
import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.layers.FLyrVect;

import es.icarto.gvsig.extgex.forms.reversions.FormReversions;
import es.icarto.gvsig.extgex.preferences.DBNames;
import es.icarto.gvsig.navtableforms.gui.TableUtils;
import es.icarto.gvsig.navtableforms.utils.TOCLayerManager;
import es.udc.cartolab.gvsig.navtable.AbstractNavTable;

public class FormReversionsLauncher implements MouseListener {

    private static final int BUTTON_RIGHT = 3;

    private FormReversions formReversions;
    private JTable table;
    private final FLyrVect layerReversions;

    public FormReversionsLauncher(FormExpropiations form) {
	this.layerReversions = getLayer();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	table = (JTable) e.getComponent();
	if ((e.getClickCount() == 2) && TableUtils.hasRows(table)) {
	    openForm();
	} else if ((e.getButton() == BUTTON_RIGHT) && TableUtils.hasRows(table)
		&& (table.getSelectedRow() != -1)) {
	    JPopupMenu popup = new JPopupMenu();

	    JMenuItem menuOpenForm = new JMenuItem("Abrir reversiones");
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
	    int index = (int) TableUtils.getFeatureIndexFromJTable(table,
		    layerReversions.getRecordset(), "exp_id");
	    if (index != AbstractNavTable.EMPTY_REGISTER) {
		formReversions = new FormReversions(layerReversions, null);
		if (formReversions.init()) {
		    formReversions.setPosition(index);
		    selectFeaturesInForm();
		    PluginServices.getMDIManager().addWindow(formReversions);
		}
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	}
    }

    private void selectFeaturesInForm() {
	try {
	    ArrayList<Long> rowIndexes = TableUtils
		    .getFeatureIndexesFromJTable(table,
			    layerReversions.getRecordset(),
			    DBNames.FIELD_IDREVERSION_FINCAS_REVERSIONES);
	    formReversions.clearSelection();
	    formReversions.setOnlySelected(false);
	    if (rowIndexes.size() > 0) {
		for (long rowIndex : rowIndexes) {
		    formReversions.selectFeature(rowIndex);
		}
		formReversions.setOnlySelected(true);
	    }
	} catch (ReadDriverException e) {
	    e.printStackTrace();
	    formReversions.clearSelection();
	    formReversions.setOnlySelected(false);
	}
    }

    private FLyrVect getLayer() {
	if (layerReversions == null) {
	    TOCLayerManager toc = new TOCLayerManager();
	    return toc.getLayerByName(FormReversions.TOCNAME);
	}
	return layerReversions;
    }

}
