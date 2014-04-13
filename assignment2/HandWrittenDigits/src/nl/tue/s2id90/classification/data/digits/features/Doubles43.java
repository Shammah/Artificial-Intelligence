package nl.tue.s2id90.classification.data.digits.features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import nl.tue.s2id90.classification.data.digits.LabeledImage;

/**
 *
 * @author J.P.H. Snoeren, 0772658
 * @since 11-apr-2014
 */
public class Doubles43 extends Doubles {

    public Doubles43(LabeledImage labeledImage) {
        super(labeledImage);
        List<Double> valuesList = new ArrayList<>(values.length);
        for (Double value : values) {
            valuesList.add(value);
        }
        valuesList.add(nrBlack());
        values = new Double[values.length + 1];
        int i = 0;
        for (Object value : valuesList) {
            values[i] = (Double) value;
            i ++;
        }
    }

    /**
     * Returns the average greyscale value of {@code this}.
     * @return the average greyscale value of {@code this}.
     */
    public double average() {
        double result = 0;
        for (Double value : values) {
            result += value;
        }
        result = result / values.length;
        return result;
    }

    /**
     * Returns the number of black pixels. A pixel is considered black if it is
     * more black than white. That is if the value of the pixel is more than
     * 255/2 = 128.
     * @return the number of black pixels
     */
    public double nrBlack() {
        int result = 0;
        for (Double value : values) {
            if (value < 128) //pixel is black
            result += 1;
        }
        return result;
    }

}
