package nl.tue.s2id90.classification.knn;

import java.io.IOException;
import java.util.Map;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.golf.GolfFeatures;

/**
 *
 * @author Group 43
 * @since 16-mrt-2014
 */
public class KNNGolf extends KNN43 {

    /**
     * Constructor.
     * @param trainingData training data
     * @param k parameter of k nearest neighbours
     * @throws IOException
     */
    public KNNGolf(Map trainingData, int k) throws IOException {
        super(trainingData, k);
    }

    /**
     * Commputes the distance by a combination of discrete and continuous
     * variables.
     * @param f0
     * @param f1
     * @return
     */
    @Override
    double distance(Features f0, Features f1) {
       GolfFeatures gf0 = (GolfFeatures) f0;
       GolfFeatures gf1 = (GolfFeatures) f1;
       final int n = gf0.getNumberOfAttributes();
       double d = 0;
       for (int i = 0; i < n; i ++) {
           if (gf0.isContinuous(i)) {
               double h = (int) gf0.get(i) - (int) gf1.get(i);
               d += h * h;
           } else { // feature is discrete
               if (gf0.get(i) == gf1.get(i)) {
                   d += 1; //TODO
               }
           }
       }
       return d;
    }
}
