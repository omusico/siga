package org.gvsig.mapsheets.print.series.layout;

import java.awt.geom.Point2D;

import org.gvsig.mapsheets.print.series.utils.IMapSheetsIdentified;

import com.iver.cit.gvsig.project.documents.exceptions.SaveException;
import com.iver.cit.gvsig.project.documents.layout.fframes.FFrameView;
import com.iver.cit.gvsig.project.documents.layout.fframes.IFFrame;
import com.iver.cit.gvsig.project.documents.layout.gui.Layout;
import com.iver.utiles.XMLEntity;

/**
 * Special view frame to prevent the user from resizing it.
 * 
 * @author jldominguez
 *
 */
public class MapSheetFrameView extends FFrameView implements IMapSheetsIdentified{
	
	
	public MapSheetFrameView() {
		super();
		setId(System.currentTimeMillis());
	}
	
    /**
     * Devuelve un entero que representa donde esta contenido el punto que se
     * pasa como par�metro.
     *
     * @param p punto a comparar.
     *
     * @return entero que representa como esta contenido el punto.
     */
    public int getContains(Point2D p) {
    	if (getBoundingBox(null).contains(p.getX(), p.getY())) {
            return RECT;
        } else {
        	return NOSELECT;
        }
    }
    
	public IFFrame cloneFFrame(Layout lyt) {
		// return super.cloneFFrame(lyt);
		return this;
	}

	/**
	 * helps in persistency 
	 */
	private long msid = -1;
	public long getId() {
		return msid;
	}

	public void setId(long id) {
		msid = id;
		try { Thread.sleep(40); } catch (Exception e) {}
	}

    public XMLEntity getXMLEntity() throws SaveException {
        XMLEntity xml = super.getXMLEntity();
        xml.remove("className");
        xml.putProperty("className", MapSheetFrameView.class.getName());
        return xml;
    }

}
