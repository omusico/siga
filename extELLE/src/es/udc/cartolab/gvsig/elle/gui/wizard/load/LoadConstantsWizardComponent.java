package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.fmap.drivers.DBException;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class LoadConstantsWizardComponent extends WizardComponent {
    
    private JPanel listPanel;
    private JList constantsList;
    private JList valuesList;
    private DBSession dbs;
    
    private String selectedConstant;
    private String selectedValue;
    
    public final static String PROPERTY_VIEW = "view";

    public LoadConstantsWizardComponent(Map<String, Object> properties) {
	super(properties);
	setLayout(new BorderLayout());
	add(getListPanel());
    }
    
    private JPanel getListPanel() {
	dbs = DBSession.getCurrentSession();
	
	if (listPanel == null) {
	    listPanel = new JPanel();
	    FormPanel form = new FormPanel("forms/loadConstants.jfrm");
	    listPanel.add(form);
	    
	    constantsList = form.getList("constantsList");
	    JLabel constantsLabel = form.getLabel("constantsLabel");
	    constantsLabel.setText(PluginServices.getText(this, "constants_load"));
	     
	    String[] constants = getConstants();
	    constantsList.setListData(constants);
	    constantsList.setSelectedIndex(0);
	    selectedConstant = (String) constantsList.getSelectedValues()[0];
	    
	    valuesList = form.getList("valuesList");
	    JLabel valuesLabel = form.getLabel("valuesLabel");
	    valuesLabel.setText(PluginServices.getText(this, "values_load"));
	    valuesList.setListData(getValuesFromConstant());
	    
	    constantsList.addListSelectionListener(new ListSelectionListener() {

		public void valueChanged(ListSelectionEvent arg0) {
		    int[] selected = constantsList.getSelectedIndices();
		    callStateChanged();

		    if (selected.length == 1) {
			selectedConstant = (String) constantsList.getSelectedValues()[0];
			String[] values = getValuesFromConstant();
			valuesList.setListData(values);
		    } else {
			//TODO: several constants selected at the same time	
		    }
		}
	    });
	    
	    valuesList.addListSelectionListener(new ListSelectionListener() {

		public void valueChanged(ListSelectionEvent arg0) {
		    int[] selected = valuesList.getSelectedIndices();
		    callStateChanged();

		    if (selected.length == 1) {
			selectedValue = (String) valuesList.getSelectedValues()[0];
		    } else {
			//TODO: several constants selected at the same time	
		    }
		}
	    });
	}
	return listPanel;
    }

    private String[] getConstants() {
	try {
	    String[] constants = dbs.getDistinctValues("_constants", "constante");
	    return constants;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    private String[] getValuesFromConstant() {
	try {
	    String[] values = dbs.getDistinctValues("exp_finca", getFilteredFieldOfConstant());
	    return values;
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }
    
    private String getFilteredFieldOfConstant() {
	String query = "SELECT campo_filtro FROM " + dbs.getSchema() + "._constants WHERE constante = " + "'" + selectedConstant + "'" + ";";
	PreparedStatement statement;
	try {
	    statement = dbs.getJavaConnection().prepareStatement(query);
	    statement.execute();
	    ResultSet rs = statement.getResultSet();
	    rs.first();
	    return rs.getString(1);
	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return null;
    }

    @Override
    public boolean canFinish() {
	return true;
    }

    @Override
    public boolean canNext() {
	return true;
    }

    @Override
    public void finish() throws WizardException {
	Object tmp = properties.get(LoadMapWizardComponent.PROPERTY_MAP_NAME);
	String mapName = (tmp == null ? "" : tmp.toString());
	
	Object aux = properties.get(PROPERTY_VIEW);
	if (aux!=null && aux instanceof View) {
		View view = (View) aux;
		try {
			ELLEMap map = MapDAO.getInstance().getMap(view, mapName, "");
			if (selectedValue != null) {
			    
			    
			    String where = "WHERE " + getFilteredFieldOfConstant() + " = " + "'" + selectedValue + "'";
			    map.setWhereClause(where);
			}
			map.load(view.getProjection());
			if (view.getModel().getName().equals("ELLE View") && (view.getModel() instanceof ProjectView)) {
				((ProjectView) view.getModel()).setName(mapName);
			}
		} catch (Exception e) {
			throw new WizardException(e);
		}
	} else {
		throw new WizardException("Couldn't retrieve the view");
	}
    }

    @Override
    public String getWizardComponentName() {
	return "constants_wizard_component";
    }

    @Override
    public void setProperties() throws WizardException {
	// TODO Auto-generated method stub

    }

    @Override
    public void showComponent() throws WizardException {
	// TODO Auto-generated method stub

    }

}
