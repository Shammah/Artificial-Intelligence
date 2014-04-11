package nl.tue.s2id90.classification.decisiontree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.LabeledDataset2;

/**
 *
 * @author Group 43
 * @param <F>
 * @param <L>
 */
public class DecisionTree43<F extends Features, L> extends DecisionTree<F, L> {

    public DecisionTree43(LabeledDataset2 dataset) {
        super(dataset);
    }

    /**
     * Leaf constructor.
     * @param dataset The dataset.
     * @param clazz   The final classification of the leaf.
     */
    public DecisionTree43(LabeledDataset2 dataset, L clazz) {
        super(dataset, clazz);
    }

    /**
     * Build the tree with a given dataset.
     * @param dataset  The dataset to build the tree for.
     */
    @Override
    protected void build(LabeledDataset2<F, L> dataset) {
        // Find the attribute with the maximum information gain, on which we may split.
        double maxGain = findAttribute(dataset);
        
        // If maxgain is (near) zero, then we have a leaf. There are no more attributes
        // that we can split by, because it will give us no new information.
        if (maxGain <= 0.00001) {
            setLabel(dataset.getMostFrequentClass());
            return;
        }

        // We are not dealing with a leaf, thus have to split.
        Map<Object, LabeledDataset2<F, L>> split;
        
        // We will either have to split a continous or discrete attribute.
        if (dataset.isContinuousFeature(splitAttribute)) {
            setSplit(splitAttribute, splitValue); // Set label in internal node.
            split = dataset.continuousSplit(splitAttribute, splitValue);
        } else {
            setSplit(splitAttribute, null); // Set label in internal node.
            split = dataset.discreteSplit(splitAttribute);
        }

        // We have now split the dataset for an attribute. Each splitted tree
        // will now be added as a sub-tree to this current tree.
        for (Object value : split.keySet()) {
            LabeledDataset2<F, L> subDataset = split.get(value);

            // If we are dealing with an empty set, we are dealing with a leaf.
            if (split.keySet().size() == 1) {
                DecisionTree<F, L> subTree = new DecisionTree43<>(subDataset, dataset.getMostFrequentClass());
                addSubTree(value, subTree);
            } else if (!subDataset.isEmpty()){
                DecisionTree43<F, L> subTree = new DecisionTree43<>(subDataset);
                addSubTree(value, subTree);
            }
        }
    }

    /**
     * Finds the attribute and value that give the highest information gain.
     * Returns the maximum gain.
     *
     * @param dataset dataset containing features
     * @modifies {@code this.splitAttribute}, {@code this.splitValue}
     * @post {@code this.splitAttribute} is the attribute with the largest gain
     * 
     * If this attribute is continuous, splitValue contains the value giving the
     * highest gain.
     */
    private double findAttribute(LabeledDataset2<F,L> dataset) {
        // Maximum gain found so far.
        double maxGain          = -1;
        
        // Calculate the information gain for each attribute.
        for (int i = 0; i < dataset.getNumberOfDimensions(); i++) {
            // The gain of the current attribute.
            double gain;
            
            // The value that maximizes gain in case of a continuous split.
            double bestValue    = Double.NaN;
            
            // Calculate the gain of the attribute.
            if (dataset.isContinuousFeature(i)) {
                bestValue       = average(i, dataset);
                gain            = dataset.gain(i, bestValue);
            } else { //discrete
                gain            = dataset.gain(i);
            }
            
            // Check whether the newly calculated gain is better than the previous one.
            if (gain > maxGain) {
                maxGain         = gain;
                this.splitAttribute = i;
                if (dataset.isContinuousFeature(i)) {
                    splitValue  = bestValue;
                }
            }
        }
        return maxGain;
    }

    /**
     * Returns the value for the attribute at index {@code splitIndex} that
     * maximizes the information gain, that is, the optimal one.
     * 
     * We compute the information gain for each value, and return the best value.
     * We use a set in order to improve performance.
     *
     * @param splitIndex index of the desired attribute to split on
     * @param dataset dataset containing the features
     * @pre dataset.isContinuousFeature(splitIndex)
     * @return the argument maximizing the information gain
     */
    public double findSplitValue(int splitIndex, LabeledDataset2<F,L> dataset) {
        // The value corresponding with the maximum gain.
        double maxGain      = -1;               // Maximum gain to be gained.
        double bestValue    = Double.NaN;       // Best value to split on.
        Set<Number> values  = new HashSet<>();
        
        for (F feature : dataset.featureVectors()) {
            values.add((Number) feature.get(splitIndex));
        }
        
        for (Number attributeValue : values) {
            double gain     = dataset.gain(splitIndex, attributeValue);
            if (gain > maxGain) {
                maxGain     = gain;
                bestValue   = attributeValue.doubleValue();
            }
        }
        return bestValue;
    }

