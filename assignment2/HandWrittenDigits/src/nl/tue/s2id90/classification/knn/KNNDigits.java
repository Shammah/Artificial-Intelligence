package nl.tue.s2id90.classification.knn;

import java.io.IOException;
import java.util.Map;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.digits.features.ImageFeatures;

/**
 * This class determines the distance for Images.
 *
 * @author Group 43
 * @since 16-mrt-2014
 */
public class KNNDigits extends KNN43 {

    /**
     * Constructor.
     *
     * @param trainingData training data
     * @param k parameter of k nearest neighbors
     * @throws IOException
     */
    public KNNDigits(Map trainingData, int k) throws IOException {
        super(trainingData, k);
    }

    /**
     * Uses the sum of squared distance to compute the distance for Images.
     *
     * @param f0 features of the first image
     * @param f1 features of the second image
     * @return the distance between the two feature vectors
     */
    @Override
    double distance(Features f0, Features f1) {
        ImageFeatures<Double> if0 = (ImageFeatures<Double>) f0;
        ImageFeatures<Double> if1 = (ImageFeatures<Double>) f1;
        final int n = if0.getNumberOfAttributes();
        double d = 0;
        for (int i = 0; i < n; i++) {
            double h = if0.get(i) - if1.get(i);
            d += h * h;
        }
        return d;
    }

}
