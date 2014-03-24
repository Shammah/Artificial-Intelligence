package nl.tue.s2id90.classification.decisiontree;

import nl.tue.s2id90.classification.labeledtree.LabeledTree;
import java.util.Map;
import nl.tue.s2id90.classification.Classifier;
import nl.tue.s2id90.classification.data.LabeledDataset2;
import nl.tue.s2id90.classification.data.Features;

/**
 *
 * @author huub
 * @param <F> feature type
 * @param <L> label type
 */
public abstract class DecisionTree<F extends Features,L>
    extends LabeledTree<Object,DecisionTree<F,L>> implements Classifier<F,L> {
    protected L label;
    protected int splitAttribute;
    protected Number splitValue; // for continuous Split only

    /** All the feature vectors in this tree. **/
    final protected LabeledDataset2<F,L> dataset;

    /** constructs a decision tree with the given dataset as leaves
     * @param dataset
     */
    public DecisionTree(LabeledDataset2<F,L> dataset) {
        label = null;
        this.dataset = dataset;
        build(dataset);
    }

    /** creates a leave containing the given dataset and classified as clazz.
     * @param dataset
     * @param clazz
     **/
    public DecisionTree(LabeledDataset2<F, L> dataset, L clazz) {
        assert clazz!=null;
        this.dataset = dataset;
        this.label = clazz;
    }

    public LabeledDataset2<F,L> getDataset() {
        return dataset;
    }

    public L getLabel() {
        return label;
    }

    public void setLabel(L label) {
        this.label = label;
    }

    public int getSplitAttribute() {
        return splitAttribute;
    }

    /** sets index of attribute on which this tree will discreteSplit on the root level.
     * If discreteSplitValue!=null, it indicates a continuous discreteSplit of the attribute with this index,
 and its features have been discreteSplit in : smaller or equal to discreteSplitValue and larger than discreteSplitValue.
     * @param index
     * @param splitValue
     */
    public void setSplit(int index, Number splitValue) {
        splitAttribute = index;
        this.splitValue = splitValue;
    }

    public int getSize() {
        return dataset!=null?dataset.size():0;
    }

    /** to facilitate the pruning mechanism, we will consider nodes with a
     * non-null label, as leaf nodes.
     * @return
     */
    @Override
    public boolean isLeaf() {
        if (label!=null) return true;
        else return super.isLeaf();
    }

    // for students
    @Override
    public L classify(F v) {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    // For students
    public int prune(final LabeledDataset2<F,L> testData) {
        // use visitor!?
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    // For students
    @Override
    public Map<L, Map<L, Integer>> getConfusionMatrix(Map<F, L> testData) {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    // For students
    @Override
    public double errorRate(Map<F, L> testData) {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    protected void build(LabeledDataset2<F,L> dataset) {
        throw new UnsupportedOperationException("Needs to be implemented");
    }

    @Override
    public String toDot() {
        final StringBuilder b = new StringBuilder("digraph DT {\n");
        DecisionTreeDotVisitor<F,L> visitor = new DecisionTreeDotVisitor<>(b);
        depthFirstPreOrderVisit(visitor);
        return b.append("}").toString();
    }
}