/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.data.digits;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author huub
 */
public class DigitsUtil {

    /**
     * shows a grid of labeled images in a popup frame.
     *
     * @param images images to be shown.
     * @param title title of frame
     * @param cols number of columns in the grid
     *
     */
    public static void showImages(String title, List<LabeledImage> images, int cols) {
        JFrame frame = new JFrame(title);
        Container pane = frame.getContentPane();
        int rows = images.size() / cols;
        pane.setLayout(new GridLayout(rows, cols));
        for (LabeledImage img : images) {
            byte[] values = img.getValues();

            byte[] b = Arrays.copyOf(values, values.length);

            // flip values to get black digit on white background
            for (int j = 0; j < b.length; j++) {
                b[j] = (byte) (255 - b[j]);
            }

            int imageWidth = img.getWidth();
            BufferedImage image = convertToGrayscaleImage(imageWidth, b);
            final JButton button = new JButton("" + img.getLabel(), new ImageIcon(image));
            button.setMargin(new Insets(0, 0, 0, 0));
            pane.add(button);
        }
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private static BufferedImage convertToGrayscaleImage(int width, byte[] buffer) {
        int height = buffer.length / width;
        ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_GRAY);
        int[] nBits = {8};
        ColorModel cm = new ComponentColorModel(cs, nBits, false, true,
                Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
        SampleModel sm = cm.createCompatibleSampleModel(width, height);
        DataBufferByte db = new DataBufferByte(buffer, width * height);
        WritableRaster raster = Raster.createWritableRaster(sm, db, null);
        BufferedImage result = new BufferedImage(cm, raster, false, null);

        return result;
    }

    /**
     * converts an array of bytes to an array of doubles. The bytes are treated
     * as unsigned bytes.
     *
     * @param bytes an array of unsigned bytes
     * @return an array of doubles
     *
     */
    public static Double[] toDoubles(byte[] bytes) {
        Double[] result = new Double[bytes.length];
        for (int i = 0; i < result.length; i = i + 1) {
            byte v = bytes[i];
            result[i] = DigitsUtil.unsignedByteToDouble(v);
        }
        return result;
    }

    /**
     * converts an array of bytes to an array of integers. The bytes are treated
     * as unsigned bytes.
     *
     * @param bytes an array of unsigned bytes
     * @return an array of integers
     *
     */
    public static Integer[] toIntegers(byte[] bytes) {
        Integer[] result = new Integer[bytes.length];
        for (int i = 0; i < result.length; i = i + 1) {
            byte v = bytes[i];
            result[i] = DigitsUtil.unsignedByteToInt(v);
        }
        return result;
    }

    /**
     * converts an array of bytes to an array of bytes that are either 0 or 1.
     * The input bytes are treated as unsigned bytes and result in 0 if they are
     * less than 128 and 1, otherwise.
     *
     * @param bytes an array of unsigned bytes
     * @return an array of doubles converted from bytes
     *
     */
    public static Byte[] toBinary(byte[] bytes) {
        Byte[] result = new Byte[bytes.length];
        for (int i = 0; i < result.length; i = i + 1) {
            byte v = bytes[i];
            int h = DigitsUtil.unsignedByteToInt(v);
            if (h < 128) {
                result[i] = 0;
            } else {
                result[i] = 1;
            }
        }
        return result;
    }

    /**
     * method to wrap a collection of labeled images in a LabeledDataset2 object
     * containing objects of type ImageFeatures<T>. These objects are created
     * using object of the requested type as a factory to create new objects of
     * that type. A typical call to this method reads as follows.
     * <pre>
     *{@code
     *      LabeledDataset2<ImageFeatures<Double>,Byte> dataset;
     *      dataset = HandWrittenDigits.getDataset(images, new Doubles(null));
     * }
     *
     * /* cache the Integer objects (maximum 256 elements)
     */
    private static final HashMap<Byte, Integer> b2i = new HashMap<>(256);

    private static Integer unsignedByteToInt(Byte b) {
        Integer result = b2i.get(b);
        if (result == null) {
            result = b & 0xFF;
            b2i.put(b, result);
        }
        return result;
    }

    /* cache the Double objects (maximum 256 elements) */
    private static final HashMap<Byte, Double> b2d = new HashMap<>(256);

    private static Double unsignedByteToDouble(Byte b) {
        Double result = b2d.get(b);
        if (result == null) {
            result = (double) (b & 0xFF);
            b2d.put(b, result);
        }
        return result;
    }
}
