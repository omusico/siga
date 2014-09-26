package es.icarto.gvsig.extgia.consultas;

import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.commons.queries.QueriesWidget;
import es.icarto.gvsig.navtableforms.ormlite.domainvalues.KeyValue;

public class QueriesWidgetCombo implements QueriesWidget {

    public static final String CARACTERISTICAS = "Características";
    public static final String TRABAJOS_AGRUPADOS = "Trabajos Agrupados";
    private final JComboBox queriesWidget;

    public QueriesWidgetCombo(FormPanel formPanel, String name) {
	queriesWidget = (JComboBox) formPanel.getComponentByName(name);
    }

    @Override
    public String getQueryId() {
	Object s = queriesWidget.getSelectedItem();
	return s == null ? "" : s.toString().trim();
    }

    @Override
    public boolean isQueryIdSelected(String id) {
	return getQueryId().equals(id);
    }

    public void addActionListener(ActionListener listener) {
	queriesWidget.addActionListener(listener);
    }

    public KeyValue getQuery() {
	return (KeyValue) queriesWidget.getSelectedItem();
    }
}