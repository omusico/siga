package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

public class LoadLegendWizard extends LoadMapWizard {

	public LoadLegendWizard(View view) {
		super(view);

	}

	@Override
	public WindowInfo getWindowInfo() {
		WindowInfo wi = super.getWindowInfo();
		wi.setHeight(300);
		return wi;
	}

	@Override
	protected void addWizardComponents() {
		views.add(new LoadLegendWizardComponent(properties));
	}

}
