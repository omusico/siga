package org.gvsig.mapsheets.print.audasa;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;


import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

public class VariablesTemplatePanel extends JPanel implements IWindow, ActionListener {

    private String template = null;
    private ArrayList<Component> componentsList = null;

    private static final int WIDTH = 520;
    private static final int HEIGHT = 320;
    private AudasaTemplate audasaTemplate = null;

    private WindowInfo winfo = null;

    public VariablesTemplatePanel(String template) {
	this.template = template;
	audasaTemplate = new AudasaTemplate();
	init();
    }

    public void init() {
	this.setLayout(null);
	componentsList = new ArrayList<Component>();
	if(template.equals(AudasaPreferences.A3_DIMENSIONES) || 
		template.equals(AudasaPreferences.A3_DIMENSIONES_LOCALIZADOR)) {
	    componentsList = TemplatePanel.create(AudasaPreferences.A3_DIMENSIONES);
	} else {
	    componentsList = TemplatePanel.create(AudasaPreferences.A4_CONSULTAS);
	}
	for(Component c : componentsList) {
	    this.add(c, null);
	    if(c instanceof JButton) {
		((JButton) c).addActionListener(this);
	    }
	}
    }

    public AudasaTemplate getAudasaTemplateVariables() {
	return audasaTemplate;
    }

    public void actionPerformed(ActionEvent e) {
	for(Component c : componentsList) {
	    if(c instanceof JTextField) {
		audasaTemplate.setProperty(c.getName(), ((JTextField) c).getText());
	    }
	}
	PluginServices.getMDIManager().closeWindow(this);
    }

    public WindowInfo getWindowInfo() {
	if (winfo == null) {
	    winfo = new WindowInfo(WindowInfo.MODALDIALOG);
	    winfo.setHeight(HEIGHT);
	    winfo.setTitle("Variables del mapa");
	    winfo.setWidth(WIDTH);
	}
	return winfo;
    }

    public Object getWindowProfile() {
	return WindowInfo.DIALOG_PROFILE;
    }

}
