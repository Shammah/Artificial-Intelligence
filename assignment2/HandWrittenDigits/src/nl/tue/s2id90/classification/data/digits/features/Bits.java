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
     * a class that results in a feature vector that contains only binary
     * values, that is bytes with value 0 or 1.
     *
     * @see LabeledImage.getBinaryValues()
     *
     */
    public class Bits extends ImageFeatures<Byte> {

        /** constructs a binary image features object. If the supplied labeledImage
         *  is null, the resulting method is only suitable as a factory object as
         *  used in getDataset2.
         * @param labeledImage 
         * @see DigitsUtil#getDataset(Collection, ImageFeatures)
         * @see DigitsUtil#toBinary(byte[])  
         */
        public Bits(LabeledImage labeledImage) {
            if (labeledImage!=null) {
                values = DigitsUtil.toBinary(labeledImage.getValues());
            }
        }

        @Override
        public ImageFeatures<Byte> create(LabeledImage image) {
            return new Bits(image);
        }
        
        @Override
        public boolean isContinuous(int i) {
            return false;
        }
    }