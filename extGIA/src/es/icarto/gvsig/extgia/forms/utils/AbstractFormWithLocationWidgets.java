package es.icarto.gvsig.extgia.forms.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public abstract class AbstractFormWithLocationWidgets extends AbstractForm {

    public static final String AREA_MANTENIMIENTO = "area_mantenimiento";
    private static final String BASE_CONTRATISTA = "base_contratista";
    private static final String TRAMO = "tramo";
    private static final String TIPO_VIA = "tipo_via";
    private static final String NOMBRE_VIA = "nombre_via";


    private JComboBox areaMantenimientoWidget;
    private JComboBox baseContratistaWidget;
    private JComboBox tramoWidget;
    private JComboBox tipoViaWidget;
    private JComboBox nombreViaWidget;

    private UpdateBaseContratistaListener updateBaseContratistaListener;
    private UpdateTramoListener updateTramoListener;
    private UpdateTipoViaListener updateTipoViaListener;
    private UpdateNombreViaListener updateNombreViaListener;

    private String areaMantenimiento;
    private String baseContratista;
    private String tramo ;
    private String tipoVia;
    private String nombreVia;

    public AbstractFormWithLocationWidgets (FLyrVect layer) {
	super(layer);
	initWidgets();
	setListeners();
    }

    @Override
    protected void setListeners() {
	super.setListeners();

	HashMap<String, JComponent> widgets = getWidgetComponents();

	areaMantenimientoWidget = (JComboBox) widgets.get(AREA_MANTENIMIENTO);
	baseContratistaWidget = (JComboBox) widgets.get(BASE_CONTRATISTA);
	tramoWidget = (JComboBox) widgets.get(TRAMO);
	tipoViaWidget = (JComboBox) widgets.get(TIPO_VIA);
	nombreViaWidget = (JComboBox) widgets.get(NOMBRE_VIA);

	updateBaseContratistaListener = new UpdateBaseContratistaListener();
	updateTramoListener = new UpdateTramoListener();
	updateTipoViaListener = new UpdateTipoViaListener();
	updateNombreViaListener = new UpdateNombreViaListener();

	areaMantenimientoWidget.addActionListener(updateBaseContratistaListener);
	baseContratistaWidget.addActionListener(updateTramoListener);
	tramoWidget.addActionListener(updateTipoViaListener);
	tipoViaWidget.addActionListener(updateNombreViaListener);
    }

    public class UpdateBaseContratistaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues()) {
		String getAreaMantenimientoIdQuery =
			"SELECT id FROM audasa_extgia_dominios.area_mantenimiento" +
				" WHERE item = '" + areaMantenimientoWidget.getSelectedItem().toString() + "';";
		String getBaseContratistaQuery =
			"SELECT item FROM audasa_extgia_dominios.base_contratista" +
				" WHERE id_am = " + getIdFromSql(getAreaMantenimientoIdQuery) + ";";
		baseContratistaWidget.removeAllItems();
		baseContratistaWidget.addItem("");
		for (String value: getValuesListFromSql(getBaseContratistaQuery)) {
		    baseContratistaWidget.addItem(value);
		}
	    }
	}

    }

    public class UpdateTramoListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && baseContratistaWidget.getItemCount()!=0) {
		String getBaseContratistaIdQuery =
			"SELECT id FROM audasa_extgia_dominios.base_contratista" +
				" WHERE item = '" + baseContratistaWidget.getSelectedItem().toString() + "';";
		String getTramoQuery =
			"SELECT item FROM audasa_extgia_dominios.tramo" +
				" WHERE id_bc = " + getIdFromSql(getBaseContratistaIdQuery) + ";";
		tramoWidget.removeAllItems();
		tramoWidget.addItem("");
		for (String value: getValuesListFromSql(getTramoQuery)) {
		    tramoWidget.addItem(value);
		}
	    }
	}

    }

    public class UpdateTipoViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tramoWidget.getItemCount()!=0) {
		String getTramoIdQuery =
			"SELECT id FROM audasa_extgia_dominios.tramo" +
				" WHERE item = '" + tramoWidget.getSelectedItem().toString() + "';";
		String getTipoViaQuery =
			"SELECT item FROM audasa_extgia_dominios.tipo_via" +
				" WHERE id_tramo = " + getIdFromSql(getTramoIdQuery) + ";";
		tipoViaWidget.removeAllItems();
		tipoViaWidget.addItem("");
		for (String value: getValuesListFromSql(getTipoViaQuery)) {
		    tipoViaWidget.addItem(value);
		}
	    }
	}

    }

    public class UpdateNombreViaListener implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent arg0) {
	    if (!isFillingValues() && tipoViaWidget.getItemCount()!=0) {
		String getTipoViaIdQuery =
			"SELECT id FROM audasa_extgia_dominios.tipo_via" +
				" WHERE item = '" + tipoViaWidget.getSelectedItem().toString() + "';";
		String getNombreViaQuery =
			"SELECT item FROM audasa_extgia_dominios.nombre_via" +
				" WHERE id_tv = " + getIdFromSql(getTipoViaIdQuery) + ";";
		nombreViaWidget.removeAllItems();
		nombreViaWidget.addItem("");
		for (String value: getValuesListFromSql(getNombreViaQuery)) {
		    nombreViaWidget.addItem(value);
		}
	    }
	}

    }

    public JComboBox getAreaMantenimientoWidget() {
	return areaMantenimientoWidget;
    }

    public JComboBox getBaseContratistaWidget() {
	return baseContratistaWidget;
    }

    public JComboBox getTramoWidget() {
	return tramoWidget;
    }

    public JComboBox getTipoViaWidget() {
	return tipoViaWidget;
    }

    public JComboBox getNombreViaWidget() {
	return nombreViaWidget;
    }

    private int getIdFromSql(String query) {
	PreparedStatement statement = null;
	Connection connection = DBSession.getCurrentSession().getJavaConnection();
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    statement = connection.prepareStatement(query);
	    rs.next();
	    return rs.getInt(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	    return -1;
	}
    }

    private ArrayList<String> getValuesListFromSql(String query) {
	ArrayList<String> values = new ArrayList<String>();
	PreparedStatement statement = null;
	Connection connection = DBSession.getCurrentSession().getJavaConnection();
	try {
	    statement = connection.prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    statement = connection.prepareStatement(query);
	    while (rs.next()) {
		values.add(rs.getString(1));
	    }
	    return values;
	} catch (SQLException e) {
	    e.printStackTrace();
	    return new ArrayList<String>();
	}
    }

    @Override
    protected abstract void fillSpecificValues();

    @Override
    public abstract FormPanel getFormBody();

    @Override
    public abstract Logger getLoggerName();

    @Override
    public abstract String getXMLPath();
}
