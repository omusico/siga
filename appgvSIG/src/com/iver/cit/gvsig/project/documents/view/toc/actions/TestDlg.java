/* gvSIG. Geographic Information System of the Valencian Government
*
* Copyright (C) 2007-2008 Infrastructures and Transports Department
* of the Valencian Government (CIT)
* 
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version.
* 
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
* 
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
* MA  02110-1301, USA.
* 
*/

/*
* AUTHORS (In addition to CIT):
* 2011 Software Colaborativo (www.scolab.es)   development
*/
 
package com.iver.cit.gvsig.project.documents.view.toc.actions;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.Popup;
import javax.swing.PopupFactory;

public class TestDlg {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		final JPopupMenu pop = new JPopupMenu();
		JMenuItem itUp = new JMenuItem("Move_above_layer");
		JMenuItem itDown = new JMenuItem("Move_below_layer");
		JMenuItem itGroup = new JMenuItem("Group_with_layer");

		itUp.setActionCommand("UP");
		itDown.setActionCommand("DOWN");
		itGroup.setActionCommand("GROUP");

		final JDialog dlgPop = new JDialog();
		dlgPop.setUndecorated(true);
		dlgPop.getContentPane().add(pop);
		


		ActionListener lis = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlgPop.dispose();
			}
		};
		
		itUp.addActionListener(lis);
		itDown.addActionListener(lis);
		itGroup.addActionListener(lis);

		pop.add(itUp);
		pop.add(itDown);
		pop.add(itGroup);

		
		dlgPop.setModal(true);
//		
		

		PointerInfo pi = MouseInfo.getPointerInfo();
		final Point p = pi.getLocation();
		dlgPop.pack();
//		dlgPop.setSize(600, 600);
//		dlgPop.setLocation(300, 300);

		pop.setLocation(p.x, p.y);
//		Popup pop2 = PopupFactory.getSharedInstance().getPopup(dlgPop, pop, p.x, p.y);
		pop.setInvoker(dlgPop);
		pop.setVisible(true);
		itDown.validate();
		dlgPop.setVisible(true);

	}

}

