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

        valuesList.add(average());
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

}
