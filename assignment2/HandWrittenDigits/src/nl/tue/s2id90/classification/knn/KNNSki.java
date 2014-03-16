package nl.tue.s2id90.classification.knn;

import java.io.IOException;
import java.util.Map;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.ski.SkiFeatures;

/**
 * Concrete class defining distance for the Ski data.
 * @author Group 43
 * @since 16-mrt-2014
 */
public class KNNSki extends KNN43 {

    /**
     * Constructor.
     * @param trainingData training data
     * @param k parameter of k nearest neighbours
     * @throws IOException
     */
    public KNNSki(Map trainingData, int k) throws IOException {
        super(trainingData, k);
    }

    /**
     * Computes the distance simply by checking how many attributes differ.
     * @param f0 the first feature vector
     * @param f1 the second feature vector
     * @return the distance between the two feature vectors
     */
    @Override
    double distance(Features f0, Features f1) {
        SkiFeatures sf0 = (SkiFeatures) f0;
        SkiFeatures sf1 = (SkiFeatures) f1;
        final int n = sf0.getNumberOfAttributes();
        double d = 0;
        for (int i = 0; i < n; i++) {
            if (sf0.get(i) != sf1.get(i)) {
                d += 1;
            }
        }
        return d;
    }


}
