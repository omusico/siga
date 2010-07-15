package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import java.util.ArrayList;

import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;

public class LoadLegendWizard extends LoadMapWizard {

	public LoadLegendWizard(View view) {
		super(view);

		views = new ArrayList<WizardComponent>();
		views.add(new LoadLegendWizardComponent(this));

	}

	@Override
	public WindowInfo getWindowInfo() {
		WindowInfo wi = super.getWindowInfo();
		wi.setHeight(300);
		return wi;
	}

}
