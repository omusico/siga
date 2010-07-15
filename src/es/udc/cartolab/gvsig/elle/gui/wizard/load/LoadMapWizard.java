package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardWindow;

public class LoadMapWizard extends WizardWindow {

	private WindowInfo viewInfo;
	private String mapName;

	public LoadMapWizard(View view) {
		super();

		properties.put(LoadMapWizardComponent.PROPERTY_VEW, view);

	}

	@Override
	public WindowInfo getWindowInfo() {
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this, "Load_map"));
			viewInfo.setWidth(525);
			viewInfo.setHeight(520);
		}
		return viewInfo;
	}

	@Override
	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void addWizardComponents() {
		views.add(new LoadMapWizardComponent(properties));
		views.add(new LoadLegendWizardComponent(properties));
	}

}
