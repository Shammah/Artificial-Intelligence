/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.data.digits;

import java.util.Arrays;

/**
 *
 * @author huub
 */
public class LabeledImage {
    private final int WIDTH, HEIGHT;   // dimensions of image
    private byte label;                // the classification class
    private final byte[] values;       // the grayscale values

    public LabeledImage(int cols, int rows, byte[] values) {
        this.WIDTH = cols;
        this.HEIGHT = rows;
        this.values = values;
    }

    /** @return the category in which this image is classified. **/
    public byte getLabel() {
        return label;
    }

    /** @return horizontal dimension of this image in pixels. **/
    public int getWidth() {
        return WIDTH;
    }

    /** @return vertical dimension of this image in pixels. **/
    public int getHeight() {
        return HEIGHT;
    }

    /** @return image data in a byte array. **/
    public byte[] getValues() {
        return values;
    }

   @Override
   public String toString() {
       String[] h = {"width =" + WIDTH,"height =" + HEIGHT, "label="+label};
       return Arrays.toString(h);
   }

    /** sets the classification label. **/
    public void setLabel(byte b) {
       label = b;
    }
}