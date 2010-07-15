package es.udc.cartolab.gvsig.elle.gui.wizard.load;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardWindow;

public class LoadMapWizard extends WizardWindow {

	private WindowInfo viewInfo;
	private String mapName;
	private View view;

	public LoadMapWizard(View view) {
		super();
		this.view = view;
		views.add(new LoadMapWizardComponent(this));
		views.add(new LoadLegendWizardComponent(this));
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
	protected void finish() {
		for (WizardComponent wc : views) {
			wc.finish();
		}
		close();
	}

	@Override
	protected void next() {
		if (currentPos == 0) {
			mapName = ((LoadMapWizardComponent) views.get(currentPos)).getMapName();
		}
		changeView(currentPos+1);
	}

	public String getMapName() {
		return mapName;
	}

	@Override
	protected void previous() {
		changeView(currentPos-1);
	}

	public View getView() {
		return view;
	}

}
