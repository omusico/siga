/*
 * Copyright (c) 2010. CartoLab, Universidad de A Coruña
 *
 * This file is part of extDBConnection
 *
 * extDBConnection is free software: you can redistribute it and/or modify it under the terms
 * of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or any later version.
 *
 * extDBConnection is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with extDBConnection.
 * If not, see <http://www.gnu.org/licenses/>.
*/
package es.udc.cartolab.gvsig.users;

import java.util.Set;

import com.iver.andami.PluginServices;
import com.iver.andami.plugins.Extension;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.drivers.DBException;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class CloseSessionExtension extends Extension {

	public void execute(String actionCommand) {
		DBSession dbs = DBSession.getCurrentSession();
		if (dbs!=null) {
			try {

				if (!dbs.askSave()) {
					return;
				}
				dbs.close();

				ProjectExtension pExt = (ProjectExtension) PluginServices.getExtension(ProjectExtension.class);
				pExt.execute("NUEVO");
				stopFakeRequestsThread();

			} catch (DBException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void stopFakeRequestsThread() {
	    Thread fakeRequestsThread = getThreadByName("fake_requests");
	    fakeRequestsThread.interrupt();
	    fakeRequestsThread = null;
	}

	public Thread getThreadByName(String threadName) {
	    Thread thread = null;

	    Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
	    Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);

	    for (int i = 0; i < threadArray.length; i++) {
	        if (threadArray[i].getName().equalsIgnoreCase(threadName))
	            thread =  threadArray[i];
	    }
	    return thread;
	}


	public void initialize() {
		registerIcons();
	}

	protected void registerIcons() {
		PluginServices.getIconTheme().registerDefault(
				"DBClose",
				this.getClass().getClassLoader().getResource("images/sessiondisc.png")
			);
	}

	public boolean isEnabled() {
		return true;
	}

	public boolean isVisible() {
		DBSession dbs = DBSession.getCurrentSession();
		return dbs!=null;
	}

}
