package org.gvsig.mapsheets.print.series;

import java.awt.Component;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.gvsig.gui.beans.swing.JFileChooser;
import org.gvsig.mapsheets.print.audasa.AudasaPreferences;
import org.gvsig.mapsheets.print.series.fmap.MapSheetGrid;
import org.gvsig.mapsheets.print.series.gui.MapSheetSelectionDialog;
import org.gvsig.mapsheets.print.series.gui.MapSheetsSettingsPanel;
import org.gvsig.mapsheets.print.series.layout.MapSheetsLayoutTemplate;
import org.gvsig.mapsheets.print.series.layout.MapSheetsProjectMap;
import org.gvsig.mapsheets.print.series.tool.MapSheetsDragger;
import org.gvsig.mapsheets.print.series.utils.MapSheetsUtils;

import com.iver.andami.PluginServices;
import com.iver.andami.messages.NotificationManager;
import com.iver.andami.plugins.Extension;
import com.iver.andami.ui.mdiManager.IWindow;
import com.iver.cit.gvsig.ProjectExtension;
import com.iver.cit.gvsig.fmap.MapContext;
import com.iver.cit.gvsig.fmap.MapControl;
import com.iver.cit.gvsig.fmap.tools.Behavior.Behavior;
import com.iver.cit.gvsig.project.Project;
import com.iver.cit.gvsig.project.ProjectFactory;
import com.iver.cit.gvsig.project.documents.exceptions.OpenException;
import com.iver.cit.gvsig.project.documents.layout.ProjectMap;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.cit.gvsig.project.documents.view.ProjectView;
import com.iver.cit.gvsig.project.documents.view.ProjectViewFactory;
import com.iver.cit.gvsig.project.documents.view.gui.View;
import com.iver.utiles.GenericFileFilter;
import com.iver.utiles.XMLEntity;
import com.iver.utiles.xml.XMLEncodingUtils;
import com.iver.utiles.xmlEntity.generate.XmlTag;

/**
 * This extension deals with grid manipulation (except printing). Deals with
 * export to shapefile, adjustment tool and creation of layout template from
 * current grid.
 * 
 * 
 * @author jldominguez
 *
 */
public class MapSheetsManipulationExtension extends Extension {

	// private static MapSheetsDragger dragBBhvr = new MapSheetsDragger();
	
