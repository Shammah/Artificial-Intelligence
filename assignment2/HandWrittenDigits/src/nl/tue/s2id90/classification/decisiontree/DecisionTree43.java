package nl.tue.s2id90.classification.decisiontree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    @Override
    protected void build(LabeledDataset2<F, L> dataset) {
        System.out.println((dataset == null) + " " + dataset.isEmpty());
        int splitIndex = -1; //index of attribute on which to split
        double maxGain = -1; //maximum gain
        //Find the attribute that gives the highest gain
        for (int i = 0; i < dataset.getNumberOfDimensions(); i ++) {
            double gain = dataset.gain(i);
            if (gain > maxGain) {
                maxGain = gain;
                splitIndex = i;
            }
        }

        /** Base case */
        System.out.println("maxGain: " + maxGain);
        if (maxGain <= 0.000001) {
            this.setLabel(dataset.getMostFrequentClass());
            return;
        }
        /** Step case */
        Map<Object, LabeledDataset2<F,L>> split;
        if (dataset.isContinuousFeature(splitIndex)) {
            //Continuous feature, We split at the median
            List<Integer> numbers = new ArrayList<>();
            for (F feature : dataset.featureVectors()) {
                numbers.add((Integer) feature.get(splitIndex));
            }
            Collections.sort(numbers);
            int median = (int) numbers.size() / 2;
            this.setSplit(splitIndex, numbers.get(median));
            split = dataset.continuousSplit(splitIndex, numbers.get(median));
        } else {
            //Concrete feature
            this.setSplit(splitIndex, null);
            split = dataset.discreteSplit(splitIndex);
        }

        for (Object value : split.keySet()) {
            LabeledDataset2<F,L> subDataset = split.get(value);
            if (!dataset.isEmpty()) {
                System.out.println(value);
                System.out.println("Attribute: " + this.getSplitAttribute());
                DecisionTree43<F,L> subTree = new DecisionTree43<F,L>(subDataset);
                this.addSubTree(value, subTree);
            } else {
                DecisionTree<F,L> subTree = new DecisionTree43<F,L>(subDataset,dataset.getMostFrequentClass());
                this.addSubTree(value, subTree);
                return;
            }
        }
/*
        // We are at a leaf.
        if (maxGain <= 0.0001) {
            setLabel(dataset.getMostFrequentClass());
            return;
        }

        // Split the tree by the attribute giving the highest gain.
        Map<Object, LabeledDataset2<F, L>> split;
        if (dataset.isContinuousFeature(maxAttr)) {
            // Splitvalue = median.
            List<Integer> numbers = new ArrayList<>();
            for (F f : dataset.featureVectors()) {
                numbers.add((int) f.get(maxAttr));
            }

            Collections.sort(numbers);
            System.out.println((int) (numbers.size() / 2) + " "  + numbers.get((int) (numbers.size() / 2)));
            split = dataset.continuousSplit(maxAttr, numbers.get((int) (numbers.size() / 2)));
        } else {
            split = dataset.discreteSplit(maxAttr);
            this.setLabel((L) dataset.getFeatureName(maxAttr));
        }

        // Create subtrees from splitting.
        for (Object attribute : split.keySet()) {
            DecisionTree43<F, L> subTree;
            LabeledDataset2<F, L> subDataset = split.get(attribute);

            if (split.keySet().size() == 1) {
                subTree = new DecisionTree43<>(subDataset, subDataset.getLabels().iterator().next());
            } else {
                subTree = new DecisionTree43<>(subDataset);
            }

            addSubTree(attribute, subTree);
        }*/
    }

    @Override
    public L classify(F v) {
        DecisionTree<F, L> root = this;
        if (root.isLeaf()) {
            return root.getLabel();
        } else {
            int index = root.getSplitAttribute();
            Object attributeValue = v.get(index);
            root = root.getSubTree(attributeValue);
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
