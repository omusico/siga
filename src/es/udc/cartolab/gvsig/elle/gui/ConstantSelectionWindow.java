package es.udc.cartolab.gvsig.elle.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;
import com.jeta.forms.components.panel.FormPanel;

import es.udc.cartolab.gvsig.elle.utils.Constants;
import es.udc.cartolab.gvsig.users.utils.DBSession;

public class ConstantSelectionWindow extends JPanel implements IWindow, ActionListener {

	private WindowInfo viewInfo = null;
	private JComboBox municipioCB, entidadCB, nucleoCB;
	private JCheckBox municipioCHB;
	private JPanel northPanel = null;
	private JPanel centerPanel = null;
	private JPanel southPanel = null;
	private JButton okButton;
	private JButton cancelButton;

	//Constantes (deberia moverse a otro archivo general?
	private final String municipioTable = "municipio";
	private final String munCodField = "municipio";
	private final String entidadTable = "entidad_singular";
	private final String entCodField = "entidad";
	private final String nucleoTable = "nucleo_poblacion";
	private final String nucCodField ="poblamiento";
	private final String denominacion = "denominaci";
	private final String fase = "2005";

	public WindowInfo getWindowInfo() {
		if (viewInfo == null) {
			viewInfo = new WindowInfo(WindowInfo.MODELESSDIALOG | WindowInfo.PALETTE);
			viewInfo.setTitle(PluginServices.getText(this, "select_constants"));
			viewInfo.setWidth(425);
			viewInfo.setHeight(265);
		}
		return viewInfo;
	}

	public ConstantSelectionWindow() {
		init();
	}

	protected JPanel getCenterPanel() {
		if (centerPanel == null) {
			centerPanel = new JPanel();
			FormPanel form = new FormPanel("forms/constantSelection.jfrm");
			form.setFocusTraversalPolicyProvider(true);
			centerPanel.add(form);

			JLabel municipioLabel = form.getLabel("municipioLabel");
			municipioLabel.setText(PluginServices.getText(this, "municipio"));
			JLabel entidadLabel = form.getLabel("entidadLabel");
			entidadLabel.setText(PluginServices.getText(this, "entidad"));
			JLabel nucleoLabel = form.getLabel("nucleoLabel");
			nucleoLabel.setText(PluginServices.getText(this, "nucleo"));

			municipioCB = form.getComboBox("municipioCB");
			entidadCB = form.getComboBox("entidadCB");
			nucleoCB = form.getComboBox("nucleoCB");

			municipioCHB = form.getCheckBox("municipioCHB");
			municipioCHB.setText(PluginServices.getText(this, "adjacent_councils"));

			try {
				String text = PluginServices.getText(this, "all_prov");
				fillComboBox(text, municipioTable, munCodField, "WHERE fase='"+ fase +"'", municipioCB);
				municipioCB.addActionListener(this);
				municipioCB.setSelectedIndex(0);
			} catch (SQLException e) {
				e.printStackTrace();
			}


		}
		return centerPanel;
	}


	protected JPanel getNorthPanel() {

		//Set header if any
		//Current header (Pontevedra) size: 425x79
		if (northPanel == null) {
			northPanel = new JPanel();
			File iconPath = new File("gvSIG/extensiones/es.udc.cartolab.gvsig.elle/images/header.png");
			if (iconPath.exists()) {
				northPanel.setBackground(new Color(36, 46, 109));
				ImageIcon logo = new ImageIcon(iconPath.getAbsolutePath());
				JLabel icon = new JLabel();
				icon.setIcon(logo);
				northPanel.add(icon, BorderLayout.WEST);
			}
		}
		return northPanel;
	}

	protected JPanel getSouthPanel() {

		if (southPanel == null) {
			southPanel = new JPanel();
			FlowLayout layout = new FlowLayout();
			layout.setAlignment(FlowLayout.RIGHT);
			southPanel.setLayout(layout);
			okButton = new JButton(PluginServices.getText(this, "ok"));
			cancelButton = new JButton(PluginServices.getText(this, "cancel"));
			okButton.addActionListener(this);
			cancelButton.addActionListener(this);
			southPanel.add(okButton);
			southPanel.add(cancelButton);
		}
		return southPanel;
	}

