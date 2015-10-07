package es.icarto.gvsig.extgia.forms;

import javax.swing.JButton;

import com.jeta.forms.components.image.ImageComponent;
import com.jeta.forms.components.panel.FormPanel;

import es.icarto.gvsig.extgia.forms.images.AddImageListener;
import es.icarto.gvsig.extgia.forms.images.DeleteImageListener;
import es.icarto.gvsig.extgia.forms.images.ShowImageAction;

public class ImagesInForms {

    private final FormPanel formPanel;
    private final String tablename;
    private final String fk;

    public ImagesInForms(FormPanel formPanel, String tablename, String fk) {
	this.formPanel = formPanel;
	this.tablename = tablename;
	this.fk = fk;
    }

    protected ImageComponent imageComponent;
    protected JButton addImageButton;
    protected JButton deleteImageButton;

    protected AddImageListener addImageListener;
    protected DeleteImageListener deleteImageListener;

    public void setListeners() {
	imageComponent = (ImageComponent) formPanel
		.getComponentByName("element_image");
	addImageButton = (JButton) formPanel
		.getComponentByName("add_image_button");
	deleteImageButton = (JButton) formPanel
		.getComponentByName("delete_image_button");

	if (addImageListener == null) {
	    addImageListener = new AddImageListener(imageComponent,
		    addImageButton, tablename, fk);
	    addImageButton.addActionListener(addImageListener);
	}

	if (deleteImageListener == null) {
	    deleteImageListener = new DeleteImageListener(imageComponent,
		    addImageButton, tablename, fk);
	    deleteImageButton.addActionListener(deleteImageListener);
	}
    }

    public void removeListeners() {
	addImageButton.removeActionListener(addImageListener);
	deleteImageButton.removeActionListener(deleteImageListener);
    }

    public void fillSpecificValues(String fkValue) {
	if (addImageListener != null) {
	    addImageListener.setPkValue(fkValue);
	}

	if (deleteImageListener != null) {
	    deleteImageListener.setPkValue(fkValue);
	}

	// Element image
	new ShowImageAction(imageComponent, addImageButton, tablename, fk,
		fkValue);
    }

}
