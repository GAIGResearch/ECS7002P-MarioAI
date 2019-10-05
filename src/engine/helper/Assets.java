package engine.helper;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;


public class Assets {
    public static Image[][] mario;
    public static Image[][] smallMario;
    public static Image[][] fireMario;
    public static Image[][] enemies;
    public static Image[][] items;
    public static Image[][] level;
    public static Image[][] particles;
    public static Image[][] font;
    public static Image[][] map;
    final static String curDir = System.getProperty("user.dir");
    final static String img = curDir + "/img/";

    public static void init(GraphicsConfiguration gc) {
        try {
            mario = cutImage(gc, "mariosheet.png", 32, 32);
            smallMario = cutImage(gc, "smallmariosheet.png", 16, 16);
            fireMario = cutImage(gc, "firemariosheet.png", 32, 32);
            enemies = cutImage(gc, "enemysheet.png", 16, 32);
            items = cutImage(gc, "itemsheet.png", 16, 16);
            level = cutImage(gc, "mapsheet.png", 16, 16);
            particles = cutImage(gc, "particlesheet.png", 16, 16);
            font = cutImage(gc, "font.gif", 8, 8);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Image getImage(Image[][] collection, int x, int y, float scale) {
        return getImage(collection[x][y], scale);
    }

    public static Image getImage(Image img, float scale) {
        if (scale != 1) {
            int w = (int) (img.getWidth(null) * scale);
            int h = (int) (img.getHeight(null) * scale);
            BufferedImage resizedImage = new BufferedImage(w, h, TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(img, 0, 0, w, h, null);
            g.dispose();
            return resizedImage;
        }
        return img;
    }

    private static Image getImage(GraphicsConfiguration gc, String imageName) throws IOException {
        BufferedImage source = null;
        try {
            source = ImageIO.read(Assets.class.getResourceAsStream(imageName));
        } catch (Exception e) {
        }

        if (source == null) {
            imageName = img + imageName;
            File file = new File(imageName);
            source = ImageIO.read(file);
        }
        if (source == null) {
            File file = new File(imageName);
            ImageInputStream iis = ImageIO.createImageInputStream(file);
            String suffix = imageName.substring(imageName.length() - 3, imageName.length());
            ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
            reader.setInput(iis, true);
            source = reader.read(0);
        }
        Image image = gc.createCompatibleImage(source.getWidth(), source.getHeight(), Transparency.BITMASK);
        Graphics2D g = (Graphics2D) image.getGraphics();
        g.setComposite(AlphaComposite.Src);
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return image;
    }

    private static Image[][] cutImage(GraphicsConfiguration gc, String imageName, int xSize, int ySize) throws IOException {
        Image source = getImage(gc, imageName);
        Image[][] images = new Image[source.getWidth(null) / xSize][source.getHeight(null) / ySize];
        for (int x = 0; x < source.getWidth(null) / xSize; x++) {
            for (int y = 0; y < source.getHeight(null) / ySize; y++) {
                Image image = gc.createCompatibleImage(xSize, ySize, Transparency.BITMASK);
                Graphics2D g = (Graphics2D) image.getGraphics();
                g.setComposite(AlphaComposite.Src);
                g.drawImage(source, -x * xSize, -y * ySize, null);
                g.dispose();
                images[x][y] = image;
            }
        }

        return images;
    }

}