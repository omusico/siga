package org.gvsig.mapsheets.print.series.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.mapsheets.print.series.fmap.MapSheetGrid;
import org.gvsig.mapsheets.print.series.fmap.MapSheetGridGraphic;
import org.gvsig.mapsheets.print.series.gui.utils.LayerComboItem;
import org.gvsig.mapsheets.print.series.gui.utils.SheetComboItem;
import org.gvsig.mapsheets.print.series.layout.MapSheetsLayoutTemplate;
import org.gvsig.mapsheets.print.series.utils.MapSheetsUtils;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.fmap.layers.FLayers;
import com.iver.cit.gvsig.fmap.layers.SelectionSupport;
import com.iver.cit.gvsig.project.documents.view.ProjectView;

/**
 * Dialog to browse map sheets (layout template will refresh)
 * and print them with various options.
 * 
 * @author jldominguez
 *
 */
public class PrintSelectionDialog extends JPanel implements IWindow, ActionListener, ListSelectionListener {

	private static Logger logger = Logger.getLogger(PrintSelectionDialog.class);
	private static String PDF_OUTPUT_DIR_FILE_CHOOSER_ID = "PDF_OUTPUT_DIR_FILE_CHOOSER_ID";

	public static final int WIDTH = 336+50;
	public static final int BUTTONS_PANEL_WIDTH = 300;
	public static final int HEIGHT = 370;
	
	private static final int BUTTON_LEN = 111;
	private static final int BUTTON_SEP = 15;
	
	private static final int MARGIN_LEFT = 15;
	private static final int MARGIN_TOP = 15;
	private static final int COMPONENT_SEP = 35;
	private static final int COMPONENT_SEP_small = 30;
	
	private static final int BUTTON_WIDTH_LONG = 130;
	private static final int PRINTER_CHK_WIDTH = 35 - BUTTON_SEP;
	private static final int BUTTON_HEIGHT = 26;

	private MapSheetsLayoutTemplate layout_template = null;
	private WindowInfo winfo = null;
	
	private JPanel sheetListPanel = null;
	private JPanel otherSettingsPanel = null;
	private JPanel buttonsPanel = null;
	
	private JList sheetList = null;
	private JRadioButton printAllRB = null;
	private JRadioButton printSelRB = null;
	private JCheckBox useThisBackLayerChk = null;
	private JComboBox backLayerCombo = null;

	private JCheckBox printPdfCheckbox;
	private JLabel pdfOutputDirLabel;
	private JTextField pdfOutputDirTextfield;
	private JButton pdfOutputDirButton;
	private JFileChooser pdfOutputDirFileChooser;
	
	private JButton printPrinterButton = null;
	private JCheckBox winPrinterSettsChk = null;
	private JPanel winPrinterSettsPanel = null;
	private boolean isWindows = false;
	
	private JButton closeButton = null;
	private JScrollPane sheetScroll = null;
	
	public WindowInfo getWindowInfo() {
		
		if (winfo == null) {
			winfo = new WindowInfo(WindowInfo.MODELESSDIALOG);
			winfo.setTitle(PluginServices.getText(this, "Browse_print_sheets"));
			winfo.setHeight(HEIGHT);
			winfo.setWidth(WIDTH);
		}
		return winfo;
	}

	
	public Object getWindowProfile() {
		return WindowInfo.DIALOG_PROFILE;
	}
	
	public PrintSelectionDialog(MapSheetsLayoutTemplate lyt_template) {
		super();
		layout_template = lyt_template;
		init();
	}


	private void init() {
		
		isWindows = findOutWindows();
		
		this.setSize(WIDTH, HEIGHT);
		this.setLayout(new BorderLayout());

		this.add(getOtherSettingsPanel(), BorderLayout.EAST);
		
		try {
			this.add(getSheetListPanel(), BorderLayout.CENTER);
		} catch (Exception ex) {
			NotificationManager.addError("Getting sheet list. ", ex);
		}
		
		this.add(getButtonsPanel(), BorderLayout.SOUTH);
				
		try {
			loadBackListCombo(getBackLayerCombo());
		} catch (Exception ex) {
			NotificationManager.addError("While loading BG layer combo. ", ex);
		}		

	}


