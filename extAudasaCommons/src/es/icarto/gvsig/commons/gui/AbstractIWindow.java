/*
Copyright (C) 2013-2014  Cartolab. (Universade da Coruña)
Copyright (C) 2014 iCarto

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.icarto.gvsig.commons.gui;

import java.awt.Dimension;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.andami.ui.mdiManager.WindowInfo;

@SuppressWarnings("serial")
/**
 * A gvsig window that autocalculate its size and adds a AcceptCancelPane dock to the south of the panel
 * @author Francisco Puga <fpuga (at) icarto.es
 */
public abstract class AbstractIWindow extends JPanel implements IWindow {

    private WindowInfo windowInfo;
    private String title = "";
    private int windowInfoProperties = WindowInfo.MODALDIALOG;

    public AbstractIWindow() {
	super(new MigLayout("insets 10"));
    }

    public void openDialog() {
	if (getWindowInfo().isModeless()) {
	    PluginServices.getMDIManager().addCentredWindow(this);
	} else {
	    PluginServices.getMDIManager().addWindow(this);
	}
    }

    @Override
    public Object getWindowProfile() {
	return WindowInfo.DIALOG_PROFILE;
    }

    @Override
    public WindowInfo getWindowInfo() {
	if (windowInfo == null) {
	    windowInfo = new WindowInfo(windowInfoProperties);

	    windowInfo.setTitle(title);
	    Dimension dim = getPreferredSize();
	    // To calculate the maximum size of a form we take the size of the
	    // main frame minus a "magic number" for the menus, toolbar, state
	    // bar
	    // Take into account that in edition mode there is less available
	    // space
	    MDIFrame a = (MDIFrame) PluginServices.getMainFrame();
	    final int MENU_TOOL_STATE_BAR = 205;
	    int maxHeight = a.getHeight() - MENU_TOOL_STATE_BAR;
	    int maxWidth = a.getWidth() - 15;

	    int width, heigth = 0;
	    if (dim.getHeight() > maxHeight) {
		heigth = maxHeight;
	    } else {
		heigth = new Double(dim.getHeight()).intValue();
	    }
	    if (dim.getWidth() > maxWidth) {
		width = maxWidth;
	    } else {
		width = new Double(dim.getWidth()).intValue();
	    }

	    // getPreferredSize doesn't take into account the borders and other
	    // stuff
	    // introduced by Andami, neither scroll bars so we must increase the
	    // "preferred"
	    // dimensions
	    windowInfo.setWidth(width + 25);
	    windowInfo.setHeight(heigth + 15);
	}
	return windowInfo;
    }

    protected void setWindowTitle(String title) {
	this.title = title;
    }

    protected void setWindowInfoProperties(int properties) {
	this.windowInfoProperties = properties;
    }

    protected void addAcceptCancelPanel(ActionListener accept,
	    ActionListener cancel) {
	AcceptCancelPanel acceptCancelPanel = new AcceptCancelPanel(accept,
		cancel);
	add(acceptCancelPanel, "dock south");
    }
}
