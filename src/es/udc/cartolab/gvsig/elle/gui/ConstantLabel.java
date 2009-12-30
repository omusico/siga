package es.udc.cartolab.gvsig.elle.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import org.gvsig.gui.beans.controls.IControl;

import com.iver.andami.PluginServices;


public class ConstantLabel extends JLabel implements IControl {

	private ArrayList<ActionListener> actionCommandListeners = new ArrayList<ActionListener>();
	
	public ConstantLabel() {
		super();
		setText(PluginServices.getText(this, "all_prov"));
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
	}
	
	public void addActionListener(ActionListener listener) {
		// TODO Auto-generated method stub
		actionCommandListeners.add(listener);
	}

	public void removeActionListener(ActionListener listener) {
		// TODO Auto-generated method stub
		actionCommandListeners.remove(listener);
	}

	public Object setValue(Object value) {
		// TODO Poner lo que haga falta
		String text = value.toString() + " ";
		setText(text);
		return text;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "constants";
	}

	public void setName(String name) {
		// TODO Auto-generated method stub
		
	}

}
