package nl.tue.s2id90.classification.decisiontree;

import nl.tue.s2id90.classification.data.Features;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.labeledtree.LabeledTree;
import nl.tue.s2id90.classification.labeledtree.Visitor;

/**
 *
 * @author J.P.H. Snoeren, 0772658
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
        DecisionTree43<F,L> vertex = (DecisionTree43) node;
        if (! vertex.isLeaf()) {
            double errorRate = root.errorRate(testData.getClassification());
            vertex.setLabel(vertex.getDataset().getMostFrequentClass());
            double errorRate2 = root.errorRate(testData.getClassification());
            if (errorRate2 > errorRate) { // pruning is worse
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
        //do nothing
    }
}
