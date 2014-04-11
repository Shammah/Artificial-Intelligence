package nl.tue.s2id90.classification.decisiontree;

import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.labeledtree.LabeledTree;
import nl.tue.s2id90.classification.labeledtree.Visitor;

/**
 *
 * @author Group43
 * @param <F>
 * @param <L>
 * @since 4-apr-2014
 */
public class Visitor43<F extends Features, L> implements Visitor<Object, DecisionTree<F,L>> {

    DecisionTree43<F,L> root;
    LabeledDataset2<F, L> testData;
    int numberPruned;

    /**
     * Constructor.
     * @param tree the tree that {@code this} will visit
     * @param testData the test data that will prune the tree.
     * @post the instance variables have been initialized. {@code numberPruned}
     * is set to 0.
     */
    public Visitor43(DecisionTree43 tree, LabeledDataset2 testData) {
         root = tree;
         this.testData = testData;
         this.numberPruned = 0;
    }

    @Override
    public void visitNode(LabeledTree<Object, DecisionTree<F, L>> node) {
        // Current tree node we are visiting.
        DecisionTree43<F,L> vertex = (DecisionTree43) node;
        
        // If the node is a leaf, we can't do any pruning.
        if (! vertex.isLeaf()) {
            // Get the error-rate of the current tree.
            double errorRate = root.errorRate(testData.getClassification());
            
            // Get the error-rate of the tree where we prune a node, such that
            // it becomes a leaf with the most frequent class as its classification.
            vertex.setLabel(vertex.getDataset().getMostFrequentClass());
            double errorRate2 = root.errorRate(testData.getClassification());
            
            // If the pruning is wurse, we undo the pruning, else we continue visiting.
            if (errorRate2 > errorRate) {
                vertex.setLabel(null);
            } else {
                numberPruned ++;
            }
        }
    }

    /**
     * Returns the number of vertices pruned.
     * @return the number of vertices pruned.
     */
    public int getNumberPruned() {
        return numberPruned;
    }

    @Override
    public void visitEdge(LabeledTree<Object, DecisionTree<F, L>> parent, DecisionTree<F, L> child, Object label) {
        // Do nothing.
    }
}
