package es.icarto.gvsig.extgex.preferences;

public class GEXPreferences {

    private static GEXPreferences preferences;

    private GEXPreferences() {

    }

    public static GEXPreferences getPreferences() {
	if (preferences == null) {
	    preferences = new GEXPreferences();
	}
	return preferences;
    }

    public String getXMLFilePath() {
	return this.getClass().getClassLoader().getResource("data/extgex.xml")
		.getPath();
    }

}
