/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A dataset that holds the classification label for a set of feature vectors.
 * This dataset only contains feature vectors of the same dimensionality.
 * @author huub
 * @param <V>
 * @param <L>
 */
public class LabeledDataset<V extends Features, L> {
    protected final Map<V, L> classification;
    protected Integer dimensions; // number of dimensions in the feature vectors.

    public LabeledDataset(Map<V,L> classification) {
        this.classification = classification;
        checkDimensionality(classification);
    }
    
    public LabeledDataset() {
        classification = new HashMap<>();
    }

    /** returns the set of all labels in this dataset. 
     * 
     * @return set of all labels. 
     */
    public Set<L> getLabels() {
        return new HashSet<>(classification.values());
    }

    /**
     * returns the dimensionality of the feature vectors. All feature vectors
     * should have the same number of dimensions.
     * @return the number of dimensions of the feature vectors;
     *          null, if this dataset is actually empty.
     */
    public Integer getNumberOfDimensions() {
        return dimensions;
    }

    /** number of classification labels.
     * @return the number of different classes used to classify this dataset.
     */
    public Integer getNumberOfLabels() {
        return classification.values().size();
    }

    /** returns true iff this dataset contains no feature vectors.
     * @return whether or not this dataset is empty.
     */
    public boolean isEmpty() {
        return classification.isEmpty();
    }

    /** returns the number of feature vectors in this dataset.
     * 
     * @return  number of feature vectors.
     */
    public int size() {
        return classification.size();
    }

    /**
     * returns the feature vectors in this dataset.
     * @return the feature vectors in this dataset 
     */
    public List<V> featureVectors() {
        return new ArrayList<>(classification.keySet());
    }
  
    /** 
     * returns classification label of feature vector v.
     * @param v feature vector
     * @return classification label of v 
     */
    public L getLabel(V v) {
        return classification.get(v);
    }

    /** returns a map that maps all feature vectors in this dataset to
     * their respective classification label.
     * @return classification
     */
    public Map<V, L> getClassification() {
        return Collections.unmodifiableMap(classification);
    }
    
        
    /** returns name of the i-th feature of the vectors in this dataset.
     * It is assumed that all feature vectors have the same names for their
     * featureVectors.
     * @param index
     * @return name of i-the feature.
     * @see Features#getName(int) 
     */
    public String getFeatureName(int index) {
        if (isEmpty()) {
            throw new IllegalStateException("empty dataset");
        } else {
            V v = classification.keySet().iterator().next();
            return v.getName(index);            
        }
    }  
    
    public boolean isContinuousFeature(int index) {
        if (isEmpty()) {
            throw new IllegalStateException("empty dataset");
        } else {
            V v = classification.keySet().iterator().next();
            return v.isContinuous(index);
        }
    }

    protected void checkDimensionality(V v) throws IllegalArgumentException {
        int n = v.getNumberOfAttributes();
        if (dimensions == null) {
            dimensions = n;
        } else if (n != dimensions) {
            throw new IllegalArgumentException("Dataset with features of different dimensionality!");
        }
    }   

    private void checkDimensionality(Map<V, L> classification) {
        for(V v: classification.keySet()) {
            checkDimensionality(v);
        }
    }
}