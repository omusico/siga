package es.icarto.gvsig.extpm;

import com.iver.andami.plugins.Extension;

import es.icarto.gvsig.extpm.wrapperscadtools.InsertPointWrapper;

public class CreateNewPMFileExtension extends Extension {
    
    private InsertPointWrapper insertPointWrapper;

    @Override
    public void initialize() {
	insertPointWrapper = new InsertPointWrapper();
	insertPointWrapper.initialize();
    }

    @Override
    public void execute(String actionCommand) {
	insertPointWrapper.execute(actionCommand);
    }

    @Override
    public boolean isEnabled() {
	return insertPointWrapper.isEnabled();
    }

    @Override
    public boolean isVisible() {
	return insertPointWrapper.isVisible();
    }

}
