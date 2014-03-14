package nl.tue.s2id90.classification.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Compared to a <code>LabeledDataset</code> this dataset maintains a reversed
 * map, that maps a label on a list of all feature vectors with that label
 * as classification label. Furthermore, this dataset has methods for splitting
 * the dataset in parts, and computing the information gain after splitting.
 * @author huub
 * @param <V> feature vector
 * @param <L> classification label
 * @see LabeledDataset
 */
public abstract class LabeledDataset2<V extends Features, L> extends LabeledDataset<V, L> {
    protected final Map<L, List<V>> reversedMap; // maps class to list of elements with that class

    public LabeledDataset2() {
        reversedMap = new HashMap<>();
    }
    
    /**
     * @param label 
     * @return the featureVectors classified as label.
     **/
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
            put(cd.getValue(),cd.getKey());
        }
    }
    
    /**
     * adds classified data to this dataset.
     *
     * @param t     classification
     * @param data  collection of feature vectors
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
    
    /** splits dataset in subsets with a constant value for the i-th feature.
     * @return result of splitting, the keys in this map are the values of the i-th
     *        attribute.
     **/
    public Map<Object,LabeledDataset2<V, L>> discreteSplit(int i) {
        throw new UnsupportedOperationException("needs to be implemented");
    }
    
    /** partitions dataset in 2 subsets, one with only the vectors with values of the i-th attribute higher 
     *  than the given splitValue.
     * @return result of splitting, the keys in this map are "<= splitValue" or ">splitValue".
     **/
    private Map<Object,LabeledDataset2<V, L>> continuousSplit(int index, Number splitValue) {
        throw new UnsupportedOperationException("needs to be implemented");
    }

    /** returns the probabilities of the classification labels for this dataset.
     *  @return probability of the labels.
     **/
    public double[] classProbabilities() {
        throw new UnsupportedOperationException("needs to be implemented");
    }

    /**
     * @param index index of attribute used for discrete splitting.
     * @return gain by discrete splitting on attribute i.
     */
    public double gain(int index) {
        throw new UnsupportedOperationException("needs to be implemented");
    }
    
     /**
     * @param index index of attribute used for continuous splitting
     * @param splitValue
     * @return gain by discreteSplitting on attribute i.
     */
    public double gain(int index, Number splitValue) {
        throw new UnsupportedOperationException("needs to be implemented");
    }
    
     /** @return the most frequently occurring class in this dataset;
      * null, if the dataset is empty.
     */
    public L getMostFrequentClass() {
        throw new UnsupportedOperationException("needs to be implemented");
    }
    
    /** @return the frequencies of the subclasses . **/
    public List<Integer> getFrequencies() {
        List<Integer> result = new ArrayList<>();
        for (L t : getLabels()) {
            result.add(getFeatures(t).size());
        }
        return result;
    }
}