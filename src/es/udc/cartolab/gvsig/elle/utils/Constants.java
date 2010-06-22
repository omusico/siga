package es.udc.cartolab.gvsig.elle.utils;

import java.util.ArrayList;
import java.util.List;

import com.iver.andami.PluginServices;
import com.iver.andami.ui.mdiFrame.MDIFrame;
import com.iver.andami.ui.mdiFrame.NewStatusBar;

public class Constants {

	private static Constants instance = null;

	List<String> municipios;
	String munCod;
	String entCod;
	String nucCod;

	private Constants() {

	}
	/**
	 * @return user defined constants, or null if they're not defined yet.
	 */
	public static Constants getCurrentConstants() {
		return instance;
	}

	public static Constants newConstants(String munCod, String entCod, String nucCod,
			List<String> municipios) {
		if (instance == null) {
			instance = new Constants();
		}
		instance.munCod = munCod;
		instance.entCod = entCod;
		instance.nucCod = nucCod;
		if (entCod==null) {
			instance.entCod="";
		}
		if (nucCod==null) {
			instance.nucCod="";
		}
		instance.municipios = municipios;
		if (!municipios.contains(munCod)) {
			instance.municipios.add(munCod);
		}
		instance.changeStatusBar();
		return instance;
	}

	public static Constants newConstants(String munCod, String entCod, String nucCod) {
		ArrayList<String> municipios = new ArrayList<String>();
		municipios.add(munCod);
		return newConstants(munCod, entCod, nucCod, municipios);
	}

	public String getMunCod() {
		return munCod;
	}

	public String getEntCod() {
		return entCod;
	}

	public String getNucCod() {
		return nucCod;
	}

	public List<String> getMunicipios() {
		return municipios;
	}

	private void changeStatusBar() {
		MDIFrame mF = (MDIFrame) PluginServices.getMainFrame();
		NewStatusBar footerStatusBar = mF.getStatusBar();
		String nuc = nucCod;
		if (nucCod==null || nucCod.equals("")) {
			nuc = "-";
		}
		String ent = entCod;
		if (entCod==null || entCod.equals("")) {
			ent = "-";
		}
		String text = PluginServices.getText(this, "status_mun_ent_nuc");
		text = String.format(text, munCod, ent, nuc);
		footerStatusBar.setMessage("constants", text);
	}

	public static void removeConstants() {
		if (instance!=null) {
			instance = null;
			MDIFrame mF = (MDIFrame) PluginServices.getMainFrame();
			NewStatusBar footerStatusBar = mF.getStatusBar();
			footerStatusBar.setMessage("constants", PluginServices.getText(null, "all_prov"));
		}
	}

}
