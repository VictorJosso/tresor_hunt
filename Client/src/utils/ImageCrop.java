package utils;

import javafx.scene.image.Image;

public class ImageCrop {
    private final Image image;
    private final int width;
    private final int height;
    private final int cropStartX;
    private final int cropStartY;
    private final int cropWidth;
    private final int cropHeight;

    public ImageCrop(Image image, int width, int height, int cropStartX, int cropStartY, int cropWidth, int cropHeight) {
        System.out.println("ImageCrop() called with: image = [" + image + "], width = [" + width + "], height = [" + height + "], cropStartX = [" + cropStartX + "], cropStartY = [" + cropStartY + "], cropWidth = [" + cropWidth + "], cropHeight = [" + cropHeight + "]");
        this.image = image;
        this.width = width;
        this.height = height;
        this.cropStartX = cropStartX;
        this.cropStartY = cropStartY;
        this.cropWidth = cropWidth;
        this.cropHeight = cropHeight;
    }

    public Image getImage() {
        return image;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getCropStartX() {
        return cropStartX;
    }

    public int getCropStartY() {
        return cropStartY;
    }

    public int getCropWidth() {
        return cropWidth;
    }

    public int getCropHeight() {
        return cropHeight;
    }
}
