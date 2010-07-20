package es.udc.cartolab.gvsig.elle.gui.wizard.save;

import javax.swing.JOptionPane;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.elle.gui.wizard.WizardComponent;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardException;
import es.udc.cartolab.gvsig.elle.gui.wizard.WizardWindow;

public class SaveMapWizard extends WizardWindow {

	private WindowInfo viewInfo;
	private final int width = 750;
	private final int height = 500;


	public SaveMapWizard(View view) {
		super();

		properties.put("view", view);

	}

	@Override
	public WindowInfo getWindowInfo() {
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODALDIALOG | WindowInfo.RESIZABLE);
			viewInfo.setTitle(PluginServices.getText(this, "save_map"));
			viewInfo.setWidth(width);
			viewInfo.setHeight(height);
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
		views.add(new SaveMapWizardComponent(properties));
		views.add(new SaveLegendsWizardComponent(properties));
	}

	@Override
	protected void finish() {
		boolean close = true;
		boolean success = true;
		try {
			for (WizardComponent wc : views) {
				wc.finish();
			}
		} catch (WizardException e) {
			// TODO Auto-generated catch block
			close = e.closeWizard();
			success = false;
			if (e.showMessage()) {
				JOptionPane.showMessageDialog(
						this,
						e.getMessage(),
						"",
						JOptionPane.ERROR_MESSAGE);
			}
			e.printStackTrace();
		}
		if (close) {
			close();
		}
		if (success) {
			JOptionPane.showMessageDialog(
					this,
					PluginServices.getText(this, "map_saved_correctly"),
					"",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
