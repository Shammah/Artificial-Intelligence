package nl.tue.s2id90.classification.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import nl.tue.s2id90.classification.decisiontree.Information;

/**
 * Compared to a <code>LabeledDataset</code> this dataset maintains a reversed
 * map, that maps a label on a list of all feature vectors with that label as
 * classification label. Furthermore, this dataset has methods for splitting the
 * dataset in parts, and computing the information gain after splitting.
 *
 * @author huub
 * @param <V> feature vector
 * @param <L> classification label
 * @see LabeledDataset
 */
public class LabeledDataset2<V extends Features, L> extends LabeledDataset<V, L> {

    protected final Map<L, List<V>> reversedMap; // maps class to list of elements with that class

    public LabeledDataset2() {
        reversedMap = new HashMap<>();
    }

    /**
     * @param label
     * @return the featureVectors classified as label.
     *
     */
    public List<V> getFeatures(L label) {
        return reversedMap.get(label);
    }

    /**
     * adds classified data to this dataset.
     *
     * @param data
     */
    public void putAll(Map<V, L> data) {
        for (Entry<V, L> cd : data.entrySet()) {
            put(cd.getValue(), cd.getKey());
        }
    }

    /**
     * adds classified data to this dataset.
     *
     * @param t classification
     * @param data collection of feature vectors
     */
    public void putAll(L t, Collection<V> data) {
        for (V v : data) {
            put(t, v);
        }
    }

    /**
     * adds a single feature vector V and it's classification T to this dataset.
     *
     * @param t classification class
     * @param v feature vector
     */
    public void put(L t, V v) {
        checkDimensionality(v);
        List<V> list = reversedMap.get(t);
        if (list == null) {
            list = new ArrayList<>();
            reversedMap.put(t, list);
        }
        list.add(v);
        classification.put(v, t);
    }

    /**
     * splits dataset in subsets with a constant value for the i-th feature.
     *
     * @param i The i-th attribute we want to split for.
     * @return result of splitting, the keys in this map are the values of the
     * i-th attribute.
     *
     */
    public Map<Object, LabeledDataset2<V, L>> discreteSplit(int i) {
        // We map an attribute value to a labeled dataset.
        // So, if the i-th attribute can have values HIGH, MED and LOW, these
        // three attribute values will be the keys.
        Map<Object, LabeledDataset2<V, L>> result = new HashMap<>();
        List<V> features = featureVectors();

        // We will categorize each feature.
        for (V feature : features) {
            // Check if the attribute value already exists as a key.
            Object attributeValue = feature.get(i);
            if (!result.containsKey(attributeValue)) {
                // The key for the attribute value does not yet exist,
                // thus we will create it.
                LabeledDataset2<V, L> newSet = new LabeledDataset2<>();
                newSet.put(getLabel(feature), feature);
                result.put(attributeValue, newSet);
            } else {
                result.get(attributeValue).put(getLabel(feature), feature);
            }
        }

        return result;
    }

    /**
     * partitions dataset in 2 subsets, one with only the vectors with values of
     * the i-th attribute higher than the given splitValue.
     *
     * @return result of splitting, the keys in this map are "<= splitValue" or
     * ">splitValue".
     *
     */
    public Map<Object, LabeledDataset2<V, L>> continuousSplit(int i, Number splitValue) {
        // We will split two ways, so we can create the final map easily.
        Map<Object, LabeledDataset2<V, L>> result = new HashMap<>();
        LabeledDataset2<V, L> smallerEqual = new LabeledDataset2<>();
        LabeledDataset2<V, L> bigger = new LabeledDataset2<>();
        List<V> features = featureVectors();

        // We will categorize each feature.
        for (V feature : features) {
            // Put the feature in the correct splitted subtree.
            // We will assume the numbers are doubles.
            double attributeValue = ((Number) feature.get(i)).doubleValue();

            if (attributeValue <= splitValue.doubleValue()) {
                smallerEqual.put(getLabel(feature), feature);
            } else {
                bigger.put(getLabel(feature), feature);
            }
        }

        if (!smallerEqual.isEmpty()) {
            result.put("<= " + splitValue.doubleValue(), smallerEqual);
        }

        if (!bigger.isEmpty()) {
            result.put("> " + splitValue.doubleValue(), bigger);
        }

        return result;
    }

    /**
     * returns the probabilities of the classification labels for this dataset.
     *
     * @return probability of the labels.
     *
     */
    public double[] classProbabilities() {
        List<Integer> frequencies = getFrequencies();
        double[] probs = new double[frequencies.size()];

        for (int i = 0; i < probs.length; i++) {
            probs[i] = frequencies.get(i);
            probs[i] /= this.size();
        }

        return probs;
    }

    /**
     * @param index index of attribute used for discrete splitting.
     * @return gain by discrete splitting on attribute i.
     */
    public double gain(int index) {
        double entDataSet = Information.entropy(this.classProbabilities());
        Map<Object, LabeledDataset2<V, L>> split = discreteSplit(index);

        return entDataSet - averageEntropy(split) / size();
    }

    /**
     * @param index index of attribute used for continuous splitting
     * @param splitValue
     * @return gain by discreteSplitting on attribute i.
     */
    public double gain(int index, Number splitValue) {
        double entDataSet = Information.entropy(this.classProbabilities());
        Map<Object, LabeledDataset2<V, L>> split = continuousSplit(index, splitValue);

        return entDataSet - averageEntropy(split) / size();
    }

    /**
     * Calculates the average entropy for a splitted tree.
     *
     * @param split splitted tree.
     * @return average entropy of splitted tree.
     */
    public double averageEntropy(Map<Object, LabeledDataset2<V, L>> split) {
        double avgEntropy = 0;

        for (LabeledDataset2<V, L> attributeValue : split.values()) {
            avgEntropy += attributeValue.size()
                    * Information.entropy(attributeValue.classProbabilities());
        }

        return avgEntropy;
    }

    /**
     * @return the most frequently occurring class in this dataset; null, if the
     * dataset is empty.
     */
    public L getMostFrequentClass() {
        L mostFrequent = null;
        int highestFrequency = 0;

        for (L label : getLabels()) {
            if (getFeatures(label).size() > highestFrequency) {
                mostFrequent = label;
                highestFrequency = getFeatures(label).size();
            }
        }

        return mostFrequent;
    }

    /**
     * @return the frequencies of the subclasses . *
     */
    public List<Integer> getFrequencies() {
        List<Integer> result = new ArrayList<>();
        for (L t : getLabels()) {
            result.add(getFeatures(t).size());
        }
        return result;
    }
}
