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

    public DecisionTree43(LabeledDataset2 dataset, L clazz) {
        super(dataset, clazz);
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

    public double average(int splitIndex, LabeledDataset2<F, L> dataset) {
        double sum = 0;

        for (F feature : dataset.featureVectors()) {
            Number attributeValue = (Number) feature.get(splitIndex);
            sum += attributeValue.doubleValue();
        }

        return sum / dataset.size();
    }

    public double maxArgument(int splitIndex, LabeledDataset2<F,L> dataset) {
        double maxGain = -1; //maximum gain to be gained
        double bestValue = Double.NaN; // best value to split on
        for (F feature : dataset.featureVectors()) {
            Number attributeValue = (Number) feature.get(splitIndex);
            double gain = dataset.gain(splitIndex, attributeValue);
            if (gain > maxGain) {
                maxGain = gain;
                bestValue = attributeValue.doubleValue();
            }
        }
        return bestValue;
    }

    /**
     * Build the tree with a given dataset.
     * @param dataset  The dataset to build the tree for.
     */
    @Override
    protected void build(LabeledDataset2<F, L> dataset) {
        int splitIndex = -1; // Index of attribute on which to split
        double maxGain = -1; // Maximum gain
        double maxArgument = 0; // In case we end up with a continuous split.

        // Find the attribute that gives the highest gain.
        for (int i = 0; i < dataset.getNumberOfDimensions(); i++) {
            double gain;
            double maxarg = Double.NaN;
            if (dataset.isContinuousFeature(i)) {
                maxarg = maxArgument(i, dataset);
                gain = dataset.gain(i, maxarg);
            } else {
                gain = dataset.gain(i);
            }

            if (gain > maxGain) {
                maxGain = gain;
                splitIndex = i;
                if (dataset.isContinuousFeature(i)) {
                    maxArgument = maxarg;
                }
            }
        }

        // If maxgain is (near) zero, then we have a leaf.
        if (maxGain <= 0.00001) {
            this.setLabel(dataset.getMostFrequentClass());
            return;
        }

        // We are not dealing with a leaf, thus have to split.
        Map<Object, LabeledDataset2<F, L>> split;
        if (dataset.isContinuousFeature(splitIndex)) {
            setSplit(splitIndex, maxArgument); // Set label in internal node.
            split = dataset.continuousSplit(splitIndex, maxArgument);
        } else {
            //Concrete feature
            setSplit(splitIndex, null); // Set label in internal node.
            split = dataset.discreteSplit(splitIndex);
        }

        // We have now split the dataset for an attribute. Each split will now
        // be added as a sub-tree to this current tree.
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

    @Override
    public L classify(F v) {
        DecisionTree<F, L> root = this;
        if (root.isLeaf()) {
            return root.getLabel();
        } else {
            int index = root.getSplitAttribute();
            Object attributeValue = v.get(index);
            if (v.isContinuous(index) && (Double) attributeValue <= root.splitValue.doubleValue()) {
                root = root.getSubTree("<= " + root.splitValue);
                //System.out.println("<= " + (Double) attributeValue);
            } else if (v.isContinuous(index)) {
                root = root.getSubTree("> " + root.splitValue);
            } else {
                //discrete attribute
                root = root.getSubTree(root.splitValue);
            }
            return root.classify(v);
        }
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

    @Override
    public int prune(final LabeledDataset2<F, L> testData) {
        // use visitor!?
        throw new UnsupportedOperationException("Needs to be implemented");
    }
}