	private void fillComboBox(String firstItem, String tableName, String codField,
			String whereClause, JComboBox comboBox) throws SQLException {

		DBSession dbs = DBSession.getCurrentSession();
		if (dbs != null) {

			Connection con = dbs.getJavaConnection();
			Statement stat = con.createStatement();
			String schema = dbs.getSchema();
			String query = "SELECT " + codField + ", " + denominacion + " FROM " + schema + "." + tableName;
			query = query + " " + whereClause + " ORDER BY " + codField + ";";

			comboBox.removeAllItems();

			if ((firstItem != null) && !firstItem.equals("")) {
				comboBox.addItem(firstItem);
			}

			ResultSet rs = stat.executeQuery(query);

			while (rs.next()) {
				String text = rs.getString(codField) + " - " + rs.getString("denominaci");
				comboBox.addItem(text);
			}
			rs.close();

		}
	}

	private String getCode(JComboBox cb) {
		String text = "";
		if (cb.getSelectedItem() != null) {
			text = cb.getSelectedItem().toString();
			text = text.substring(0, text.indexOf(" - "));
		}
		return text;
	}

	private void init() {

		GridBagLayout layout = new GridBagLayout();
		setLayout(layout);

		add(getNorthPanel(), new GridBagConstraints(0, 0, 1, 1, 0, 0,
				GridBagConstraints.NORTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getCenterPanel(), new GridBagConstraints(0, 1, 1, 1, 0, 1,
				GridBagConstraints.CENTER, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		add(getSouthPanel(), new GridBagConstraints(0, 2, 1, 1, 10, 0,
				GridBagConstraints.SOUTH, GridBagConstraints.BOTH,
				new Insets(0, 0, 0, 0), 0, 0));

		//enables tabbing navigation
		setFocusCycleRoot(true);
	}

	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == cancelButton) {
			PluginServices.getMDIManager().closeWindow(this);
		}
		if (event.getSource() == okButton) {
			if (municipioCB.getSelectedIndex() != 0) {
				//Get codes
				String munCod = getCode(municipioCB);
				String entCod = "";
				String nucCod = "";
				if ((entidadCB.getSelectedIndex()!=0) && (nucleoCB.getSelectedIndex()!=0)) {
					entCod = getCode(entidadCB);
					nucCod = getCode(nucleoCB);
				}
				//Close window
				PluginServices.getMDIManager().closeWindow(this);
				//hacer lo que haga falta
				if (municipioCHB.isSelected()) {
					SelectAdjacentsWindow win = new SelectAdjacentsWindow(munCod, entCod, nucCod);
					PluginServices.getMDIManager().addCentredWindow(win);
				} else {
					Constants cts = Constants.newConstants(munCod, entCod, nucCod);
				}
			} else {
				Constants.removeConstants();
				PluginServices.getMDIManager().closeWindow(this);
			}

		}
		if (event.getSource() == municipioCB) {
			if (municipioCB.getSelectedIndex() != 0) {
				String cod = getCode(municipioCB);
				try {
					entidadCB.removeActionListener(this);
					String text = PluginServices.getText(this, "all_ent");
					fillComboBox(text, entidadTable, entCodField,
							"WHERE fase='" + fase + "' AND " + munCodField + "='" + cod + "'",
							entidadCB);
					entidadCB.addActionListener(this);
					entidadCB.setSelectedIndex(0);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				municipioCHB.setEnabled(true);
			} else {
				entidadCB.removeActionListener(this);
				entidadCB.removeAllItems();
				entidadCB.addActionListener(this);
				municipioCHB.setEnabled(false);
			}
		}
		if (event.getSource() == entidadCB) {
			if (entidadCB.getSelectedIndex()!=0) {
				String entCod = getCode(entidadCB);
				String munCod = getCode(municipioCB);
				System.out.println("WHERE fase='" + fase + "' AND " + munCodField + "='" + munCod + "' AND "
						+ entCodField + "='" + entCod + "'");
				try {
					String text = PluginServices.getText(this, "all_nuc");
					fillComboBox(text, nucleoTable, nucCodField,
							"WHERE fase='" + fase + "' AND " + munCodField + "='" + munCod + "' AND "
							+ entCodField + "='" + entCod + "'",
							nucleoCB);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				nucleoCB.removeAllItems();
			}
		}
	}

	public Object getWindowProfile() {
		// TODO Auto-generated method stub
		return null;
	}
}
