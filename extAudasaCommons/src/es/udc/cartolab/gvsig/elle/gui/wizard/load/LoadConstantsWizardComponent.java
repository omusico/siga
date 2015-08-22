package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.awt.BorderLayout;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

import org.apache.log4j.Logger;

import com.iver.andami.PluginServices;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.jeta.forms.components.panel.FormPanel;
import com.jeta.forms.gui.common.FormException;

import es.icarto.gvsig.audasacommons.ConstantReload;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.utils.ELLEMap;
import es.udc.cartolab.gvsig.elle.utils.MapDAO;

@SuppressWarnings("serial")
public class LoadConstantsWizardComponent extends WizardComponent {

    private static final Logger logger = Logger
	    .getLogger(LoadConstantsWizardComponent.class);

    private JPanel listPanel;
    private JList valuesList;

    private boolean reload = false;

    public final static String PROPERTY_VIEW = "view";

    private final static String SELECTED_CONSTANT = "Municipio";

    public LoadConstantsWizardComponent(Map<String, Object> properties) {
	super(properties);
	setLayout(new BorderLayout());
	makeListPanel();
    }

    private void makeListPanel() {

	FormPanel form = null;
	try {
	    InputStream stream = getClass().getClassLoader()
		    .getResourceAsStream("forms/loadConstants.jfrm");
	    form = new FormPanel(stream);
	} catch (FormException e) {
	    logger.error(e.getStackTrace(), e);
	    return;
	}

	JLabel constantsLabel = form.getLabel("constantsLabel");
	constantsLabel.setText(PluginServices.getText(this, "constants_load"));

	valuesList = form.getList("valuesList");
	final String[] valueListData = ConstantUtils
		.getValuesFromConstantByQuery(SELECTED_CONSTANT);
	if (valueListData == null) {
	    return;
	}
	valuesList.setListData(valueListData);

	preselectConstants();

	listPanel = new JPanel();
	listPanel.add(form);
	this.add(listPanel);
    }

    private void preselectConstants() {
	List<String> constants = Arrays.asList(ELLEMap
		.getConstantValuesSelected());
	if (constants.isEmpty()) {
	    return;
	}

	ListModel model = valuesList.getModel();
	List<Integer> indexes = new ArrayList<Integer>();
	for (int i = 0; i < model.getSize(); i++) {
	    final String displayedText = (String) model.getElementAt(i);
	    String ineCode = ConstantUtils.getIdByConstantTag(displayedText);
	    if (constants.contains(ineCode)) {
		indexes.add(i);
	    }
	}

	if (!indexes.isEmpty()) {
	    int[] idx = new int[indexes.size()];
	    for (int j = 0; j < indexes.size(); j++) {
		idx[j] = indexes.get(j).intValue();
	    }
	    valuesList.setSelectedIndices(idx);
	}
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
    public String getWizardComponentName() {
	return "constants_wizard_component";
    }

    @Override
    public void setProperties() throws WizardException {
    }

    @Override
    public void showComponent() throws WizardException {
    }

    @Override
    public void finish() throws WizardException {

	Object[] selectedValuesList = valuesList.getSelectedValues();
	String[] values = new String[selectedValuesList.length];

	if (reload) {
	    new ConstantReload();
	    return;
	}

	Object tmp = properties
		.get(SigaLoadMapWizardComponent.PROPERTY_MAP_NAME);
	String mapName = (tmp == null ? "" : tmp.toString());

	Object aux = properties.get(PROPERTY_VIEW);
	if (aux != null && aux instanceof View) {
	    View view = (View) aux;
	    try {
		ELLEMap map = MapDAO.getInstance().getMap(view, mapName);
		// TODO: An index on selectedConstant field could speed up the
		// query
		String where = "WHERE "
			+ ConstantUtils
				.getValueOfFieldByConstant(SELECTED_CONSTANT)
			+ " IN (";

		if (selectedValuesList.length > 0) {

		    for (int i = 0; i < selectedValuesList.length; i++) {
			values[i] = ConstantUtils
				.getIdByConstantTag(selectedValuesList[i]
					.toString());
		    }
		    ELLEMap.setConstantValuesSelected(values);

		    for (String s : values) {
			where += "'" + s + "', ";
		    }
		    where = where.substring(0, where.length() - 2) + ")";

		    map.setWhereOnAllLayers(where);
		    map.setWhereOnAllOverviewLayers(where);
		    ELLEMap.setFiltered(true);

		} else if (!ConstantUtils.getAreaByConnectedUser()
			.equalsIgnoreCase("ambas")) {
		    where += ConstantUtils
			    .getWhereWithAllCouncilsOfArea(SELECTED_CONSTANT);
		    map.setWhereOnAllLayers(where);
		    map.setWhereOnAllOverviewLayers(where);
		} else {
		    ELLEMap.setFiltered(false);
		}

		// TODO
		String[] tablesAffectedByConstant = ConstantUtils
			.getTablesAffectedByConstant(SELECTED_CONSTANT);
		//
		map.load(view.getProjection(), tablesAffectedByConstant);
		if (view.getModel().getName().equals("ELLE View")
			&& (view.getModel() instanceof ProjectView)) {
		    ((ProjectView) view.getModel()).setName(mapName);
		}
		writeCouncilsLoadedInStatusBar(values);
		Constant constant = new Constant(values, view.getMapControl());
		ZoomToConstant zoomToConstant = new ZoomToConstant(
			view.getMapControl(), constant);
		zoomToConstant.zoom(values);
	    } catch (Exception e) {
		throw new WizardException(e);
	    }
	} else {
	    throw new WizardException("Couldn't retrieve the view");
	}
    }

    private void writeCouncilsLoadedInStatusBar(String[] values) {
	String areaByConnectedUser = ConstantUtils.getAreaByConnectedUser();
	if (values.length != 1) {
	    if (areaByConnectedUser.equalsIgnoreCase("ambas")) {
		PluginServices
		.getMainFrame()
		.getStatusBar()
		.setMessage("constants",
			SELECTED_CONSTANT + ": " + "TODOS");
	    } else if (areaByConnectedUser.equalsIgnoreCase("norte")) {
		PluginServices
		.getMainFrame()
		.getStatusBar()
		.setMessage("constants",
			SELECTED_CONSTANT + ": " + "Área Norte");
	    } else {
		PluginServices
		.getMainFrame()
		.getStatusBar()
		.setMessage("constants",
			SELECTED_CONSTANT + ": " + "Área Sur");
	    }
	} else {
	    PluginServices
	    .getMainFrame()
	    .getStatusBar()
	    .setMessage(
		    "constants",
		    SELECTED_CONSTANT
				    + ": "
		    + ConstantUtils
					    .getNombreMunicipioById(values[0]));
	}
    }

    public void setReload(boolean reload) {
	this.reload = true;
    }
}
