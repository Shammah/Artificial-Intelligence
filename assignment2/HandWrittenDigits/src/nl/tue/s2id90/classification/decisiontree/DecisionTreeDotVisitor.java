/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.tue.s2id90.classification.decisiontree;

import java.util.Iterator;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.labeledtree.DotVisitor;
import nl.tue.s2id90.classification.labeledtree.LabeledTree;

/**
 *
 * @author huub
 * @param <V>
 * @param <T>
 */
public class DecisionTreeDotVisitor<V extends Features, T>
        extends DotVisitor<Object, DecisionTree<V, T>> {

    public DecisionTreeDotVisitor(StringBuilder b) {
        super(b);
    }

    /**
     * generate an informative label for a node in a decision tree.
     * @param tree
     * @param index
     * @return 
     */
    @Override
    public String getLabelFor(LabeledTree<Object, DecisionTree<V, T>> tree, int index) {
        DecisionTree<V, T> node = (DecisionTree<V, T>) tree;
        LabeledDataset2<V, T> dataset = node.getDataset();
        T classification = node.getLabel();
        int splitAttribute = node.getSplitAttribute();

        String dotLabel = node.isLeaf() ? (classification==null?"null":classification.toString()) : "" + dataset.getFeatureName(splitAttribute);
        return dotLabel + sizes(node);
    }

    private <V extends Features, T> String sizes(DecisionTree<V, T> node) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("[");
//
//        LabeledDataset2<V, T> dataset = node.getDataset();
//        Iterator<T> i = dataset.getLabels().iterator();
//        while (i.hasNext()) {
//            T t = i.next();
//            sb.append(dataset.getFeatures(t).size());
//            if (i.hasNext()) {
//                sb.append(',');
//            }
//        }
//        return sb.append("]").toString();
        return node.getDataset().getFrequencies().toString();
    }
}
