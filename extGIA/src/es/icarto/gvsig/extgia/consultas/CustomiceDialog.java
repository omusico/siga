package es.icarto.gvsig.extgia.consultas;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;

import es.icarto.gvsig.commons.gui.AbstractIWindow;
import es.icarto.gvsig.commons.gui.AcceptCancelPanel;

@SuppressWarnings("serial")
public class CustomiceDialog extends AbstractIWindow implements ActionListener {

    public static final int CANCEL = -1;
    public static final int OK = 1;

    private int status;

    private final List<JComboBox> order;

    private final DualListBox<String> dualListBox;;

    public CustomiceDialog() {
	super();
	setWindowTitle("Personalizar consulta");
	setWindowInfoProperties(WindowInfo.MODALDIALOG | WindowInfo.NOTCLOSABLE);
	addAcceptCancelPanel(this, this);

	dualListBox = new DualListBox<String>();
	dualListBox.addDestListDataListener(new UpdateOrderBy());
	add(dualListBox, "growx, growy");

	JPanel southPanel = new JPanel(new MigLayout("insets 10",
		"[grow][grow][grow][grow]", ""));
	southPanel.setBorder(BorderFactory
		.createTitledBorder("Seleccione Orden"));

	order = new ArrayList<JComboBox>(4);
	addOrderCB(southPanel);
	addOrderCB(southPanel);
	addOrderCB(southPanel);
	addOrderCB(southPanel);
	add(southPanel, BorderLayout.SOUTH);
    }

    private void addOrderCB(JPanel panel) {
	JComboBox jComboBox = new JComboBox();
	jComboBox.setPrototypeDisplayValue(DualListBox.prototypeCellValue);
	order.add(jComboBox);
	panel.add(jComboBox, "growx");
    }

    public void addSourceElements(String[] values) {
	dualListBox.addSourceElements(values);
    }

    private class UpdateOrderBy implements ListDataListener {

	@Override
	public void intervalAdded(ListDataEvent e) {
	    update((ListModel) e.getSource());
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
	    update((ListModel) e.getSource());
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
	    update((ListModel) e.getSource());
	}

	private void update(ListModel model) {
	    for (JComboBox c : order) {
		c.removeAllItems();
		c.addItem("");
		for (int i = 0; i < model.getSize(); i++) {
		    c.addItem(model.getElementAt(i));
		}
	    }
	}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	if (e.getActionCommand()
		.equals(AcceptCancelPanel.CANCEL_ACTION_COMMAND)) {
	    status = CANCEL;
	} else {
	    status = OK;
	}
	PluginServices.getMDIManager().closeWindow(this);
    }

    public int open() {
	PluginServices.getMDIManager().addCentredWindow(this);
	return status;
    }

    public List<String> getFields() {
	return dualListBox.getDestList();
    }

    public List<String> getOrderBy() {
	List<String> list = new ArrayList<String>(4);
	for (JComboBox c : order) {
	    String sel = c.getSelectedItem() == null ? "" : c.getSelectedItem()
		    .toString().trim();
	    if (!sel.isEmpty()) {
		if (list.indexOf(sel) == -1) {
		    list.add(sel);
		}
	    }
	}
	return list;
    }
}
