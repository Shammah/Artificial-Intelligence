package nl.tue.s2id90.classification.knn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.win.util.Pair;

/**
 * Abstract implementation of our KNN algorithm.
 *
 * @author Group 43
 * @param <F> The feature type.
 * @param <L> The label type.
 */
public abstract class KNN43<F extends Features, L> extends KNN<F, L> {

    public KNN43(Map trainingData, int k) throws IOException {
        super(trainingData, k);
    }

    /**
     * Finds the closest of two features in reference to a local feature v.
     */
    public class DistanceComparator implements Comparator<Pair<F, Double>> {

        @Override
        public int compare(Pair<F, Double> t, Pair<F, Double> t1) {
            if (t.second < t1.second) {
                return -1;
            } else if (t.second > t1.second) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * Returns the most common element in a list.
     * @param <T> The list type.
     * @param list The list containing the element.
     * @return The element that occurred most frequently in the list.
     * @author http://stackoverflow.com/users/1357341/arshajii
     *         http://stackoverflow.com/questions/19031213/java-get-most-common-element-in-a-list
     */
    public static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();

        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }

        Entry<T, Integer> max = null;

        for (Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue()) {
                max = e;
            }
        }

        return max.getKey();
    }

    @Override
    public L classify(F v) {
        // Initialize all neighbours with the distance.
        List<Pair<F, Double>> neighbours = new ArrayList<>();
        for (F data : trainingData.keySet()) {
            neighbours.add(new Pair<>(data, distance(v, data)));
        }

        // Sort neighbours by distance.
        Collections.sort(neighbours, new DistanceComparator());

        // Find the most common label and return that.
        List<L> labels = new ArrayList<>();
        for (Pair<F, Double> neighbour : neighbours.subList(0, k)) {
            labels.add(trainingData.get(neighbour.first));
        }

        return mostCommon(labels);
    }

    @Override
    public double errorRate(Map<F, L> testData) {
        // The total number of wrong classifications.
        int wrong = 0;

        // Check for each test data.
        for (F data : testData.keySet()) {
            // Was the classifier wrong?
            if (classify(data) != testData.get(data)) {
                wrong++;
            }
        }

        // Return the ratio between 0 and 1 that was classified correctly.
        return ((double) wrong) / ((double) testData.size());
    }

    @Override
    public Map<L, Map<L, Integer>> getConfusionMatrix(Map<F, L> testData) {
        //initialize Confusion Matrix
        Map<L, Map<L, Integer>> matrix = new HashMap<L, Map<L, Integer>>();
        for (L label : testData.values()) {
            matrix.put(label, new HashMap<L, Integer>());
            for (L label2 : testData.values()) {
                matrix.get(label).put(label2, 0);
            }
        }
        // fill confusion matrix with values
        for (F data : testData.keySet()) {
            L correctLabel = testData.get(data);
            L classifiedLabel = classify(data);
            Integer currentFrequency = matrix.get(correctLabel).
                    get(classifiedLabel);
            matrix.get(correctLabel).put(classifiedLabel, currentFrequency + 1);
        }
        return matrix;
    }
}