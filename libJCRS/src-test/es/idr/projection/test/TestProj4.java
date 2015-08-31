package es.idr.projection.test;

import java.awt.geom.Point2D;

import org.cresques.cts.ICoordTrans;
import org.gvsig.crs.CrsException;
import org.gvsig.crs.CrsFactory;
import org.gvsig.crs.ICrs;

public class TestProj4 {
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {	        	

			System.out.println("java.library.path:"+System.getProperty("java.library.path"));
			System.out.println("PROJ_LIB:"+System.getenv("PROJ_LIB"));
					
    		String strEpsgCodeSrc, strEpsgCodeDest;
    		// He tenido que incluir un mecanismo para valores concretos de parametros
    		strEpsgCodeSrc="EPSG:23030";  // ED50 UTM 30
    		strEpsgCodeDest="EPSG:25830";  // ETRS89 UTM 30 
    		ICrs crs=null;
    		//CoordinateReferenceSystem source;
    		//source = CRS.decode(strEpsgCode);
        	crs = new CrsFactory().getCRS(strEpsgCodeSrc); 
    		
        	System.out.println(crs.getWKT());        	
			System.out.println(crs.getProj4String()); //proj4.exportToProj4(crs));
			
			ICrs crs2 = new CrsFactory().getCRS(strEpsgCodeDest);
			
			// Origen= 40 grados lat y -1 grado longitud en ED50
			Point2D.Double ptOrig = new Point2D.Double(670733.214,4429748.958); 
			
			// Segun la calculadora de IGN, debería dar
			// 670623.740, 4429540.085 en ETRS89
			
			Point2D ptDest;
			ICoordTrans cTrans = crs.getCT(crs2);
			Point2D p = cTrans.convert(ptOrig, null);
			System.out.println("pOrig=" + ptOrig + " pDest=" + p.toString());
			
			System.out.println("Code: " + crs2.getProj4String());
			
			crs.setTransformationParams("+nadgrids=sped2et.gsb", null);
			ICoordTrans cTrans2 = crs.getCT(crs2);
			ptDest = cTrans2.convert(ptOrig, null);
			System.out.println("Con rejilla: pOrig=" + ptOrig + " pDest=" + ptDest.toString());
			System.out.println("Debería dar: 670623.740, 4429540.085 en ETRS89");
			System.out.println("Code orig: " + crs.getProj4String());

			ptOrig.setLocation(731720, 4371764); // En el puerto de Valencia 
			ptDest = cTrans2.convert(ptOrig, null);
			System.out.println("Con rejilla: pOrig=" + ptOrig + " pDest=" + ptDest.toString() + " Segun IGN: 731610, 4371554");
			System.out.println(cTrans2.getPOrig().getFullCode() + "-> " + cTrans2.getPDest().getFullCode());

				
					
		} catch (CrsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
