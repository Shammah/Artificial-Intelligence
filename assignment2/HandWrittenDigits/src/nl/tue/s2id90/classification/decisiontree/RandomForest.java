/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nl.tue.s2id90.classification.decisiontree;

import java.util.ArrayList;
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
public abstract class RandomForest<V extends Features,T> implements Classifier<V, T>{
    List<DecisionTree<V,T>> trees = new ArrayList<>();
    /**
     * A random forest consisting of k decision trees.
     * 
     * @param k number of trees in forest.
     */
    public RandomForest(LabeledDataset2<V,T> dataset, int k) {
        // split datasset
        // build your forest, e.g. build decision trees
    }
    
    @Override
    public T classify(V v) { 
        throw new UnsupportedOperationException("Needs to be implemented");
    }
    
    /** build k decision trees, one for each data set. **/
    private void build(List<LabeledDataset2<V, T>> datasets) {
            throw new UnsupportedOperationException("Needs to be implemented");
    }    
    
    public void prune(LabeledDataset2<V,T> testData) {
            throw new UnsupportedOperationException("Needs to be implemented");
    }
    
    @Override
     public double errorRate(Map<V, T> testData) {
            throw new UnsupportedOperationException("Needs to be implemented");
    }

    @Override
    public Map<T, Map<T, Integer>> getConfusionMatrix(Map<V, T> testData) {
            throw new UnsupportedOperationException("Needs to be implemented");
    }
}
