package ru.netology.graphics.image;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {
    private double maxRatio;
    private int maxWidth;
    private int maxHeight;
    private TextColorSchema schema;

    public TextGraphicsConverterImpl() {
        this.maxRatio = 0;
        this.maxWidth = 0;
        this.maxHeight = 0;
        this.schema = new TextColorSchemaImpl();
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        if (img == null) {
            throw new IOException("Не удалось загрузить изображение по URL: " + url);
        }

        double ratio = (double) img.getWidth() / img.getHeight();
        if (maxRatio > 0 && ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int originalWidth = img.getWidth();
        int originalHeight = img.getHeight();
        int newWidth = originalWidth;
        int newHeight = originalHeight;

        if (maxWidth > 0 && originalWidth > maxWidth) {
            double scaleFactor = (double) maxWidth / originalWidth;
            newWidth = maxWidth;
            newHeight = (int) (originalHeight * scaleFactor);
        }

        if (maxHeight > 0 && newHeight > maxHeight) {
            double scaleFactor = (double) maxHeight / newHeight;
            newHeight = maxHeight;
            newWidth = (int) (newWidth * scaleFactor);
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        graphics.dispose();

        WritableRaster bwRaster = bwImg.getRaster();
        StringBuilder result = new StringBuilder();
        int[] pixelArray = new int[3];

        for (int h = 0; h < newHeight; h++) {
            for (int w = 0; w < newWidth; w++) {
                int color = bwRaster.getPixel(w, h, pixelArray)[0];
                char c = schema.convert(color);

                result.append(c).append(c);
            }
            result.append("\n");
        }

        return result.toString();

    }

    @Override
    public void setMaxWidth(int width) {
        this.maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        this.maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
