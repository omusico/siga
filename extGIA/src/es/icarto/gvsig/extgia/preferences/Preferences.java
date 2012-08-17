package es.icarto.gvsig.extgia.preferences;

public class Preferences {

    private static Preferences preferences;

    private Preferences() {

    }

    public static Preferences getPreferences() {
	if (preferences == null) {
	    preferences = new Preferences();
	}
	return preferences;
    }

    public String getXMLFilePath() {
	return this.getClass().getClassLoader().getResource("data/audasa.xml")
		.getPath();
    }

}