	private boolean findOutWindows() {
		String osname = System.getProperty("os.name");
		osname = osname.toLowerCase();
		return (osname.indexOf("windows") != -1);
	}


	private JPanel getButtonsPanel() {
		
		if (buttonsPanel == null) {
			
			buttonsPanel = new JPanel();
			buttonsPanel.setLayout(new FlowLayout());
			buttonsPanel.setPreferredSize(new Dimension(BUTTONS_PANEL_WIDTH, 40));
			// buttonsPanel.add(getAcceptButton(), null);
			buttonsPanel.add(getCancelButton(), null);

		}
		return buttonsPanel;
	}


	private void loadBackListCombo(JComboBox combo) {
		
		ProjectView pv = layout_template.getProjectView();
		FLayers lyrs = pv.getMapContext().getLayers();
		addAll(combo, lyrs, false);
	}

	private void addAll(JComboBox cmb, FLayers lyrs, boolean alsothis) {
		
		if (alsothis) {
			cmb.addItem(new LayerComboItem(lyrs));
		}
		
		int n = lyrs.getLayersCount();
		for (int i=0; i<n; i++) {
			if (lyrs.getLayer(i) instanceof FLayers) {
				addAll(cmb, (FLayers) lyrs.getLayer(i), true);
			} else {
				cmb.addItem(new LayerComboItem(lyrs.getLayer(i)));
			}
			
		}
	}


	private JList getSheetList() throws Exception {
		if (sheetList == null) {
			
			MapSheetGrid gr = layout_template.getGrid();
			int cont = gr.getSource().getShapeCount();
			DefaultListModel dlm = new DefaultListModel(); 
			for (int i=0; i<cont; i++) {
				SheetComboItem item = new SheetComboItem(
						(MapSheetGridGraphic) gr.getGraphic(i));
				int position = 0;
				if (dlm.getSize() > 0) {
					do {
						if (item.toString().compareTo(
								dlm.get(position).toString()) < 0) {
							break;
						}
						position++;
					} while (position < dlm.getSize());
				}
				dlm.add(position, new SheetComboItem(
						(MapSheetGridGraphic) gr.getGraphic(i)));
			}
			sheetList = new JList(dlm);
			// sheetList.setPreferredSize(new Dimension(100,100));
			sheetList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
			sheetList.addListSelectionListener(this);

			//int[] sheets = MapSheetsUtils.filterMapSheetGridSheets(dlm, gr, "MUNICIPIO", ValueFactory.createValue("TEST"));
			//sheetList.setSelectedIndices(sheets);
			
		}
		return sheetList;
	}







