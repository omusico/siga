package es.icarto.gvsig.extgia;

import org.apache.log4j.Logger;

import com.iver.cit.gvsig.fmap.layers.FLyrVect;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.navtableforms.AbstractForm;

@SuppressWarnings("serial")
public class TaludesForm extends AbstractForm {

    public static final String ABEILLE_FILENAME = "taludes.xml";
    private FormPanel form;

    // private JCheckBox chb;
    // private JTextField cmp;
    // private ComponentEnablerListener componentEnablerListener;

    public TaludesForm(FLyrVect layer) {
	super(layer);
	initWindow();
    }

    private void initWindow() {
	this.viewInfo.setHeight(400);
	this.viewInfo.setWidth(500);
	this.viewInfo.setTitle("Taludes");
    }

    @Override
    public FormPanel getFormBody() {
	// System.out.println(System.getProperty("user.dir"));
	// System.out.println(PluginServices.getPluginServices(this)
	// .getPluginDirectory().getAbsolutePath());
	// System.out.println(this.getClass().getClassLoader()
	// .getResource("taludes.xml").toString());
	// System.out.println(Preferences.XMLDATAFILE_PATH);
	// System.out.println(this.getClass().getResource("taludes.xml")
	// .toString());

	if (this.form == null) {
	    // this.form = new FormPanel(PluginServices.getPluginServices(this)
	    // .getPluginDirectory().getAbsolutePath()
	    // + "/" + "test.jfrm");
	    this.form = new FormPanel(TaludesForm.ABEILLE_FILENAME);
	}
	return this.form;
    }

    @Override
    public String getXMLPath() {
	return Preferences.XMLDATAFILE_PATH;
    }

    @Override
    public Logger getLoggerName() {
	return Logger.getLogger(this.getClass().getName());
    }

    @Override
    protected void fillSpecificValues() {
	// enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    }

    // private void enableComponentIfCheckBoxIsSelected(String chbName,
    // String cmpName) {
    //
    // if (chb.isSelected()) {
    // cmp.setEnabled(true);
    // } else {
    // cmp.setEnabled(false);
    // }
    // }

    // @Override
    // protected void setListeners() {
    // super.setListeners();
    //
    // HashMap<String, JComponent> widgets = getWidgetComponents();
    //
    // cmp = (JTextField) widgets.get("resultado");
    // chb = (JCheckBox) widgets.get("hay_anali");
    //
    // componentEnablerListener = new ComponentEnablerListener();
    // chb.addActionListener(componentEnablerListener);
    // }

    // @Override
    // protected void removeListeners() {
    // chb.removeActionListener(componentEnablerListener);
    // super.removeListeners();
    // }

    // public class ComponentEnablerListener implements ActionListener {
    //
    // public void actionPerformed(ActionEvent e) {
    // enableComponentIfCheckBoxIsSelected("hay_anali", "resultado");
    // }
    //
    // }

}
