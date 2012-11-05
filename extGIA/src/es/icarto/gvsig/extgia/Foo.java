package es.icarto.gvsig.extgia;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.crs.CRSFactory;
import com.iver.cit.gvsig.fmap.layers.FLayer;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.documents.ProjectDocument;
import com.iver.cit.gvsig.project.documents.ProjectDocumentFactory;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;

import es.udc.cartolab.gvsig.users.utils.DBSession;

public class Foo {
    private static boolean hecho = false;

    public void execute() {

	if (!hecho) {
	    // Properties p = new Properties();
	    // try {
	    // p.load(this.getClass().getClassLoader()
	    // .getResourceAsStream("db/db_config_local"));
	    // } catch (IOException e1) {
	    // // TODO Auto-generated catch block
	    // e1.printStackTrace();
	    // }
	    try {
		//		DBSession.createConnection("localhost", 5434, "audasa_test",
		//			"", "postgres", "postgres");
		//		createViewIfNeeded();
		FLayer layer = DBSession.getCurrentSession().getLayer(
			"taludes", "taludes", "audasa_extgia", null,
			CRSFactory.getCRS("EPSG:23029"));

		View view = (View) PluginServices.getMDIManager()
			.getActiveWindow();
		view.getMapControl().getMapContext().getLayers()
		.addLayer(layer);
	    } catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	    hecho = true;
	}
    }

    private View createViewIfNeeded() {

	// TODO: fpuga: Check what happens when exists a view in the project but
	// is not active
	IWindow iWindow = PluginServices.getMDIManager().getActiveWindow();
	View view = null;

	if (iWindow instanceof View) {
	    view = (View) iWindow;
	} else {

	    Project project = ((ProjectExtension) PluginServices
		    .getExtension(ProjectExtension.class)).getProject();
	    ProjectDocumentFactory viewFactory = Project
		    .getProjectDocumentFactory(ProjectViewFactory.registerName);
	    ProjectDocument projectDocument = viewFactory.create(project);
	    projectDocument.setName("ELLE View");
	    project.addDocument(projectDocument);
	    view = (View) projectDocument.createWindow();
	    view.getWindowInfo().setMaximized(true);
	    PluginServices.getMDIManager().addWindow(view);
	}
	return view;
    }

}