    /**
     * Returns the median value of the i-th continuous attribute.
     *
     * @param splitIndex The attribute to find the median for.
     * @param dataset The dataset which contains the i-th attribute.
     * @return The median value of the i-th attribute.
     */
    public double median(int splitIndex, LabeledDataset2<F, L> dataset) {
        List<Double> attributeValues = new ArrayList<>();

        for (F feature : dataset.featureVectors()) {
            Number attributeValue = (Number) feature.get(splitIndex);
            attributeValues.add(attributeValue.doubleValue());
        }

        Collections.sort(attributeValues);
        int medianIndex = (int) attributeValues.size() / 2;
        return attributeValues.get(medianIndex);
    }

    /**
     * Returns the average of the attribute with index {@code splitIndex}.
     *
     * @param splitIndex index of the desired attribute
     * @param dataset dataset containing the features
     * @return the average of the attribute with index {@code splitIndex}
     */
    public double average(int splitIndex, LabeledDataset2<F, L> dataset) {
        double sum = 0;

        for (F feature : dataset.featureVectors()) {
            Number attributeValue = (Number) feature.get(splitIndex);
            sum += attributeValue.doubleValue();
        }

        return sum / dataset.size();
    }

    /**
     * Classifies {@code v} according to this decision tree.
     * Recursively walks the tree to find the classification.
     * @param v the feature vector to be classified
     * @return the classification of {@code v}
     */
    @Override
    public L classify(F v) {
        // The root of the tree is the tree itself.
        DecisionTree<F, L> root = this;
        
        // If the tree is a leaf, we can return the label, which is its classification.
        if (root.isLeaf()) {
            return root.getLabel();
        }
        
        // Get the index of the splitattribute on in this node.
        int index = root.getSplitAttribute();
        
        // The the attribute value, which will decide what path we have to take.
        Object attributeValue = v.get(index);
        
        if (v.isContinuous(index) && (Double) attributeValue <= root.splitValue.doubleValue()) {
            // The value of v <= splitValue, thus we need to go left.
            root = root.getSubTree("<= " + root.splitValue);
        } else if (v.isContinuous(index)) {
            // The value of v > splitValue, thus we need to go right.
            root = root.getSubTree("> " + root.splitValue);
        } else {
            // Discrete attribute.
            root = root.getSubTree(root.splitValue);
        }
        
        // Recursively go through the tree.
        return root.classify(v);
    }

    /**
     * Returns the confusion matrix for this decision tree on {@code testData}.
     * Since this method is exactly the same for kNN, consider placing it more general.
     * @param testData test data for testing
     * @return the confusion matrix for this decision tree on {@code testData}.
     */
    @Override
    public Map<L, Map<L, Integer>> getConfusionMatrix(Map<F, L> testData) {
        //initialize Confusion Matrix
        Map<L, Map<L, Integer>> matrix = new HashMap<>();
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
        System.out.println(errorRate(testData));
        return matrix;
    }

    /**
     * Returns the error rate for this decision tree on {@code testData}.
     * This method is exactly the same for kNN, so consider placing it more general.
     * @param testData the test data to test on
     * @return the error rate for this decision tree on {@code testData}
     */
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

    /**
     * Prune the tree to see whether the error rate decreases.
     *
     * @param testData the test data used to generalize (that is, prune) {@code this} tree.
     * @modifies {@code this}
     * @post the tree is pruned in such a way that the error rate on {@code testData}
     * is minimized.
     * @return the number of vertices removed from {@code this}
     * @see LabeledTree.depthFirstPostOrderVisit()
     */
    @Override
    public int prune(final LabeledDataset2<F, L> testData) {
        Visitor43<F,L> visitor = new Visitor43<>(this, testData);
        depthFirstPostOrderVisit(visitor);
        return visitor.getNumberPruned();
    }
}