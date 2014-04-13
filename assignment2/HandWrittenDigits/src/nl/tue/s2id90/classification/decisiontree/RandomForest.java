/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.decisiontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nl.tue.s2id90.classification.Classifier;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.data.Features;

/**
 *
 * @author huub
 * @param <V>
 * @param <T>
 * @see ListUtil#mostOccurringElement(java.util.List)
 */
public class RandomForest<V extends Features,T> implements Classifier<V, T>{
    List<DecisionTree<V,T>> trees = new ArrayList<>();
    /**
     * A random forest consisting of k decision trees.
     *
     * @param k number of trees in forest.
     */
    public RandomForest(LabeledDataset2<V,T> dataset, int k) {
        // assign features to k data sets in a round robin fashion
        List<LabeledDataset2<V, T>> datasets = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            datasets.add(new LabeledDataset2<V,T>());
        }
        int listIndex = 0; //index of list to add the next feature to
        for (V feature : dataset.featureVectors()) {
            datasets.get(listIndex).put(dataset.getLabel(feature), feature);
            listIndex = (listIndex + 1) % k; //increment the index modulo k
        }
        build(datasets);
    }

    @Override
    public T classify(V v) {
        List<T> labels = new ArrayList<>();
        for (DecisionTree<V,T> tree : trees) {
            labels.add(tree.classify(v));
        }
        return mostCommon(labels);
    }

    /**
     * Returns the most common element in a {@code List}. If more than one element
     * is most common, the last one is returned.
     *
     * @param list the input list
     * @return the most common element in @{code list}.
     */
    private T mostCommon(List<T> list) {
        Map<T, Integer> frequencies = new HashMap<>();
        for (T element : list) {
            if (frequencies.containsKey(element)) {
                // increment the frequency by one
                frequencies.put(element, frequencies.get(element) + 1);
            } else {
                frequencies.put(element, 1);
            }
        }
        T mostCommon = null; //most common element
        Integer largestFrequency = -1;
        for (T element : frequencies.keySet()) {
            if (frequencies.get(element) > largestFrequency) {
                mostCommon = element;
                largestFrequency = frequencies.get(element);
            }
        }
        return mostCommon;
    }

    /** build k decision trees, one for each data set. **/
    private void build(List<LabeledDataset2<V, T>> datasets) {
            for (int i = 0; i < datasets.size(); i ++) {
                DecisionTree43<V, T> tree = new DecisionTree43<>(datasets.get(i));
                trees.add(tree);
            }
    }

    public void prune(LabeledDataset2<V,T> testData) {
           for (DecisionTree<V,T> tree : trees) {
               tree.prune(testData);
           }
    }

    /**
     * Computes the error rate for this random forest with the {@code testData}.
     * Method is exactly the same, so could be put more general.
     *
     * @param testData test data
     * @return the error rate for this random forst on {@code testData}.
     */
    @Override
     public double errorRate(Map<V, T> testData) {
        // The total number of wrong classifications.
        int wrong = 0;

        // Check for each test data.
        for (V data : testData.keySet()) {
            // Was the classifier wrong?
            if (classify(data) != testData.get(data)) {
                wrong++;
            }
        }

        // Return the ratio between 0 and 1 that was classified correctly.
        return ((double) wrong) / ((double) testData.size());
    }

    @Override
    public Map<T, Map<T, Integer>> getConfusionMatrix(Map<V, T> testData) {
        //initialize Confusion Matrix
        Map<T, Map<T, Integer>> matrix = new HashMap<>();
        for (T label : testData.values()) {
            matrix.put(label, new HashMap<T, Integer>());
            for (T label2 : testData.values()) {
                matrix.get(label).put(label2, 0);
            }
        }
        // fill confusion matrix with values
        for (V data : testData.keySet()) {
            T correctLabel = testData.get(data);
            T classifiedLabel = classify(data);
            Integer currentFrequency = matrix.get(correctLabel).
                    get(classifiedLabel);
            matrix.get(correctLabel).put(classifiedLabel, currentFrequency + 1);
        }
        System.out.println(errorRate(testData));
        return matrix;
    }
}