	public void execute(String comm) {
		
		 // =============================================================================
		 // =============================================================================
		 if (comm.compareToIgnoreCase("MAP_SHEETS_TO_SHP") == 0) {
			 
		        try {
		            IWindow w = PluginServices.getMDIManager().getActiveWindow();
		            

		            if (w instanceof View) {
		                View v = (View) w;
		                MapControl mc = v.getMapControl();
		                MapContext mx = mc.getMapContext();
		                ArrayList act_lyrs = MapSheetsUtils.getActiveLayers(mx.getLayers());
		                if ((act_lyrs.size() == 1) && (act_lyrs.get(0) instanceof MapSheetGrid)) {
		                	
		                	MapSheetGrid msg = (MapSheetGrid) act_lyrs.get(0);
		                	MapSheetsUtils.toSHP(msg, mx);
		                	
		                } else {
		                	throw new Exception("Did not find a MapSheetsGrid layer.");
		                }
		            }
		        } catch (Exception ex) {
		        	NotificationManager.addError("While exporting map sheets grid to SHP. ", ex);
		        }
			 
		 }
		 // =============================================================================
		 // =============================================================================
		 if (comm.compareToIgnoreCase("MAP_SHEETS_ADJUST") == 0) {
			 
		        try {
		            IWindow w = PluginServices.getMDIManager().getActiveWindow();

		            if (w instanceof View) {
		                View v = (View) w;
		                MapControl mc = v.getMapControl();
		                MapContext mx = mc.getMapContext();
			        	MapSheetGrid msg = MapSheetsUtils.getActiveMapSheetGrid(mx);
			        	if (msg != null) {
			        		MapSheetsDragger dragBBhvr = new MapSheetsDragger(v);  
				        	dragBBhvr.setGrid(msg, false);
				        	String back_to_tool = setGridTool(mc, dragBBhvr);
				        	dragBBhvr.setPreviousTool(back_to_tool);
				        	dragBBhvr.setListener(mx);
				        	
				        	mc.drawMap(true);
			        	}
		            } else {
		            	throw new Exception("Windows is not a view");
		            }
		        } catch (Exception ex) {
		        	NotificationManager.addError("While setting adjust tool.", ex);
		        }
			 
			 
		 }
		 // =============================================================================
		 // =============================================================================
		 if (comm.compareToIgnoreCase("MAP_SHEETS_TO_TEMPLATE") == 0) {
			 
			 IWindow w = PluginServices.getMDIManager().getActiveWindow();
			 if (w instanceof View) {
				 View v = (View) w;
				 MapControl mc = v.getMapControl();
				 MapContext mx = mc.getMapContext();
				 
				 MapSheetSelectionDialog dlg = new MapSheetSelectionDialog(mx, null);
//				 PluginServices.getMDIManager().addWindow(dlg);
				 
				 Object[] grid_auxlyt = dlg.getSelectedAndAuxLayout();
				 MapSheetGrid msg = (MapSheetGrid) grid_auxlyt[0]; 
				 Layout auxlayout = (Layout) grid_auxlyt[1];
				 
				 if (msg != null) {
					 
//					 double left_cm = 0.1 * dlg.getLeftMargin();
//					 double top_cm = 0.1 * dlg.getTopMargin();
				     	 double left_cm = AudasaPreferences.VIEW_X_POSITION;
				     	 double top_cm = AudasaPreferences.VIEW_Y_POSITION;
					 ArrayList act_flds = dlg.getActiveFieldsList();
					 ArrayList act_flds_tem = dlg.getActiveFieldsTemplateList();
					 ArrayList act_flds_idx = dlg.getActiveFieldsIndexList();
					 
					 // x
					 // MapSheetsLayoutTemplate
					 // msg.setVisible(false);
					 Project p = v.getModel().getProject();
					 
					 ProjectView _pv = (ProjectView) v.getModel();
					 MapContext _mc = _pv.getMapContext();
					 MapContext _omc = _pv.getMapOverViewContext();
					 
					 MapContext clo_mc =
						 MapSheetsUtils.cloneMapContextRemoveGrids(_mc);
					 MapContext clo_omc =
						 MapSheetsUtils.cloneMapContextRemoveGrids(_omc);
					 
					 Dimension aux_dim = _mc.getViewPort().getImageSize();
					 clo_mc.getViewPort().setImageSize(aux_dim);
					 
					 ProjectView cloned_pv = new ProjectView();
					 
					 cloned_pv.setName("");
					 cloned_pv.setProjectDocumentFactory(new ProjectViewFactory());
					 cloned_pv.setMapContext(clo_mc);
					 cloned_pv.setMapOverViewContext(clo_omc);
					 
					 // FLayers ll = cloned_pv.getMapContext().getLayers();
					 // MapSheetsUtils.setGridsToVisible(ll, false);
					 
					 double wh_ratio = 1;
					 
					 try {
						wh_ratio = MapSheetsUtils.getWHRatio(msg);
					} catch (Exception e) {
						NotificationManager.addError(e);
					}
					 
					 MapSheetsUtils.initViewPort(cloned_pv, wh_ratio);

					 MapSheetsLayoutTemplate mslt = 
						 new MapSheetsLayoutTemplate(msg, cloned_pv, auxlayout, null);
					 // mslt.setGrid(msg);
					 ProjectMap _pmap = ProjectFactory.createMap("Sheets layout");
					 
					 MapSheetsProjectMap mspm = new MapSheetsProjectMap();
					 mspm.setName("Layout Template " + MapSheetsLayoutTemplate.nextId());
					 mspm.setProjectDocumentFactory(_pmap.getProjectDocumentFactory());
					 
					 mspm.setModel(mslt);
					 mslt.setProjectMap(mspm);
					 p.addDocument(mspm);
					 
					 mslt.init(act_flds,
						 act_flds_idx,
						 act_flds_tem,
						 left_cm,
						 top_cm,
						 false);
					 
					 PluginServices.getMDIManager().addWindow(mslt);
					 try {
						mslt.update(0);
					} catch (Exception e) {
						NotificationManager.addError(e);
					}
				 }
			 }			 
			 
		 }
		 // =============================================================================
		 // =============================================================================
	}

	public void initialize() {
		// TODO Auto-generated method stub

	}

	public boolean isEnabled() {
	    try {
		IWindow w = PluginServices.getMDIManager().getActiveWindow();

		if (w instanceof View) {
		    View v = (View) w;
		    MapControl mc = v.getMapControl();
		    MapContext mx = mc.getMapContext();
		    ArrayList act_lyrs = MapSheetsUtils.getActiveLayers(mx.getLayers());
		    return (act_lyrs.size() == 1) && (act_lyrs.get(0) instanceof MapSheetGrid); 
		}
	    } catch (Exception ex) {
		return false;
	    }

	    return false;		
	}

	public boolean isVisible() {
		return true;
	}
	
	
	 public static String setGridTool(MapControl mc, MapSheetsDragger drag_beha) {
		 
		 // remove
		 if (mc.hasTool(MapSheetsDragger.MAP_SERIES_SET_GRID_TOOL_ID)) {
			 mc.getNamesMapTools().remove(MapSheetsDragger.MAP_SERIES_SET_GRID_TOOL_ID);
		 }
		 
		 Behavior[] behs = { drag_beha };
		 mc.addMapTool(MapSheetsDragger.MAP_SERIES_SET_GRID_TOOL_ID, behs);
		 String resp = mc.getCurrentTool();
		 mc.setTool(MapSheetsDragger.MAP_SERIES_SET_GRID_TOOL_ID);
		 mc.repaint();
		 return resp;
	}

}