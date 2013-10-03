package es.icarto.gvsig.extgia.utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public abstract class ImageUtils {

    public static final Color NOT_ENABLED_COLOR = new Color(240, 240, 240);

    public static byte[] convertImageToBytea(BufferedImage image) throws IOException {
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	ImageIO.write(image, "png", baos);
	baos.flush();
	byte[] imageBytes = baos.toByteArray();
	baos.close();
	return imageBytes;
    }

    public static byte[] convertImageToBytea(File image) throws IOException {
	BufferedImage bufferedImage = ImageIO.read(image);
	return convertImageToBytea(bufferedImage);
    }

    public static BufferedImage convertByteaToImage(byte[] imageBytes) {
	InputStream in = new ByteArrayInputStream(imageBytes);
	try {
	    return ImageIO.read(in);
	} catch (IOException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public static BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	BufferedImage resizedImage = new BufferedImage(width, height, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, width, height, null);
	g.dispose();

	return resizedImage;
    }

    public static BufferedImage resizeImageWithHint(BufferedImage originalImage, int width, int height) {
	int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
	BufferedImage resizedImage = new BufferedImage(width, height, type);
	Graphics2D g = resizedImage.createGraphics();
	g.drawImage(originalImage, 0, 0, width, height, null);
	g.dispose();
	g.setComposite(AlphaComposite.Src);

	g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
		RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	g.setRenderingHint(RenderingHints.KEY_RENDERING,
		RenderingHints.VALUE_RENDER_QUALITY);
	g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);

	return resizedImage;
    }

}
