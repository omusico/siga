package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.awt.geom.Rectangle2D;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import org.apache.log4j.Logger;

import es.icarto.gvsig.commons.utils.CollUtils;
import es.icarto.gvsig.commons.utils.Field;
import es.icarto.gvsig.elle.db.DBStructure;
import es.icarto.gvsig.siga.models.CurrentUser;
import es.icarto.gvsig.siga.models.MunicipioConstantes;
import es.udc.cartolab.gvsig.elle.constants.Constant;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.users.utils.DBSession;

@SuppressWarnings("serial")
public class ConstantsPanel extends JPanel {

    private static final Logger logger = Logger.getLogger(ConstantsPanel.class);
    private JTable table;
    private MunicipioConstantes municipioConstantes;
    private CurrentUser user;

    public ConstantsPanel() {
	super(new BorderLayout());
	setUpUI();
	setUpData();
	preselectConstants();
    }

    private void setUpUI() {
	table = new JTable();
	table.getTableHeader().setReorderingAllowed(false);
	this.add(new JScrollPane(table), BorderLayout.CENTER);
    }

    private void setUpData() {
	user = new CurrentUser();
	municipioConstantes = new MunicipioConstantes(user);
	final TableModel valueListData = municipioConstantes.getAsTableModel();
	table.setModel(valueListData);
	table.removeColumn(table.getColumnModel().getColumn(0));
    }

    private void preselectConstants() {
	List<String> constants = ELLEMap.getConstantValuesSelected();
	if (constants.isEmpty()) {
	    return;
	}
	for (int i = 0; i < table.getRowCount(); i++) {
	    Object id = table.getModel().getValueAt(i, 0);
	    if (constants.contains(id)) {
		table.getSelectionModel().addSelectionInterval(i, i);
	    }
	}
    }

    private List<String> getSelectedIds() {
	List<String> values = new ArrayList<String>();
	int[] idx = table.getSelectedRows();
	for (int i : idx) {
	    values.add(table.getModel().getValueAt(i, 0).toString());
	}
	return values;
    }

    private List<Field> getSelectedAsFields() {
	List<Field> values = new ArrayList<Field>();
	int[] idx = table.getSelectedRows();
	for (int i : idx) {
	    String id = table.getModel().getValueAt(i, 0).toString();
	    String name = table.getModel().getValueAt(i, 1).toString();
	    values.add(new Field(id, name));
	}
	return values;
    }

    public String buildWhereAndSetConstants() {
	List<String> values = getSelectedIds();
	// TODO: An index on selectedConstant field could speed up the queryS
	String where = "WHERE municipio IN (";
	if (!values.isEmpty()) {

	    ELLEMap.setConstantValuesSelected(values);

	    for (String s : values) {
		where += "'" + s + "', ";
	    }
	    where = where.substring(0, where.length() - 2) + ")";

	} else if (!user.getArea().equalsIgnoreCase("ambas")) {
	    ELLEMap.setConstantValuesSelected(new ArrayList<String>());

	    Collection<String> councils = municipioConstantes.getAsIds();
	    for (String s : councils) {
		where += "'" + s + "', ";
	    }
	    where = where.substring(0, where.length() - 2) + ")";

	} else {
	    ELLEMap.setConstantValuesSelected(new ArrayList<String>());
	    where = "";
	}

	return where;
    }

    public String getStatusBarMsg() {

	List<String> values = getSelectedIds();
	if (values.size() != 1) {
	    if (user.getArea().equalsIgnoreCase("ambas")) {
		return "Municipio: " + "TODOS";

	    } else if (user.getArea().equalsIgnoreCase("norte")) {
		return "Municipio: " + "Área Norte";

	    } else {
		return "Municipio: " + "Área Sur";

	    }
	} else {
	    String msg = "Municipio: " + getSelectedAsFields().get(0);
	    return msg;
	}
    }

    public Rectangle2D getZoomGeometry() {
	Constant constant = new Constant(getSelectedIds());
	return constant.getGeometry();

    }

    // TODO. Modificar tabla _constants porque sólo se la toca desde aqui
    private final static String CONSTANTS_TABLE_NAME = "_constants";
    private final static String CONSTANTS_AFFECTED_TABLE_NAME = "nombre_tabla";

    public Collection<String> getTablesAffectedByConstant() {
	try {
	    String[][] table = DBSession.getCurrentSession().getTable(
		    CONSTANTS_TABLE_NAME, DBStructure.SCHEMA_NAME,
		    new String[] { CONSTANTS_AFFECTED_TABLE_NAME }, null, null,
		    false);
	    return CollUtils.flat(table);
	} catch (SQLException e) {
	    logger.error(e.getStackTrace(), e);
	}
	return Collections.emptyList();
    }

}
