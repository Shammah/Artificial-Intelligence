package nl.tue.s2id90.classification.data.digits.features;

import nl.tue.s2id90.classification.data.digits.LabeledImage;
import nl.tue.s2id90.classification.data.digits.DigitsUtil;

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
    /**
     * a class that results in a feature vector that contains double
     * values in the range [0..255]
     *
     * @see LabeledImage.getBinaryValues()
         *
     */
    public class Doubles extends ImageFeatures<Double> {
        /** constructs a doubles image features object. If the supplied labeledImage
         *  is null, the resulting method is only suitable as a factory object as
         *  used in getDataset2.
         * @param labeledImage 
         * @see DigitsUtil#getDataset(Collection, ImageFeatures)
         * @see DigitsUtil#toDoubles(byte[]) 
         */
        public Doubles(LabeledImage labeledImage) {
            if (labeledImage!=null) {
                values = DigitsUtil.toDoubles(labeledImage.getValues());
            }
        }

        @Override
        public ImageFeatures<Double> create(LabeledImage image) {
            return new Doubles(image);
        }
    }