	/**
	 * This method initializes acceptButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JCheckBox getPrintPdfCheckBox() {
		if (printPdfCheckbox == null) {
			printPdfCheckbox = new JCheckBox(PluginServices.getText(null, "Print_as_pdf"));
			printPdfCheckbox.addActionListener(this);
			printPdfCheckbox.setBounds(
					MARGIN_LEFT,
					getPrintSelRB().getBounds().y + 4*COMPONENT_SEP/3,
					WIDTH / 3, 21);
			
		}
		return printPdfCheckbox;
	}
	
	/**
	 * This method initializes acceptButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getPrintPrinterButton(boolean less_space) {
		if (printPrinterButton == null) {
			printPrinterButton = new JButton(PluginServices.getText(null, "Print_button"));
			
			if (less_space) {
				printPrinterButton.setPreferredSize(new Dimension(
						BUTTON_WIDTH_LONG - 10, BUTTON_HEIGHT));
			} else {
				printPrinterButton.setPreferredSize(new Dimension(
						BUTTON_WIDTH_LONG + BUTTON_SEP + PRINTER_CHK_WIDTH, BUTTON_HEIGHT));
			}
			
			/*
			printPrinterButton.setBounds(new Rectangle(
					(WIDTH-BUTTON_SEP-2*BUTTON_LEN)/2,
					15,
					BUTTON_LEN, 26));
			*/
			printPrinterButton.addActionListener(this);
			
		}
		return printPrinterButton;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */
	private JButton getCancelButton() {
		if (closeButton == null) {
			closeButton = new JButton(PluginServices.getText(null, "Close"));
			closeButton.setPreferredSize(new Dimension(BUTTON_LEN, 26));
			closeButton.addActionListener(this);
		}
		return closeButton;
	}
	
	/**
	 * This method initializes the PDF output directory label
	 *
	 * @return javax.swing.JLabel
	 */
	private JLabel getPdfOutputDirLabel() {
		if (pdfOutputDirLabel == null) {
			pdfOutputDirLabel = new JLabel(PluginServices.getText(null, "Print_in_label"));
			pdfOutputDirLabel.setEnabled(false);
		}
		return pdfOutputDirLabel;
	}

	/**
	 * This method initializes the text field which displays the chosen PDF output directory
	 *
	 * @return javax.swing.JTextField
	 */
	private JTextField getPdfOutputDirTextField() {
		if (pdfOutputDirTextfield == null) {
			pdfOutputDirTextfield = new JTextField(System.getProperty("user.home"));
			pdfOutputDirTextfield.setBounds(new Rectangle(10, 0, 180, 21));
			pdfOutputDirTextfield.setEditable(false);
			pdfOutputDirTextfield.setEnabled(false);
		}
		return pdfOutputDirTextfield;
	}

	/**
	 * This method initializes the PDF output directory selection button
	 *
	 * @return javax.swing.JButton
	 */
	private JButton getPdfOutputDirButton() {
		if (pdfOutputDirButton == null) {
			pdfOutputDirButton = new JButton("...");
			pdfOutputDirButton.setEnabled(false);
			pdfOutputDirButton.setBounds(new Rectangle(200, 0, 30, 21));
			pdfOutputDirButton.addActionListener(this);
		}
		return pdfOutputDirButton;
	}


	private JPanel getOtherSettingsPanel() {
		
		if (otherSettingsPanel == null) {
			otherSettingsPanel = new JPanel();
			otherSettingsPanel.setLayout(new GridLayout(0,1));
			
			otherSettingsPanel.setBorder(BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "Print_options"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION,
					new Font("Dialog", Font.BOLD, 12),
					new Color(51, 51, 51)));
			
			otherSettingsPanel.setPreferredSize(new Dimension(250,140));
			otherSettingsPanel.add(this.getPrintAllRB());
			otherSettingsPanel.add(this.getPrintSelRB());
			
			otherSettingsPanel.add(new JLabel());
			
			ArrayList lis = new ArrayList();
			lis.add(getPrintAllRB());
			lis.add(getPrintSelRB());
			MapSheetsUtils.joinRadioButtons(lis);
			
			otherSettingsPanel.add(this.getUseThisBackLayerChk());
			
			JPanel auxp = new JPanel(new FlowLayout());
			auxp.add(getBackLayerCombo());
			otherSettingsPanel.add(auxp);
			
			otherSettingsPanel.add(new JLabel());

			otherSettingsPanel.add(getPrintPdfCheckBox());
			otherSettingsPanel.add(getPdfOutputDirLabel());
			auxp = new JPanel();
			auxp.setLayout(null);
			auxp.add(getPdfOutputDirTextField(), null);
			auxp.add(getPdfOutputDirButton(), null);
			otherSettingsPanel.add(auxp);

			pdfOutputDirFileChooser = new JFileChooser(PDF_OUTPUT_DIR_FILE_CHOOSER_ID, System.getProperty("user.home"));
			pdfOutputDirFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

			if (pdfOutputDirFileChooser.getLastPath() != null) {
				getPdfOutputDirTextField().setText(pdfOutputDirFileChooser.getLastPath().getAbsolutePath());
			}

			
			auxp = new JPanel(new FlowLayout());
			auxp.add(getPrintPrinterButton(getIsWindows()));
			if (getIsWindows()) {
				auxp.add(getWindowsPrinterSettsPanel());
			}
			otherSettingsPanel.add(auxp);


		}
		return otherSettingsPanel;
	}


	private JPanel getWindowsPrinterSettsPanel() {
		
		if (winPrinterSettsPanel == null) {
			winPrinterSettsPanel = new JPanel();
			winPrinterSettsPanel.setLayout(new GridLayout(1,2));
			
			winPrinterSettsPanel.add(getWindowsPrinterSettsChk());
			
			JLabel lbl = new JLabel();
			lbl.setToolTipText(PluginServices.getText(this, "Tip_show_printer_settings_dlg_before_printing"));
			ImageIcon ico = PluginServices.getIconTheme().get("printer-opts");
			lbl.setIcon(ico);
			winPrinterSettsPanel.add(lbl);
		}
		return winPrinterSettsPanel;
	}
	
	private JCheckBox getWindowsPrinterSettsChk() {
		
		if (winPrinterSettsChk == null) {
			winPrinterSettsChk = new JCheckBox();
		}
		return winPrinterSettsChk;
	}



	private boolean getIsWindows() {
		return isWindows;
	}
	
	public boolean getUserWantsPrinterSettings() {
		return getWindowsPrinterSettsChk().isSelected();
	}


	private JPanel getSheetListPanel() throws Exception {
		
		if (sheetListPanel == null) {
			
			sheetListPanel = new JPanel();
			sheetListPanel.setLayout(new BorderLayout());
			// sheetListPanel.setBounds(new Rectangle(5, 5, 631, 67));
			sheetListPanel.setBorder(BorderFactory.createTitledBorder(
					null, PluginServices.getText(this, "Preview"),
					TitledBorder.DEFAULT_JUSTIFICATION,
					TitledBorder.DEFAULT_POSITION,
					new Font("Dialog", Font.BOLD, 12),
					new Color(51, 51, 51)));
			
			sheetListPanel.add(getSheetsScroll(), BorderLayout.CENTER);
		}
		return sheetListPanel;
	}


	private JScrollPane getSheetsScroll() throws Exception {

		if (sheetScroll == null) {
			sheetScroll = new JScrollPane(getSheetList());
			sheetScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		}
		return sheetScroll;
	}
	

	public JRadioButton getPrintAllRB() {
		if (printAllRB == null) {
			printAllRB = new JRadioButton();
			printAllRB.setText(PluginServices.getText(this, "Print_all_sheets"));
			printAllRB.addActionListener(this);
			printAllRB.setBounds(
					MARGIN_LEFT, MARGIN_TOP,
					WIDTH / 3, 21);
		}
		return printAllRB;
	}


	public JRadioButton getPrintSelRB() {
		if (printSelRB == null) {
			printSelRB = new JRadioButton();
			printSelRB.setText(PluginServices.getText(this, "Print_sel_sheets"));
			printSelRB.addActionListener(this);
			printSelRB.setBounds(
					MARGIN_LEFT,
					getPrintAllRB().getBounds().y + COMPONENT_SEP,
					WIDTH / 3, 21);
		}
		return printSelRB;
	}


	public JCheckBox getUseThisBackLayerChk() {
		if (useThisBackLayerChk == null) {
			useThisBackLayerChk = new JCheckBox();
			useThisBackLayerChk.setText(PluginServices.getText(this,
					"Use_back_layer"));
			useThisBackLayerChk.addActionListener(this);
			useThisBackLayerChk.setBounds(
					MARGIN_LEFT,
					getPrintSelRB().getBounds().y + 4*COMPONENT_SEP/3,
					WIDTH / 3, 21);
		}
		return useThisBackLayerChk;
		
	}
	
	private int selectListMapsOnGrid() {

		try {
			Object[] values = this.getSheetList().getSelectedValues();
			SelectionSupport selection = layout_template.getGrid().getSelectionSupport();
			selection.clearSelection();

			for(Object value:values) {
				SheetComboItem item = (SheetComboItem) value;
				for (int i = 0; i < layout_template.getGrid().getTheMemoryDriver().getShapeCount(); i++) {
					MapSheetGridGraphic grid_item = layout_template.getGrid().getGraphic(i);
					if (item.getObject().equals(grid_item)) {
						selection.getSelection().set(i);
					}
				}
			}

			return values.length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;

	}
	private JComboBox getBackLayerCombo() {
		if (backLayerCombo == null) {
			backLayerCombo = new JComboBox();
			backLayerCombo.setEnabled(false);
//			backLayerCombo.setBounds(
//					2 * MARGIN_LEFT / 3,
//					getUseThisBackLayerChk().getBounds().y + COMPONENT_SEP_small,
//					180, 21);
			
		}
		return backLayerCombo;
	}


	public void actionPerformed(ActionEvent e) {
		
		Object src = e.getSource();
		
		if (src == getUseThisBackLayerChk()) {
			getBackLayerCombo().setEnabled(getUseThisBackLayerChk().isSelected());
			return;
		}
		
		if (src == getPrintPdfCheckBox()) {
			if (printPdfCheckbox.isSelected()) {
			    pdfOutputDirTextfield.setEnabled(true);
			    pdfOutputDirButton.setEnabled(true);
			    pdfOutputDirTextfield.setBackground(UIManager.getColor("TextField.background"));
			    pdfOutputDirLabel.setEnabled(true);
			} else {
			    pdfOutputDirTextfield.setEnabled(false);
			    pdfOutputDirButton.setEnabled(false);
			    pdfOutputDirTextfield.setBackground(UIManager.getColor("TextField.inactiveBackground"));
			    pdfOutputDirLabel.setEnabled(false);
			}
		}

		if (src == pdfOutputDirButton) {
		    int returnVal = pdfOutputDirFileChooser.showOpenDialog(this);

		    if (returnVal == JFileChooser.APPROVE_OPTION) {
		    pdfOutputDirTextfield.setText(pdfOutputDirFileChooser.getSelectedFile().getAbsolutePath());
		    }

		}
		
		if (src == getPrintPrinterButton(getIsWindows())) {

			if (!this.getPrintAllRB().isSelected()) {
				selectListMapsOnGrid();
			}

			if (printPdfCheckbox.isSelected()) {

				if (!new File(pdfOutputDirTextfield.getText()).canWrite()) {
					JOptionPane.showMessageDialog(this,
							PluginServices.getText(this, "Directory_not_writable"),
							PluginServices.getText(this, "Error"), JOptionPane.ERROR_MESSAGE);
					return;

				}

				LayerComboItem sel_back = null;

				if (this.getUseThisBackLayerChk().isSelected()) {
					sel_back = (LayerComboItem) getBackLayerCombo().getSelectedItem();
				}

				PrintTaskWindow.startPrintTask(
						layout_template,
						this.getPrintAllRB().isSelected(),
						sel_back == null ? null : sel_back.getLayer(),
						pdfOutputDirTextfield.getText(),
						PluginServices.getText(this, "sheet"), this, false);
				getBackLayerCombo().setEnabled(getUseThisBackLayerChk().isSelected());
				return;

			} else {

				LayerComboItem sel_back = null;
				FLayer b_l = null;

				PluginServices.getMDIManager().closeWindow(this);

				JOptionPane.showMessageDialog(
						layout_template,
						PluginServices.getText(this, "Se_va_a_cerrar_mapa_para_acelerar_proceso"),
						PluginServices.getText(this, "Print"),
						JOptionPane.INFORMATION_MESSAGE);

				PluginServices.getMDIManager().closeWindow(layout_template);

				if (this.getUseThisBackLayerChk().isSelected()) {
					sel_back = (LayerComboItem) getBackLayerCombo().getSelectedItem();
				}

				if (sel_back != null) {
					b_l = sel_back.getLayer();
					b_l.setVisible(true);
					MapSheetsUtils.addBackLayer(layout_template, b_l);
				}

				boolean p_all = getPrintAllRB().isSelected();
				layout_template.setPrintSelectedOnly(!p_all);

				MapSheetsUtils.printMapSheetsLayout(layout_template, getUserWantsPrinterSettings());

				if (b_l != null) {
					MapSheetsUtils.removeLayer(layout_template, b_l);
				}

			}
		}
		
		if (src == getCancelButton()) {
			PluginServices.getMDIManager().closeWindow(this);
		}

	}


	public void valueChanged(ListSelectionEvent e) {
		
		Object src = e.getSource();
		
		try {
			if ((src == this.getSheetList()) && (this.getSheetList().getSelectedValues().length == 1)) {
				SheetComboItem sci = (SheetComboItem) getSheetList().getSelectedValue();
				layout_template.updateWithSheet(sci.getObject());
			}
		} catch (Exception ex) {
			NotificationManager.addError("Getting sheet list. ", ex);
		}
		
	}
	

}
