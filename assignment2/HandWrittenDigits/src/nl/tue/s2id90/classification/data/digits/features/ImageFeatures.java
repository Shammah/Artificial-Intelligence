package nl.tue.s2id90.classification.data.digits.features;

import java.util.Arrays;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.digits.LabeledImage;

/**
 * This class is used as a wrapper around a LabeledImage to convert the image
 * to a feature vector. Some inner classes are provided for obtaining the data
 * in a specific type. See below for an example usage.  
 * <pre>
 *{@code
 * LabeledImage image = ...
 * ...
 * ImageFeatures<Double> featureVector = new ImageFeatures.Double(image);
 * }
 * </pre>
 *
 * @author huub
 * @param <T> type used for all the individual features in this feature vector
 * @see Features
 * @see LabeledImage
 * @see ImageFeatures
 */
public abstract class ImageFeatures<T> implements Features {
    protected T[] values;
    Integer hashCode;

    /**
     * factory method to create an instance of this particular ImageFeature
     * class. It would have been nice if this method was static. But java does
     * not supply abstract static methods.
     *
     * @param image labeled image that supplies the features
     * @return an ImageFeature<T> object
     */
    public abstract ImageFeatures<T> create(LabeledImage image);

    public T[] getValues() {
        return values;
    }

    @Override
    public T get(int i) {
        return values[i];
    }

    @Override
    public int getNumberOfAttributes() {
        return values.length;
    }

    @Override
    public boolean isContinuous(int i) {
        return true;
    }

    @Override
    public String getName(int i) {
        return Integer.toString(i);
    }

    @Override
    public int hashCode() {
        if (hashCode == null) { // we assume that the array never changes
            hashCode = 3;
            hashCode = 23 * hashCode + Arrays.deepHashCode(this.values);
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ImageFeatures<?> other = (ImageFeatures<?>) obj;
        return Arrays.deepEquals(this.values, other.values);
    }
}
