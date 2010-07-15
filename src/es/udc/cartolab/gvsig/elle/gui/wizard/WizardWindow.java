package es.udc.cartolab.gvsig.elle.gui.wizard;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;

public abstract class WizardWindow extends JPanel implements IWindow, WizardListener, ActionListener {

	protected JButton nextButton, prevButton, cancelButton, finishButton;
	private JPanel mainPanel;
	protected List<WizardComponent> views = new ArrayList<WizardComponent>();
	protected int currentPos;
	protected Map <String, Object> properties = new HashMap<String, Object>();


	public WizardWindow() {
		nextButton = new JButton("Siguiente");
		nextButton.addActionListener(this);
		prevButton = new JButton("Anterior");
		prevButton.addActionListener(this);
		cancelButton = new JButton("Cancelar");
		cancelButton.addActionListener(this);
		finishButton = new JButton("Terminar");
		finishButton.addActionListener(this);

		addWizardComponents();

	}

	protected abstract void addWizardComponents();

	public void open() {
		MigLayout layout = new MigLayout("inset 0, align center",
				"10[grow]10",
		"10[grow][]");

		setLayout(layout);

		mainPanel = getMainPanel();
		changeView(0);

		add(mainPanel, "shrink, growx, growy, wrap");
		add(getSouthPanel(), "shrink, align right");
		PluginServices.getMDIManager().addCentredWindow(this);
	}

	private JPanel getMainPanel() {
		JPanel panel = new JPanel(new CardLayout());

		for (WizardComponent view : views) {
			panel.add(view, view.getWizardComponentName());
		}

		return panel;
	}

	private JPanel getSouthPanel() {

		JPanel panel = new JPanel();
		panel.add(cancelButton);
		panel.add(prevButton);
		panel.add(nextButton);
		panel.add(finishButton);
		return panel;
	}

	protected void changeView(int position) {
		if (position>=0 && position<views.size()) {
			views.get(currentPos).removeWizardListener(this);
			currentPos = position;
			WizardComponent newView = views.get(currentPos);
			newView.addWizardListener(this);
			CardLayout cl = (CardLayout) mainPanel.getLayout();
			cl.show(mainPanel, newView.getWizardComponentName());
			newView.showComponent();
			updateButtons();
		}
	}

	public void updateButtons() {
		if (views.size()<2) {
			prevButton.setVisible(false);
			nextButton.setVisible(false);
			finishButton.setText("Aceptar");
		}
		WizardComponent currentView = views.get(currentPos);
		int nViews = views.size();
		nextButton.setEnabled(currentPos!=nViews-1 && currentView.canNext() );
		prevButton.setEnabled(currentPos>0);
		finishButton.setEnabled(currentView.canFinish());
	}

	public void wizardChanged() {
		updateButtons();
	}

	public void close() {
		PluginServices.getMDIManager().closeWindow(this);
	}

	protected void finish() {
		try {
			for (WizardComponent wc : views) {
				wc.finish();
			}
		} catch (WizardFinishException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		close();
	}

	protected void next() {
		views.get(currentPos).setProperties();
		changeView(currentPos+1);
	}

	protected void previous() {
		changeView(currentPos-1);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cancelButton) {
			close();
		}
		if (e.getSource() == prevButton) {
			previous();
		}
		if (e.getSource() == nextButton) {
			next();
		}
		if (e.getSource() == finishButton) {
			finish();
		}
	}


}
