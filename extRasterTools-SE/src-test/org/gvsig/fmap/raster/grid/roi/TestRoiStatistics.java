package org.gvsig.fmap.raster.grid.roi;

import junit.framework.TestCase;

import org.gvsig.raster.buffer.RasterBufferInvalidException;
import org.gvsig.raster.dataset.IBuffer;
import org.gvsig.raster.grid.Grid;
import org.gvsig.raster.grid.GridException;
import org.gvsig.raster.grid.GridExtent;
import org.gvsig.raster.grid.OutOfGridException;

import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;

/*import com.iver.cit.gvsig.fmap.core.GeneralPathX;
import com.iver.cit.gvsig.fmap.core.IGeometry;
import com.iver.cit.gvsig.fmap.core.ShapeFactory;*/

/**
 * Test sobre la obtenci�n de estad�sticas de regiones de inter�s.
 * 
 *  @author Diego Guerrero Sevilla (diego.guerrero@uclm.es)
 *
 */
public class TestRoiStatistics extends TestCase {
	Grid grid = null;
	GridExtent extent = null;
	int cellSize = 1;
	double xMin = 0;
	double xMax = 10;
	double yMin = 0;
	double yMax = 10;
	VectorialROI roi = null;
	GeneralPathX path = null;
	IGeometry geometry = null;
	
	public void setUp() {
		int bands[] = {0};
		extent = new GridExtent();
		extent.setCellSize(cellSize);
		extent.setXRange(xMin, xMax);
		extent.setYRange(yMin, yMax);
		try {
			grid = new Grid(extent, extent, IBuffer.TYPE_DOUBLE, bands);
			for (int x = 0; x<5; x++)
				for (int y = 0; y<5; y++){
					grid.setCellValue(x, y, 0.0);
				}
			for (int x = 5; x<xMax; x++)
				for (int y = 0; y<5; y++){
					grid.setCellValue(x, y, 1.0);
				}
			for (int x = 0; x<5; x++)
				for (int y = 5; y<10; y++){
					grid.setCellValue(x, y, 2.0);
				}
			for (int x = 5; x<10; x++)
				for (int y = 5; y<10; y++){
					grid.setCellValue(x, y, 3.0);
				}
		} catch (RasterBufferInvalidException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OutOfGridException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
		}
		
	}

	public void start() {
		this.setUp();
		this.testStack();
	}

	public void testStack() {
		
		path = new GeneralPathX();
		
		
		path.moveTo(4,4);
		path.lineTo(4,0);
		path.lineTo(0,0);
		path.lineTo(0,4);
		path.closePath();
		
		geometry=ShapeFactory.createPolygon2D(path);
				
		roi = new VectorialROI(grid);
		roi.addGeometry(geometry);
		try {
			assertEquals(2.0,roi.getMaxValue(),0.0);
			assertEquals(2.0,roi.getMeanValue(),0.0);
			assertEquals(2.0,roi.getMinValue(),0.0);
		} catch (GridException e) {
			e.printStackTrace();
		}
		
		
		path = new GeneralPathX();
		
		path.moveTo(9,4);
		path.lineTo(9,2);
		path.lineTo(6,2);
		path.lineTo(6,4);
		path.closePath();
		
		geometry=ShapeFactory.createPolygon2D(path);
				
		//roi = new VectorialROI(grid);
		roi.clear();
		roi.addGeometry(geometry);
		try {
			assertEquals(3.0,roi.getMaxValue(),0.0);
			assertEquals(3.0,roi.getMeanValue(),0.0);
			assertEquals(3.0,roi.getMinValue(),0.0);
		} catch (GridException e) {
			e.printStackTrace();
		}
	}
}
