package nl.tue.s2id90.classification.decisiontree;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
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
        int maxAttr = -1;
        double maxGain = -1;

        // Find the attribute that gives the highest gain.
        for (int i = 0; i < dataset.getNumberOfDimensions(); i++) {
            double gain = dataset.gain(i);

            if (gain > maxGain) {
                maxGain = gain;
                maxAttr = i;
            }
        }

        // We are at a leaf.
        if (maxGain <= 0.0001) {
            setLabel(dataset.getMostFrequentClass());
            return;
        }
        System.out.println(maxGain);

        // Split the tree by the attribute giving the highest gain.
        Map<Object, LabeledDataset2<F, L>> split;

        if (dataset.isContinuousFeature(maxAttr)) {
            // Splitvalue = median.
            List<Integer> numbers = new ArrayList<>();
            for (F f : dataset.featureVectors()) {
                numbers.add((int) f.get(maxAttr));
            }
            
            Collections.sort(numbers);
            split = dataset.continuousSplit(maxAttr, numbers.get((int) (numbers.size() / 2)));
        } else {
            split = dataset.discreteSplit(maxAttr);
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
        }
    }
}
